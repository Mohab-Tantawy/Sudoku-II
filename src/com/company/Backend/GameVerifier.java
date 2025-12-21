package com.company.Backend;

public class GameVerifier {
    public static GameState Verify(Game game){
        int[][] grid = game.getBoard();
        Check check = new Check(grid);
        boolean hasZero = false;
         for(int i = 1; i <= 9; i++){
             if(!check.checkBox(i) || !check.checkColumn(i) || !check.checkRow(i))
                 return GameState.INVALID;
         }
         for(int r = 0; r < 9; r++) {
             for (int c = 0; c < 9; c++) {
                 if(grid[r][c] == 0)
                     hasZero = true;
             }
         }
         if(hasZero)
             return GameState.INCOMPLETE;
         else
             return GameState.VALID;
    }
}
