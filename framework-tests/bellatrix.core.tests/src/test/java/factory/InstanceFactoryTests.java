package factory;

import factory.data.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.core.plugins.junit.BaseTest;
import solutions.bellatrix.core.utilities.InstanceFactory;

public class InstanceFactoryTests extends BaseTest {
    @Test
    public void objectReturned_When_UsedNoArgsConstructor() {
        Assertions.assertNotNull(InstanceFactory.create(Employee.class));
    }

    @Test
    public void objectReturned_When_UsedAllArgsConstructor() {
        Assertions.assertNotNull(InstanceFactory.create(Employee.class, "0", "John", "Doe", "jdoe@corp.com", "john.doe@gmail.com"));
    }

    @Test
    public void objectReturned_When_UsedAllArgsWithVarArgsConstructor() {
        Assertions.assertNotNull(InstanceFactory.create(Employee.class, "0", "John", "Doe", "jdoe@corp.com", "john.doe@gmail.com", new Object[] { new Employee("Jane", "Doe") }));
    }

    @Test
    public void objectReturned_When_UsedCustomArgsConstructor() {
        Assertions.assertNotNull(InstanceFactory.create(Employee.class, "John", "Doe"));
    }

    @Test
    public void objectNotReturned_When_UsedNonExistentConstructor() {
        Assertions.assertNull(InstanceFactory.create(Employee.class, "John Doe"));
    }
}
