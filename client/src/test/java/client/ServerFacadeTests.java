package client;

import dataaccess.*;
import org.junit.jupiter.api.*;
import results.*;
import server.Server;
import serverAccess.ServerFacade;
import requests.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private String demoUsername;
    private String demoAuthToken;
    private String demoGameName;
    int demoGameID;


        @BeforeEach
        public void setup() throws Exception{
            demoUsername = "demoUsername";
            demoGameName = "demoGame";
            server = new Server();
            int port = server.run(0);
            serverFacade = new ServerFacade(server);
            serverFacade.clearDataBase();
            RegisterRequest registerRequest = new RegisterRequest(demoUsername,"password","email");
            try {
                RegisterResult registerResult = serverFacade.register(registerRequest);
                demoAuthToken = registerResult.getAuthToken();
                CreateGameRequest createGameRequest = new CreateGameRequest(demoGameName, demoAuthToken);
                CreateGameResult createGameResult = serverFacade.createGame(createGameRequest);
                demoGameID = createGameResult.getGameID();
            } catch (Exception e) {
                Assertions.fail(e.getMessage());
            }
        }

        @AfterEach
        public void stop(){
            server.stop();
        }


        @Test
        @Order(1)
        @DisplayName("serverFacade: Register normal")
        public void registerUserSuccess(){
            RegisterRequest registerRequest = new RegisterRequest("username","password","email");
            try {
                RegisterResult registerResult = serverFacade.register(registerRequest);
                Assertions.assertEquals(registerRequest.getUsername(), registerResult.getUsername());
                Assertions.assertNotNull(registerResult.getAuthToken());
            } catch (Exception e) {
                Assertions.fail(e.getMessage());
            }
        }

        @Test
        @Order(2)
        @DisplayName("serverFacade: Register duplicate")
        public void registerUserDuplicate(){
            RegisterRequest registerRequest = new RegisterRequest("username","password","email");
            try {
                serverFacade.register(registerRequest);
                RegisterResult registerResult = serverFacade.register(registerRequest);
                Assertions.fail("Registering a duplicate user should throw an exception");
            } catch (Exception e) {}
        }

        @Test
        @Order(3)
        @DisplayName("serverFacade: Login normal")
        public void loginUserSuccess(){
            LoginRequest loginRequest = new LoginRequest(demoUsername,"password");
            try {
                LoginResult loginResult = serverFacade.login(loginRequest);
                Assertions.assertEquals(loginRequest.getUsername(), loginResult.getUsername());
                Assertions.assertNotNull(loginResult.getAuthToken());
            } catch (Exception e) {
                Assertions.fail(e.getMessage());
            }
        }

        @Test
        @Order(4)
        @DisplayName("serverFacade: Login username doesn't exist")
        public void loginUserIncorrectUsername(){
            LoginRequest loginRequest = new LoginRequest("newUsername","password");
            try {
                LoginResult loginResult = serverFacade.login(loginRequest);
                Assertions.fail("Exception should be thrown when unrecognized username is attempting to log in");
            } catch (Exception e) {}
        }

        @Test
        @Order(5)
        @DisplayName("serverFacade: Logout normal")
        public void logoutUserSuccess(){
            LogoutRequest logoutRequest = new LogoutRequest(demoAuthToken);
            try {
                serverFacade.logout(logoutRequest);
            } catch (Exception e) {}
        }

        @Test
        @Order(6)
        @DisplayName("serverFacade: Logout wrong authToken")
        public void createGameSuccess(){
            LogoutRequest logoutRequest = new LogoutRequest("wrongAuthToken");
            try {
                serverFacade.logout(logoutRequest);
                Assertions.fail("Exception should be thrown when unrecognized authToken is attempting to log out");
            } catch (Exception e) {}
        }

        @Test
        @Order(7)
        @DisplayName("serverFacade: Create game normal")
        public void createGameInvalidAuthToken(){
            CreateGameRequest createGameRequest = new CreateGameRequest(demoGameName, demoAuthToken);
            try {
                serverFacade.createGame(createGameRequest);
            } catch (Exception e) {
                Assertions.fail(e.getMessage());
            }
        }

        @Test
        @Order(8)
        @DisplayName("serverFacade: Create game wrong authToken")
        public void createGameWrongAuthToken(){
            CreateGameRequest createGameRequest = new CreateGameRequest(demoGameName, "wrongAuthToken");
            try {
                serverFacade.createGame(createGameRequest);
                Assertions.fail("Exception should be thrown when unrecognized authToken is attempting to create new game");
            } catch (Exception e) {}
        }

        @Test
        @Order(9)
        @DisplayName("serverFacade: list one game")
        public void listGamesSuccess(){
            ListGamesRequest listGamesRequest = new ListGamesRequest(demoAuthToken);
            try {
                ListGamesResult listGamesResult = serverFacade.listGames(listGamesRequest);
                Assertions.assertEquals(listGamesResult.getGames().size(),1);
            } catch (Exception e) {
                Assertions.fail(e.getMessage());
            }
        }

        @Test
        @Order(10)
        @DisplayName("serverFacade: Create game wrong authToken")
        public void listGamesIncorrectAuthToken(){
            ListGamesRequest listGamesRequest = new ListGamesRequest("wrongAuthToken");
            try {
                ListGamesResult listGamesResult = serverFacade.listGames(listGamesRequest);
                Assertions.fail("Exception should be thrown when unrecognized authToken is attempting to list games");
            } catch (Exception e) {}
        }

        @Test
        @Order(11)
        @DisplayName("serverFacade: join game normal")
        public void joinGameSuccess(){
            JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", demoGameID, demoAuthToken);
            try {
                JoinGameResult joinGameResult = serverFacade.joinGame(joinGameRequest);
                Assertions.assertEquals(joinGameResult.getPlayerColor(), "WHITE");
                Assertions.assertEquals(joinGameResult.getGameID(), demoGameID);
            } catch (Exception e) {
                Assertions.fail(e.getMessage());
            }
        }

        @Test
        @Order(12)
        @DisplayName("serverFacade: Join game that does not exist")
        public void joinNonexistentGame(){
            JoinGameRequest joinGameRequest = new JoinGameRequest("WHITE", 500, demoAuthToken);
            try {
                JoinGameResult joinGameResult = serverFacade.joinGame(joinGameRequest);
                Assertions.fail("Exception should be thrown when user attempts to join game with unknown gameID");
            } catch (Exception e) {}
        }

        @Test
        @Order(13)
        @DisplayName("serverFacade: delete database")
        public void deleteSuccess() throws Exception{
            serverFacade.clearDataBase();
            Assertions.assertThrows(
                    Exception.class,
                    () -> {
                        serverFacade.listGames(new ListGamesRequest(demoAuthToken));
                    }
            );
        }
    }
