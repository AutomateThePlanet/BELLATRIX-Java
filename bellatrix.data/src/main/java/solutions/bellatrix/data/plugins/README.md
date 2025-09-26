# TestDataCleanupPlugin

The `TestDataCleanupPlugin` is a powerful plugin for the Bellatrix framework that automatically tracks and cleans up test data created during test execution. This plugin helps prevent test data pollution by ensuring that entities created during tests are properly cleaned up after test completion.

## Features

- **Automatic Entity Tracking**: Monitors `HttpRepository` events to track entity creation
- **Intelligent Cleanup**: Automatically deletes tracked entities after test completion
- **Thread-Safe**: Uses thread-local storage for proper isolation between parallel tests
- **Performance Optimized**: Caches repository instances and uses efficient data structures
- **Configurable**: Supports enabling/disabling cleanup and logging
- **Error Handling**: Gracefully handles cleanup failures with detailed logging
- **Dependency Aware**: Cleans up entities in reverse order to handle dependencies

## How It Works

1. **Event Listening**: The plugin listens to `HttpRepository.ENTITY_CREATED` and `HttpRepository.ENTITY_DELETED` events
2. **Entity Tracking**: When entities are created, they are added to a thread-local tracking queue
3. **Cleanup Prevention**: If entities are manually deleted, they are removed from tracking
4. **Automatic Cleanup**: After test completion, all tracked entities are deleted in reverse order
5. **Repository Resolution**: Uses `RepositoryFactory` to get the appropriate repository for each entity type

## Usage

### Basic Usage

```java
public class MyTest extends BaseTest {
    @Override
    protected void configure() {
        // Add the plugin with default settings (enabled=true, logging=true)
        addPlugin(TestDataCleanupPlugin.class);
    }
    
    public void testWithEntityCreation() {
        // Create entities - they will be automatically tracked
        User user = new User().name("Test User").email("test@example.com").create();
        Order order = new Order().userId(user.getId()).amount(100.0).create();
        
        // Test logic here...
        
        // Entities will be automatically cleaned up after test completion
    }
}
```

### Advanced Configuration

```java
public class MyTest extends BaseTest {
    @Override
    protected void configure() {
        // Custom configuration: enabled=true, logging=false
        addPlugin(TestDataCleanupPlugin.class, true, false);
    }
}
```

### Disabling Cleanup

```java
public class MyTest extends BaseTest {
    @Override
    protected void configure() {
        // Disable cleanup (useful for debugging)
        addPlugin(TestDataCleanupPlugin.class, false, true);
    }
}
```

## Configuration Options

| Parameter | Type | Default | Description |
|-----------|------|---------|-------------|
| `isEnabled` | boolean | true | Whether the plugin should perform cleanup |
| `enableLogging` | boolean | true | Whether to log cleanup operations |

## Event Integration

The plugin integrates with the following `HttpRepository` events:

- `ENTITY_CREATED`: Tracks newly created entities
- `ENTITY_DELETED`: Removes manually deleted entities from tracking

## Lifecycle Integration

The plugin hooks into the following test lifecycle events:

- `preBeforeTest`: Clears previous entity tracking
- `postAfterTest`: Performs cleanup of tracked entities
- `postAfterClass`: Clears any remaining tracked entities

## Thread Safety

The plugin is designed to be thread-safe and works correctly with parallel test execution:

- Uses `ThreadLocal` storage for entity tracking
- Uses `ConcurrentHashMap` for repository caching
- Uses `ConcurrentLinkedQueue` for entity tracking

## Error Handling

The plugin includes comprehensive error handling:

- Continues cleanup even if individual entities fail to delete
- Logs detailed error information for failed cleanup operations
- Provides summary statistics of successful and failed cleanups

## Performance Considerations

- **Repository Caching**: Repository instances are cached to avoid repeated lookups
- **Efficient Data Structures**: Uses `ConcurrentLinkedQueue` for O(1) insertion and removal
- **Minimal Overhead**: Event listeners are lightweight and only track necessary information
- **Optional Logging**: Logging can be disabled for maximum performance

## Best Practices

1. **Enable Logging in Development**: Use `enableLogging=true` during development to monitor cleanup operations
2. **Disable Logging in CI/CD**: Use `enableLogging=false` in production environments for better performance
3. **Handle Dependencies**: The plugin cleans up entities in reverse order, but consider entity relationships when designing tests
4. **Test Isolation**: Each test gets its own tracking queue, ensuring proper isolation
5. **Manual Cleanup**: If you manually delete entities in your tests, they will be automatically removed from tracking

## Troubleshooting

### Entities Not Being Cleaned Up

- Ensure the plugin is properly configured in your test class
- Check that entities are being created through `HttpRepository` (not directly)
- Verify that entity classes are properly registered with `RepositoryFactory`

### Cleanup Failures

- Check the logs for detailed error messages
- Ensure the test environment has proper permissions for delete operations
- Verify that entity identifiers are valid and entities exist

### Performance Issues

- Disable logging if not needed: `addPlugin(TestDataCleanupPlugin.class, true, false)`
- Consider the number of entities being created per test
- Monitor repository caching effectiveness

## Example Test Scenarios

### Simple Entity Creation
```java
public void testCreateUser() {
    User user = new User().name("Test User").create();
    // User will be automatically cleaned up
}
```

### Multiple Entity Creation
```java
public void testCreateUserWithOrders() {
    User user = new User().name("Test User").create();
    Order order1 = new Order().userId(user.getId()).create();
    Order order2 = new Order().userId(user.getId()).create();
    // All entities will be cleaned up in reverse order
}
```

### Mixed Creation and Deletion
```java
public void testCreateAndDeleteSomeEntities() {
    User user = new User().name("Test User").create();
    Order order = new Order().userId(user.getId()).create();
    
    order.delete(); // Removed from tracking
    
    // Only user will be cleaned up
}
```

## Integration with Other Plugins

The `TestDataCleanupPlugin` works well with other Bellatrix plugins:

- **ScreenshotPlugin**: Cleanup happens after screenshots are taken
- **VideoPlugin**: Cleanup happens after video recording
- **Logging Plugins**: Cleanup operations are logged if enabled

## Limitations

1. **HttpRepository Only**: Currently only works with `HttpRepository` implementations
2. **Entity Dependencies**: Complex entity relationships may require manual cleanup ordering
3. **Cross-Test Dependencies**: Entities created in one test are not cleaned up by other tests
4. **Repository Registration**: All entity types must be properly registered with `RepositoryFactory`
