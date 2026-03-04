package dataaccess;
import model.*;
import dataaccess.UnauthorizedException;

public class MySqlUserDAO implements UserDAO{

    @Override
    public UserData getUserData(String username) throws UnauthorizedException{
        return null;
    }

    @Override
    public void addUserData(UserData userData){

    }

    @Override
    public void clearDataBase(){

    }
}
