package dataaccess;

import model.UserData;

public class MemoryUserDAO implements UserDAO{
    /**
     *
     * @param username the username to get user data from
     * @return a UserData Object for the given username
     */
    @Override
    public UserData getUserData(String username)  throws DataAccessException{
        return null;
    }
    /**
     * add a UserData to the database
     * @param userData the UserData Object to add
     */
    @Override
    public void addUserData(UserData userData)  throws DataAccessException{

    }
    /**
     * Clears all UserData from the database
     */
    @Override
    public void clearDataBase()  throws DataAccessException{

    }
}
