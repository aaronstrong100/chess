package serverAccess;

import requests.*;
import results.*;
import server.Server;

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
        return null;
    }

    //javalin.post("/session", new LoginHandler(userService));
    public LoginResult login(LoginRequest loginRequest){
        return null;
    }

    //javalin.delete("/session", new LogoutHandler(userService));
    public void logout(LogoutRequest logoutRequest){

    }

    //javalin.get("/game", new ListGamesHandler(gameService));
    public ListGamesResult listGames(ListGamesRequest listGamesRequest){
        return null;
    }

    //javalin.post("/game", new CreateGameHandler(gameService));
    public CreateGameResult createGame(CreateGameRequest createGameRequest){
        return null;
    }

    //javalin.put("/game", new JoinGameHandler(gameService));
    public JoinGameResult joinGame(JoinGameRequest joinGameRequest){
        return null;
    }
}
