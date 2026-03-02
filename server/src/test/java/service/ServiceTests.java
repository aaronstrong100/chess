package service;

import org.junit.jupiter.api.*;
import passoff.model.*;
import service.*;
import requests.*;
import results.*;
import dataaccess.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceTests {
    GameDAO gameDAO;
    AuthDAO authDAO;
    UserDAO userDAO;
    UserService userService;
    GameService gameService;
    DeleteService deleteService;
    String demoUsername;
    String demoAuthToken;
    String demoGameName;
    int demoGameID;


    @BeforeEach
    public void setup() {
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
        userDAO = new MemoryUserDAO();
        demoUsername = "demoUsername";
        demoGameName = "demoGame";
        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);
        deleteService = new DeleteService(gameDAO, authDAO, userDAO);
        RegisterRequest registerRequest = new RegisterRequest(demoUsername,"password","email");
        try {
            RegisterResult registerResult = userService.register(registerRequest);
            demoAuthToken = registerResult.getAuthToken();
            CreateGameRequest createGameRequest = new CreateGameRequest(demoGameName, demoAuthToken);
            CreateGameResult createGameResult = gameService.createGame(createGameRequest);
            demoGameID = createGameResult.getGameID();
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }


    @Test
    @Order(1)
    @DisplayName("UserService: Register normal")
    public void registerUserSuccess(){
        RegisterRequest registerRequest = new RegisterRequest("username","password","email");
        try {
            RegisterResult registerResult = userService.register(registerRequest);
            Assertions.assertEquals(registerRequest.getUsername(), registerResult.getUsername());
            Assertions.assertNotNull(registerResult.getAuthToken());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @Order(2)
    @DisplayName("UserService: Register duplicate")
    public void registerUserDuplicate(){
        RegisterRequest registerRequest = new RegisterRequest("username","password","email");
        try {
            userService.register(registerRequest);
            RegisterResult registerResult = userService.register(registerRequest);
            Assertions.fail("Registering a duplicate user should throw an exception");
        } catch (Exception e) {}
    }

    @Test
    @Order(3)
    @DisplayName("UserService: Login normal")
    public void loginUserSuccess(){
        LoginRequest loginRequest = new LoginRequest(demoUsername,"password");
        try {
            LoginResult loginResult = userService.login(loginRequest);
            Assertions.assertEquals(loginRequest.getUsername(), loginResult.getUsername());
            Assertions.assertNotNull(loginResult.getAuthToken());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @Order(4)
    @DisplayName("UserService: Login username doesn't exist")
    public void loginUserIncorrectUsername(){
        LoginRequest loginRequest = new LoginRequest("newUsername","password");
        try {
            LoginResult loginResult = userService.login(loginRequest);
            Assertions.fail("Exception should be thrown when unrecognized username is attempting to log in");
        } catch (Exception e) {}
    }

    @Test
    @Order(5)
    @DisplayName("UserService: Logout normal")
    public void logoutUserSuccess(){
        LogoutRequest logoutRequest = new LogoutRequest(demoAuthToken);
        try {
            userService.logout(logoutRequest);
        } catch (Exception e) {}
    }

    @Test
    @Order(6)
    @DisplayName("UserService: Logout wrong authToken")
    public void createGameSuccess(){
        LogoutRequest logoutRequest = new LogoutRequest("wrongAuthToken");
        try {
            userService.logout(logoutRequest);
            Assertions.fail("Exception should be thrown when unrecognized authToken is attempting to log out");
        } catch (Exception e) {}
    }

    @Test
    @Order(7)
    @DisplayName("GameService: Create game normal")
    public void createGameInvalidAuthToken(){
        CreateGameRequest createGameRequest = new CreateGameRequest(demoGameName, demoAuthToken);
        try {
            gameService.createGame(createGameRequest);
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @Order(8)
    @DisplayName("GameService: Create game wrong authToken")
    public void createGameWrongAuthToken(){
        CreateGameRequest createGameRequest = new CreateGameRequest(demoGameName, "wrongAuthToken");
        try {
            gameService.createGame(createGameRequest);
            Assertions.fail("Exception should be thrown when unrecognized authToken is attempting to create new game");
        } catch (Exception e) {}
    }

    @Test
    @Order(9)
    @DisplayName("GameService: list one game")
    public void listGamesSuccess(){
        ListGamesRequest listGamesRequest = new ListGamesRequest(demoAuthToken);
        try {
            ListGamesResult listGamesResult = gameService.listGames(listGamesRequest);
            Assertions.assertEquals(listGamesResult.getGames().size(),1);
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @Order(10)
    @DisplayName("GameService: Create game wrong authToken")
    public void listGamesIncorrectAuthToken(){
        ListGamesRequest listGamesRequest = new ListGamesRequest("wrongAuthToken");
        try {
            ListGamesResult listGamesResult = gameService.listGames(listGamesRequest);
            Assertions.fail("Exception should be thrown when unrecognized authToken is attempting to list games");
        } catch (Exception e) {}
    }

    @Test
    @Order(11)
    @DisplayName("GameService: join game normal")
    public void joinGameSuccess(){
        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", demoGameID, demoAuthToken);
        try {
            JoinGameResult joinGameResult = gameService.joinGame(joinGameRequest);
            Assertions.assertEquals(joinGameResult.getPlayerColor(), "WHITE");
            Assertions.assertEquals(joinGameResult.getGameID(), demoGameID);
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @Order(12)
    @DisplayName("GameService: Join game that does not exist")
    public void joinNonexistentGame(){
        JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", 500, demoAuthToken);
        try {
            JoinGameResult joinGameResult = gameService.joinGame(joinGameRequest);
            Assertions.fail("Exception should be thrown when user attempts to join game with unknown gameID");
        } catch (Exception e) {}
    }

    @Test
    @Order(13)
    @DisplayName("DeleteService: delete database")
    public void deleteSuccess(){
        deleteService.delete();
        try{
            authDAO.getAuthData(demoAuthToken);
            Assertions.fail("delete should delete all authTokens");
        } catch (Exception e) {}
        try{
            userDAO.getUserData(demoUsername);
            Assertions.fail("delete should delete all users");
        } catch (Exception e) {}
        Assertions.assertEquals(gameDAO.getCurrentGames().size(), 0);
    }
}
