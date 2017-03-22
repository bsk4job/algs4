import edu.princeton.cs.algs4.Queue;

/**
 * Puzzle's board
 */
public class Board {
    private int[][] tiles;
    private int n = 0;

    // construct a board from an n-by-n array of blocks
    // (where blocks[i][j] = block in row i, column j)
    public Board(int[][] blocks) {
        if (blocks == null) throw new java.lang.NullPointerException();

        this.tiles = blocks.clone();
        this.n = tiles[0].length;
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // calculates the correct row of the block
    private int getCorrectRow(int block) {
        if (block == 0) return n - 1;

        return (block - 1) / n;
    }

    // calculates the correct row of the block
    private int getCorrectColumn(int block) {
        if (block == 0) return n - 1;

        return (block - 1) % n;
    }

    // number of blocks out of place
    public int hamming() {
        int result = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int val = tiles[i][j];

                if (val == 0) continue;

                int row = getCorrectRow(val), col = getCorrectColumn(val);
                if (i != row || j != col) {
                    result++;
                }
            }
        }

        return result;
    }

    // sum of Manhattan distances between blocks and goal
    public int manhattan() {
        int result = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int val = tiles[i][j];

                if (val == 0) continue;

                int row = getCorrectRow(val), col = getCorrectColumn(val);
                result += Math.abs(row - i) + Math.abs(col - j);
            }
        }

        return result;
    }

    // is this board the goal board?
    public boolean isGoal() {

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int val = tiles[i][j];
                if (getCorrectRow(val) != i
                        || getCorrectColumn(val) != j) {
                    return false;
                }
            }
        }

        return true;
    }

    // returns new array of tiles where tiles [i1, j1] and [i2, j2] are swapped
    private int[][] swap(int i1, int j1, int i2, int j2) {
        int[][] result = new int[n][n];

        for (int i = 0; i < n; i++) {
            result[i] = tiles[i].clone();
        }

        // swapping two tiles
        int t = result[i2][j2];
        result[i2][j2] = result[i1][j1];
        result[i1][j1] = t;

        return result;
    }

    // a board that is obtained by exchanging any pair of blocks
    public Board twin() {
        // swapping the first two tiles
        int[][] twinTiles = this.swap(0, 0, 0, 1);

        return new Board(twinTiles);
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;

        if (this.dimension() != that.dimension()) return false;

        return (this.toString().equals(that.toString()));
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> result = new Queue<>();

        // looking for the empty tile
        int zeroRow = -1, zeroCol = -1;
        for (int i = 0; i < n; i++) {

            if (zeroRow > 0 && zeroCol > 0) break;

            for (int j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    zeroRow = i;
                    zeroCol = j;
                    break;
                }
            }
        }

        if (zeroRow < 0 || zeroCol < 0) return result;

        // getting the neighbours
        if (zeroRow > 0) {
            int[][] neighbour = this.swap(zeroRow, zeroCol, zeroRow - 1, zeroCol);
            result.enqueue(new Board(neighbour));
        }

        if (zeroRow < n - 1) {
            int[][] neighbour = this.swap(zeroRow, zeroCol, zeroRow + 1, zeroCol);
            result.enqueue(new Board(neighbour));
        }

        if (zeroCol > 0) {
            int[][] neighbour = this.swap(zeroRow, zeroCol, zeroRow, zeroCol - 1);
            result.enqueue(new Board(neighbour));
        }

        if (zeroCol < n - 1) {
            int[][] neighbour = this.swap(zeroRow, zeroCol, zeroRow, zeroCol + 1);
            result.enqueue(new Board(neighbour));
        }

        return result;
    }

    public static void main(String[] args) {
        int[][] testTiles = new int[][] { {8, 1, 3}, {4, 0, 2}, {7, 6, 5} };
        int[][] testGoalTiles = new int[][] { {1, 2, 3}, {4, 5, 6}, {7, 8, 0} };

        Board testBoard = new Board(testTiles);
        Board goalBoard = new Board(testGoalTiles);

        System.out.println(testBoard.toString());

        System.out.println("dimension: " + testBoard.dimension());
        System.out.println("hamming: " + testBoard.hamming());
        System.out.println("manhattan: " + testBoard.manhattan());
        System.out.println("isGoal (false): " + testBoard.isGoal());
        System.out.println("isGoal (true): " + goalBoard.isGoal());

        Board twinBoard = testBoard.twin();
        System.out.println("twin:");
        System.out.println(twinBoard.toString());

        Board equalBoard = new Board(testTiles);

        System.out.println("equals (true): " + testBoard.equals(equalBoard));
        System.out.println("equals (false): " + testBoard.equals(goalBoard));

        Iterable<Board> testNeighbours = testBoard.neighbors();

        System.out.println("neighbours(): ");
        for (Board neighbour : testNeighbours) {
            System.out.println(neighbour.toString());
        }
    }

}
