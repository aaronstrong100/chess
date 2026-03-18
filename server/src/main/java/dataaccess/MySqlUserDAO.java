package dataaccess;
import model.*;
import exceptions.UnauthorizedException;
import java.sql.SQLException;

public class MySqlUserDAO implements UserDAO{

    @Override
    public UserData getUserData(String username) throws UnauthorizedException{
        try(var conn = DatabaseManager.getConnection()) {
            var getUserStatement = "SELECT username, password, email FROM user_data WHERE username=?";
            try (var preparedGetUserStatement = conn.prepareStatement(getUserStatement)) {
                preparedGetUserStatement.setString(1, username);
                try (var rs = preparedGetUserStatement.executeQuery()) {
                    if (rs.next()) {
                        String usernameData = rs.getString("username");
                        String passwordData = rs.getString("password");
                        String emailData = rs.getString("email");
                        return new UserData(usernameData, passwordData, emailData);
                    }
                } catch (Exception e){
                    throw new UnauthorizedException("The user does not exist");
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error accessing database: " + e.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error connecting to database: " + e.getMessage());
        }
        throw new UnauthorizedException("The user does not exist");
    }

    @Override
    public void addUserData(UserData userData){
        try (var conn = DatabaseManager.getConnection()) {
            var addUserStatement = "INSERT INTO user_data (username, password, email) VALUES(?,?,?)";
            try (var preparedAddUserStatement = conn.prepareStatement(addUserStatement)) {
                preparedAddUserStatement.setString(1, userData.getUsername());
                preparedAddUserStatement.setString(2, userData.getPassword());
                preparedAddUserStatement.setString(3, userData.getEmail());
                preparedAddUserStatement.executeUpdate();
            } catch (SQLException e) {
                if(!e.getMessage().contains("Duplicate")){
                    throw new RuntimeException(e.getMessage());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void clearDataBase(){
        DatabaseManager.clearTable("user_data");
    }
}
