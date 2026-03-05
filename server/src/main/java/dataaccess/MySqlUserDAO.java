package dataaccess;
import model.*;
import dataaccess.UnauthorizedException;

public class MySqlUserDAO implements UserDAO{

    @Override
    public UserData getUserData(String username) throws UnauthorizedException{
        var conn = DatabaseManager.getConnection();
        var getUserStatement = "SELECT username, password, email FROM user_data WHERE username=?";
        try(var preparedGetUserStatement = conn.prepareStatement(getUserStatement)){
             preparedGetUserStatement.setString(1,username);
             try(var rs = preparedGetUserStatement.executeQuery()){
                 if(rs.next()){
                     String usernameData = rs.getString("username");
                     String passwordData = rs.getString("password");
                     String emailData = rs.getString("email");
                     return new UserData(usernameData, passwordData, emailData);
                 }
             }
        }
        throw new UnauthorizedException("The user does not exist");
    }

    @Override
    public void addUserData(UserData userData){
        var conn = DatabaseManager.getConnection();
        var addUserStatement = "INSERT INTO user_data (username, password, email) VALUES(?,?,?)";
        try(var preparedAddUserStatement = conn.prepareStatement(addUserStatement)){
            preparedAddUserStatement.setString(1,userData.getUsername());
            preparedAddUserStatement.setString(2,userData.getPassword());
            preparedAddUserStatement.setString(3,userData.getEmail());
            preparedAddUserStatement.executeUpdate();
        }
    }

    @Override
    public void clearDataBase(){

    }
}
