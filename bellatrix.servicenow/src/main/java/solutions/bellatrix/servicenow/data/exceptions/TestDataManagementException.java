package solutions.bellatrix.servicenow.data.exceptions;

public class TestDataManagementException extends RuntimeException{
    public TestDataManagementException(Exception e) {
        super(e);
    }

    public TestDataManagementException() {
        super();
    }
}
