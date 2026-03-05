package dataaccess;
import model.*;

import java.sql.SQLException;
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
        try(var conn = DatabaseManager.getConnection()) {
            var deleteStatement = "TRUNCATE game_data";
            try (var preparedDeleteStatement = conn.prepareStatement(deleteStatement)) {
                preparedDeleteStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException("Error accessing database");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error connecting to database");
        }
    }
}
