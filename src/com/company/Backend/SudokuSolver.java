/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.company.Backend;

/**
 *
 * @author Mohamed Helal
 */
import java.util.ArrayList;
import java.util.List;

public class SudokuSolver {

    public static class InvalidGame extends Exception {
        public InvalidGame(String message) {
            super(message);
        }
    }

    private int attempts = 0;

    public int[][] solveGame(int[][] grid) throws InvalidGame {
        List<int[]> emptyCells = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (grid[i][j] == 0) {
                    emptyCells.add(new int[]{i, j});
                }
            }
        }

        if (emptyCells.size() != 5) {
            throw new InvalidGame("Solver only works when exactly 5 cells are empty.");
        }

        Check checker = new Check(grid);
        if (!isPartialValid(checker)) {
            throw new InvalidGame("The current board has conflicts and cannot be solved.");
        }

        PermutationIterator iterator = new PermutationIterator();
        attempts = 0;

        while (iterator.hasNext()) {
            int[] values = iterator.next();
            attempts++;

            for (int i = 0; i < 5; i++) {
                int x = emptyCells.get(i)[0];
                int y = emptyCells.get(i)[1];
                grid[x][y] = values[i];
            }

            if (isPartialValid(checker)) {
                int[][] result = buildResult(emptyCells, values);
                revert(grid, emptyCells);
                return result;
            }

            revert(grid, emptyCells);
        }

        throw new InvalidGame("No valid solution found after checking all possibilities.");
    }

    private boolean isPartialValid(Check checker) {
        for (int i = 1; i <= 9; i++) {
            if (checker.checkRow(i)==0) return false;
            if (checker.checkColumn(i)==0) return false;
            if (checker.checkBox(i)==0) return false;
        }
        return true;
    }

    private void revert(int[][] grid, List<int[]> emptyCells) {
        for (int[] cell : emptyCells) {
            grid[cell[0]][cell[1]] = 0;
        }
    }

    private int[][] buildResult(List<int[]> emptyCells, int[] values) {
        int[][] result = new int[5][3];
        for (int i = 0; i < 5; i++) {
            result[i][0] = emptyCells.get(i)[0];
            result[i][1] = emptyCells.get(i)[1];
            result[i][2] = values[i];
        }
        return result;
    }

    public int[][] solveGame(Game game) throws InvalidGame {
        return solveGame(game.getGrid());
    }
}
