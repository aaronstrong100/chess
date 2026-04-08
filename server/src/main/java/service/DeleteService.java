package service;

import websocket.ConnectionManager;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;

public class DeleteService {
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    private UserDAO userDAO;
    private ConnectionManager connectionManager;
    public DeleteService(GameDAO gameDAO, AuthDAO authDAO, UserDAO userDAO){
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }
    public DeleteService(GameDAO gameDAO, AuthDAO authDAO, UserDAO userDAO, ConnectionManager connectionManager){
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.connectionManager = connectionManager;
    }
    public void delete(){
        this.gameDAO.clearDataBase();
        this.authDAO.clearDataBase();
        this.userDAO.clearDataBase();
        if(this.connectionManager!=null){
            this.connectionManager.clearConnections();
        }
    }
}
