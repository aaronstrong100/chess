package server;

import requests.LoginRequest;
import requests.RegisterRequest;
import requests.LogoutRequest;
import requests.ListGamesRequest;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.DeleteRequest;
import results.LoginResult;
import results.RegisterResult;
import results.ListGamesResult;
import results.CreateGameResult;
import results.JoinGameResult;
import com.google.gson.*;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.DataAccessException;
import dataaccess.UnauthorizedException;
import dataaccess.AlreadyTakenException;
import io.javalin.*;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import service.UserService;
import service.GameService;
import service.DeleteService;

public class Server {

    private final Javalin javalin;

    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    private UserService userService;
    private GameService gameService;
    private DeleteService deleteService;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();

        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);
        deleteService = new DeleteService(gameDAO, authDAO, userDAO);

        javalin.post("/user", new RegisterHandler(userService));

        javalin.post("/session", new LoginHandler(userService));

        javalin.delete("/session", new LogoutHandler(userService));

        javalin.get("/game", new ListGamesHandler(gameService));

        javalin.post("/game", new CreateGameHandler(gameService));

        javalin.put("/game", new JoinGameHandler(gameService));

        javalin.delete("/db", new ClearDataBaseHandler(deleteService));
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    public static int getErrorCode(Exception e){
        if(e instanceof UnauthorizedException){
            return 401;
        } else if(e instanceof AlreadyTakenException){
            return 403;
        }
        return 400;
    }

    public static void handleException(Exception e, Context context){
        System.out.println(e.getMessage());
        context.status(Server.getErrorCode(e));
        context.json("{\"message\": \"Error: "+ e.getMessage().replace("\"","\\\"") + "\"}");
    }

    public static class RegisterHandler implements Handler {

        UserService userService;

        public RegisterHandler(UserService userService){
            this.userService = userService;
        }

        @Override
        public void handle(@NotNull Context context){
            Gson gson = new Gson();
            try {
                String jsonString = context.body();
                RegisterRequest registerRequest = gson.fromJson(jsonString, RegisterRequest.class);
                if(registerRequest.getUsername()==null || registerRequest.getPassword()==null || registerRequest.getEmail()==null){
                    throw new Exception("Bad Request");
                }
                RegisterResult registerResult = userService.register(registerRequest);
                context.json(gson.toJson(registerResult));
            } catch (Exception e){
                Server.handleException(e, context);
            }
        }
    }


    public static class LoginHandler implements Handler {

        UserService userService;

        public LoginHandler(UserService userService){
            this.userService = userService;
        }

        @Override
        public void handle(@NotNull Context context){
            Gson gson = new Gson();
            try {
                String jsonString = context.body();
                LoginRequest loginRequest = gson.fromJson(jsonString, LoginRequest.class);
                if(loginRequest.getUsername()==null || loginRequest.getPassword()==null){
                    throw new Exception("Bad Request");
                }
                LoginResult loginResult = userService.login(loginRequest);
                context.json(gson.toJson(loginResult));
            } catch (Exception e){
                Server.handleException(e, context);
            }
        }
    }

    public static class LogoutHandler implements Handler {

        UserService userService;

        public LogoutHandler(UserService userService){
            this.userService = userService;
        }

        @Override
        public void handle(@NotNull Context context){
            Gson gson = new Gson();
            try {
                String authToken = context.header("Authorization");
                LogoutRequest logoutRequest = new LogoutRequest(authToken);
                if(logoutRequest.getAuthToken()==null){
                    throw new Exception("Bad Request");
                }
                userService.logout(logoutRequest);
                context.json("{}").contentType("application/json");
            } catch (Exception e){
                Server.handleException(e, context);
            }
        }
    }

    public static class ListGamesHandler implements Handler {

        GameService gameService;

        public ListGamesHandler(GameService gameService){
            this.gameService = gameService;
        }

        @Override
        public void handle(@NotNull Context context){
            Gson gson = new Gson();
            try {
                String authToken = context.header("Authorization");
                ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
                if(listGamesRequest.getAuthToken()==null){
                    throw new Exception("Bad Request");
                }
                ListGamesResult listGamesResult = gameService.listGames(listGamesRequest);
                context.json(gson.toJson(listGamesResult));
            } catch (Exception e){
                Server.handleException(e, context);
            }
        }
    }

    public static class CreateGameHandler implements Handler {

        GameService gameService;

        public CreateGameHandler(GameService gameService){
            this.gameService = gameService;
        }

        @Override
        public void handle(@NotNull Context context){
            Gson gson = new Gson();
            try {
                String authToken = context.header("Authorization");
                String gameName = JsonParser.parseString(context.body()).getAsJsonObject().get("gameName").getAsString();
                CreateGameRequest createGameRequest = new CreateGameRequest(gameName, authToken);
                if(createGameRequest.getGameName()==null || createGameRequest.getAuthToken()==null){
                    throw new Exception("Bad Request");
                }
                CreateGameResult createGameResult = gameService.createGame(createGameRequest);
                context.json(gson.toJson(createGameResult));
            } catch (Exception e){
                Server.handleException(e, context);
            }
        }
    }

    public static class JoinGameHandler implements Handler {

        GameService gameService;

        public JoinGameHandler(GameService gameService){
            this.gameService = gameService;
        }

        @Override
        public void handle(@NotNull Context context){
            Gson gson = new Gson();
            try {
                String authToken = context.header("Authorization");
                int gameID = JsonParser.parseString(context.body()).getAsJsonObject().get("gameID").getAsInt();
                String playerColor = JsonParser.parseString(context.body()).getAsJsonObject().get("playerColor").getAsString();
                JoinGameRequest joinGameRequest = new JoinGameRequest(playerColor, gameID, authToken);
                if(joinGameRequest.getPlayerColor()==null || joinGameRequest.getAuthToken()==null){
                    throw new Exception("Bad Request");
                }
                JoinGameResult joinGameResult = this.gameService.joinGame(joinGameRequest);
                context.json(gson.toJson(joinGameResult));
            } catch (Exception e){
                Server.handleException(e, context);
            }
        }
    }

    public static class ClearDataBaseHandler implements Handler {

        DeleteService deleteService;

        public ClearDataBaseHandler(DeleteService deleteService){
            this.deleteService = deleteService;
        }

        @Override
        public void handle(@NotNull Context context) {
            try {
                this.deleteService.delete();
            } catch (Exception e){
                Server.handleException(e, context);
            }
        }
    }
}
