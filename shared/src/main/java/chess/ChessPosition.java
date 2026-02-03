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
    public final boolean equals(Object o) {
        if (!(o instanceof ChessPosition that)) {
            return false;
        }

        return rowNum == that.rowNum && colNum == that.colNum;
    }

    @Override
    public int hashCode() {
        int result = rowNum;
        result = 31 * result + colNum;
        return result;
    }

    @Override
    public String toString() {
        return "ChessPosition[" + this.rowNum +", " + this.colNum + "]";
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
