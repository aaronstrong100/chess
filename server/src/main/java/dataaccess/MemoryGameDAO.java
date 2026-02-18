package dataaccess;

import model.GameData;

public class MemoryGameDAO implements GameDAO{
    @Override
    public GameData[] getCurrentGames() {
        return new GameData[0];
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public void createGame(String gameName) {

    }

    @Override
    public void overwriteGame(int gameID, GameData updatedGame) {

    }

    @Override
    public void clearDataBase() {

    }
}
