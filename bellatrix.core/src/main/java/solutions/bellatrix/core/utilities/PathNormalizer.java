package solutions.bellatrix.core.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathNormalizer {
    public static String normalizePath(String path) {
        path = path.replaceAll("[\\\\/]", System.getProperty("file.separator"));

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
