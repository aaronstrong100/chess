package model;

import chess.ChessGame;

public class GameData {
    private final int gameID;
    private final String whiteUsername;
    private final String blackUsername;
    private final String gameName;
    private final ChessGame game;
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
    public String getBlackUsername(){
        return this.blackUsername;
    }
    public String getGameName(){
        return this.gameName;
    }
    public ChessGame getGame(){
        return this.game;
    }
}
