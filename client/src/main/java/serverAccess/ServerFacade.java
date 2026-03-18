package serverAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.AlreadyTakenException;
import dataaccess.UnauthorizedException;
import model.GameData;
import requests.*;
import results.*;
import server.Server;

import java.util.ArrayList;

public class ServerFacade {
    private ServerCommunicator serverCommunicator;
    private Gson gson;

    public ServerFacade(Server server){
        this.serverCommunicator = new ServerCommunicator(server);
        gson = new Gson();
    }

    public void stop(){
        this.serverCommunicator.stop();
    }

    public void handleException(Exception e) throws UnauthorizedException, AlreadyTakenException, RuntimeException {
        if(e instanceof UnauthorizedException){
            throw new UnauthorizedException(e.getMessage());
        } else if (e instanceof AlreadyTakenException) {
            throw new AlreadyTakenException(e.getMessage());
        } else {
            throw new RuntimeException();
        }
    }

    //javalin.post("/user", new RegisterHandler(userService));
    public RegisterResult register(RegisterRequest registerRequest) throws UnauthorizedException, AlreadyTakenException, RuntimeException {
        String httpResult = "";
        try{
            httpResult = serverCommunicator.post("", "/user", this.gson.toJson(registerRequest)).body();
        } catch (Exception e){
            handleException(e);
        }
        return gson.fromJson(httpResult, RegisterResult.class);
        //URISyntaxException, IOException, InterruptedException, UnauthorizedException, AlreadyTakenException
        //return new RegisterResult(registerRequest.getUsername(), "auth");
    }

    //javalin.post("/session", new LoginHandler(userService));
    public LoginResult login(LoginRequest loginRequest) throws UnauthorizedException, AlreadyTakenException, RuntimeException {
        try{
            String httpResult = serverCommunicator.post("", "/session", this.gson.toJson(loginRequest)).body();
            return gson.fromJson(httpResult, LoginResult.class);
        } catch (UnauthorizedException | AlreadyTakenException e){
            throw e;
        } catch (Exception e){
            throw new RuntimeException();
        }
    }

    //javalin.delete("/session", new LogoutHandler(userService));
    public void logout(LogoutRequest logoutRequest){
    }

    //javalin.get("/game", new ListGamesHandler(gameService));
    public ListGamesResult listGames(ListGamesRequest listGamesRequest) throws UnauthorizedException, AlreadyTakenException, RuntimeException {
        try{
            String httpResult = serverCommunicator.get(listGamesRequest.getAuthToken(), "/game").body();
            return gson.fromJson(httpResult, ListGamesResult.class);
        } catch (UnauthorizedException | AlreadyTakenException e){
            throw e;
        } catch (Exception e){
            throw new RuntimeException();
        }
    }

    //javalin.post("/game", new CreateGameHandler(gameService));
    public CreateGameResult createGame(CreateGameRequest createGameRequest){
        return new CreateGameResult(1);
    }

    //javalin.put("/game", new JoinGameHandler(gameService));
    public JoinGameResult joinGame(JoinGameRequest joinGameRequest){
        return new JoinGameResult(joinGameRequest.getPlayerColor(), joinGameRequest.getGameID());
    }

    //javalin.delete("/db", new ClearDataBaseHandler(deleteService));
    public void clearDataBase(){

    }
}
