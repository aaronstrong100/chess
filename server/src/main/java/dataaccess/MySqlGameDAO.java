package dataaccess;
import chess.ChessGame;
import com.google.gson.Gson;
import model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class MySqlGameDAO implements GameDAO{

    @Override
    public ArrayList<GameData> getCurrentGames() {
        Gson gson = new Gson();
        ArrayList<GameData> currentGames = new ArrayList<>();
        try(var conn = DatabaseManager.getConnection()) {
            var getUserStatement = "SELECT gameID, white_username, black_username, game_name, chess_game FROM game_data";
            try (var preparedGetUserStatement = conn.prepareStatement(getUserStatement)) {
                try (var rs = preparedGetUserStatement.executeQuery()) {
                    if (rs.next()) {
                        int gameID = rs.getInt("gameID");
                        String whiteUsername = rs.getString("white_username");
                        String blackUsername = rs.getString("black_username");
                        String gameName = rs.getString("game_name");
                        String chessGameJson = rs.getString("chess_game");
                        ChessGame chessGame = gson.fromJson(chessGameJson, ChessGame.class);
                        currentGames.add(new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame));
                    }
                } catch (Exception e){
                    throw new UnauthorizedException("The user does not exist");
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error accessing database: " + e.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error connecting to database: " + e.getMessage());
        }
        return currentGames;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public int createGame(String gameName) {
        Gson gson = new Gson();
        try (var conn = DatabaseManager.getConnection()) {
            var addGameStatement = "INSERT INTO game_data (white_username, black_username, game_name, chess_game) VALUES (?,?,?,?)";
            try (var preparedAddGameStatement = conn.prepareStatement(addGameStatement, RETURN_GENERATED_KEYS)) {
                preparedAddGameStatement.setString(1, null);
                preparedAddGameStatement.setString(2, null);
                preparedAddGameStatement.setString(3, gameName);
                preparedAddGameStatement.setString(4, gson.toJson(new ChessGame()));
                preparedAddGameStatement.executeUpdate();

                ResultSet rs = preparedAddGameStatement.getGeneratedKeys();
                if(rs.next()){
                    return rs.getInt(1);
                }

                return 0;
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
