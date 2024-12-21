package factory;

import factory.data.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import solutions.bellatrix.core.plugins.junit.BaseTest;
import solutions.bellatrix.core.utilities.InstanceFactory;

public class InstanceFactoryTests extends BaseTest {
    @Test
    public void testNoArgsConstructor() {
        Assertions.assertNotNull(InstanceFactory.create(Employee.class));
    }

    @Test
    public void testAllArgsConstructor() {
        Assertions.assertNotNull(InstanceFactory.create(Employee.class, "0", "John", "Doe", "jdoe@corp.com", "john.doe@gmail.com"));
    }

    @Test
    public void testAllArgsConstructorWithVarArgs() {
        Assertions.assertNotNull(InstanceFactory.create(Employee.class, "0", "John", "Doe", "jdoe@corp.com", "john.doe@gmail.com", new Object[] { new Employee("Jane", "Doe") }));
    }

    @Test
    public void testCustomArgsConstructor() {
        Assertions.assertNotNull(InstanceFactory.create(Employee.class, "John", "Doe"));
    }
}
