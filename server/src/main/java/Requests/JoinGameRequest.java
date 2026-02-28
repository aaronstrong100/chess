package Requests;

public class JoinGameRequest {
    private String playerColor;
    private String gameID;
    private String authToken;
    public JoinGameRequest(String playerColor, String gameID, String authToken){
        this.playerColor = playerColor;
        this.gameID = gameID;
        this.authToken = authToken;
    }
    public String getPlayerColor(){
        return this.playerColor;
    }
    public String getGameID(){
        return this.gameID;
    }
    public String getAuthToken(){
        return this.authToken;
    }
}
