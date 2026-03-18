package requests;

import com.google.gson.annotations.Expose;

public class JoinGameRequest {
    private String playerColor;
    private int gameID;
    @Expose(serialize = false)
    private String authToken;
    public JoinGameRequest(String playerColor, int gameID, String authToken){
        this.playerColor = playerColor;
        this.gameID = gameID;
        this.authToken = authToken;
    }
    public String getPlayerColor(){
        return this.playerColor;
    }
    public int getGameID(){
        return this.gameID;
    }
    public String getAuthToken(){
        return this.authToken;
    }
}
