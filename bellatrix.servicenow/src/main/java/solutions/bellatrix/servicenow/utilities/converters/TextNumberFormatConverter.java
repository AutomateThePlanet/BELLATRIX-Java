package solutions.bellatrix.servicenow.utilities.converters;

import java.text.NumberFormat;
import java.util.Locale;

public class TextNumberFormatConverter {
    public static String convertStringDoubleToUsStyle(String doubleText) {
        var number = Double.parseDouble(doubleText);

        return NumberFormat.getNumberInstance(Locale.US).format(number);
    }

    public static Double convertStringDoubleUsStyleToDouble(String doubleUsText) {
        var element = doubleUsText.replace("$", "");
        element = element.replace(",", "");

        return Double.parseDouble(element);
    }

    public static String convertStringDoublePercentageStyleToRounded(String doubleTextPercentage) {
        var element = doubleTextPercentage.replace("%", "");

        var number = Double.parseDouble(element);
        int numberRounded = (int) Math.round(number);

        return String.format("%s%%", NumberFormat.getNumberInstance(Locale.US).format(numberRounded));
    }
}