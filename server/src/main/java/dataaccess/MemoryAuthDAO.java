package dataaccess;

import model.AuthData;

public class MemoryAuthDAO implements AuthDAO{
    @Override
    public void addAuthData(AuthData authData)  throws DataAccessException{

    }

    @Override
    public AuthData getAuthData(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void clearDataBase()  throws DataAccessException{

    }
}
