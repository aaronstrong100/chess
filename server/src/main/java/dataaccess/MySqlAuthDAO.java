package dataaccess;
import dataaccess.UnauthorizedException;
import model.*;

import java.util.UUID;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class MySqlAuthDAO implements AuthDAO{

    public MySqlAuthDAO(){

    }
    /**
     * add AuthData object to the database
     * @param authData
     */
    @Override
    public AuthData addAuthData(AuthData authData){
        var conn = DatabaseManager.getConnection();
        var addAuthStatement = "INSERT INTO auth_data (username,auth_token) VALUES(?,?)";
        try(var preparedAddAuthStatement = conn.prepareStatement(addAuthStatement)){
            preparedAddAuthStatement.setString(1,authData.getUsername());
            preparedAddAuthStatement.setString(2,authData.getAuthToken());
            preparedAddAuthStatement.executeUpdate();
        }
        return AuthData;
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
        return UUID.randomUUID().toString();
    }
}
