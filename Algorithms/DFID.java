package Algorithms;

import API.Algorithm;
import API.IProblem;
import MarblesPuzzle.Model.Node;
import MarblesPuzzle.Model.Operator;
import MarblesPuzzle.Model.State;

import java.util.Hashtable;

public class DFID extends Algorithm {

    private Hashtable<State, Node<State>> frontier;

    public DFID(IProblem p, boolean verbose) {
        super(p, verbose);
        this.name = "DFID";
    }

    private String LimitedDFS(Node<State> curr, int depth, Hashtable<State, Node<State>> workingBranch) {
        if (withOpen()) {
            System.out.println(curr);
        }
        if (isGoal(curr)) {
            return output(path(curr), curr.getWeight());
        } else if (depth == 0) {
            return "cutoff";
        }
        workingBranch.put(curr.getState(), curr);
        boolean isCutoff = false;
        Node<State> next = null;
        String result;

        for (Operator operator : Operator.allowedOperators(curr)) {
            State g = operator.apply(curr);
            if (null == g || workingBranch.containsKey(g)) {continue;}
            next = new Node<>(curr, g);
            result = LimitedDFS(next, depth-1, workingBranch);

            if (result.equals("cutoff")) {
                isCutoff = true;
            } else if (!result.equals("fail")) {
                return result;
            }
        }

        if (null != next) {
            workingBranch.remove(next.getState());
        }
        if (isCutoff) {
            return "cutoff";
        } else return "fail";
    }

    @Override
    public String execute() {

        int l = Integer.MAX_VALUE; // limit

        Node<State> root = new Node<>(getStart());
        timerOn();
        String output;
        for (int i = 1; i < l; ++i) {
            frontier = new Hashtable<>();
            output = LimitedDFS(root, i, frontier);
            if (!output.equals("cutoff")) {
                return output;
            }
        }
        return "no path";
    }
}