package hw2;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;
import java.lang.*;

public class Percolation {
    int[][] grid;
    int N;
    int num_open;
    int top_node;
    int bottom_node;
    WeightedQuickUnionUF grid_ds;

    // create N-by-N grid, with all sites initially blocked
    // O(N^2)
    public Percolation(int N){
        if (N <= 0) {
            throw new IllegalArgumentException();
        }
        grid = new int[N][N];
        for (int i=0; i<N; i++){
            for (int j=0; j<N; j++){
                grid[i][j] = 1;
            }
        }
        this.N = N;
        num_open = 0;
        top_node = N;
        bottom_node = N + 1;
        grid_ds = new WeightedQuickUnionUF(N * N + 2);  // Add two for the top and bottom nodes

        // Union top node with the first layer
        for (int i=0; i<N; i++){
            grid_ds.union(top_node, xyTo1D(0, i));
        }

        // Union bottom node with the bottom layer
        for (int i=0; i<N; i++){
            grid_ds.union(bottom_node, xyTo1D(N - 1, i));
        }
    }

    // excavate the site (row, col) if it is not excavated already
    // O(1)
    public void open(int row, int col){
        if (!(0 <= row && row < N) || !(0 <= col && col < N)){
            throw new IndexOutOfBoundsException();
        }
        if (grid[row][col] == 1){
            num_open += 1;
        }
        grid[row][col] = 0;

        // Make curr cell its own set in case it has no neighbors
        grid_ds.union(xyTo1D(row, col), xyTo1D(row, col));

        // Update grid disjoint sets
        if (row - 1 >= 0){
            if (isOpen(row - 1, col)){
                grid_ds.union(xyTo1D(row - 1, col), xyTo1D(row, col));
            }
        }
        if (row + 1 < N){
            if (isOpen(row + 1, col)){
                grid_ds.union(xyTo1D(row + 1, col), xyTo1D(row, col));
            }
        }
        if (col - 1 >= 0){
            if (isOpen(row, col - 1)){
                grid_ds.union(xyTo1D(row, col - 1), xyTo1D(row, col));
            }
        }
        if (col + 1 < N){
            if (isOpen(row, col + 1)){
                grid_ds.union(xyTo1D(row, col + 1), xyTo1D(row, col));
            }
        }
    }


    // is the site (row, col) excavated? Open means that a site
    // has been excavated out. It could be full or empty.
    // O(1)
    public boolean isOpen(int row, int col){
        if (!(0 <= row && row < N) || !(0 <= col && col < N)){
            throw new IndexOutOfBoundsException();
        }
        return grid[row][col] == 0;
    }

    // is the site (row, col) full? Full means that a site
    // is excavated and filled with water.
    // O(1)
    public boolean isFull(int row, int col){
        if (!(0 <= row && row < N) || !(0 <= col && col < N)){
            throw new IndexOutOfBoundsException();
        }
        if (grid[row][col] == 1){
            return false;
        }
        return grid_ds.connected(top_node, xyTo1D(row, col));
    }

    // number of excavated sites
    // O(1)
    public int numberOfOpenSites(){
        return num_open;
    }

    // does the system percolate?
    // O(1)
    public boolean percolates(){
        return grid_ds.connected(top_node, bottom_node);
    }

    // Converts x,y coordinates to a single integer
    private int xyTo1D(int r, int c){
        return (r * N) + c;
    }

    // use for unit testing (not required, but keep this here for the autograder)
    public static void main(String[] args){

    }
}
