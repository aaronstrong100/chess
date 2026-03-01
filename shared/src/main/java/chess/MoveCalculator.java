package chess;

import java.util.Map;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Static class that implements the calculateMoves method. Returns array of valid moves.
 */
public class MoveCalculator {
    /**
     * Class that
     */
    private static final Map<ChessPiece.PieceType, int[][]> MOVE_VECTORS = Map.of(
            ChessPiece.PieceType.KING, new int[][]{{1,0},{1,1},{0,1},{-1,1},{-1,0},{-1,-1},{0,-1},{1,-1}},
            ChessPiece.PieceType.QUEEN, new int[][]{{1,0},{1,1},{0,1},{-1,1},{-1,0},{-1,-1},{0,-1},{1,-1}},
            ChessPiece.PieceType.ROOK, new int[][]{{1,0},{0,1},{-1,0},{0,-1}},
            ChessPiece.PieceType.BISHOP, new int[][]{{1,1},{-1,1},{-1,-1},{1,-1}},
            ChessPiece.PieceType.KNIGHT, new int[][]{{1,2},{2,1},{2,-1},{1,-2},{-1,2},{-2,1},{-1,-2},{-2,-1}},
            ChessPiece.PieceType.PAWN, new int[][]{{1,0},{1,1},{1,-1},{2,0}}
    );
    private static final Map<ChessPiece.PieceType, Boolean> SCALES = Map.of(
            ChessPiece.PieceType.KING, false,
            ChessPiece.PieceType.QUEEN, true,
            ChessPiece.PieceType.ROOK, true,
            ChessPiece.PieceType.BISHOP, true,
            ChessPiece.PieceType.KNIGHT, false,
            ChessPiece.PieceType.PAWN, false
    );
    private static final ChessPiece.PieceType[] PROMOTION_PIECE_TYPES = {ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.ROOK,
            ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT};
    public static Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position, ChessPiece piece){
        if(piece.getPieceType()==ChessPiece.PieceType.PAWN)
        {
            return getPawnMoves(position, piece, board);
        }
        if(!SCALES.get(piece.getPieceType()))
        {
            return getNonScalarMoves(board, position, piece);
        }
        else
        {
            return getScalarMoves(board, position, piece);
        }
    }
    private static Collection<ChessMove> getNonScalarMoves(ChessBoard board, ChessPosition position, ChessPiece  piece){
        Collection<ChessMove> validMoves = new ArrayList<>();
        for(int[] vector: MOVE_VECTORS.get(piece.getPieceType()))
        {
            ChessPosition newPosition = new ChessPosition(position.getRow()+vector[0],position.getColumn()+vector[1]);
            if(isEmpty(newPosition, board) || canCapture(newPosition, board, piece))
                validMoves.add(new ChessMove(position, newPosition, null));
        }
        return validMoves;
    }
    private static Collection<ChessMove> getScalarMoves(ChessBoard board, ChessPosition position, ChessPiece piece){
        Collection<ChessMove> validMoves = new ArrayList<>();
        for(int[] vector: MOVE_VECTORS.get(piece.getPieceType()))
        {
            ChessPosition newPosition = new ChessPosition(position.getRow()+vector[0],position.getColumn()+vector[1]);
            for(int i = 1; onBoard(newPosition);) {
                if (isEmpty(newPosition, board)) {
                    validMoves.add(new ChessMove(position, newPosition, null));
                }
                else
                {
                    if(canCapture(newPosition, board, piece))
                    {
                        validMoves.add(new ChessMove(position, newPosition, null));
                    }
                    break;
                }
                i++;
                newPosition = new ChessPosition(position.getRow() + i * vector[0], position.getColumn() + i * vector[1]);
            }
        }
        return validMoves;
    }
    private static Collection<ChessMove> getPawnMoves(ChessPosition position, ChessPiece piece, ChessBoard board)
    {
        Collection<ChessMove> validMoves = new ArrayList<>();
        validMoves.addAll(getPawnForward(board,position,piece));
        validMoves.addAll(getPawnCaptures(board,position,piece));
        return validMoves;
    }
    private static boolean pawnCanDouble(ChessPosition position, ChessPiece pawn, ChessBoard board){
        return (pawn.getTeamColor()==ChessGame.TeamColor.WHITE && position.getRow()==2 &&
                board.getPiece(new ChessPosition(position.getRow()+1, position.getColumn()))==null) ||
                (pawn.getTeamColor()==ChessGame.TeamColor.BLACK && position.getRow()==7  &&
                        board.getPiece(new ChessPosition(position.getRow()-1, position.getColumn()))==null);
    }
    private static Collection<ChessMove> getPawnForward(ChessBoard board, ChessPosition position, ChessPiece piece){
        Collection<ChessMove> validMoves = new ArrayList<>();
        int[] vector = Arrays.copyOf(MOVE_VECTORS.get(piece.getPieceType())[0], 2);
        if(piece.getTeamColor()==ChessGame.TeamColor.BLACK)
        {
            vector[0] = -vector[0];
        }
        ChessPosition newPosition = new ChessPosition(position.getRow()+vector[0],position.getColumn()+vector[1]);
        if (onBoard(newPosition) && !lastRank(newPosition, piece)) {
            if (isEmpty(newPosition, board))
                validMoves.add(new ChessMove(position, newPosition, null));
        } else if (lastRank(newPosition, piece)) {
            if (isEmpty(newPosition, board))
                for (ChessPiece.PieceType pieceType : PROMOTION_PIECE_TYPES) {
                    validMoves.add(new ChessMove(position, newPosition, pieceType));
                }
        }
        if(pawnCanDouble(position, piece, board)) {
            vector = Arrays.copyOf(MOVE_VECTORS.get(piece.getPieceType())[3], 2);
            if(piece.getTeamColor()==ChessGame.TeamColor.BLACK)
            {
                vector[0] = -vector[0];
            }
            newPosition = new ChessPosition(position.getRow()+vector[0],position.getColumn()+vector[1]);
            if (isEmpty(newPosition, board))
                validMoves.add(new ChessMove(position, newPosition, null));
        }
        return validMoves;
    }
    private static Collection<ChessMove> getPawnCaptures(ChessBoard board, ChessPosition position, ChessPiece piece){
        Collection<ChessMove> validMoves = new ArrayList<>();
        int[] vector;
        ChessPosition newPosition;
        for(int i = 1; i<=2; i++) {
            vector = Arrays.copyOf(MOVE_VECTORS.get(piece.getPieceType())[i], 2);
            if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                vector[0] = -vector[0];
            }
            newPosition = new ChessPosition(position.getRow() + vector[0], position.getColumn() + vector[1]);
            if(canCapture(newPosition, board, piece)) {
                if (!lastRank(newPosition, piece)) {
                    validMoves.add(new ChessMove(position, newPosition, null));
                } else if (lastRank(newPosition, piece)) {
                    for (ChessPiece.PieceType pieceType : PROMOTION_PIECE_TYPES) {
                        validMoves.add(new ChessMove(position, newPosition, pieceType));
                    }
                }
            }
        }
        return validMoves;
    }
    private static boolean onBoard(ChessPosition position){
        return position.getRow() <= 8 && position.getRow() >=1  && position.getColumn() <= 8 && position.getColumn() >= 1;
    }
    private static boolean lastRank(ChessPosition position, ChessPiece piece){
        return (piece.getTeamColor() == ChessGame.TeamColor.WHITE && position.getRow() == 8) ||
        (piece.getTeamColor() == ChessGame.TeamColor.BLACK && position.getRow() == 1);
    }
    private static boolean isEmpty(ChessPosition position, ChessBoard board){
        return onBoard(position) && board.getPiece(position) == null;
    }
    private static boolean canCapture(ChessPosition position, ChessBoard board, ChessPiece piece){
        return onBoard(position) && board.getPiece(position) != null && board.getPiece(position).getTeamColor() != piece.getTeamColor();
    }
}
