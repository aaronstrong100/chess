package dataaccess;

import java.util.ArrayList;

import model.GameData;

public interface GameDAO {
    /**
     *
     * @return all current games in the database as GameData objects
     */
    public ArrayList<GameData> getCurrentGames() throws DataAccessException;

    /**
     *
     * @param gameID the id of the game you are trying to get
     * @return GameData object representing the game you are trying to get
     */
    public GameData getGame(int gameID) throws DataAccessException;

    /**
     * creates a game with the given name
     * @param gameName name of the new game
     */
    public int createGame(String gameName);

    /**
     * updates a game by replacing the GameData with a new GameData Object
     * @param gameID ID of the game to update
     * @param updatedGame an updated version of the game
     */
    public void overwriteGame(int gameID, GameData updatedGame) throws DataAccessException;

    /**
     * Clear all GameData from the database
     */
    public void clearDataBase();
}
