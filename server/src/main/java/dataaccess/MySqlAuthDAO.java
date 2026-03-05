package dataaccess;
import dataaccess.UnauthorizedException;
import model.*;
import dataaccess.DatabaseManager;

import java.sql.SQLException;
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
        try(var conn = DatabaseManager.getConnection()) {
            var addAuthStatement = "INSERT INTO auth_data (username,auth_token) VALUES(?,?)";
            try (var preparedAddAuthStatement = conn.prepareStatement(addAuthStatement)) {
                preparedAddAuthStatement.setString(1, authData.getUsername());
                preparedAddAuthStatement.setString(2, authData.getAuthToken());
                preparedAddAuthStatement.executeUpdate();
            }
        } catch (Exception e) {}
        return authData;
    }

    /**
     * returns an AuthData object given an authToken. Returns null if it does not exist
     * @param authToken
     * @return
     */
    @Override
    public AuthData getAuthData(String authToken) throws UnauthorizedException{
        try(var conn = DatabaseManager.getConnection()) {
            var getAuthStatement = "SELECT username, auth_token FROM auth_data WHERE auth_token=?";
            try (var preparedGetAuthStatement = conn.prepareStatement(getAuthStatement)) {
                preparedGetAuthStatement.setString(1, authToken);
                try (var rs = preparedGetAuthStatement.executeQuery()) {
                    if (rs.next()) {
                        String usernameData = rs.getString("username");
                        String authData = rs.getString("auth_token");
                        return new AuthData(usernameData, authData);
                    }
                } catch (Exception e){
                    throw new UnauthorizedException("The user does not exist");
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error accessing database");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error connecting to database");
        }
        throw new RuntimeException("Unknown error occurred");
    }

    @Override
    public void deleteAuthData(String authToken){

    }

    /**
     * clear all AuthData from the database
     */
    @Override
    public void clearDataBase(){
        try(var conn = DatabaseManager.getConnection()) {
            var deleteStatement = "TRUNCATE auth_data";
            try (var preparedDeleteStatement = conn.prepareStatement(deleteStatement)) {
                preparedDeleteStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Error accessing database");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error connecting to database");
        }
    }

    @Override
    public String generateNewAuthToken(){
        return UUID.randomUUID().toString();
    }
}
