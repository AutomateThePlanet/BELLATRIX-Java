package solutions.bellatrix.api;

public class HttpErrorException extends Exception {
    public HttpErrorException(String errorMessage) {
        super(errorMessage);
    }
}