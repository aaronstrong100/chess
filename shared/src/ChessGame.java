package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessGame.TeamColor turn = ChessGame.TeamColor.WHITE;
    ChessBoard board;
    public ChessGame() {
        this.board = new ChessBoard();
        board.resetBoard();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj) return true;
        if(obj==null || this.getClass() != obj.getClass()) return false;
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.turn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.turn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    private TeamColor otherTeam(TeamColor team){
        if(team==TeamColor.WHITE){
            return TeamColor.BLACK;
        }
        else {
            return TeamColor.WHITE;
        }
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> validMoves = this.board.getPiece(startPosition).pieceMoves(this.board, startPosition);
        for (ChessMove move : validMoves){

        }
        return validMoves;
    }

    private Collection<ChessMove> validMovesNoCheck(ChessPosition startPosition) {
        Collection<ChessMove> validMoves = this.board.getPiece(startPosition).pieceMoves(this.board, startPosition);
        for (ChessMove move : validMoves){

        }
        return validMoves;
    }

    private Collection<ChessMove> teamValidMoves(ChessGame.TeamColor team){
        Collection<ChessMove> teamMoves = new ArrayList<>();

        return teamMoves;
    }

    private Collection<ChessMove> teamValidMovesNoCheck(ChessGame.TeamColor team){
        Collection<ChessMove> teamMoves = new ArrayList<>();
        for (Iterator<ChessPosition> it = this.board.getPositionsIterator(team); it.hasNext(); ) {
            ChessPosition startPosition = it.next();
            teamMoves.addAll(validMovesNoCheck(startPosition));
        }
        return teamMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(this.validMoves(move.getStartPosition()).contains(move))
        {
            this.board.addPiece(move.getEndPosition(), this.board.getPiece(move.getStartPosition()));
            this.board.addPiece(move.getStartPosition(), null);
        }
        else{
            throw new InvalidMoveException("The move is invalid");
        }
        this.turn = otherTeam(this.turn);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessMove> teamMoves = teamValidMovesNoCheck(otherTeam(teamColor));
        ChessPosition kingPos = getKingPosition(teamColor);
        for(ChessMove move : teamMoves){
            if(move.getEndPosition().equals(kingPos)){
                return true;
            }
        }
        return false;
    }

    public ChessPosition getKingPosition(TeamColor teamColor){
        for (Iterator<ChessPosition> it = this.board.getPositionsIterator(teamColor); it.hasNext(); ) {
            ChessPosition pos = it.next();
            if(this.board.getPiece(pos).getPieceType()==ChessPiece.PieceType.KING)
            {
                return pos;
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && teamValidMoves(teamColor).isEmpty();
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return !isInCheck(teamColor) && teamValidMoves(teamColor).isEmpty();
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
