package PercolationFun;

import java.lang.*;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    int T;
    double[] thresholds;

    // perform T independent experiments on an N-by-N grid
    public PercolationStats(int N, int T, PercolationFactory pf){
        if (N <= 0 || T <= 0){
            throw new IllegalArgumentException();
        }
        this.T = T;
        thresholds = new double[T];
        for (int i=0; i<T; i++){
            Percolation p = pf.make(N);
            simulateUntilPercolation(p, N);
            thresholds[i] = (double) p.numberOfOpenSites() / (double) (N * N);
        }
    }

    private void simulateUntilPercolation(Percolation p, int N){
        while (!(p.percolates())) {
            int randomRow = StdRandom.uniform(N);
            int randomCol = StdRandom.uniform(N);
            p.open(randomRow, randomCol);
        }
    }

    // sample mean of percolation threshold
    public double mean(){
        return StdStats.mean(thresholds);
    }

    // sample standard deviation of percolation threshold
    public double stddev(){
        return StdStats.stddev(thresholds);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow(){
        double mean = mean();
        double stddev = stddev();
        return mean - 1.96 * stddev / Math.sqrt(T);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh(){
        double mean = mean();
        double stddev = stddev();
        return mean + 1.96 * stddev / Math.sqrt(T);
    }
}
