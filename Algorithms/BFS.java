package Algorithms;

import API.*;
import MarblesPuzzle.Model.Operator;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

public class BFS extends Algorithm {

    private final Hashtable<IState, Node> frontier;
    private final Hashtable<IState, Node> exploredSet;
    private final Queue<Node> Q;

    public BFS(IProblem p, boolean verbose) {
        super(p, verbose);
        this.name = "BFS";
        frontier = new Hashtable<>();
        exploredSet = new Hashtable<>();
        Q = new LinkedList<>();
        Q.add(new Node(getStart()));
    }

    @Override
    public String execute() {
        Node curr;
        long startTime = System.currentTimeMillis();

        while (!Q.isEmpty()) {
            curr = Q.poll();

            print(curr);

            frontier.remove(curr.getState());
            exploredSet.put(curr.getState(), curr);

            // Do exploring...
            // Apply allowed operators on any movable marble from current board state:
            for (IOperator operator : Operator.allowedOperators(curr)) {
                IState g = operator.apply();

                if (!(frontier.containsKey(g) || exploredSet.containsKey(g))) {
                    Node next = new Node(curr, g);
                    if (isGoal(g)) {
                        return output(path(next), next.getWeight(), startTime);
                    } else {
                        frontier.put(g, next);
                        Q.add(next);
                    }
                }
            }
        }
        return output(path(null), -1, startTime);
    }
}