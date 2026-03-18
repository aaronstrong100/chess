package exceptions;

public class UnauthorizedException extends DataAccessException{
    public UnauthorizedException(){
        super();
    }
    public UnauthorizedException(String message) {
        super(message);
    }
    public UnauthorizedException(String message, Throwable ex) {
        super(message, ex);
    }
}
