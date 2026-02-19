package dataaccess;

import model.GameData;

public class MemoryGameDAO implements GameDAO{
    @Override
    public GameData[] getCurrentGames()  throws DataAccessException{
        return new GameData[0];
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public void createGame(String gameName)  throws DataAccessException{

    }

    @Override
    public void overwriteGame(int gameID, GameData updatedGame)  throws DataAccessException{

    }

    @Override
    public void clearDataBase()  throws DataAccessException{

    }
}
