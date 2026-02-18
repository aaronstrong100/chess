package dataaccess;

import model.AuthData;

public interface AuthDAO {
    public void addAuthData(AuthData authData);
    public AuthData getAuthData(String authToken);
    public void clearDataBase();
}
