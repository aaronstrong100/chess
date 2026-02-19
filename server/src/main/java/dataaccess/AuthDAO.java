package dataaccess;

import model.AuthData;

public interface AuthDAO {
    /**
     * add AuthData object to the database
     * @param authData
     */
    public void addAuthData(AuthData authData) throws DataAccessException;

    /**
     * returns an AuthData object given an authToken. Returns null if it does not exist
     * @param authToken
     * @return
     */
    public AuthData getAuthData(String authToken) throws DataAccessException;

    /**
     * clear all AuthData from the database
     */
    public void clearDataBase() throws DataAccessException;
}
