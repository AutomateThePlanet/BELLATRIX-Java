package solutions.bellatrix.servicenow.components.data;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.NotImplementedException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@Data
@SuperBuilder
public class ServiceNowForm {
    public String getNumber() {
        throw new NotImplementedException("Implement in child classes");
    }

    public ServiceNowForm() {
    }

    public void setNumber(String number) {
    }

    public <Form extends ServiceNowForm> List<Field> getNotNullFields() {
        return Arrays.stream(this.getClass().getDeclaredFields()).filter(f -> {
            try {
                f.setAccessible(true);
                return f.get(this) != null;
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }
}