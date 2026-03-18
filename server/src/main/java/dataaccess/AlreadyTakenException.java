package dataaccess;

import dataaccess.DataAccessException;

public class AlreadyTakenException extends DataAccessException{
    public AlreadyTakenException(){
        super();
    }
    public AlreadyTakenException(String message) {
        super(message);
    }
    public AlreadyTakenException(String message, Throwable ex) {
        super(message, ex);
    }
}
