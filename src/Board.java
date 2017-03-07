/**
 * Created by jg on 27/02/2017.
 */

import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Iterator;

public class Board {

    private int[][] mBlocks;
    private int[] correctPosY; // 1 2 3 4 5 6 7 8 0
    private int[] correctPosX; // 1 2 3 4 5 6 7 8 0
    private int blankPosY;
    private int blankPosX;
    private ArrayList<Board> neighbors;
    private int manhanttan;
    private int N;

    public Board(int[][] blocks) {

        if (blocks == null) {
            throw new java.lang.NullPointerException();
        }

        int total_num = 0;
        mBlocks = new int[blocks.length][];
        for (int i = 0; i < blocks.length; ++i) {
            mBlocks[i] = new int[blocks[i].length];
            total_num +=blocks[i].length;
            for (int j = 0; j < blocks[i].length; ++j) {
                mBlocks[i][j] = blocks[i][j];
                if (blocks[i][j] == 0) {
                    blankPosY = i;
                    blankPosX = j;
                }
            }
        }

        correctPosY = new int[total_num];
        correctPosX = new int[total_num];

        int count = 0;
        for (int i = 0; i < mBlocks.length; ++i) {
            for (int j = 0; j < mBlocks[i].length; ++j) {
                correctPosY[count] = i;
                correctPosX[count] = j;
                ++count;
            }
        }

        neighbors = new ArrayList<Board>();

        manhanttan = -1;
    }

    public int dimension() {
        return mBlocks.length;
    }

    public int hamming() {
        int correct_num = 0;
        int hamming_dis = 0;
        for (int i = 0; i < mBlocks.length; ++i) {
            for (int j = 0; j < mBlocks[i].length; ++j) {
                if ((++correct_num) != mBlocks[i][j] && mBlocks[i][j] != 0) {
                    ++hamming_dis;
                }
            }
        }
        return hamming_dis;
    }

    public int manhattan() {
        if (manhanttan != -1)
            return manhanttan;
        int manhattan_dis = 0;
        for (int i = 0; i < mBlocks.length; ++i) {
            for (int j = 0; j < mBlocks[i].length; ++j) {
                int current_num = mBlocks[i][j] - 1;
                if (current_num == -1) continue;
                int y_dis = (i - correctPosY[current_num]) > 0 ? (i - correctPosY[current_num]) : (correctPosY[current_num] - i);
                int x_dis = (j - correctPosX[current_num]) > 0 ? (j - correctPosX[current_num]) : (correctPosX[current_num] - j);
                manhattan_dis += y_dis + x_dis;
            }
        }
        manhanttan = manhattan_dis;

        return manhattan_dis;
    }

    public boolean isGoal() {
        if (mBlocks[this.dimension() - 1][this.dimension() - 1] != 0) {
            return false;
        }
        int correct_num = 0;
        for (int i = 0; i < mBlocks.length; ++i) {
            for (int j = 0; j < mBlocks[i].length; ++j) {
                if ((++correct_num) != mBlocks[i][j] && mBlocks[i][j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public Board twin() {

        int y_1 = 0;
        int x_1 = 0;
        int y_2 = this.dimension() - 1;
        int x_2 = this.dimension() - 1;

        boolean valid_pair = false;
        while (!valid_pair) {
            int index1 = StdRandom.uniform(0, this.dimension() * this.dimension());
            int index2 = StdRandom.uniform(0, this.dimension() * this.dimension());
            while (index1 == index2) {
                index1 = StdRandom.uniform(0, this.dimension() * this.dimension());
                index2 = StdRandom.uniform(0, this.dimension() * this.dimension());
            }
            y_1 = index1 / this.dimension();
            x_1 = index1 % this.dimension();
            y_2 = index2 / this.dimension();
            x_2 = index2 % this.dimension();
            if (mBlocks[y_1][x_1] != 0 && mBlocks[y_2][x_2] != 0)
                valid_pair = true;
        }

        Board new_board = new Board(this.mBlocks);

//        int temp = new_board.mBlocks[y_1][x_1];
//        new_board.mBlocks[y_1][x_1] = new_board.mBlocks[y_2][x_2];
//        new_board.mBlocks[y_2][x_2] = temp;
        new_board.mBlocks = swap(new_board.mBlocks, y_1, x_1, y_2, x_2);

        return new_board;
    }

    public boolean equals(Object y) {
        if (y == null) {
            throw new java.lang.NullPointerException();
        }
        if (((Board) y).dimension() != this.dimension()) {
            return false;
        }
        for (int i = 0; i < mBlocks.length; ++i) {
            for (int j = 0; j < mBlocks[i].length; ++j) {
                if (((Board) y).mBlocks[i][j] != this.mBlocks[i][j]){
                    return false;
                }
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        neighbors.clear();
        // U
        if (blankPosY - 1 >= 0) {
            Board U_neighbor =new Board(this.mBlocks);
            U_neighbor.mBlocks = swap(U_neighbor.mBlocks, blankPosY, blankPosX, blankPosY - 1, blankPosX);
            neighbors.add(U_neighbor);
        }
        // B
        if (blankPosY + 1 < this.dimension()) {
            Board B_neighbor = new Board(this.mBlocks);
            B_neighbor.mBlocks = swap(B_neighbor.mBlocks, blankPosY, blankPosX, blankPosY + 1, blankPosX);
            neighbors.add(B_neighbor);
        }
        // L
        if (blankPosX - 1 >= 0) {
            Board L_neighbor = new Board(this.mBlocks);
            L_neighbor.mBlocks = swap(L_neighbor.mBlocks, blankPosY, blankPosX, blankPosY, blankPosX - 1);
            neighbors.add(L_neighbor);
        }
        // R
        if (blankPosX + 1 < this.dimension()) {
            Board R_neighbor = new Board(this.mBlocks);
            R_neighbor.mBlocks = swap(R_neighbor.mBlocks, blankPosY, blankPosX, blankPosY, blankPosX + 1);
            neighbors.add(R_neighbor);
        }
        return neighbors;
    }

    private int[][] swap(int[][] blocks, int y1, int x1, int y2, int x2) {
        int temp = blocks[y1][x1];
        blocks[y1][x1] = blocks[y2][x2];
        blocks[y2][x2] = temp;
        return blocks;
    }

    public String toString() {
        this.N = this.dimension();
        String board = this.N + "\n";
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {
                String addNum = this.mBlocks[i][j] + " ";
                board = board.concat(addNum);
            }
            board = board.concat("\n");
        }
        return board;
    }

    public static void main(String[] args) {
        int[][] blocks = {{8, 1, 3}, {4, 0, 2}, {7, 6, 5}};
        Board b = new Board(blocks);
        Board copy = new Board(blocks);
        System.out.println(b.toString());
        System.out.println("manhattan:\t" + b.manhattan());
        System.out.println("hamming:\t" + b.hamming());
        System.out.println(b.equals(copy));
        System.out.println("Twin: ");
        System.out.println(b.twin().toString());
        System.out.println("Neighbors: ");
        Iterable<Board> neighbors = b.neighbors();
        Iterator<Board> iter = neighbors.iterator();
        while (iter.hasNext()) {
            Board temp = iter.next();
            System.out.println(temp.toString());
        }
    }
}


//import edu.princeton.cs.algs4.Queue;
//
//public class Board {
//    private final int[][] board;
//    private final int N;
//
//    public Board(int[][] blocks)
//    {
//        // construct a board from an N-by-N array of blocks
//        // (where blocks[i][j] = block in row i, column j)
//        this.board = blocks;
//        this.N = blocks.length;
//    }
//
//    public int dimension()
//    {
//        // board dimension N
//        return this.board.length;
//    }
//
//    public int hamming()
//    {
//        // number of blocks out of place
//        int counter = 0;
//        int goalNum = 1;
//        for (int i = 0; i < this.N; i++) {
//            for (int j = 0; j < this.N; j++) {
//                if (i == (this.N - 1) && j == (this.N - 1))
//                    break;
//                if (this.board[i][j] == goalNum)
//                    counter++;
//                goalNum++;
//            }
//        }
//        int outOfPlace = ((this.N * this.N) -1) - counter;
//
//        return outOfPlace;
//    }
//
//    public int manhattan()
//    {
//        // sum of Manhattan distances between blocks and goal
//
//        int total = 0;
//        for (int i = 0; i < this.N; i++) {
//            for (int j = 0; j < this.N; j++) {
//                int goalRow;
//                int goalCol;
//                if (this.board[i][j] != 0) {
//                    goalRow = (this.board[i][j] - 1) / this.N;
//                    goalCol = (this.board[i][j] - 1) % this.N;
//                    total += Math.abs(i - goalRow) + Math.abs(j - goalCol);
//                }
//            }
//        }
//
//        return total;
//    }
//
//    public boolean isGoal()
//    {
//        // is this board the goal board?
//        int outOfPlace = this.hamming();
//        if (outOfPlace == 0)
//            return true;
//        else
//            return false;
//    }
//
//    private static Board copy(Board x)
//    {
//        int[][] twin = new int[x.N][x.N];
//
//        for (int i = 0; i < x.N; i++) {
//            for (int j = 0; j< x.N; j++) {
//                twin[i][j] = x.board[i][j];
//            }
//        }
//
//        Board boardCopy = new Board(twin);
//        return boardCopy;
//    }
//
//    public Board twin()
//    {
//        // a board obtained by exchanging two adjacent blocks in the same row
//        int block1;
//        int block2;
//
//        int[][] twin = new int[this.N][this.N];
//        int zeroRow = 0;
//        for (int i = 0; i < this.N; i++) {
//            for (int j = 0; j< this.N; j++) {
//                if (this.board[i][j] == 0) {
//                    zeroRow = i;
//                }
//                twin[i][j] = this.board[i][j];
//            }
//        }
//        if (zeroRow == (this.N - 1)) {
//            block1 = this.board[zeroRow -1][0];
//            block2 = this.board[zeroRow -1][1];
//            twin[zeroRow - 1][0] = block2;
//            twin[zeroRow - 1][1] = block1;
//        } else {
//            block1 = this.board[zeroRow + 1][0];
//            block2 = this.board[zeroRow + 1][1];
//            twin[zeroRow + 1][0] = block2;
//            twin[zeroRow + 1][1] = block1;
//        }
//
//        Board twinBoard = new Board(twin);
//        return twinBoard;
//
//
//    }
//
//    public boolean equals(Object y)
//    {
//        int counter = 0;
//        if (!(y instanceof Board)) return false;
//        Board yBoard = (Board)y;
//        // does this board equal y?
//        if (this.N != yBoard.N)
//            return false;
//
//        for (int i = 0; i < this.N; i++) {
//            for (int j = 0; j < this.N; j++) {
//                if (this.board[i][j] == yBoard.board[i][j])
//                    counter++;
//            }
//        }
//
//        if (counter == (this.N*this.N))
//            return true;
//        else
//            return false;
//    }
//
//    public String toString()
//    {
//        // string representation of the board (in the output format specified below)
//        String board = this.N + "\n";
//        for (int i = 0; i < this.N; i++) {
//            for (int j = 0; j < this.N; j++) {
//                String addNum = this.board[i][j] + " ";
//                board = board.concat(addNum);
//            }
//            board = board.concat("\n");
//        }
//        return board;
//    }
//
//    public Iterable<Board> neighbors()
//    {
//        // all neighboring boards
//        int x = 0;
//        int y = 0;
//        // Find out where the zero is
//        for (int i = 0; i < this.N; i++) {
//            for (int j = 0; j < this.N; j++) {
//                if (this.board[i][j] == 0) {
//                    x = i;
//                    y = j;
//                    break;
//                }
//            }
//        }
//
//        Queue<Board> aque = new Queue<Board>();
//
//        try {
//            Board newBoard = copy(this);
//            newBoard.board[x][y] = this.board[x+1][y];
//            newBoard.board[x+1][y] = 0;
//            aque.enqueue(newBoard);
//        } catch (ArrayIndexOutOfBoundsException e) {
//
//        }
//
//        try {
//            Board newBoard2 = copy(this);;
//            newBoard2.board[x][y] = this.board[x-1][y];
//            newBoard2.board[x-1][y] = 0;
//            aque.enqueue(newBoard2);
//        } catch (ArrayIndexOutOfBoundsException e) {
//
//        }
//
//        try {
//            Board newBoard3 = copy(this);
//            newBoard3.board[x][y] = this.board[x][y + 1];
//            newBoard3.board[x][y + 1] = 0;
//            aque.enqueue(newBoard3);
//        } catch (ArrayIndexOutOfBoundsException e) {
//
//        }
//
//        try {
//            Board newBoard4 = copy(this);
//            newBoard4.board[x][y] = this.board[x][y - 1];
//            newBoard4.board[x][y - 1] = 0;
//            aque.enqueue(newBoard4);
//        } catch (ArrayIndexOutOfBoundsException e) {
//
//        }
//
//
//        return aque;
//    }
//
//
//    public static void main(String[] args) {
//        // TODO Auto-generated method stub
//		/*
//		int[][] arr = new int[3][3];
//		arr[0][0] = 0;
//		arr[0][1] = 1;
//		arr[0][2] = 3;
//		arr[1][0] = 4;
//		arr[1][1] = 2;
//		arr[1][2] = 5;
//		arr[2][0] = 7;
//		arr[2][1] = 8;
//		arr[2][2] = 6;
//
//		Board newB = new Board(arr);
//		StdOut.println(newB.manhattan());
//		*/
//        //Iterable<Board> neighbors = newB.neighbors();
//        //Iterator<Board> itr = neighbors.iterator();
//        //StdOut.println(newB);
//        //while (itr.hasNext()) {
//        //	Board output = itr.next();
//        //	if (output.equals(newB))
//        //		StdOut.println(output);
//        //}
//
//
//        //System.out.println(newB2);
//
//    }
//
//}


