package dataaccess;
import chess.ChessGame;
import com.google.gson.Gson;
import model.*;

import java.sql.SQLException;
import java.util.ArrayList;

public class MySqlGameDAO implements GameDAO{
    private int nextID;

    public MySqlGameDAO(){
        nextID = 0;
    }

    @Override
    public ArrayList<GameData> getCurrentGames() {
        return null;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    private int getNextID(){
        nextID++;
        return nextID;
    }

    @Override
    public int createGame(String gameName) {
        GameData gameData = new GameData(getNextID(), null, null, gameName, new ChessGame());
        Gson gson = new Gson();
        try (var conn = DatabaseManager.getConnection()) {
            var addUserStatement = "INSERT INTO game_data (gameID, white_username, black_username, game_name, chess_game) VALUES(?,?,?,?,?)";
            try (var preparedAddUserStatement = conn.prepareStatement(addUserStatement)) {
                preparedAddUserStatement.setInt(1, gameData.getGameID());
                preparedAddUserStatement.setString(2, gameData.getWhiteUsername());
                preparedAddUserStatement.setString(3, gameData.getBlackUsername());
                preparedAddUserStatement.setString(4, gameData.getGameName());
                preparedAddUserStatement.setString(5, gson.toJson(gameData.getGame()));
                preparedAddUserStatement.executeUpdate();
                return gameData.getGameID();
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
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
