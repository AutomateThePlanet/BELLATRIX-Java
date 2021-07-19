package hubplugins;

public class UnsupportedHttpMethodException extends RuntimeException {
    public UnsupportedHttpMethodException(String method) {
        super(String.format("Method %s is not supported", method));
    }
}
