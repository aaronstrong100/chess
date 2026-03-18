package dataaccess;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import org.junit.jupiter.api.*;
import model.*;
import shared_exceptions.DataAccessException;
import shared_exceptions.UnauthorizedException;

import java.util.ArrayList;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SqlDAOTests {
    private static GameDAO gameDAO;
    private static AuthDAO authDAO;
    private static UserDAO userDAO;
    private static String demoUsername;
    private static String demoPassword;
    private static String demoEmail;
    private static String demoAuthToken;
    private static String demoGameName;
    private static int demoGameID;
    private static ChessGame demoChessGame;
    private static ChessGame demoChessGameUpdate;


    private static String wrongUsername;
    private static String wrongPassword;
    private static String wrongAuthToken;
    private static int wrongGameID;


    @BeforeAll
    public static void initialize() {
        gameDAO = new MySqlGameDAO();
        authDAO = new MySqlAuthDAO();
        userDAO = new MySqlUserDAO();
        demoUsername = "demoUsername";
        demoPassword = "demoPassword";
        demoEmail = "demoEmail";
        demoAuthToken = authDAO.generateNewAuthToken();
        demoGameName = "demoGame";
        demoChessGame = new ChessGame();
        demoChessGameUpdate = new ChessGame();
        try {
            demoChessGameUpdate.makeMove(new ChessMove(new ChessPosition(2, 1), new ChessPosition(3, 1)));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        wrongUsername = "wrongUsername";
        wrongPassword = "wrongPassword";
        wrongAuthToken = "wrongAuthToken";
        wrongGameID = -1;
    }

    @BeforeEach
    public void setup(){
        userDAO.clearDataBase();
        authDAO.clearDataBase();
        gameDAO.clearDataBase();
        UserData userData = new UserData(demoUsername, demoPassword, demoEmail);
        userDAO.addUserData(userData);
        demoGameID = gameDAO.createGame(demoGameName);
    }

    @Test
    @Order(1)
    @DisplayName("UserDAO: addUser normal")
    public void addUserSuccess(){
        UserData userData = new UserData("newUsername", demoPassword, demoEmail);
        userDAO.addUserData(userData);
    }

    @Test
    @Order(2)
    @DisplayName("UserDAO: addUser null username")
    public void addUserFailure(){
        UserData userData = new UserData(null, demoPassword, demoEmail);
        Assertions.assertThrows(
                Exception.class,
                () -> {userDAO.addUserData(userData);}
        );
    }

    @Test
    @Order(3)
    @DisplayName("UserDAO: getUser normal")
    public void getUserSuccess() throws UnauthorizedException {
        userDAO.getUserData(demoUsername);
    }

    @Test
    @Order(4)
    @DisplayName("UserDAO: getUser null username")
    public void getUserFailure(){
        Assertions.assertThrows(
                Exception.class,
                () -> {userDAO.getUserData(wrongUsername);}
        );
    }

    @Test
    @Order(5)
    @DisplayName("AuthDAO: addAuth normal")
    public void addAuthSuccess() throws UnauthorizedException{
        AuthData authData = new AuthData(demoUsername, demoAuthToken);
        authDAO.addAuthData(authData);
    }

    @Test
    @Order(6)
    @DisplayName("AuthDAO: add authToken with username not in database")
    public void addAuthFailure(){
        AuthData authData = new AuthData(wrongUsername, demoAuthToken);
        Assertions.assertThrows(
                Exception.class,
                () -> {authDAO.addAuthData(authData);}
        );
    }

    @Test
    @Order(7)
    @DisplayName("AuthDAO: getAuth normal")
    public void getAuthSuccess() throws UnauthorizedException{
        AuthData authData = new AuthData(demoUsername, demoAuthToken);
        authDAO.addAuthData(authData);
        AuthData resultAuthData = authDAO.getAuthData(demoAuthToken);
        Assertions.assertEquals(authData.getUsername(), resultAuthData.getUsername());
    }

    @Test
    @Order(8)
    @DisplayName("AuthDAO: get authToken that doesn't exist")
    public void getAuthFailure(){
        AuthData authData = new AuthData(demoUsername, demoAuthToken);
        authDAO.addAuthData(authData);
        Assertions.assertThrows(
                Exception.class,
                () -> {authDAO.getAuthData(wrongAuthToken);}
        );
    }

    @Test
    @Order(7)
    @DisplayName("AuthDAO: deleteAuth normal")
    public void deleteAuthSuccess() throws UnauthorizedException{
        AuthData authData = new AuthData(demoUsername, demoAuthToken);
        authDAO.addAuthData(authData);
        authDAO.deleteAuthData(demoAuthToken);
    }

    @Test
    @Order(8)
    @DisplayName("AuthDAO: get authToken after deleting it")
    public void deleteAuthFailure(){
        AuthData authData = new AuthData(demoUsername, demoAuthToken);
        authDAO.addAuthData(authData);
        authDAO.deleteAuthData(demoAuthToken);
        Assertions.assertThrows(
                Exception.class,
                () -> {authDAO.getAuthData(demoAuthToken);}
        );
    }

    @Test
    @Order(9)
    @DisplayName("GameDAO: createGame normal")
    public void createGameSuccess() throws UnauthorizedException{
        Assertions.assertTrue(gameDAO.createGame("newGameName")>0);
    }

    @Test
    @Order(10)
    @DisplayName("GameDAO: create game with null gameName")
    public void createGameFailure(){
        Assertions.assertThrows(
                Exception.class,
                () -> {gameDAO.createGame(null);}
        );
    }

    @Test
    @Order(11)
    @DisplayName("GameDAO: listGames normal")
    public void listGamesSuccess() throws UnauthorizedException{
        ArrayList<GameData> gameDatas = gameDAO.getCurrentGames();
        Assertions.assertEquals(gameDatas.size(),1);
        Assertions.assertEquals(gameDatas.get(0).getGameName(), demoGameName);
    }

    @Test
    @Order(12)
    @DisplayName("GameDAO: listGames multiple")
    public void listMultipleGamesSuccess() throws UnauthorizedException{
        for(int i = 0; i<5; i++){
            gameDAO.createGame(demoGameName);
        }
        ArrayList<GameData> gameDatas = gameDAO.getCurrentGames();
        Assertions.assertEquals(6,gameDatas.size());
        Assertions.assertEquals(gameDatas.get(0).getGameName(), demoGameName);
    }

    @Test
    @Order(13)
    @DisplayName("GameDAO: list games persistent")
    public void listGamesPersistent(){
        gameDAO.clearDataBase();
        gameDAO.createGame(demoGameName);
        gameDAO = new MySqlGameDAO();
        ArrayList<GameData> gameDatas = gameDAO.getCurrentGames();
        Assertions.assertEquals(1, gameDatas.size());
        Assertions.assertEquals(gameDatas.get(0).getGameName(), demoGameName);
    }

    @Test
    @Order(13)
    @DisplayName("GameDAO: getGame normal")
    public void getGameSuccess() throws DataAccessException {
        GameData gameData = gameDAO.getGame(demoGameID);
        Assertions.assertEquals(gameData.getGameName(), demoGameName);
        Assertions.assertEquals(gameData.getGame(), demoChessGame);
    }

    @Test
    @Order(14)
    @DisplayName("GameDAO: get game with wrong gameID")
    public void getGameFailure(){
        Assertions.assertThrows(
                Exception.class,
                () -> {gameDAO.getGame(wrongGameID);}
        );
    }

    @Test
    @Order(15)
    @DisplayName("GameDAO: overwriteGame normal")
    public void overwriteGameSuccess() throws DataAccessException{
        GameData gameData = gameDAO.getGame(demoGameID);
        GameData updatedGameData = new GameData(gameData.getGameID(), gameData.getWhiteUsername(), gameData.getBlackUsername(),
                                                gameData.getGameName(), demoChessGameUpdate);
        gameDAO.overwriteGame(demoGameID, updatedGameData);
        gameData = gameDAO.getGame(demoGameID);
        Assertions.assertEquals(demoChessGameUpdate, gameData.getGame());
    }

    @Test
    @Order(16)
    @DisplayName("GameDAO: overwrite game with invalid gameID")
    public void overwriteGameFailure(){
        GameData gameData = null;
        try {
            gameData = gameDAO.getGame(demoGameID);
        } catch(Exception e) {}
        GameData updatedGameData = new GameData(wrongGameID, gameData.getWhiteUsername(), gameData.getBlackUsername(),
                                                gameData.getGameName(), demoChessGameUpdate);
        Assertions.assertThrows(
                Exception.class,
                () -> {gameDAO.overwriteGame(wrongGameID, updatedGameData);}
        );
    }
}
