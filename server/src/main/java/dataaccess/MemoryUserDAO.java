package dataaccess;

import model.UserData;

import java.util.ArrayList;

public class MemoryUserDAO implements UserDAO{
    private ArrayList<UserData> userData;
    public MemoryUserDAO(){
        this.userData = new ArrayList<UserData>();
    }
    /**
     *
     * @param username the username to get user data from
     * @return a UserData Object for the given username
     */
    @Override
    public UserData getUserData(String username)  throws DataAccessException{
        for(UserData user : this.userData){
            if(username.equals(user.getUsername())){
                return user;
            }
        }
        throw new DataAccessException("The user does not exist");
    }
    /**
     * add a UserData to the database
     * @param newUserData the UserData Object to add
     */
    @Override
    public void addUserData(UserData newUserData){
        this.userData.add(newUserData);
    }
    /**
     * Clears all UserData from the database
     */
    @Override
    public void clearDataBase(){
        this.userData = new ArrayList<>();
    }
}
