package com.myprojecticaro.poc_automated_test_a_b.domain;

import org.springframework.stereotype.Service;

/**
 * Service class that provides algorithms to solve the "Dungeon Game" problem.
 * <p>
 * Given a 2D dungeon grid where each cell contains a positive or negative integer,
 * the goal is to determine the minimum initial health required for a knight to
 * reach the bottom-right corner starting from the top-left corner.
 * </p>
 *
 * <p>
 * The knight:
 * <ul>
 *   <li>Starts with an initial amount of health (to be calculated).</li>
 *   <li>Can only move right or down at each step.</li>
 *   <li>Must always maintain health greater than 0.</li>
 * </ul>
 * </p>
 *
 * <p>
 * This service provides two implementations:
 * <ul>
 *   <li><b>Bottom-up DP:</b> Iterative solution using a dynamic programming table.</li>
 *   <li><b>Top-down DP:</b> Recursive solution with memoization.</li>
 * </ul>
 * </p>
 */
@Service
public class DungeonService {

    /**
     * Calculates the minimum initial health required using a bottom-up
     * dynamic programming approach.
     * <p>
     * Builds a DP table starting from the destination (bottom-right) back
     * to the starting point (top-left).
     * </p>
     *
     * @param dungeon 2D integer grid representing the dungeon
     * @return the minimum initial health required to survive the dungeon
     */
    public int minHealthBottomUp(int[][] dungeon) {
        int rows = dungeon.length;
        int cols = dungeon[0].length;

        /**
         * Dynamic Programming (DP) matrix that stores the minimum health required
         * at each cell in order for the hero to survive until reaching the destination.
         *
         * The matrix is created with one extra row and column (rows+1, cols+1) to simplify
         * boundary conditions, avoiding index out-of-bounds checks when accessing neighbors.
         */
        int[][] minHealth = new int[rows + 1][cols + 1];

        /**
         * Constant used to represent an "infinite" value in the DP table.
         * Initialized as Integer.MAX_VALUE / 4 to prevent integer overflow
         * during calculations. It ensures invalid paths are ignored in the
         * minimum health computation.
         */
        final int INF = Integer.MAX_VALUE / 4;

        // Initialize DP array with infinity values
        for (int r = 0; r <= rows; r++) {
            for (int c = 0; c <= cols; c++) {
                minHealth[r][c] = INF;
            }
        }

        // Base condition: one step outside the destination cell
        minHealth[rows][cols - 1] = 1;
        minHealth[rows - 1][cols] = 1;

        // Fill the DP table backwards
        for (int r = rows - 1; r >= 0; r--) {
            for (int c = cols - 1; c >= 0; c--) {
                int requiredHealth = Math.min(minHealth[r + 1][c], minHealth[r][c + 1]) - dungeon[r][c];
                minHealth[r][c] = requiredHealth <= 0 ? 1 : requiredHealth;
            }
        }

        return minHealth[0][0];
    }

    /**
     * Calculates the minimum initial health required using a top-down
     * recursive approach with memoization.
     * <p>
     * Starts at the beginning (0,0) and explores paths recursively,
     * caching results to avoid recomputation.
     * </p>
     *
     * @param dungeon 2D integer grid representing the dungeon
     * @return the minimum initial health required to survive the dungeon
     */
    public int minHealthTopDown(int[][] dungeon) {
        int m = dungeon.length, n = dungeon[0].length;
        Integer[][] memo = new Integer[m][n];
        return dfs(0,0,dungeon,m,n,memo);
    }

    /**
     * Depth-first search helper function for the top-down recursive solution.
     *
     * @param row   current row index
     * @param col   current column index
     * @param dungeon    dungeon grid
     * @param totalRows    total number of rows
     * @param totalCols    total number of columns
     * @param memo memoization cache to store intermediate results
     * @return the minimum health required at position (i,j)
     */
    private int dfs(int row, int col, int[][] dungeon, int totalRows, int totalCols, Integer[][] memo) {
        // If out of bounds, return a very large value to ignore this path
        if (row >= totalRows || col >= totalCols)
            return Integer.MAX_VALUE / 4;

        // Return previously computed result if available
        if (memo[row][col] != null)
            return memo[row][col];

        // Base case: bottom-right cell (destination)
        if (row == totalRows - 1 && col == totalCols - 1)
            return memo[row][col] = Math.max(1, 1 - dungeon[row][col]);

        // Minimum health needed to move right or down
        int minHealthNeeded = Math.min(
                dfs(row + 1, col, dungeon, totalRows, totalCols, memo),
                dfs(row, col + 1, dungeon, totalRows, totalCols, memo)
        ) - dungeon[row][col];

        // Ensure at least 1 health is required
        return memo[row][col] = minHealthNeeded <= 0 ? 1 : minHealthNeeded;
    }
}