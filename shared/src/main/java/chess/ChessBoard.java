package chess;
import java.util.Arrays;
import java.util.Iterator;

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
        return super.toString();
    }

    /**
     *
     * @return copy of this ChessBoard
     */
    public ChessBoard copy(){
        ChessBoard copyBoard = new ChessBoard();
        copyBoard.pieces = Arrays.copyOf(this.pieces, 8);
        for(int i = 1; i<=8; i++)
        {
            for(int j = 1; j<=8; j++)
            {
                copyBoard.addPiece(new ChessPosition(i,j), this.getPiece(new ChessPosition(i,j)));
            }
        }
        return copyBoard;
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
