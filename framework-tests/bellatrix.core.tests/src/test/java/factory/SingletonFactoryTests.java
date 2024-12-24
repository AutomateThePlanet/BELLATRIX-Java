package factory;

import factory.data.Employee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.core.plugins.junit.BaseTest;
import solutions.bellatrix.core.utilities.SingletonFactory;

public class SingletonFactoryTests extends BaseTest {
    @Test
    public void existingObjectReturned_When_GetInstance() {
        var johnDoe = new Employee("John", "Doe");
        SingletonFactory.register(johnDoe);

        var employee = SingletonFactory.getInstance(Employee.class, "Jane", "Doe");

        Assertions.assertEquals(johnDoe.firstName, employee.firstName);
    }

    @Test
    public void noEntriesRemaining_When_SingletonFactoryClear() {
        var johnDoe = new Employee("John", "Doe");
        SingletonFactory.register(johnDoe);

        SingletonFactory.clear();

        Assertions.assertNotEquals(johnDoe, SingletonFactory.getInstance(Employee.class));
    }

    @Test
    public void objectReturned_When_UsedNoArgsConstructor() {
        Assertions.assertNotNull(SingletonFactory.getInstance(Employee.class));
    }

    @Test
    public void objectReturned_When_UsedAllArgsConstructor() {
        Assertions.assertNotNull(SingletonFactory.getInstance(Employee.class, "0", "John", "Doe", "jdoe@corp.com", "john.doe@gmail.com"));
    }

    @Test
    public void objectReturned_When_UsedAllArgsWithVarArgsConstructor() {
        Assertions.assertNotNull(SingletonFactory.getInstance(Employee.class, "0", "John", "Doe", "jdoe@corp.com", "john.doe@gmail.com", new Object[]{new Employee("Jane", "Doe")}));
    }

    @Test
    public void objectReturned_When_UsedCustomArgsConstructor() {
        Assertions.assertNotNull(SingletonFactory.getInstance(Employee.class, "John", "Doe"));
    }

    @Test
    public void objectNotReturned_When_UsedNonExistentConstructor() {
        Assertions.assertNull(SingletonFactory.getInstance(Employee.class, "John Doe"));
    }

    @AfterEach
    public void clearSingletonFactoryMap() {
        SingletonFactory.clear();
    }
}
