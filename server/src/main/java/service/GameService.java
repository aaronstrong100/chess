package service;

import Requests.CreateGameRequest;
import Requests.JoinGameRequest;
import Requests.ListGamesRequest;
import Results.CreateGameResult;
import Results.JoinGameResult;
import Results.ListGamesResult;
import dataaccess.GameDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.DataAccessException;

public class GameService {
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    public GameService(GameDAO gameDAO, AuthDAO authDAO){
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }
    public ListGamesResult listGames(ListGamesRequest gameListRequest) throws DataAccessException{
        this.authDAO.getAuthData(gameListRequest.getAuthToken());
        return new ListGamesResult(this.gameDAO.getCurrentGames());
    }
    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws DataAccessException{
        this.authDAO.getAuthData(createGameRequest.getAuthToken());
        return new CreateGameResult(this.gameDAO.createGame(createGameRequest.getGameName()));
    }
    public JoinGameResult joinGame(JoinGameRequest joinGameRequest) {
        return null;
    }
}
