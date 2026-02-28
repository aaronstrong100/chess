package Results;

import chess.ChessGame;
import model.GameData;

public class ListGamesResult {
    private GameData[] games;
    public ListGamesResult(GameData[] games){
        this.games = games;
    }
    public GameData[] getGames(){
        return this.games;
    }
}
