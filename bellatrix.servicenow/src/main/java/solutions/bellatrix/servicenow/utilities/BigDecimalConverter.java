package solutions.bellatrix.servicenow.utilities;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import lombok.SneakyThrows;

public class BigDecimalConverter {
    @SneakyThrows
    public static BigDecimal getFromSting(String value) {
        BigDecimal result = null;
        var format = new DecimalFormat();
        format.setParseBigDecimal(true);
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);

        if (value.matches("^[\\d{0,3},]*\\d{1,3}\\.\\d{2}$")) {
            var symbols = new DecimalFormatSymbols(Locale.US);
            symbols.setGroupingSeparator(',');
            symbols.setDecimalSeparator('.');
            var pattern = "$#,##0.00";
            format.applyPattern(pattern);
            format.setDecimalFormatSymbols(symbols);
        }

        result = ((BigDecimal) format.parse(value));

        return result.setScale(2, RoundingMode.UNNECESSARY);
    }
}