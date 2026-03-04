package dataaccess;
import dataaccess.UnauthorizedException;
import model.*;

public class DatabaseAuthDAO implements AuthDAO{

    public DatabaseAuthDAO(){

    }
    /**
     * add AuthData object to the database
     * @param authData
     */
    @Override
    public AuthData addAuthData(AuthData authData){
        return null;
    }

    /**
     * returns an AuthData object given an authToken. Returns null if it does not exist
     * @param authToken
     * @return
     */
    @Override
    public AuthData getAuthData(String authToken) throws UnauthorizedException{
        return null;
    }

    @Override
    public void deleteAuthData(String authToken){

    }

    /**
     * clear all AuthData from the database
     */
    @Override
    public void clearDataBase(){

    }

    @Override
    public String generateNewAuthToken(){
        return null;
    }
}
