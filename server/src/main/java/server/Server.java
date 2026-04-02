package server;

import WebSocket.WebSocketHandler;
import requests.LoginRequest;
import requests.RegisterRequest;
import requests.LogoutRequest;
import requests.ListGamesRequest;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import results.LoginResult;
import results.RegisterResult;
import results.ListGamesResult;
import results.CreateGameResult;
import results.JoinGameResult;
import com.google.gson.*;
import dataaccess.*;
import io.javalin.*;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import org.jetbrains.annotations.NotNull;
import service.UserService;
import service.GameService;
import service.DeleteService;
import exceptions.AlreadyTakenException;
import exceptions.UnauthorizedException;

public class Server {

    private final Javalin javalin;

    private WebSocketHandler webSocketHandler;

    private UserDAO userDAO;
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    private UserService userService;
    private GameService gameService;
    private DeleteService deleteService;

    private int port;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here.
        userDAO = new MySqlUserDAO();
        authDAO = new MySqlAuthDAO();
        gameDAO = new MySqlGameDAO();

        userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);
        deleteService = new DeleteService(gameDAO, authDAO, userDAO);

        webSocketHandler = new WebSocketHandler(authDAO);

        javalin.post("/user", new RegisterHandler(userService));

        javalin.post("/session", new LoginHandler(userService));

        javalin.delete("/session", new LogoutHandler(userService));

        javalin.get("/game", new ListGamesHandler(gameService));

        javalin.post("/game", new CreateGameHandler(gameService));

        javalin.put("/game", new JoinGameHandler(gameService));

        javalin.delete("/db", new ClearDataBaseHandler(deleteService));

        javalin.ws("/ws", ws -> {
            ws.onConnect(webSocketHandler);
            ws.onMessage(webSocketHandler);
            ws.onClose(webSocketHandler);
        });
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        this.port = javalin.port();
        return javalin.port();
    }

    public int getPort(){
        return this.port;
    }

    public void stop() {
        javalin.stop();
    }

    public static int getErrorCode(Exception e){
        if(e instanceof UnauthorizedException){
            return 401;
        } else if(e instanceof AlreadyTakenException){
            return 403;
        } else if(e instanceof RuntimeException){
            return 500;
        }
        return 400;
    }

    public static void handleException(Exception e, Context context){
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
                if(JsonParser.parseString(context.body()).getAsJsonObject().get("gameName")==null || context.header("Authorization")==null){
                    throw new Exception("Bad Request");
                }
                String authToken = context.header("Authorization");
                String gameName = JsonParser.parseString(context.body()).getAsJsonObject().get("gameName").getAsString();
                CreateGameRequest createGameRequest = new CreateGameRequest(gameName, authToken);
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
                if(JsonParser.parseString(context.body()).getAsJsonObject().get("playerColor")==null ||
                        context.header("Authorization")==null ||
                        JsonParser.parseString(context.body()).getAsJsonObject().get("gameID")==null){
                    throw new Exception("Bad Request");
                }
                String authToken = context.header("Authorization");
                int gameID = JsonParser.parseString(context.body()).getAsJsonObject().get("gameID").getAsInt();
                String playerColor = JsonParser.parseString(context.body()).getAsJsonObject().get("playerColor").getAsString();
                JoinGameRequest joinGameRequest = new JoinGameRequest(playerColor, gameID, authToken);
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
