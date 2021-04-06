package by.tut.shershnev.heartbeat.service.exception;

public class IPAddressAlreadyInPoolException extends RuntimeException{

    private String message;

    public IPAddressAlreadyInPoolException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }


}
