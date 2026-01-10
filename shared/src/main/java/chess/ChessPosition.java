package chess;
import java.util.Objects;
/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private int rowNum;
    private int colNum;
    public ChessPosition(int row, int col) {
        this.rowNum = row;
        this.colNum = col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rowNum, colNum);
    }

    @Override
    public boolean equals(Object obj) {
        return this.hashCode()==obj.hashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return this.rowNum;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return this.colNum;
    }
}
