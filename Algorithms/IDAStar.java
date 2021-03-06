package Algorithms;

import API.*;
import MarblesPuzzle.Model.*;

import java.util.Hashtable;
import java.util.Stack;

public class IDAStar extends Algorithm {
    private final Stack<Node> STK;                  // Stack
    private final Hashtable<IState, Node> frontier;  // Open-list
    private final HeuristicEval heuristics;                // Heuristic evaluation object
    private int t;                                         // Threshold
    private final IState start;


    public IDAStar(IProblem p, boolean verbose, HeuristicEval heuristic) {
        super(p, verbose);
        this.name = "IDA*";
        this.start = p.getInitialState();
        this.heuristics = heuristic;
        this.t = heuristic.h(start);
        STK = new Stack<>();
        frontier = new Hashtable<>();
    }

    @Override
    public String execute() {
        int _f, minF;
        Node curr, n;
        long startTime = System.currentTimeMillis();

        while (t != Integer.MAX_VALUE) {
            // Minimum f(x) value we have seen through an iteration
            minF = Integer.MAX_VALUE;
            curr = new Node(start);
            curr.setTag(false); // Means node is out of the stack
            STK.add(curr);
            frontier.put(start, curr);

            while (!STK.isEmpty()) {
                n = STK.pop();
                if (withOpen()) {
                    System.out.println(n);
                }
                if (n.isOut()) {
                    frontier.remove(n.getState());
                }
                else {
                    // Mark the node to be removed next time it's added to the stack
                    n.markAsOut();
                    // Push back to the stack
                    STK.add(n);

                    IState g;
                    for (IOperator operator : Operator.allowedOperators(n)) {
                        g = operator.apply();
                        _f =  // f(x) = h(x.getState()) + g(x)
                                heuristics
                                .h(g)
                                + n.getWeight(); // == g(x)
                        if (_f > t) {
                            minF = Math.min(minF, _f);
                            continue;
                        }
                        if (frontier.contains(g) && frontier.get(g).isOut()) {
                            continue;
                        }

                        Node next = new Node(n, g);
                        if (frontier.contains(g) && !frontier.get(g).isOut()) {
                            if (heuristics.
                                f(frontier.get(g)) >
                                heuristics.f(next)) {
                                    STK.remove(frontier.get(g));
                                    frontier.remove(g);
                            }
                            else {
                                continue;
                            }
                        }

                        if (isGoal(next)) {
                            return output(path(next), next.getWeight(), startTime);
                        }

                        STK.add(next);
                        frontier.put(g, next);
                        }
                    }
                }
            t = minF;
        }
        return output(path(null), -1, startTime);
    }
}