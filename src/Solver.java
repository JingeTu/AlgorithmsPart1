//import edu.princeton.cs.algs4.MinPQ;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//
///**
// * Created by jg on 27/02/2017.
// */
//public class Solver {
//
//    private MinPQ<SearchNode> pQ;
//    private MinPQ<SearchNode> pQTwin;
//    private ArrayList<Board> solution;
//    private boolean findGoal;
//    private boolean findGoalTwin;
//
//    private class SearchNode implements Comparable<SearchNode> {
//        public int moves;
//        public Board current;
//        public Board prev;
//
//        public SearchNode(Board current, Board prev, int moves) {
//            this.current = current;
//            this.prev = prev;
//            this.moves = moves;
//        }
//
//        @Override
//        public int compareTo(SearchNode that) {
//            if ((this.current.manhattan() + this.moves) < (that.current.manhattan() + that.moves))
//                return -1;
//            else if ((this.current.manhattan() + this.moves) > (that.current.manhattan() + that.moves))
//                return 1;
//            else
//                return 0;
//        }
//    }
//
//    public Solver(Board initial) {
//        pQ = new MinPQ<SearchNode>();
//        pQ.insert(new SearchNode(initial, null, 0));
//
//        Board initial_twin = initial.twin();
//
//        pQTwin = new MinPQ<SearchNode>();
//        pQTwin.insert(new SearchNode(initial_twin, null, 0));
//
//        findGoal = initial.isGoal();
//        findGoalTwin = initial_twin.isGoal();
//
//        solution = new ArrayList<Board>();
//
//        while (true) {
//            SearchNode cSN = pQ.delMin();
//            solution.add(cSN.current);
//            if (cSN.current.isGoal()) {
//                findGoal = true;
//                break;
//            }
//            Iterable<Board> neighbors = cSN.current.neighbors();
//            Iterator<Board> iter = neighbors.iterator();
//            while (iter.hasNext()) {
//                Board temp = iter.next();
//                if (cSN.prev != null && temp.equals(cSN.prev))
//                    continue;
//                pQ.insert(new SearchNode(temp, cSN.current, cSN.moves + 1));
//            }
//
//            SearchNode cSN_twin = pQTwin.delMin();
//            if (cSN_twin.current.isGoal()) {
//                findGoalTwin = true;
//                break;
//            }
//            Iterable<Board> neighbors_twin = cSN_twin.current.neighbors();
//            Iterator<Board> iter_twin = neighbors_twin.iterator();
//            while (iter_twin.hasNext()) {
//                Board temp_twin = iter_twin.next();
//                if (cSN_twin.prev != null && temp_twin.equals(cSN_twin.prev))
//                    continue;
//                pQTwin.insert(new SearchNode(temp_twin, cSN_twin.current, cSN_twin.moves + 1));
//            }
//        }
//    }           // find a solution to the initial board (using the A* algorithm)
//
//    public boolean isSolvable() {
//        return findGoal;
//    }            // is the initial board solvable?
//
//    public int moves() {
//        return solution.size();
//    }                     // min number of moves to solve initial board; -1 if unsolvable
//
//    public Iterable<Board> solution() {
//        return solution;
//    }      // sequence of boards in a shortest solution; null if unsolvable
//
//    public static void main(String[] args) {
//
////        MinPQ<Board> pQ = new MinPQ<Board>();
////        int[][] blocks = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
////        Board b = new Board(blocks);
////        pQ.insert(b);
////        Iterable<Board> neighbors = b.neighbors();
////        Iterator<Board> iter = neighbors.iterator();
////        while (iter.hasNext()) {
////            Board temp = iter.next();
////            pQ.insert(temp);
////        }
////
////        while (!pQ.isEmpty()) {
////            Board temp = pQ.delMin();
////            System.out.println("manhattan: " + temp.manhattan());
////            System.out.println(temp);
////        }
//
//        int[][] blocks = {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
//        Board b = new Board(blocks);
//        Solver sol = new Solver(b);
//
//        System.out.println(sol.isSolvable());
//        System.out.println(sol.moves());
//
//        Iterable<Board> neighbors = sol.solution();
//        Iterator<Board> iter = neighbors.iterator();
//        if (sol.isSolvable())
//            while (iter.hasNext()) {
//                System.out.println(iter.next());
//            }
//
//    } // solve a slider puzzle (given below)
//}


import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Queue;

import java.util.Comparator;
import java.util.Iterator;


public class Solver {

    private Node solutionNode;
    private Node solutionNodeTwin;
    private MinPQ<Node> thePQ;
    private MinPQ<Node> thePQTwin;
    private boolean initialSolvable = false;
    private boolean initialSolvableTwin = false;
    private final static Comparator<Node> BOARD_ORDER = new BoardOrder();
    private Queue<Board> solutionBoards;
    private class Node {
        private Board theBoard;
        private Node previousNode;
        private int numberOfMovesMade;

        Node() {
            this.numberOfMovesMade = 0;
        }
    }

    private static class BoardOrder implements Comparator<Node>
    {

        @Override
        public int compare(Node o1, Node o2) {
            // TODO Auto-generated method stub
            int a = o1.theBoard.manhattan()  + o1.numberOfMovesMade;
            int b = o2.theBoard.manhattan()  + o2.numberOfMovesMade;

            if (a < b)
                return -1;
            else if (a > b)
                return 1;
            else if (a == b)
                return 0;

            return -1;
        }

    }

    public Solver(Board initial)
    {
        // find a solution to the initial board (using the A* algorithm)
		/*
		1. Create first Node from initial Board
		2. Add initial Node to priority queue
		3. Dequeue Node with least priority
		4. Get back an list of all neighbor boards for dequeued Node
		5. Add each new Node to priority queue with the Nodes previous board set to last dequeued board
		6. Repeat until dequeued board is equal to goal board
		*/
        solutionNode = new Node();
        solutionNodeTwin = new Node();

        solutionNode.theBoard = initial;
        solutionNodeTwin.theBoard = initial.twin();

        solutionNode.previousNode = null;
        solutionNodeTwin.previousNode = null;

        thePQ = new MinPQ<Node>(Solver.BOARD_ORDER);
        thePQTwin = new MinPQ<Node>(Solver.BOARD_ORDER);

        thePQ.insert(solutionNode);
        thePQTwin.insert(solutionNodeTwin);
        solutionBoards = new Queue<Board>();

        int counter = 0;
        while(!solutionNode.theBoard.isGoal() | !solutionNodeTwin.theBoard.isGoal()) {

            solutionNode = thePQ.delMin();
            solutionBoards.enqueue(solutionNode.theBoard);
            if (solutionNode.theBoard.isGoal()) {
                initialSolvable = true;
                break;
            } else {
                solutionNode.numberOfMovesMade++;
                Iterable<Board> neighborBoards = solutionNode.theBoard.neighbors();
                Iterator<Board> itr = neighborBoards.iterator();
                while(itr.hasNext()) {
                    Node neighborNode = new Node();
                    neighborNode.theBoard = itr.next();
                    neighborNode.numberOfMovesMade = solutionNode.numberOfMovesMade;
                    neighborNode.previousNode = solutionNode;
                    if (counter == 0)
                        thePQ.insert(neighborNode);
                    else if (!neighborNode.theBoard.equals(solutionNode.previousNode.theBoard))
                        thePQ.insert(neighborNode);
                }
            }

            solutionNodeTwin = thePQTwin.delMin();
            if (solutionNodeTwin.theBoard.isGoal()) {
                initialSolvableTwin = true;
                break;
            } else {
                solutionNodeTwin.numberOfMovesMade++;
                Iterable<Board> neighborBoardsTwin = solutionNodeTwin.theBoard.neighbors();
                Iterator<Board> itr2 = neighborBoardsTwin.iterator();
                while(itr2.hasNext()) {
                    Node neighborNodeTwin = new Node();
                    neighborNodeTwin.theBoard = itr2.next();
                    neighborNodeTwin.numberOfMovesMade = solutionNodeTwin.numberOfMovesMade;
                    neighborNodeTwin.previousNode = solutionNodeTwin;
                    if (counter == 0)
                        thePQTwin.insert(neighborNodeTwin);
                    else if (!neighborNodeTwin.theBoard.equals(solutionNodeTwin.previousNode.theBoard))
                        thePQTwin.insert(neighborNodeTwin);
                }
            }
            counter++;
        }
    }

    public boolean isSolvable()
    {
        // is the initial board solvable?
        if (initialSolvable)
            return true;
        else if (initialSolvableTwin)
            return false;
        else
            return false;
    }

    public int moves()
    {
        // min number of moves to solve initial board; -1 if no solution
        if (initialSolvable)
            return solutionNode.numberOfMovesMade;
        else
            return -1;
    }

    public Iterable<Board> solution()
    {
        // sequence of boards in a shortest solution; null if no solution

        if (initialSolvable) {
            return solutionBoards;
        } else
            return null;

    }

    public static void main(String[] args) {

        int[][] blocks = {{0, 1, 3}, {4, 2, 5}, {7, 8, 6}};
        Board b = new Board(blocks);
        Solver sol = new Solver(b);

        System.out.println(sol.isSolvable());
        System.out.println(sol.moves());

        Iterable<Board> neighbors = sol.solution();
        Iterator<Board> iter = neighbors.iterator();
        if (sol.isSolvable())
            while (iter.hasNext()) {
                System.out.println(iter.next());
            }

    }

}