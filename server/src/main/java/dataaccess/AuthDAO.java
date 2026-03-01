package dataaccess;

import model.AuthData;

import dataaccess.UnauthorizedException;

public interface AuthDAO {
    /**
     * add AuthData object to the database
     * @param authData
     */
    public AuthData addAuthData(AuthData authData);

    /**
     * returns an AuthData object given an authToken. Returns null if it does not exist
     * @param authToken
     * @return
     */
    public AuthData getAuthData(String authToken) throws UnauthorizedException;

    public void deleteAuthData(String authToken);

    /**
     * clear all AuthData from the database
     */
    public void clearDataBase();

    String generateNewAuthToken();
}
