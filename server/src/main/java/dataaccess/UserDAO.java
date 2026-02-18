package dataaccess;

import model.UserData;

public interface UserDAO {
    public UserData getUserData(String username);
    public void addUserData(UserData userData);
    public void clearDataBase();
}
