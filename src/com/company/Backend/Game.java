package com.company.Backend;

import com.company.Backend.GameConstants.Difficulty;

public class Game {
    private int[][] grid;
    private GameConstants.Difficulty difficulty;
    private String gameId;
    private boolean isComplete;

    public Game(int[][] grid, Difficulty difficulty) {
        this.grid = copyGrid(grid);
        this.difficulty = difficulty;
        this.gameId = java.util.UUID.randomUUID().toString();
        this.isComplete = false;
    }

    // Add this static method
    public static int[][] copyGrid(int[][] grid) {
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(grid[i], 0, copy[i], 0, 9);
        }
        return copy;
    }
    public int[][] getGrid() {return grid;}
    public void setGrid(int[][] grid) {this.grid = grid;}
    public GameConstants.Difficulty getDifficulty() {return difficulty;}
    public void setDifficulty(GameConstants.Difficulty difficulty) {this.difficulty = difficulty;}
    public String getGameId() {return gameId;}
    public void setGameId(String gameId) {this.gameId = gameId;}
    public boolean isComplete() {return isComplete;}
    public void setComplete(boolean complete) {isComplete = complete;}

    // Converts Grid to STRING format
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sb.append(grid[i][j]);
            }
        }
        return sb.toString();
    }

    public static Game deserialize(String data, Difficulty difficulty) {
        int[][] grid = new int[9][9];
        for (int i = 0; i < 81; i++) {
            int row = i / 9;
            int col = i % 9;
            grid[row][col] = Character.getNumericValue(data.charAt(i));
        }
        return new Game(grid, difficulty);
    }

    public static int getEmptyCellCount(int[][] grid) {
        int count = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grid[i][j] == 0) count++;
            }
        }
        return count;
    }

    public boolean isCellEmpty(int x, int y) {
        return grid[x][y] == 0;
    }

    public void setCell(int x, int y, int value) {
        if (x >= 0 && x < 9 && y >= 0 && y < 9 && value >= 0 && value <= 9)
            grid[x][y] = value;
    }

    public int getCell(int x, int y) {
        if (x >= 0 && x < 9 && y >= 0 && y < 9)
            return grid[x][y];
        return -1;
    }
}
