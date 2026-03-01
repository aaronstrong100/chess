package results;

import java.util.ArrayList;

import chess.ChessGame;
import model.GameData;

public class ListGamesResult {
    private ArrayList<GameData> games;
    public ListGamesResult(ArrayList<GameData> games){
        this.games = games;
    }
    public ArrayList<GameData> getGames(){
        return this.games;
    }
}
