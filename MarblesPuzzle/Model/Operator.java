package MarblesPuzzle.Model;

import API.Algorithm;
import MarblesPuzzle.Utils.Direction;
import MarblesPuzzle.Utils.Pair;

import java.util.ArrayList;
import java.util.List;

import static MarblesPuzzle.Utils.Direction.*;

/**
 * src.Model.Operator class.
 *
 * A `src.Model.Node` is the Data-Structure, which holds
 * `src.Model.State`s of a src.Model.Problem `p`, during the search of an `src.API.Algorithm`.
 * It contains the the state,
 * it's depth in the search tree,
 * it's weight (`src.Model.Node`s gain weight only when being applied an `src.Model.Operator`.
 * @see State
 * @see Algorithm
 * @see Operator
 */
public class Operator {
    private Pair p;

    private Operator(Pair p) {this.p = p;}

    // Apply the operator to node `n` with a `src.Model.Marble` and a `src.Utils.Direction` (Located in field `p`).
    public State apply(Node<State> n) {
        if (null == n || null == p) {return null;}
        return new State(n.getState(), p);
    }

    /**
     * Find all operators which could be applied to a given node `n`.
     * Allowed operators are any single `src.Model.Marble` movement on the board
     * to an empty space, IF AND ONLY IF - it will not bring the board
     * back to its previous state (If it was generated by one).
     * @param n - src.Model.Node to find allowed operators for.
     * @return - A list of all allowed operators, which will be applied
     *           to `n` by invoking operator.apply().
     */
    public static List<Operator> allowedOperators(Node<State> n) {
        List<Operator> allowedOperators = new ArrayList<>();
        Marble badMarble = null;
        Direction prevDirection = null;
        if (null != n.getParent()) {
            badMarble = n.getState().getOperatedMarble();
            prevDirection = opposite(n.getState().getOperatedMarbleDirection());
        }

        for (Direction d : values()) {
            for (Marble m : n.getState().getMovableMarbles()) {
                if (null != badMarble) {
                    if (m.equals(badMarble) && prevDirection.equals(d)) {continue;}
                }
                if (n.getState().movableMarble(m, d)) {
                    allowedOperators.add(new Operator(new Pair(d, m)));
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