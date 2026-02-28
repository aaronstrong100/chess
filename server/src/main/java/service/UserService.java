package service;

import Requests.LoginRequest;
import Requests.LogoutRequest;
import Requests.RegisterRequest;
import Results.LoginResult;
import Results.RegisterResult;
import dataaccess.*;
import model.AuthData;
import model.UserData;

public class UserService {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    public UserService(){
        this.userDAO = new MemoryUserDAO();
        this.authDAO = new MemoryAuthDAO();
    }

    public UserService(UserDAO userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }
    /**
     * register a new user
     * @param registerRequest RegisterRequest object containing the username and password of the new user
     * @return a RegisterResult Object containing the new authToken for the user
     */
    public RegisterResult register(RegisterRequest registerRequest) throws Exception{
        try{
            this.userDAO.getUserData(registerRequest.getUsername());
            throw new Exception("The username is already taken");
        } catch(DataAccessException e){
            this.userDAO.addUserData(new UserData(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getEmail()));
            AuthData returnData = this.authDAO.addAuthData(new AuthData(registerRequest.getUsername(), authDAO.generateNewAuthToken()));
            return new RegisterResult(returnData.getUsername(), returnData.getAuthToken());
        }
    }

    /**
     * log a user in
     * @param loginRequest LoginRequest Object containing the username and password of the user
     * @return a LoginResult containing the authToken for the user
     */
    public LoginResult login(LoginRequest loginRequest) throws Exception{
        UserData userData = this.userDAO.getUserData(loginRequest.getUsername());
        if(userData.getPassword().equals(loginRequest.getPassword())){
            AuthData newAuthData = new AuthData(userData.getUsername(), this.authDAO.generateNewAuthToken());
            this.authDAO.addAuthData(newAuthData);
            return new LoginResult(newAuthData.getUsername(), newAuthData.getAuthToken());
        } else {
            throw new Exception("Password incorrect");
        }
    }

    /**
     * logs a user out
     * @param logoutRequest LogoutRequest Object containing the authToken of the user
     */
    public void logout(LogoutRequest logoutRequest) throws Exception{
        this.authDAO.getAuthData(logoutRequest.getAuthToken());
        this.authDAO.deleteAuthData(logoutRequest.getAuthToken());
    }
}
