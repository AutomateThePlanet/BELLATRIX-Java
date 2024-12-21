package factory;

import factory.data.Employee;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.core.plugins.junit.BaseTest;
import solutions.bellatrix.core.utilities.SingletonFactory;

public class SingletonFactoryTests extends BaseTest {
    @Test
    public void testExistingObjectReturnedWhenFound() {
        var johnDoe = new Employee("John", "Doe");
        SingletonFactory.register(johnDoe);

        var employee = SingletonFactory.getInstance(Employee.class, "Jane", "Doe");

        Assertions.assertEquals(johnDoe.firstName, employee.firstName);
    }

    @Test
    public void testNoArgsConstructor() {
        Assertions.assertNotNull(SingletonFactory.getInstance(Employee.class));
    }

    @Test
    public void testAllArgsConstructor() {
        Assertions.assertNotNull(SingletonFactory.getInstance(Employee.class, "0", "John", "Doe", "jdoe@corp.com", "john.doe@gmail.com"));
    }

    @Test
    public void testAllArgsConstructorWithVarArgs() {
        Assertions.assertNotNull(SingletonFactory.getInstance(Employee.class, "0", "John", "Doe", "jdoe@corp.com", "john.doe@gmail.com", new Object[]{new Employee("Jane", "Doe")}));
    }

    @Test
    public void testCustomArgsConstructor() {
        Assertions.assertNotNull(SingletonFactory.getInstance(Employee.class, "John", "Doe"));
    }

    @Test
    public void testUsingNonExistentConstructor() {
        Assertions.assertNull(SingletonFactory.getInstance(Employee.class, "John Doe"));
    }

    @AfterEach
    public void clearSingletonFactoryMap() {
        SingletonFactory.clear();
    }
}
