package dataaccess;

import model.UserData;

public class MemoryUserDAO implements UserDAO{
    @Override
    public UserData getUserData(String username) {
        return null;
    }

    @Override
    public void addUserData(UserData userData) {

    }

    @Override
    public void clearDataBase() {

    }
}
