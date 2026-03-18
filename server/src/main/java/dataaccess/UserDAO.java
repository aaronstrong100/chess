package dataaccess;
import shared_exceptions.UnauthorizedException;

import model.UserData;

public interface UserDAO {
    /**
     *
     * @param username the username to get user data from
     * @return a UserData Object for the given username
     */
    public UserData getUserData(String username) throws UnauthorizedException;

    /**
     * add a UserData to the database
     * @param userData the UserData Object to add
     */
    public void addUserData(UserData userData);

    /**
     * Clears all UserData from the database
     */
    public void clearDataBase();
}
