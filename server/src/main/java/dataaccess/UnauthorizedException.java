package dataaccess;

import dataaccess.DataAccessException;

public class UnauthorizedException extends DataAccessException{
    public UnauthorizedException(String message) {
        super(message);
    }
    public UnauthorizedException(String message, Throwable ex) {
        super(message, ex);
    }
}
