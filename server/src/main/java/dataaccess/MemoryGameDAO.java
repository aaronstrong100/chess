package dataaccess;

import java.util.ArrayList;
import java.util.ListIterator;

import chess.ChessGame;

import model.GameData;

public class MemoryGameDAO implements GameDAO{
    private ArrayList<GameData> gameData;
    private int nextID;

    public MemoryGameDAO(){
        this.gameData = new ArrayList<GameData>();
        this.nextID = 0;
    }
    /**
     *
     * @return all current games in the database as GameData objects
     */
    @Override
    public ArrayList<GameData> getCurrentGames()  throws DataAccessException{
        return this.gameData;
    }
    /**
     *
     * @param gameID the id of the game you are trying to get
     * @return GameData object representing the game you are trying to get
     */
    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        for(GameData game: this.gameData){
            if(game.getGameID() == gameID){
                return game;
            }
        }
        throw new DataAccessException("Game with ID: " + gameID + " does not exist");
    }
    /**
     * creates a game with the given name
     * @param gameName name of the new game
     */
    @Override
    public int createGame(String gameName){
        GameData newGame = new GameData(nextID, "None", "None", gameName, new ChessGame());
        this.gameData.add(newGame);
        nextID++;
        return newGame.getGameID();
    }
    /**
     * updates a game by replacing the GameData with a new GameData Object
     * @param gameID ID of the game to update
     * @param updatedGame an updated version of the game
     */
    @Override
    public void overwriteGame(int gameID, GameData updatedGame)  throws DataAccessException{
        ListIterator<GameData> gameDataIterator = this.gameData.listIterator();
        while(gameDataIterator.hasNext()){
            GameData game = gameDataIterator.next();
            if(gameID == game.getGameID()){
                gameDataIterator.set(updatedGame);
                return;
            }
        }
        throw new DataAccessException("The game does not exist");
    }
    /**
     * Clear all GameData from the database
     */
    @Override
    public void clearDataBase(){
        this.gameData = new ArrayList<>();
    }
}
