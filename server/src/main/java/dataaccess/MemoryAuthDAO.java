package dataaccess;

import model.AuthData;

public class MemoryAuthDAO implements AuthDAO{
    /**
     * add AuthData object to the database
     * @param authData
     */
    @Override
    public void addAuthData(AuthData authData)  throws DataAccessException{

    }
    /**
     * returns an AuthData object given an authToken. Returns null if it does not exist
     * @param authToken
     * @return
     */
    @Override
    public AuthData getAuthData(String authToken) throws DataAccessException {
        return null;
    }
    /**
     * clear all AuthData from the database
     */
    @Override
    public void clearDataBase()  throws DataAccessException{

    }
}
