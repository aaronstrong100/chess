package ui;
import static ui.EscapeSequences.*;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class ChessGamePrinter {
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;

    private static final String EMPTY = "   ";

    private static final String KING = " K ";
    private static final String QUEEN = " Q ";
    private static final String ROOK = " R ";
    private static final String KNIGHT = " N ";
    private static final String BISHOP = " B ";
    private static final String PAWN = " P ";



    private static final Map<ChessPiece.PieceType, String> PIECE_STRINGS = Map.of(
            ChessPiece.PieceType.KING, KING,
            ChessPiece.PieceType.QUEEN, QUEEN,
            ChessPiece.PieceType.ROOK, ROOK,
            ChessPiece.PieceType.KNIGHT, KNIGHT,
            ChessPiece.PieceType.BISHOP, BISHOP,
            ChessPiece.PieceType.PAWN, PAWN
    );

    private static final String SET_BOARD_LIGHT = SET_BG_COLOR_LIGHT_GREY;
    private static final String SET_BOARD_DARK = SET_BG_COLOR_DARK_GREY;
    public static final String SET_BOARD_BACKGROUND = SET_BG_COLOR_BLACK;

    private static final String SET_TEXT_COLOR_WHITE_TEAM = SET_TEXT_COLOR_WHITE;
    private static final String SET_TEXT_COLOR_BLACK_TEAM = SET_TEXT_COLOR_BLACK;
    private static final String SET_TEXT_COLOR_BACKGROUND = SET_TEXT_COLOR_WHITE;

    private static final String SET_BOARD_LIGHT_HIGHLIGHT = EscapeSequences.SET_BG_COLOR_LIGHT_YELLOW;
    private static final String SET_BOARD_DARK_HIGHLIGHT = EscapeSequences.SET_BG_COLOR_YELLOW;
    private static final String SET_BOARD_STARTING_HIGHLIGHT = EscapeSequences.SET_BG_COLOR_RED;

    private static final String[] COL_INDICES = {
            " a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "
    };
    private static final String[] ROW_INDICES = {
            " 8 ", " 7 ", " 6 ", " 5 ", " 4 ", " 3 ", " 2 ", " 1 "
    };

    public static void main(String[] args){
        System.out.print(EscapeSequences.ERASE_SCREEN);
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        printChessBoard(board, "WHITE");
        printChessBoard(board, "BLACK");
    }
    public static void printChessBoard(ChessBoard chessBoard, String player){
        String[][] pieceStrings = getPieceStrings(chessBoard);
        String[][] setPieceColors = getPieceColors(chessBoard);
        if(player.toLowerCase().equals("white")){
            printColIndices(COL_INDICES);
            for(int i = 0; i<4; i++){
                printRowWhite(2*i, setPieceColors[reverse(2*i)], pieceStrings[reverse(2*i)]);
                printRowBlack(2*i+1, setPieceColors[reverse(2*i+1)], pieceStrings[reverse(2*i+1)]);
            }
            printColIndices(COL_INDICES);
        } else {

            printColIndices(reverse(COL_INDICES));
            for(int i = 0; i<4; i++){
                printRowWhite(reverse(2*i), setPieceColors[2*i], reverse(pieceStrings[2*i]));
                printRowBlack(reverse(2*i+1), setPieceColors[2*i+1], reverse(pieceStrings[2*i+1]));
            }
            printColIndices(reverse(COL_INDICES));
        }
    }
    public static void printChessBoardHighlightMoves(ChessBoard chessBoard, String player, ChessPosition piecePos){
        if(chessBoard.getPiece(piecePos)!=null){
            int[][] highlightMatrix = getHighlightMatrix(chessBoard, piecePos);
            String[][] pieceStrings = getPieceStrings(chessBoard);
            String[][] setPieceColors = getPieceColors(chessBoard);
            if(player.toLowerCase().equals("white")){
                printColIndices(COL_INDICES);
                for(int i = 0; i<4; i++){
                    printRowWhite(2*i, setPieceColors[reverse(2*i)], pieceStrings[reverse(2*i)], highlightMatrix[reverse(2*i)]);
                    printRowBlack(2*i+1, setPieceColors[reverse(2*i+1)], pieceStrings[reverse(2*i+1)], highlightMatrix[reverse(2*i+1)]);
                }
                printColIndices(COL_INDICES);
            } else {

                printColIndices(reverse(COL_INDICES));
                for(int i = 0; i<4; i++){
                    printRowWhite(reverse(2*i), setPieceColors[2*i], reverse(pieceStrings[2*i]), reverse(highlightMatrix[2*i]));
                    printRowBlack(reverse(2*i+1), setPieceColors[2*i+1], reverse(pieceStrings[2*i+1]), reverse(highlightMatrix[2*i+1]));
                }
                printColIndices(reverse(COL_INDICES));
            }
        }
    }
    public static int[][] getHighlightMatrix(ChessBoard chessBoard, ChessPosition piecePos){
        Collection<ChessMove> validMoves = MoveCalculator.calculateMoves(chessBoard, piecePos, chessBoard.getPiece(piecePos));
        int[][] highlightMatrix = new int[8][8];
        for(ChessMove validMove : validMoves){
            highlightMatrix[validMove.getEndPosition().getRow()-1][validMove.getEndPosition().getColumn()-1] = 1;
        }
        highlightMatrix[piecePos.getRow()-1][piecePos.getColumn()-1] = 2;
        return highlightMatrix;
    }
    public static String[] reverse(String[] original){
        String[] reverse = new String[original.length];
        for(int i = 0; i<original.length; i++){
            reverse[i] = original[original.length-i-1];
        }
        return reverse;
    }
    public static int[] reverse(int[] original){
        int[] reverse = new int[original.length];
        for(int i = 0; i<original.length; i++){
            reverse[i] = original[original.length-i-1];
        }
        return reverse;
    }
    public static int reverse(int i){
        return 7-i;
    }
    public static String[][] getPieceStrings(ChessBoard chessBoard){
        String[][] pieceStrings = new String[8][8];
        for(int row = 0; row < 8; row++){
            for(int col = 0; col < 8; col++){
                pieceStrings[row][col] = pieceToString(chessBoard.getPiece(new ChessPosition(row+1, col +1)));
            }
        }
        return pieceStrings;
    }
    public static String[][] getPieceColors(ChessBoard chessBoard){
        String[][] setPieceColors = new String[8][8];
        for(int row = 0; row < 8; row++){
            for(int col = 0; col < 8; col++){
                setPieceColors[row][col] = setPieceColor(chessBoard.getPiece(new ChessPosition(row+1, col +1)));
            }
        }
        return setPieceColors;
    }
    public static String pieceToString(ChessPiece chessPiece){
        if(chessPiece==null){
            return EMPTY;
        }
        return PIECE_STRINGS.get(chessPiece.getPieceType());
    }
    public static String setPieceColor(ChessPiece chessPiece){
        if(chessPiece==null){
            return null;
        } else if (chessPiece.getTeamColor()== ChessGame.TeamColor.WHITE){
            return SET_TEXT_COLOR_WHITE_TEAM;
        } else {
            return SET_TEXT_COLOR_BLACK_TEAM;
        }
    }
    public static void printRowWhite(int row, String[] colors, String[] pieces){
        print(SET_BOARD_BACKGROUND, SET_TEXT_COLOR_BACKGROUND, ROW_INDICES[row]);
        for(int i = 0; i<4; i++){
            print(SET_BOARD_LIGHT, colors[i*2], pieces[i*2]);
            print(SET_BOARD_DARK, colors[i*2+1], pieces[i*2+1]);
        }
        print(SET_BOARD_BACKGROUND, SET_TEXT_COLOR_BACKGROUND, ROW_INDICES[row]);
        System.out.println();
    }
    public static void printRowWhite(int row, String[] colors, String[] pieces, int[] highlights){
        print(SET_BOARD_BACKGROUND, SET_TEXT_COLOR_BACKGROUND, ROW_INDICES[row]);
        for(int i = 0; i<4; i++){
            String backgroundColor = SET_BOARD_LIGHT;
            if(highlights[i*2]==1){
                backgroundColor = SET_BOARD_LIGHT_HIGHLIGHT;
            }
            else if(highlights[i*2]==2){
                backgroundColor = SET_BOARD_STARTING_HIGHLIGHT;
            }
            print(backgroundColor, colors[i*2], pieces[i*2]);
            if(highlights[i*2+1]==1){
                backgroundColor = SET_BOARD_DARK_HIGHLIGHT;
            }
            else if(highlights[i*2+1]==2){
                backgroundColor = SET_BOARD_STARTING_HIGHLIGHT;
            }
            else{
                backgroundColor = SET_BOARD_DARK;
            }
            print(backgroundColor, colors[i*2+1], pieces[i*2+1]);
        }
        print(SET_BOARD_BACKGROUND, SET_TEXT_COLOR_BACKGROUND, ROW_INDICES[row]);
        System.out.println();
    }
    public static void printRowBlack(int row, String[] colors, String[] pieces){
        print(SET_BOARD_BACKGROUND, SET_TEXT_COLOR_BACKGROUND, ROW_INDICES[row]);
        for(int i = 0; i<4; i++){
            print(SET_BOARD_DARK, colors[i*2], pieces[i*2]);
            print(SET_BOARD_LIGHT, colors[i*2+1], pieces[i*2+1]);
        }
        print(SET_BOARD_BACKGROUND, SET_TEXT_COLOR_BACKGROUND, ROW_INDICES[row]);
        System.out.println();
    }
    public static void printRowBlack(int row, String[] colors, String[] pieces, int[] highlights){
        print(SET_BOARD_BACKGROUND, SET_TEXT_COLOR_BACKGROUND, ROW_INDICES[row]);
        for(int i = 0; i<4; i++){
            String backgroundColor = SET_BOARD_DARK;
            if(highlights[i*2]==1){
                backgroundColor = SET_BOARD_DARK_HIGHLIGHT;
            }
            else if(highlights[i*2]==2){
                backgroundColor = SET_BOARD_STARTING_HIGHLIGHT;
            }
            print(backgroundColor, colors[i*2], pieces[i*2]);
            if(highlights[i*2+1]==1){
                backgroundColor = SET_BOARD_LIGHT_HIGHLIGHT;
            }
            else if(highlights[i*2+1]==2){
                backgroundColor = SET_BOARD_STARTING_HIGHLIGHT;
            }
            else{
                backgroundColor = SET_BOARD_LIGHT;
            }
            print(backgroundColor, colors[i*2+1], pieces[i*2+1]);
        }
        print(SET_BOARD_BACKGROUND, SET_TEXT_COLOR_BACKGROUND, ROW_INDICES[row]);
        System.out.println();
    }
    public static void printColIndices(String[] indices){
        print(SET_BOARD_BACKGROUND, null, EMPTY);
        for(String index: indices){
            print(null, null, index);
        }
        print(null, null, EMPTY);
        System.out.println();
    }
    public static void print(String setColor, String setTextColor, String printString){
        if(setColor!=null) {
            System.out.print(setColor);
        }
        if(setTextColor!=null){
            System.out.print(setTextColor);
        }
        System.out.print(printString);
    }

}

