package service;

import requests.DeleteRequest;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import dataaccess.UnauthorizedException;

public class DeleteService {
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private UserDAO userDAO;
    public DeleteService(GameDAO gameDAO, AuthDAO authDAO, UserDAO userDAO){
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }
    public void delete(DeleteRequest deleteRequest) throws UnauthorizedException{
        this.authDAO.getAuthData(deleteRequest.getAuthToken());
        this.gameDAO.clearDataBase();
        this.authDAO.clearDataBase();
        this.userDAO.clearDataBase();
    }
}
