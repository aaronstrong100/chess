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
    public final boolean equals(Object o) {
        if (!(o instanceof ChessGame chessGame)) {
            return false;
        }

        return turn == chessGame.turn && board.equals(chessGame.board);
    }

    @Override
    public int hashCode() {
        int result = turn.hashCode();
        result = 31 * result + board.hashCode();
        return result;
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

    /**
     *
     * @param team the team you want the opposite of
     * @return the opposite team from the input (WHITE->BLACK, BLACK->WHITE)
     */
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
        System.out.println(this.board);
        if(this.board.getPiece(startPosition)==null){
            System.out.println("No valid moves for "+startPosition);
            return new ArrayList<ChessMove>();
        }
        else {
            ChessGame.TeamColor team = this.board.getPiece(startPosition).getTeamColor();
            Collection<ChessMove> validMoves = this.board.getPiece(startPosition).pieceMoves(this.board, startPosition);
            for (Iterator<ChessMove> moveIterator = validMoves.iterator(); moveIterator.hasNext();) {
                ChessMove move = moveIterator.next();
                ChessBoard currentBoard = this.board.copy();
                this.makeMoveUnprotected(move);
                if(this.isInCheck(team)) {
                    moveIterator.remove();
                }
                this.board = currentBoard;
            }
            System.out.println("Valid moves for "+startPosition);
            for(ChessMove move: validMoves){
                System.out.println(move);
            }
            return validMoves;
        }
    }

    /**
     *
     * @param startPosition the starting position
     * @return all valid moves, not taking into account team turn or being in check. Useful for calculating check and checkmate
     */
    private Collection<ChessMove> validMovesUnprotected(ChessPosition startPosition) {
        return this.board.getPiece(startPosition).pieceMoves(this.board, startPosition);
    }

    /**
     *
     * @param team the team to get moves from
     * @return all possible moves from one team, taking into account check
     */
    private Collection<ChessMove> teamValidMoves(TeamColor team){
        Collection<ChessMove> teamMoves = new ArrayList<>();
        for (Iterator<ChessPosition> it = this.board.getPositionsIterator(team); it.hasNext(); ) {
            ChessPosition startPosition = it.next();
            teamMoves.addAll(this.validMoves(startPosition));
        }
        return teamMoves;
    }

    /**
     *
     * @param team the team to get moves from
     * @return all possible moves from one team, not taking into account team turn or being in check.
     */
    private Collection<ChessMove> teamValidMovesUnprotected(TeamColor team){
        Collection<ChessMove> teamMoves = new ArrayList<>();
        for (Iterator<ChessPosition> it = this.board.getPositionsIterator(team); it.hasNext(); ) {
            ChessPosition startPosition = it.next();
            teamMoves.addAll(this.validMovesUnprotected(startPosition));
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
        if(this.board.onBoard(move.getStartPosition()) &&
                this.board.onBoard(move.getEndPosition()) &&
                this.board.getPiece(move.getStartPosition())!=null &&
                this.board.getPiece(move.getStartPosition()).getTeamColor()==this.turn &&
                this.validMoves(move.getStartPosition()).contains(move))
        {
            if(move.getPromotionPiece()!=null){
                this.board.addPiece(move.getEndPosition(), new ChessPiece(this.turn,move.getPromotionPiece()));
            }
            else{
                this.board.addPiece(move.getEndPosition(), this.board.getPiece(move.getStartPosition()));
            }
            this.board.addPiece(move.getStartPosition(), null);
            this.turn = otherTeam(this.turn);
        }
        else{
            throw new InvalidMoveException("The move is invalid");
        }
    }

    /**
     *
     * @param move the move to apply to the chess board
     *  applies the move to the chess board without checking if the move is valid
     */
    public void makeMoveUnprotected(ChessMove move){
        this.board.addPiece(move.getEndPosition(), this.board.getPiece(move.getStartPosition()));
        this.board.addPiece(move.getStartPosition(), null);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        Collection<ChessMove> teamMoves = this.teamValidMovesUnprotected(otherTeam(teamColor));
        ChessPosition kingPos = getKingPosition(teamColor);
        for(ChessMove move : teamMoves){
            System.out.println("Checking if the move " + move + " will put " + teamColor + " in check.");
            if(move.getEndPosition().equals(kingPos)){
                return true;
            }
        }
        return false;
    }

    /**
     *
     * @param teamColor the team to get the king position from
     * @return return the position on the board of the king from given team
     */
    public ChessPosition getKingPosition(TeamColor teamColor){
        for (Iterator<ChessPosition> it = this.board.getPositionsIterator(teamColor); it.hasNext(); ) {
            ChessPosition pos = it.next();
            if(this.board.getPiece(pos).getPieceType()==ChessPiece.PieceType.KING) {
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
        return this.board;
    }
}
