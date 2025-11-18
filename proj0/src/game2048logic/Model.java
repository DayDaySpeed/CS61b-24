package game2048logic;

import game2048rendering.Board;
import game2048rendering.Side;
import game2048rendering.Tile;

import java.util.Formatter;


/** The state of a game of 2048.
 *  @author P. N. Hilfinger + Josh Hug
 */
public class Model {
    /** Current contents of the board. */
    private final Board board;
    /** Current score. */
    private int score;

    /* Coordinate System: column x, row y of the board (where x = 0,
     * y = 0 is the lower-left corner of the board) will correspond
     * to board.tile(x, y).  Be careful!
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = 0;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (x, y) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    //一款新的 2048 游戏，其中 RAWVALUES 包含了各方块的数值（若为空则为 0）
    public Model(int[][] rawValues, int score) {
        board = new Board(rawValues);
        this.score = score;
    }

    /** Rat (x, y), where 0 <= x < size(),return the current Tile
     *  0 <= y < size(). Returns null if there is no tile there.
     *  Used for testing. */
    public Tile tile(int x, int y) {
        return board.tile(x, y);
    }

    /** Return the number of squares on one side of the board. */
    public int size() {
        return board.size();
    }

    /** Return the current score. */
    public int score() {
        return score;
    }


    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        board.clear();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
    }

    /** Return true if the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        return maxTileExists() || !atLeastOneMoveExists();
    }

    /** Returns this Model's board. */
    public Board getBoard() {
        return board;
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public boolean emptySpaceExists() {
        // TODO: Task 2. Fill in this function.
        //遍历整个棋盘，若有一个tile不为null，则返回true
        for(int row = 0; row < board.size(); row++) {
            for(int col = 0; col < board.size(); col++) {
                if(board.tile(row, col) != null)return true;
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by this.MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public boolean maxTileExists() {
        // TODO: Task 3. Fill in this function.
        //遍历整个棋盘，若有一个tile等于最大值，则返回true
        for(int row = 0; row < board.size(); row++) {
            for(int col = 0; col < board.size(); col++) {
                if(board.tile(row, col) != null){
                    if(board.tile(row, col).value() == MAX_PIECE)return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public boolean atLeastOneMoveExists() {
        // TODO: Fill in this function.
        //至少有一个空格
        if(emptySpaceExists())return true;
        //相邻可合并
        for (int r = 0; r < size(); r++) {
            for (int c = 0; c < size(); c++) {
                Tile current = tile(c, r);
                if (current != null) {
                    // 检查右边
                    if (c < size() - 1) {
                        Tile right = tile(c + 1, r);
                        if (right != null && right.value() == current.value()) {
                            return true;
                        }
                    }
                    // 检查上边
                    if (r < size() - 1) {
                        Tile down = tile(c, r + 1);
                        if (down != null && down.value() == current.value()) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Moves the tile at position (x, y) as far up as possible.
     *
     * Rules for Tilt:
     * 1. If two Tiles are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     */
    //规则：
    //* 1. 如果两个相邻的方块在移动方向上具有相同的数值，那么它们会合并成一个数值是原数值两倍的新方块，并且这个新数值会被添加到得分实例变量中。
    //* 2. 作为合并结果的方块在该倾斜操作中不会再次合并。因此，在每次移动中，每个方块最多只会参与一次合并（可能不参与任何合并）。
    //* 3. 当在移动方向上相邻的三个方块具有相同的数值时，位于移动方向前端的两个方块会合并，而后面的方块则不会合并。
    public void moveTileUpAsFarAsPossible(int x, int y) {
        // TODO: Tasks 5, 6, and 10. Fill in this function.
        Tile currTile = board.tile(x, y);
        if (currTile == null) return;//确保currTile不为null
        int myValue = currTile.value();
        int targetY = y;

        for(int i = 1;i < size() - y;i++) {
            Tile above = board.tile(x, y+i);
            if(above == null) {
                //可以继续向上移动
                targetY = y+i;
            } else {
                // 遇到非空 tile，判断是否可以合并
                if(myValue == above.value() && !above.wasMerged()) {
                    //当前tile与上方tile相等且上方tile未合并
                    targetY = y+i;
                }
                break;   // 无论是否合并，都必须停止
            }
        }
        //执行移动并合并
        if (targetY != y) {
            Tile targetTile = board.tile(x, targetY);
            if (targetTile == null) {
                board.move(x, targetY, currTile); // 直接移动
            } else {
                board.move(x, targetY, currTile); // 合并
                score += 2 * myValue;             // 更新得分
            }
        }
    }

    /** Handles the movements of the tilt in column x of the board
     * by moving every tile in the column as far up as possible.
     * The viewing perspective has already been set,
     * so we are tilting the tiles in this column up.
     * */
    public void tiltColumn(int x) {
        // TODO: Task 7. Fill in this function.
        for (int y = size() - 1; y >= 0; y--) {
            moveTileUpAsFarAsPossible(x, y);
        }
    }

    public void tilt(Side side) {
        // TODO: Tasks 8 and 9. Fill in this function.
        board.setViewingPerspective(side); // 设置视角

        for (int x = 0; x < size(); x++) {
            tiltColumn(x); // 处理每一列
        }

        board.setViewingPerspective(Side.NORTH); // 恢复标准视角


    }

    /** Tilts every column of the board toward SIDE.
     */
    public void tiltWrapper(Side side) {
        board.resetMerged();
        tilt(side);
    }


    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int y = size() - 1; y >= 0; y -= 1) {
            for (int x = 0; x < size(); x += 1) {
                if (tile(x, y) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(x, y).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (game is %s) %n", score(), over);
        return out.toString();
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Model m) && this.toString().equals(m.toString());
    }

    @Override
    public int hashCode() {
        return toString().hashCode();
    }
}
