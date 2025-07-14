package solutions.bellatrix.data.configuration.http;

public class ClientSideException extends RuntimeException {

    public ClientSideException(String message) {
        super(message);
    }

    public ClientSideException(String message, Throwable cause) {
        super(message, cause);
    }
}