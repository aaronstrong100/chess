package service;

import Requests.DeleteRequest;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class DeleteService {
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private UserDAO userDAO;
    public DeleteService(GameDAO gameDAO, AuthDAO authDAO, UserDAO userDAO){
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }
    public void delete(){
        this.gameDAO.clearDataBase();
        this.authDAO.clearDataBase();
        this.userDAO.clearDataBase();
    }
}
