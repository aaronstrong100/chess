package dataaccess;
import model.*;

import java.util.ArrayList;

public class MySqlGameDAO implements GameDAO{
    @Override
    public ArrayList<GameData> getCurrentGames() {
        return null;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public int createGame(String gameName) {
        return 0;
    }

    @Override
    public void overwriteGame(int gameID, GameData updatedGame) throws DataAccessException {

    }

    @Override
    public void clearDataBase() {

    }
}
