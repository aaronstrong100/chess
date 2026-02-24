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
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
    }
    /**
     * register a new user
     * @param registerRequest RegisterRequest object containing the username and password of the new user
     * @return a RegisterResult Object containing the new authToken for the user
     */
    public RegisterResult register(RegisterRequest registerRequest) throws Exception{
        try{
            userDAO.getUserData(registerRequest.getUsername());
            throw new Exception("The username is already taken");
        } catch(DataAccessException e){
            userDAO.addUserData(new UserData(registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.getEmail()));
            AuthData returnData = authDAO.addAuthData(new AuthData(registerRequest.getUsername(), authDAO.generateNewAuthToken()));
            return new RegisterResult(returnData.getUsername(), returnData.getAuthToken());
        }
    }

    /**
     * log a user in
     * @param loginRequest LoginRequest Object containing the username and password of the user
     * @return a LoginResult containing the authToken for the user
     */
    public LoginResult login(LoginRequest loginRequest){
        return null;
    }

    /**
     * logs a user out
     * @param logoutRequest LogoutRequest Object containing the authToken of the user
     */
    public void logout(LogoutRequest logoutRequest){

    }
}
