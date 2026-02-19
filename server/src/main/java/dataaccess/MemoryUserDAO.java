package dataaccess;

import model.UserData;

public class MemoryUserDAO implements UserDAO{
    @Override
    public UserData getUserData(String username)  throws DataAccessException{
        return null;
    }

    @Override
    public void addUserData(UserData userData)  throws DataAccessException{

    }

    @Override
    public void clearDataBase()  throws DataAccessException{

    }
}
