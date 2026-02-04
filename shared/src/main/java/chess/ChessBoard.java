package chess;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] pieces;
    public ChessBoard() {
        this.pieces = new ChessPiece[8][8];
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(pieces);
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof ChessBoard that)) {
            return false;
        }

        return Arrays.deepEquals(pieces, that.pieces);
    }

    @Override
    public String toString() {
        String output = "";
        final Map<ChessPiece.PieceType, String> pieceStrings = Map.of(
                ChessPiece.PieceType.KING, "K",
                ChessPiece.PieceType.QUEEN, "Q",
                ChessPiece.PieceType.ROOK, "R",
                ChessPiece.PieceType.BISHOP, "B",
                ChessPiece.PieceType.KNIGHT, "N",
                ChessPiece.PieceType.PAWN, "P"
        );
        final Map<ChessGame.TeamColor, String> teamStrings = Map.of(
                ChessGame.TeamColor.WHITE, "W",
                ChessGame.TeamColor.BLACK, "B"
        );
        for(int i = 0; i<8; i++){
            for(int j = 0; j<8; j++)
            {
                if(this.pieces[i][j]==null){
                    output+="|  ";
                }
                else {
                    output+="|"+teamStrings.get(this.pieces[i][j].getTeamColor()) + pieceStrings.get(this.pieces[i][j].getPieceType());
                }
            }
            output+="|\n";
        }
        return output;
    }

    /**
     * Returns a copy of the chessboard
     *
     * @return copy of this ChessBoard
     */
    public ChessBoard copy(){
        ChessBoard copyBoard = new ChessBoard();
        copyBoard.pieces = new ChessPiece[8][8];
        for(int i = 1; i<=8; i++)
        {
            for(int j = 1; j<=8; j++)
            {
                if(this.getPiece(new ChessPosition(i,j))==null)
                {
                    copyBoard.addPiece(new ChessPosition(i,j), null);
                }
                else
                {
                    copyBoard.addPiece(new ChessPosition(i,j), this.getPiece(new ChessPosition(i,j)).copy());
                }
            }
        }
        return copyBoard;
    }

    /**
     * returns whether or not a position is on the board
     *
     * @param position the position on the board
     * @return true if position is on board, false if not
     */
    public boolean onBoard(ChessPosition position){
        return position.getRow() <= 8 && position.getRow() >=1  && position.getColumn() <= 8 && position.getColumn() >= 1;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        this.pieces[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return this.pieces[position.getRow()-1][position.getColumn()-1];
    }

    /**
     *
     * @param team which team to return piece positions from
     * @return an iterator of all positions on the board occupied by pieces from the given team
     */
    public Iterator<ChessPosition> getPositionsIterator(ChessGame.TeamColor team){
        return new Iterator<ChessPosition>() {
            final ChessGame.TeamColor teamColor = team;
            int row = 0;
            int col = 0;
            boolean hasNext = true;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public ChessPosition next(){
                while(pieces[row][col]==null || pieces[row][col].getTeamColor()!=teamColor) {
                    row++;
                    if (row > 7) {
                        row = 0;
                        col++;
                        if (col > 7) {
                            hasNext = false;
                        }
                    }
                }
                ChessPosition position = new ChessPosition(row+1,col+1);
                 do {
                    row++;
                    if (row > 7) {
                        row = 0;
                        col++;
                        if (col > 7) {
                            hasNext = false;
                        }
                    }
                } while(col<8 && (pieces[row][col]==null || pieces[row][col].getTeamColor()!=teamColor));
                return position;
            }
        };
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        this.pieces = new ChessPiece[][]{
                {new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK)},
                {new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN)},
                {null,null,null,null,null,null,null,null},
                {null,null,null,null,null,null,null,null},
                {null,null,null,null,null,null,null,null},
                {null,null,null,null,null,null,null,null},
                {new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN)},
                {new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK)}
        };
    }
}
