package solutions.bellatrix.servicenow.snSetupData.converters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import solutions.bellatrix.servicenow.snSetupData.FxCurrency2Instance;
import solutions.bellatrix.servicenow.snSetupData.FxCurrency2InstanceRepository;
import java.lang.reflect.Type;
import solutions.bellatrix.api.HttpErrorException;

public class FxCurrencyConverter implements JsonSerializer<FxCurrency2Instance>, JsonDeserializer<FxCurrency2Instance> {
    /**
     * When we want to create a record that contains currencies values service now provides us an easy way to create a currency record. We should add currency type before amount and split them with ';' Example: "USD;2000"
     */
    @Override
    public JsonElement serialize(FxCurrency2Instance amount, Type srcType, JsonSerializationContext context) {
        return new JsonPrimitive(amount.toStringForTableApi());
    }

    /**
     * When we get a record/s from db, through table api, the currencies values in JSON response are foreign key. To handle the problem this deserializer make a request to take the actual object and add it instead of "sys_id".
     */
    @Override
    public FxCurrency2Instance deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        var elementAsString = jsonElement.getAsString();

        if (elementAsString == null || elementAsString.isEmpty()) {
            return null;
        }

        var repository = new FxCurrency2InstanceRepository();

        FxCurrency2Instance result;
        try {
            result = repository.getById(elementAsString).getResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}