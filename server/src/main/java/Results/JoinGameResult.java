package Results;

public class JoinGameResult {
    private String playerColor;
    private int gameID;
    public JoinGameResult(String playerColor, int gameID){
        this.playerColor = playerColor;
        this.gameID = gameID;
    }
    public String getPlayerColor(){
        return this.playerColor;
    }
    public int getGameID(){
        return this.gameID;
    }
}
