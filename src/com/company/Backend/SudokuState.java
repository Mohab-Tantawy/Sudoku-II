package com.company.Backend;

public class SudokuState {
    private int [][] grid;
    private int emptyCells;

    public SudokuState(int[][] Grid){
        this.grid = copyGrid(Grid);
        this.emptyCells = countEmptyCells();
    }
    private int[][] copyGrid(int[][] org){
        int[][] copy = new int[9][9];
        for(int i = 0; i < 9; i++){
            System.arraycopy(org[i], 0, copy[i], 0, 9);
        }
        return copy;
    }
    private int countEmptyCells(){
        int count = 0;
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                if(grid[i][j] == 0) count++;
            }
        }
        return count;
    }

    public int[][] getGrid() {
        return grid;
    }

    public int getEmptyCells() {
        return emptyCells;
    }
    public String serialize(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                sb.append(grid[i][j]);
            }
        }
        return sb.toString();
    }
    public static SudokuState deserialize(String data){
        int[][] grid = new int[9][9];
        for (int i = 0; i < 81; i++){
            int row = i / 9;
            int col = i % 9;
            grid[row][col] = Character.getNumericValue(data.charAt(i));
        }
        return new SudokuState(grid);
    }
}
