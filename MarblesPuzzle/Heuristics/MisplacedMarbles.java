package MarblesPuzzle.Heuristics;

import API.HeuristicEval;
import API.IState;
import MarblesPuzzle.Model.State;

import java.util.HashMap;
public class MisplacedMarbles extends HeuristicEval {

    private final String[][] board;
    private final int dim;
    private final int R = 1;
    private final int B = 2;
    private final int G = 10;
    private final int Y = 1;
    private final HashMap<String, Integer> costs;

    public MisplacedMarbles(IState goal) {
        super(goal);
        State s = (State) goal;
        board = s.getBoard();
        dim = board.length;
        costs = new HashMap<>();
        costs.put("_", 0);
        costs.put("R", 1);
        costs.put("B", 2);
        costs.put("G", 10);
        costs.put("Y", 1);
    }
    public int h(IState s) {
        if (s.equals(getGoalState())) return 0;
        int h = 0;
        State state = (State) s;
        System.out.println("======================================");
        System.out.println("Heuristic evaluation of\n" + s);
        String[][] b = state.getBoard();
        for (int i = 0; i < dim; ++i) {
            for (int j = 0; j < dim; ++j) {
                if (!board[i][j].equals(b[i][j])) {
                    h += (costs.get(b[i][j]));
                }
            }
        }
        System.out.println("======================================");
        System.out.println("f(Board:" + s + ") :: -> " + h);
        return h;
    }
}
