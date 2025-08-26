package com.myprojecticaro.poc_automated_test_a_b.domain;

import org.springframework.stereotype.Service;


@Service
public class DungeonService {
    public int minHealthBottomUp(int[][] dungeon) {
        int m = dungeon.length, n = dungeon[0].length;
        int[][] dp = new int[m+1][n+1];
        final int INF = Integer.MAX_VALUE / 4;
        for (int i = 0; i <= m; i++) for (int j = 0; j <= n; j++) dp[i][j] = INF;
        dp[m][n-1] = dp[m-1][n] = 1;
        for (int i = m-1; i >= 0; i--) {
            for (int j = n-1; j >= 0; j--) {
                int need = Math.min(dp[i+1][j], dp[i][j+1]) - dungeon[i][j];
                dp[i][j] = need <= 0 ? 1 : need;
            }
        }
        return dp[0][0];
    }


    public int minHealthTopDown(int[][] dungeon) {
        int m = dungeon.length, n = dungeon[0].length;
        Integer[][] memo = new Integer[m][n];
        return dfs(0,0,dungeon,m,n,memo);
    }


    private int dfs(int i, int j, int[][] d, int m, int n, Integer[][] memo) {
        if (i >= m || j >= n) return Integer.MAX_VALUE/4;
        if (memo[i][j] != null) return memo[i][j];
        if (i == m-1 && j == n-1) return memo[i][j] = Math.max(1, 1 - d[i][j]);
        int need = Math.min(dfs(i+1,j,d,m,n,memo), dfs(i,j+1,d,m,n,memo)) - d[i][j];
        return memo[i][j] = need <= 0 ? 1 : need;
    }
}