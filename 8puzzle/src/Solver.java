import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

/**
 * Implements interface to find a solution to the initial board (using the A* algorithm)
 */
public class Solver {

    private MinPQ<SearchNode> initialQueue;
    private MinPQ<SearchNode> twinQueue;

    private int minMovesCount = -1;
    private Stack<Board> solutionSequence = null;

    // helper class for the search
    private class SearchNode implements Comparable<SearchNode> {

        private Board board;
        private SearchNode previousNode;
        private int movesCount = 0;
        // cache for the priority
        private int priority;

        public SearchNode(Board current, SearchNode previous) {
            this.board = current;
            this.previousNode = previous;
            if (previous != null) {
                this.movesCount = previous.movesCount + 1;
            }
            this.priority = this.board.manhattan() + this.movesCount;
        }

        public int priority() {
            return priority;
        }

        @Override
        public int compareTo(SearchNode that) {
            if (this.priority() == that.priority()) {
                return Integer.compare(this.board.manhattan(), that.board.manhattan());
            }

            return Integer.compare(this.priority(), that.priority());
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        initialQueue = new MinPQ<>();
        twinQueue = new MinPQ<>();

        SearchNode currentNode = new SearchNode(initial, null);
        initialQueue.insert(currentNode);

        SearchNode twinNode = new SearchNode(initial.twin(), null);
        twinQueue.insert(twinNode);

        int step = 0;
        do {
            currentNode = makeMove(initialQueue, currentNode.board);
            twinNode = makeMove(twinQueue, twinNode.board);
            step++;
        } while (!currentNode.board.isGoal() && !twinNode.board.isGoal());

        if (currentNode.board.isGoal()) {
            minMovesCount = currentNode.movesCount;

            SearchNode solutionNode = currentNode;
            solutionSequence = new Stack<>();

            do {
                solutionSequence.push(solutionNode.board);
                solutionNode = solutionNode.previousNode;
            } while (solutionNode != null);
        }
    }

    // performs one search step
    private SearchNode makeMove(MinPQ<SearchNode> searchQueue, Board previousBoard) {
        SearchNode result = searchQueue.delMin();

        Iterable<Board> neighbors = result.board.neighbors();
        for (Board neighborBoard : neighbors) {
            // A critical optimization: don't enqueue a neighbor if its board is the same
            // as the board of the previous search node.
            if (previousBoard == null || !neighborBoard.equals(previousBoard)) {
                searchQueue.insert(new SearchNode(neighborBoard, result));
            }
        }

        return result;
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return (moves() >= 0);
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return minMovesCount;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return solutionSequence;
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

//        int[][] blocks = new int[][] { {1, 2, 3}, {4, 5, 6}, {8, 7, 0} };
//        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
