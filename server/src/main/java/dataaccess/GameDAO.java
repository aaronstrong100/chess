package dataaccess;

import model.GameData;

public interface GameDAO {
    public GameData[] getCurrentGames();
    public GameData getGame(int gameID);
    public void createGame(String gameName);
    public void overwriteGame(int gameID, GameData updatedGame);
    public void clearDataBase();
}
