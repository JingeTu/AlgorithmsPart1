import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by jg on 24/01/2017.
 */
public class PercolationStats {

    private double mean = 0.0;

    private double std = 0.0;

    private double upperConf = 0.0;

    private double lowerConf = 0.0;

    public PercolationStats(int n, int trials) { // perform test independent
        if (n <= 0 || trials <= 0)
            throw new java.lang.IllegalArgumentException();
        double[] res = new double[trials];
        for (int trialIter = 0; trialIter < trials; trialIter++) {
            Percolation percolation = new Percolation(n);
            for (int openIter = 0; openIter < n * n; openIter++) {
                int randomRow = StdRandom.uniform(1, n + 1);
                int randomCol = StdRandom.uniform(1, n + 1);
                while (percolation.isOpen(randomRow, randomCol)) {
                    randomRow = StdRandom.uniform(1, n + 1);
                    randomCol = StdRandom.uniform(1, n + 1);
                }
                percolation.open(randomRow, randomCol);
                if (percolation.percolates()) {
                    res[trialIter] = (double) openIter / (n * n);
                    break;
                }
            }
        }
        for (int i = 0; i < trials; i++) {
            mean += res[i];
        }
        mean /= res.length;
        for (int i = 0; i < trials; i++) {
            std += (res[i] - mean) * (res[i] - mean);
        }
        std /= (trials - 1);
        std = Math.sqrt(std);
        lowerConf = mean - (1.96 * std) / Math.sqrt(trials + 0.0);
        upperConf = mean + (1.96 * std) / Math.sqrt(trials + 0.0);
    }

    public double mean() { // sample mean of percolation threshold
        return mean;
    }

    public double stddev() { // sample standard deviation of percolation
        // threshold
        return std;
    }

    public double confidenceLo() { // returns lower bound of the 95% confidence
        // interval
        return lowerConf;
    }

    public double confidenceHi() { // returns upper bound of the 95% confidence
        // interval
        return upperConf;
    }

    public static void main(String[] args) { // test client, described below
        PercolationStats ps = new PercolationStats(Integer.parseInt(args[0]),
                Integer.parseInt(args[1]));
        System.out.println("mean                    = " + ps.mean());
        System.out.println("stddev                  = " + ps.stddev());
        System.out.println("95% confidence interval = " + ps.confidenceLo()
                + ", " + ps.confidenceHi());
    }
}
