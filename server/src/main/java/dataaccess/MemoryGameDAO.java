package dataaccess;

import chess.ChessGame;

import model.GameData;

public class MemoryGameDAO implements GameDAO{
    private GameData[] gameData;
    /**
     *
     * @return all current games in the database as GameData objects
     */
    @Override
    public GameData[] getCurrentGames()  throws DataAccessException{
        return this.gameData;
    }
    /**
     *
     * @param gameID the id of the game you are trying to get
     * @return GameData object representing the game you are trying to get
     */
    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }
    /**
     * creates a game with the given name
     * @param gameName name of the new game
     */
    @Override
    public void createGame(String gameName)  throws DataAccessException{

    }
    /**
     * updates a game by replacing the GameData with a new GameData Object
     * @param gameID ID of the game to update
     * @param updatedGame an updated version of the game
     */
    @Override
    public void overwriteGame(int gameID, GameData updatedGame)  throws DataAccessException{

    }
    /**
     * Clear all GameData from the database
     */
    @Override
    public void clearDataBase()  throws DataAccessException{

    }
}
