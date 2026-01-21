package chess;
import java.util.Objects;
/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {
    private ChessPosition startPos;
    private ChessPosition endPos;
    private ChessPiece.PieceType promotion;
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPos = startPosition;
        this.endPos = endPosition;
        this.promotion = promotionPiece;
    }

    @Override
    public final boolean equals(Object o) {
        if (!(o instanceof ChessMove chessMove)) {
            return false;
        }

        return startPos.equals(chessMove.startPos) && endPos.equals(chessMove.endPos) && promotion == chessMove.promotion;
    }

    @Override
    public int hashCode() {
        int result = startPos.hashCode();
        result = 31 * result + endPos.hashCode();
        result = 31 * result + Objects.hashCode(promotion);
        return result;
    }

    @Override
    public String toString() {
        return "ChessMove[" + this.startPos.toString() + ", " +this.endPos.toString() + "]";
    }

    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return this.startPos;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return this.endPos;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return this.promotion;
    }
}
