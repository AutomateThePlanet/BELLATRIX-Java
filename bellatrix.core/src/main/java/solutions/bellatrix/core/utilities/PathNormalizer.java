package solutions.bellatrix.core.utilities;

import org.apache.commons.text.StringEscapeUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathNormalizer {
    public static String normalizePath(String path) {
        var fileSeparator = StringEscapeUtils.escapeJava(System.getProperty("file.separator"));
        path = path.replaceAll("[\\\\/]", fileSeparator);
        Matcher matcher = Pattern.compile("\\$\\{(.*?)}").matcher(path);

        while (matcher.find()) {
            String replacement = resolveVariable(matcher.group(1));
            if (replacement != null) {
                path = path.replace(matcher.group(0), replacement);
            }
        }

        return path;
    }

    public static String resolveVariable(String variable) {

////    switch (variable) {
////        case "myVar1" -> { return "resolvedVar1"; }
////        case "myVar2" -> { return "resolvedVar2"; }
////        case "myVar3" -> { return "resolvedVar3"; }
////    }

        return System.getProperty(variable);
    }
}