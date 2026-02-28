package Results;

public class JoinGameResult {
    private String playerColor;
    private String gameID;
    public JoinGameResult(String playerColor, String gameID){
        this.playerColor = playerColor;
        this.gameID = gameID;
    }
    public String getPlayerColor(){
        return this.playerColor;
    }
    public String getGameID(){
        return this.gameID;
    }
}
