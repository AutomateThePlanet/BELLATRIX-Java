/*
 * Copyright 2022 Automate The Planet Ltd.
 * Author: Anton Angelov
 * Licensed under the Apache License, Version 2.0 (the "License");
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package solutions.bellatrix.data.plugins;

import lombok.extern.slf4j.Slf4j;
import solutions.bellatrix.core.plugins.Plugin;
import solutions.bellatrix.core.plugins.TestResult;
import solutions.bellatrix.core.plugins.TimeRecord;
import solutions.bellatrix.core.utilities.Log;
import solutions.bellatrix.data.configuration.RepositoryFactory;
import solutions.bellatrix.data.contracts.Repository;
import solutions.bellatrix.data.http.infrastructure.Entity;
import solutions.bellatrix.data.http.infrastructure.HttpEntity;
import solutions.bellatrix.data.http.infrastructure.HttpRepository;
import solutions.bellatrix.data.http.infrastructure.events.EntityCreatedEventArgs;
import solutions.bellatrix.data.http.infrastructure.events.EntityDeletedEventArgs;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Plugin that automatically tracks entities created during test execution
 * and cleans them up after test completion to prevent test data pollution.
 * 
 * This plugin listens to HttpRepository events to track entity creation
 * and automatically deletes tracked entities after test completion.
 */
@Slf4j
public class TestDataCleanupPlugin extends Plugin {
    
    /**
     * Thread-local storage for entities created during the current test.
     * Uses a queue to maintain creation order for proper cleanup.
     */
    private static final ThreadLocal<Queue<EntityInfo>> CREATED_ENTITIES = 
        ThreadLocal.withInitial(ConcurrentLinkedQueue::new);
    
    /**
     * Cache of repository instances to avoid repeated lookups.
     */
    private static final Map<Class<? extends Entity<?, ?>>, Repository<?>> REPOSITORY_CACHE = 
        new ConcurrentHashMap<>();
    
    /**
     * Flag to control whether cleanup should be performed.
     * Can be disabled for debugging or specific test scenarios.
     */
    private final boolean isEnabled;
    
    /**
     * Flag to control whether to log cleanup operations.
     */
    private final boolean enableLogging;
    
    /**
     * Creates a new TestDataCleanupPlugin with default settings.
     */
    public TestDataCleanupPlugin() {
        this(true, true);
    }
    
    /**
     * Creates a new TestDataCleanupPlugin with custom settings.
     * 
     * @param isEnabled Whether the plugin should perform cleanup
     * @param enableLogging Whether to log cleanup operations
     */
    public TestDataCleanupPlugin(boolean isEnabled, boolean enableLogging) {
        this.isEnabled = isEnabled;
        this.enableLogging = enableLogging;
        setupEventListeners();
    }
    
    /**
     * Sets up event listeners for entity creation and deletion events.
     */
    private void setupEventListeners() {
        // Listen for entity creation events
        HttpRepository.ENTITY_CREATED.addListener(this::onEntityCreated);
        
        // Listen for entity deletion events to remove from tracking
        HttpRepository.ENTITY_DELETED.addListener(this::onEntityDeleted);
    }
    
    /**
     * Called when an entity is created. Adds the entity to the tracking list.
     * 
     * @param eventArgs The entity creation event arguments
     */
    private void onEntityCreated(EntityCreatedEventArgs eventArgs) {
        if (!isEnabled) {
            return;
        }
        
        Entity<?, ?> entity = eventArgs.getEntity();
        if (entity != null && !(entity instanceof HttpEntity<?, ?> httpEntity && httpEntity.hasInvalidIdentifier())) {
            EntityInfo entityInfo = new EntityInfo(entity, System.currentTimeMillis());
            CREATED_ENTITIES.get().offer(entityInfo);
            
            if (enableLogging) {
                Log.info("TestDataCleanupPlugin: Tracking entity for cleanup - Type: %s, ID: %s", 
                    entity.getClass().getSimpleName(), entity.getIdentifier());
            }
        }
    }
    
    /**
     * Called when an entity is deleted. Removes the entity from tracking.
     * 
     * @param eventArgs The entity deletion event arguments
     */
    private void onEntityDeleted(EntityDeletedEventArgs eventArgs) {
        if (!isEnabled) {
            return;
        }
        
        Entity<?, ?> deletedEntity = eventArgs.getEntity();
        if (deletedEntity != null) {
            // Remove the entity from tracking since it's already deleted
            CREATED_ENTITIES.get().removeIf(entityInfo -> 
                entityInfo.getEntity().equals(deletedEntity));
            
            if (enableLogging) {
                Log.info("TestDataCleanupPlugin: Entity already deleted, removing from tracking - Type: %s, ID: %s", 
                    deletedEntity.getClass().getSimpleName(), deletedEntity.getIdentifier());
            }
        }
    }
    
    /**
     * Called before each test starts. Clears any previously tracked entities.
     * 
     * @param testResult The test result
     * @param memberInfo The test method information
     */
    @Override
    public void preBeforeTest(TestResult testResult, Method memberInfo) {
        if (!isEnabled) {
            return;
        }
        
        // Clear any previously tracked entities for this test
        CREATED_ENTITIES.get().clear();
        
        if (enableLogging) {
            Log.info("TestDataCleanupPlugin: Starting test '%s' - cleared previous entity tracking", 
                memberInfo.getName());
        }
    }
    
    /**
     * Called after each test completes. Performs cleanup of tracked entities.
     * 
     * @param testResult The test result
     * @param timeRecord The test execution time record
     * @param memberInfo The test method information
     * @param failedTestException Any exception that caused test failure
     */
    @Override
    public void postAfterTest(TestResult testResult, TimeRecord timeRecord, Method memberInfo, Throwable failedTestException) {
        if (!isEnabled) {
            return;
        }
        
        cleanupTrackedEntities(memberInfo.getName());
    }
    
    /**
     * Performs cleanup of all tracked entities for the current test.
     * 
     * @param testName The name of the test being cleaned up
     */
    private void cleanupTrackedEntities(String testName) {
        Queue<EntityInfo> entitiesToCleanup = CREATED_ENTITIES.get();
        
        if (entitiesToCleanup.isEmpty()) {
            if (enableLogging) {
                Log.info("TestDataCleanupPlugin: No entities to cleanup for test '%s'", testName);
            }
            return;
        }
        
        if (enableLogging) {
            Log.info("TestDataCleanupPlugin: Starting cleanup for test '%s' - %d entities to cleanup", 
                testName, entitiesToCleanup.size());
        }
        
        int successCount = 0;
        int failureCount = 0;
        
        // Process entities in reverse order (LIFO) to handle dependencies
        List<EntityInfo> entitiesList = new ArrayList<>(entitiesToCleanup);
        Collections.reverse(entitiesList);
        
        for (EntityInfo entityInfo : entitiesList) {
            try {
                cleanupEntity(entityInfo);
                successCount++;
            } catch (Exception e) {
                failureCount++;
                Log.error("TestDataCleanupPlugin: Failed to cleanup entity - Type: %s, ID: %s, Error: %s", 
                    entityInfo.getEntity().getClass().getSimpleName(), 
                    entityInfo.getEntity().getIdentifier(), 
                    e.getMessage());
            }
        }
        
        // Clear the tracking list
        entitiesToCleanup.clear();
        
        if (enableLogging) {
            Log.info("TestDataCleanupPlugin: Cleanup completed for test '%s' - Success: %d, Failures: %d", 
                testName, successCount, failureCount);
        }
    }
    
    /**
     * Performs cleanup of a single entity.
     * 
     * @param entityInfo The entity information to cleanup
     */
    @SuppressWarnings("unchecked")
    private void cleanupEntity(EntityInfo entityInfo) {
        Entity<?, ?> entity = entityInfo.getEntity();
        Class<? extends Entity<?, ?>> entityClass = (Class<? extends Entity<?, ?>>) entity.getClass();
        
        try {
            // Get or create repository instance
            Repository<Entity<?, ?>> repository = (Repository<Entity<?, ?>>) getRepository(entityClass);
            
            if (enableLogging) {
                Log.info("TestDataCleanupPlugin: Deleting entity - Type: %s, ID: %s", 
                    entityClass.getSimpleName(), entity.getIdentifier());
            }
            
            // Delete the entity
            repository.delete(entity);
            
        } catch (Exception e) {
            Log.error("TestDataCleanupPlugin: Error during entity cleanup - Type: %s, ID: %s, Error: %s", 
                entityClass.getSimpleName(), entity.getIdentifier(), e.getMessage());
            throw e;
        }
    }
    
    /**
     * Gets a repository instance for the given entity class, using caching for performance.
     * 
     * @param entityClass The entity class
     * @return The repository instance
     */
    @SuppressWarnings("unchecked")
    private Repository<?> getRepository(Class<? extends Entity<?, ?>> entityClass) {
        return REPOSITORY_CACHE.computeIfAbsent(entityClass, 
            clazz -> RepositoryFactory.INSTANCE.getRepository(clazz));
    }
    
    /**
     * Called after all tests in a class complete. Clears any remaining tracked entities.
     * 
     * @param type The test class
     */
    @Override
    public void postAfterClass(Class type) {
        if (!isEnabled) {
            return;
        }
        
        // Clear any remaining tracked entities
        CREATED_ENTITIES.get().clear();
        
        if (enableLogging) {
            Log.info("TestDataCleanupPlugin: Test class completed - cleared all entity tracking");
        }
    }
    
    /**
     * Inner class to hold entity information along with creation timestamp.
     */
    private static class EntityInfo {
        private final Entity<?, ?> entity;
        
        public EntityInfo(Entity<?, ?> entity, long createdTimestamp) {
            this.entity = entity;
        }
        
        public Entity<?, ?> getEntity() {
            return entity;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            EntityInfo that = (EntityInfo) obj;
            return Objects.equals(entity, that.entity);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(entity);
        }
    }
}
