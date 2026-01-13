package chess;

import java.util.Collection;
import java.util.Objects;
/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pieceCol;
    private ChessPiece.PieceType pieceType;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceCol = pieceColor;
        this.pieceType = type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.pieceCol, this.pieceType);
    }

    @Override
    public boolean equals(Object obj) {
        return this.hashCode() == obj.hashCode();
    }

    @Override
    public String toString() {
        return "{" + this.pieceCol.toString() + " " + this.pieceType.toString() + "}";
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceCol;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.pieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        if(this.pieceType==ChessPiece.PieceType.KING)
        {
            return removeInvalidMoves(board, getKingMoves(board,myPosition));
        }
        if(this.pieceType==ChessPiece.PieceType.QUEEN)
        {
            return removeInvalidMoves(board, getQueenMoves(board,myPosition));
        }
        if(this.pieceType==ChessPiece.PieceType.BISHOP)
        {
            return removeInvalidMoves(board, getBishopMoves(board,myPosition));
        }
        if(this.pieceType==ChessPiece.PieceType.KNIGHT)
        {
            return removeInvalidMoves(board, getKnightMoves(board,myPosition));
        }
        if(this.pieceType==ChessPiece.PieceType.ROOK)
        {
            return removeInvalidMoves(board, getRookMoves(board,myPosition));
        }
        if(this.pieceType==ChessPiece.PieceType.PAWN)
        {
            return removeInvalidMoves(board, getPawnMoves(board,myPosition));
        }
        return null;
    }

    private Collection<ChessMove> removeInvalidMoves(ChessBoard board, Collection<ChessMove> moves){
        return null;
    }

    private Collection<ChessMove> getKingMoves(ChessBoard board, ChessPosition myPosition){
        return null;
    }

    private Collection<ChessMove> getQueenMoves(ChessBoard board, ChessPosition myPosition){
        return null;
    }

    private Collection<ChessMove> getBishopMoves(ChessBoard board, ChessPosition myPosition){
        return null;
    }

    private Collection<ChessMove> getKnightMoves(ChessBoard board, ChessPosition myPosition){
        return null;
    }

    private Collection<ChessMove> getRookMoves(ChessBoard board, ChessPosition myPosition){
        return null;
    }

    private Collection<ChessMove> getPawnMoves(ChessBoard board, ChessPosition myPosition){
        return null;
    }
}
