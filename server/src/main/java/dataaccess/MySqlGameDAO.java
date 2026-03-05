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
        String[] deleteStatements = {
                "SET FOREIGN_KEY_CHECKS = 0",
                "TRUNCATE game_data",
                "SET FOREIGN_KEY_CHECKS = 1"
        };
        try(var conn = DatabaseManager.getConnection()) {
            for(String deleteStatement : deleteStatements) {
                try (var preparedDeleteStatement = conn.prepareStatement(deleteStatement)) {
                    preparedDeleteStatement.executeUpdate();
                } catch (SQLException e) {
                    throw new RuntimeException("Error accessing database: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error connecting to database: " + e.getMessage());
        }
    }
}
