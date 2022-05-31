package MarblesPuzzle.Model;

import API.Algorithm;
import API.IOperator;
import API.IState;
import API.Node;
import MarblesPuzzle.Model.Utils.Direction;
import MarblesPuzzle.Model.Utils.Pair;

import java.util.ArrayList;
import java.util.List;

import static MarblesPuzzle.Model.Utils.Direction.*;

/**
 * Operator class.
 *
 * A `Node` is the Data-Structure, which holds
 * `State`s of a `Problem `p`, during the search of an `Algorithm`.
 * It contains the the state,
 * it's depth in the search tree,
 * it's weight (`Node`s gain weight only when being applied an `Operator`.
 * @see State
 * @see Algorithm
 * @see Operator
 */
public class Operator implements IOperator {
    private final Node n;
    private final Pair p;

    private Operator(Node n, Pair p) {
        this.n = n;
        this.p = p;
    }

    // Apply the operator to node `n` with a `MarbleButtonView` and a `Direction` (Located in field `p`).
    @Override
    public IState apply() {
        return new State(n.getState(), p);
    }

    /**
     * Find all operators which could be applied to a given node `n`.
     * Allowed operators are any single `src.Model.MarbleButtonView` movement on the board
     * to an empty space, IF AND ONLY IF - it will not bring the board
     * back to its previous state (If it was generated by one).
     * @param n - src.Model.Node to find allowed operators for.
     * @return - A list of all allowed operators, which will be applied
     *           to `n` by invoking operator.apply().
     */
    public static List<IOperator> allowedOperators(Node n) {
        List<IOperator> allowedOperators = new ArrayList<>();
        Marble badMarble = null;
        Direction prevDirection = null;
        State s = (State) n.getState();

        if (null != n.getParent()) {
            badMarble = s.getOperatedMarble();
            prevDirection = opposite(s.getOperatedMarbleDirection());
        }

        for (Direction d : values()) {
            for (Marble m : s.getMovableMarbles()) {
                if (null != badMarble) {
                    if (m.equals(badMarble) && prevDirection.equals(d)) {continue;}
                }
                if (s.movableMarble(m, d)) {
                    allowedOperators.add(new Operator(n, new Pair(d, m)));
                }
            }
        }
        return allowedOperators;
    }

    private static Direction opposite (Direction d) {
        switch (d) {
            case UP:
                return DOWN;
            case DOWN:
                return UP;
            case LEFT:
                return RIGHT;
            case RIGHT:
                return LEFT;
        }
        return UP;
    }

    @Override
    public String toString() {
        return "Operator{" +
                p +
                '}';
    }
}