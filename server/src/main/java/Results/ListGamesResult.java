package Results;

import chess.ChessGame;

public class ListGamesResult {
    private ChessGame[] games;
    public ListGamesResult(ChessGame[] games){
        this.games = games;
    }
    public ChessGame[] getGames(){
        return this.games;
    }
}
