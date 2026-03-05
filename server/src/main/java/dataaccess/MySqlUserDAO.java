package dataaccess;
import model.*;
import dataaccess.UnauthorizedException;

public class MySqlUserDAO implements UserDAO{

    @Override
    public UserData getUserData(String username) throws UnauthorizedException{
        var conn = DatabaseManger.getConnection();
        var getUserStatement = "SELECT username, password, email FROM user_data WHERE username="+username+";";
        try(var preparedGetUserStatement = conn.prepareStatement(getUserStatement)){
             getUserStatement.setString(1,username);
             try(var rs = preparedGetUserStatement.executeQuery()){
                 if(rs.next()){
                     String usernameData = rs.getString("username");
                     String passwordData = rs.getString("password");
                     String emailData = rs.getString("email");
                     return new UserData(usernameData, passwordData, emailData);
                 }
             }
        }
    }

    @Override
    public void addUserData(UserData userData){
    }

    @Override
    public void clearDataBase(){

    }
}
