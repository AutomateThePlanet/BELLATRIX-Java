package solutions.bellatrix.servicenow.snSetupData.converters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

public class BigDecimalCurrencyConverter implements JsonDeserializer<BigDecimal> {
    @Override
    public BigDecimal deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        var elementAsString = jsonElement.getAsString();

        if (elementAsString.isEmpty()) {
            return null;
        }

        BigDecimal result;
        var format = new DecimalFormat();
        format.setParseBigDecimal(true);
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);

        if (elementAsString.matches("^\\$[\\d{0,3},]*\\d{1,3}\\.\\d{2}$")) {
            var symbols = new DecimalFormatSymbols(Locale.US);
            symbols.setGroupingSeparator(',');
            symbols.setDecimalSeparator('.');
            var pattern = "$#,##0.00";
            format.applyPattern(pattern);
            format.setDecimalFormatSymbols(symbols);

            try {
                result = (BigDecimal) format.parse(elementAsString);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        } else {
            var pattern = "#,##0.00";
            format.applyPattern(pattern);

            try {
                result = ((BigDecimal) format.parse(elementAsString));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        return result.setScale(2, RoundingMode.DOWN);
    }
}