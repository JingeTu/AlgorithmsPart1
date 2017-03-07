import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * Created by jg on 24/01/2017.
 */
public class Percolation {

    private boolean[] sites = null;

    private int num = 0;

    private int numSites = 0;

    private WeightedQuickUnionUF ufObj = null;
    private WeightedQuickUnionUF ufObjEx = null;

    private final int[] neighbourRow = {-1, 0, 1, 0};
    private final int[] neighbourCol = {0, 1, 0, -1};

    public Percolation(int n) {
        if (n <= 0)
            throw new java.lang.IllegalArgumentException();
        this.num = n;
        sites = new boolean[num * num];
        ufObj = new WeightedQuickUnionUF(num * num + 2);
        ufObjEx = new WeightedQuickUnionUF(num * num  + 1);
        for (int i = 0; i < num * num; i++) {
            sites[i] = false;
        }
        this.numSites = 0;
    }

    public void open(int row, int col) { // open site (row i, column j) if it is not
        if (row < 1 || row > num || col < 1 || col > num)
            throw new java.lang.IndexOutOfBoundsException();
        if (!isOpen(row, col)) { // if it is not open, open it and union all the open 4-neighbours with it.
            sites[(row - 1) * num + col - 1] = true;
            numSites++;
            for (int i = 0; i < 4; i++) {
                int rowNeighbour = row + neighbourRow[i];
                int colNeighbour = col + neighbourCol[i];
                if (rowNeighbour <= num && rowNeighbour >= 1 && colNeighbour <= num && colNeighbour >= 1
                        && isOpen(rowNeighbour, colNeighbour)) {
                    ufObj.union((row - 1) * num + col - 1, (rowNeighbour - 1) * num + colNeighbour - 1);
                    ufObjEx.union((row - 1) * num + col - 1, (rowNeighbour - 1) * num + colNeighbour - 1);
                }
            }
            if (row == 1) {
                ufObj.union((row - 1) * num + col - 1, num * num);
                ufObjEx.union((row - 1) * num + col - 1, num * num);
            }
            if (row == num)
                ufObj.union((row - 1) * num + col - 1, num * num + 1);
        }
    }

    public boolean isOpen(int row, int col) { // is site (row i, column j) open?
        if (row < 1 || row > num || col < 1 || col > num)
            throw new java.lang.IndexOutOfBoundsException();
        return sites[(row - 1) * num + col - 1];
    }

    public boolean isFull(int row, int col) { // is site (row i, column j) full?
        if (row < 1 || row > num || col < 1 || col > num)
            throw new java.lang.IndexOutOfBoundsException();
        return ufObjEx.connected((row - 1) * num + col - 1, num * num) && sites[(row - 1) * num + col - 1];
    }

    public boolean percolates() { // does the system percolate?
        return ufObj.connected(num * num, num * num + 1);
    }

    public int numberOfOpenSites() {
        return numSites;
    }

    public static void main(String[] args) {
        int num = 100;
        double res = 0.f;
        Percolation perObj = new Percolation(num);
        for (int c = 0; c < num * num; c++) {
            int pt = StdRandom.uniform(0, num * num);
            int i = pt / num + 1, j = pt % num + 1;
            while (perObj.isOpen(i, j)) {
                pt = StdRandom.uniform(0, num * num);
                i = pt / num + 1;
                j = pt % num + 1;
            }
            perObj.open(i, j);
            if (perObj.percolates()) {
                res = ((double) c) / (num * num);
                break;
            }
        }
        System.out.println(res);
    }
}
