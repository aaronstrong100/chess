package serverAccess;

import chess.ChessGame;
import model.GameData;
import requests.*;
import results.*;
import server.Server;

import java.util.ArrayList;

public class ServerFacade {
    private ServerCommunicator serverCommunicator;

    public ServerFacade(Server server){
        this.serverCommunicator = new ServerCommunicator(server);
    }

    public void stop(){
        this.serverCommunicator.stop();
    }

    //javalin.post("/user", new RegisterHandler(userService));
    public RegisterResult register(RegisterRequest registerRequest){
        return new RegisterResult(registerRequest.getUsername(), "auth");
    }

    //javalin.post("/session", new LoginHandler(userService));
    public LoginResult login(LoginRequest loginRequest){
        return new LoginResult(loginRequest.getUsername(), "auth");
    }

    //javalin.delete("/session", new LogoutHandler(userService));
    public void logout(LogoutRequest logoutRequest){
    }

    //javalin.get("/game", new ListGamesHandler(gameService));
    public ListGamesResult listGames(ListGamesRequest listGamesRequest){
        ArrayList<GameData> games = new ArrayList<>();
        games.add(new GameData(1, null, null, "Cool game", new ChessGame()));
        return new ListGamesResult(games);
    }

    //javalin.post("/game", new CreateGameHandler(gameService));
    public CreateGameResult createGame(CreateGameRequest createGameRequest){
        return new CreateGameResult(1);
    }

    //javalin.put("/game", new JoinGameHandler(gameService));
    public JoinGameResult joinGame(JoinGameRequest joinGameRequest){
        return new JoinGameResult(joinGameRequest.getPlayerColor(), joinGameRequest.getGameID());
    }
}
