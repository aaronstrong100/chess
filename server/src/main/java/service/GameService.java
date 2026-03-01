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

import model.GameData;

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
    public JoinGameResult joinGame(JoinGameRequest joinGameRequest) throws DataAccessException, Exception{
        String username = this.authDAO.getAuthData(joinGameRequest.getAuthToken()).getUsername();
        GameData gameData = this.gameDAO.getGame(joinGameRequest.getGameID());
        if(joinGameRequest.getPlayerColor().equalsIgnoreCase("WHITE")){
            if(gameData.getWhiteUsername()=="None"){
                this.gameDAO.overwriteGame(joinGameRequest.getGameID(),gameData.updateWhiteUsername(username));
                return new JoinGameResult(joinGameRequest.getPlayerColor(), joinGameRequest.getGameID());
            }else {
                throw new Exception("Team WHITE is already taken");
            }
        } else if (joinGameRequest.getPlayerColor().equalsIgnoreCase("BLACK")){
            if(gameData.getBlackUsername()=="None"){
                this.gameDAO.overwriteGame(joinGameRequest.getGameID(),gameData.updateBlackUsername(username));
                return new JoinGameResult(joinGameRequest.getPlayerColor(), joinGameRequest.getGameID());
            }else {
                throw new Exception("Team BLACK is already taken");
            }
        } else {
            throw new Exception("Value of playerColor must be WHITE or BLACK");
        }
    }
}
