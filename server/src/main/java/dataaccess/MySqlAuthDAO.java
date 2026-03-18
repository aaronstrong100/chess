package dataaccess;
import shared_exceptions.UnauthorizedException;
import model.*;

import java.sql.SQLException;
import java.util.UUID;

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
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
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
                    } else {
                        throw new UnauthorizedException("The authToken does not exist");
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error accessing database: " + e.getMessage());
            }
        } catch (Exception e) {
            if(e instanceof UnauthorizedException){
                throw new UnauthorizedException(e.getMessage());
            }
            else{
                throw new RuntimeException("Error connecting to database: " + e.getMessage());
            }
        }
    }

    @Override
    public void deleteAuthData(String authToken){
        try(var conn = DatabaseManager.getConnection()) {
            var deleteAuthStatement = "DELETE FROM auth_data WHERE auth_token=?";
            try (var preparedDeleteAuthStatement = conn.prepareStatement(deleteAuthStatement)) {
                preparedDeleteAuthStatement.setString(1, authToken);
                preparedDeleteAuthStatement.executeUpdate();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error connecting to database: " + e.getMessage());
        }
    }

    /**
     * clear all AuthData from the database
     */
    @Override
    public void clearDataBase(){
        DatabaseManager.clearTable("auth_data");
    }

    @Override
    public String generateNewAuthToken(){
        return UUID.randomUUID().toString();
    }
}
