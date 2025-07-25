package solutions.bellatrix.servicenow.utilities;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TextConverter {
    public static String convertStringWordsToUpperCase(String text) {
        String convertedString = Arrays.stream(text.split(" "))
            .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
            .collect(Collectors.joining(" "));

        return convertedString;
    }
}
