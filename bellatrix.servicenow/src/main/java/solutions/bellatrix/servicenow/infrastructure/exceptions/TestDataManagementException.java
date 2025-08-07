package solutions.bellatrix.servicenow.infrastructure.exceptions;

public class TestDataManagementException extends RuntimeException{
    public TestDataManagementException(Exception e) {
        super(e);
    }

    public TestDataManagementException() {
        super();
    }
}
