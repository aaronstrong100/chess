package dataaccess;
import chess.ChessGame;
import com.google.gson.Gson;
import model.*;
import exceptions.DataAccessException;
import exceptions.UnauthorizedException;

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
            var getGameStatement = "SELECT gameID, white_username, black_username, game_name, chess_game FROM game_data";
            try (var preparedGetGameStatement = conn.prepareStatement(getGameStatement)) {
                try (var rs = preparedGetGameStatement.executeQuery()) {
                    while (rs.next()) {
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
        Gson gson = new Gson();
        try(var conn = DatabaseManager.getConnection()) {
            var getGameStatement = "SELECT gameID, white_username, black_username, game_name, chess_game FROM game_data WHERE gameID=?";
            try (var preparedGetGameStatement = conn.prepareStatement(getGameStatement)) {
                preparedGetGameStatement.setInt(1, gameID);
                try (var rs = preparedGetGameStatement.executeQuery()) {
                    if (rs.next()) {
                        String whiteUsername = rs.getString("white_username");
                        String blackUsername = rs.getString("black_username");
                        String gameName = rs.getString("game_name");
                        String chessGameJson = rs.getString("chess_game");
                        ChessGame chessGame = gson.fromJson(chessGameJson, ChessGame.class);
                        return new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
                    }
                    else{
                        throw new UnauthorizedException("Game with ID: "+gameID+" does not exist");
                    }
                } catch (Exception e){
                    throw new UnauthorizedException("Game with ID: "+gameID+" does not exist");
                }
            } catch (SQLException e) {
                throw new RuntimeException("Error accessing database: " + e.getMessage());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error connecting to database: " + e.getMessage());
        }
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

                throw new RuntimeException("was unable to create game");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void overwriteGame(int gameID, GameData updatedGame) throws DataAccessException {
        Gson gson = new Gson();
        try (var conn = DatabaseManager.getConnection()) {
            var overwriteGameStatement = "UPDATE game_data SET white_username=?, black_username=?, game_name=?, chess_game=? WHERE gameID=?";
            try (var preparedOverwriteGameStatement = conn.prepareStatement(overwriteGameStatement)) {
                preparedOverwriteGameStatement.setString(1, updatedGame.getWhiteUsername());
                preparedOverwriteGameStatement.setString(2, updatedGame.getBlackUsername());
                preparedOverwriteGameStatement.setString(3, updatedGame.getGameName());
                preparedOverwriteGameStatement.setString(4, gson.toJson(updatedGame.getGame()));
                preparedOverwriteGameStatement.setInt(5, gameID);
                int rowsUpdated = preparedOverwriteGameStatement.executeUpdate();
                if(rowsUpdated==0){
                    throw new DataAccessException("Game with ID: "+gameID+" does not exist");
                }
            } catch (SQLException e){
                throw new DataAccessException("Game with ID: "+gameID+" does not exist");
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void clearDataBase() {
        DatabaseManager.clearTable("game_data");
    }
}
