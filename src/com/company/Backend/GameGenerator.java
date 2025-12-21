package com.company.Backend;

public class GameGenerator {
    public static Game generate(Game solvedGame, int removeCount){
        int[][] board = solvedGame.getBoard();
        RandomPairs rp = new RandomPairs();

        for(int[] p : rp.generateDistinctPair(removeCount))
            board[p[0]][p[1]] = 0;

        return new Game(board);
    }
}
