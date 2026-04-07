package websocket.commands;

import chess.ChessMove;

import java.util.Objects;

public class MakeMoveCommand extends UserGameCommand {

    private ChessMove move;

    public MakeMoveCommand(String authToken, Integer gameID, String userType, ChessMove move) {
        super(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, userType);
        this.move = move;
    }


    public ChessMove getMove(){
        return this.move;
    }


    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + move.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MakeMoveCommand that)) {
            return false;
        }
        return Objects.equals(this.getMove(), that.getMove());
    }
}
