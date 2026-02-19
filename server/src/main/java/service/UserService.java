package service;

import Requests.LoginRequest;
import Requests.LogoutRequest;
import Requests.RegisterRequest;
import Results.LoginResult;
import Results.RegisterResult;

public class UserService {
    /**
     * register a new user
     * @param registerRequest RegisterRequest object containing the username and password of the new user
     * @return a RegisterResult Object containing the new authToken for the user
     */
    public RegisterResult register(RegisterRequest registerRequest){
        return null;
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
