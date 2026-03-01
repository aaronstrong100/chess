package model;

import chess.ChessGame;

public class GameData {
    private final int gameID;
    private final String whiteUsername;
    private final String blackUsername;
    private final String gameName;
    private transient final ChessGame game;
    public GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game){
        this.gameID = gameID;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.gameName = gameName;
        this.game = game;
    }
    public int getGameID(){
        return this.gameID;
    }
    public String getWhiteUsername(){
        return this.whiteUsername;
    }
    public GameData updateWhiteUsername(String newWhiteUsername){
        return new GameData(this.gameID, newWhiteUsername, this.blackUsername, this.gameName, this.game);
    }
    public String getBlackUsername(){
        return this.blackUsername;
    }
    public GameData updateBlackUsername(String newBlackUsername){
        return new GameData(this.gameID, this.whiteUsername, newBlackUsername, this.gameName, this.game);
    }
    public String getGameName(){
        return this.gameName;
    }
    public ChessGame getGame(){
        return this.game;
    }
}
