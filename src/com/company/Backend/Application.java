package com.company.Backend;

//import com.company.Grid;

import java.util.Scanner;

public class Application {
    private PersistenceManager persistenceManager;
    private Catalog catalog;
    private Game currentGame;
    private runSingleThread validator;

    public Application() {
        this.persistenceManager = new PersistenceManager();
        this.catalog = new Catalog(persistenceManager);
        this.validator = new runSingleThread();
    }

    public void initialize() {
        try {
            persistenceManager.initializeFolders();
            System.out.println(catalog.getStatus());

            if (catalog.hasUnfinished()) {
                loadUnfinishedGame();
            } else if (catalog.hasEasyMediumHard()) {
                loadGameByDifficulty(promptForDifficulty());
            } else {
                System.out.println("\n❌ No saved games found!");
                System.out.println("Please provide a solved Sudoku file path:");
                Scanner scanner = new Scanner(System.in);
                String filePath = scanner.nextLine();
                loadFromFile(filePath);
            }

        } catch (Exception e) {
            System.err.println("Initialization failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private GameConstants.Difficulty promptForDifficulty() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nSelect difficulty:");
        System.out.println("1. Easy (10 cells removed)");
        System.out.println("2. Medium (25 cells removed)");
        System.out.println("3. Hard (20 cells removed)");
        System.out.print("Choice (1-3): ");

        int choice = scanner.nextInt();
        switch (choice) {
            case 1: return GameConstants.Difficulty.EASY;
            case 2: return GameConstants.Difficulty.MEDIUM;
            case 3: return GameConstants.Difficulty.HARD;
            default: return GameConstants.Difficulty.MEDIUM;
        }
    }

    private void loadUnfinishedGame() throws Exception {
        currentGame = persistenceManager.loadCurrentGame();
        if (currentGame != null) {
            System.out.println("\n✅ Loaded unfinished game: " + currentGame.getGameId());
            System.out.println("Difficulty: " + currentGame.getDifficulty());
            System.out.println("Empty cells: " + currentGame.getEmptyCellCount());
        }
    }

    private void loadGameByDifficulty(GameConstants.Difficulty difficulty) throws Exception {
        currentGame = persistenceManager.loadRandomGame(difficulty);
        if (currentGame != null) {
            System.out.println("\n✅ Loaded new game: " + currentGame.getGameId());
            System.out.println("Difficulty: " + difficulty);
            System.out.println("Empty cells: " + currentGame.getEmptyCellCount());
            persistenceManager.saveCurrentGame(currentGame);
        } else {
            System.out.println("❌ No games available for difficulty: " + difficulty);
        }
    }

    private void loadFromFile(String filePath) {
        int[][] grid = Grid.getGrid(filePath);
        if (grid != null) {
            // Validate the loaded grid
            validator.validate(grid);

            // Create game with default difficulty
            currentGame = new Game(grid, GameConstants.Difficulty.MEDIUM);
            try {
                persistenceManager.saveCurrentGame(currentGame);
                System.out.println("✅ Game loaded from file and saved!");
            } catch (Exception e) {
                System.err.println("Failed to save game: " + e.getMessage());
            }
        }
    }

    public void makeMove(int x, int y, int newValue) {
        if (currentGame == null) {
            System.out.println("❌ No game loaded!");
            return;
        }

        if (x < 0 || x >= 9 || y < 0 || y >= 9) {
            System.out.println("❌ Invalid coordinates!");
            return;
        }

        if (newValue < 1 || newValue > 9) {
            System.out.println("❌ Value must be 1-9!");
            return;
        }

        int oldValue = currentGame.getCell(x, y);
        currentGame.setCell(x, y, newValue);

        try {
            persistenceManager.logMove(x, y, newValue, oldValue);
            persistenceManager.saveCurrentGame(currentGame);

            System.out.println("✅ Move: (" + x + "," + y + ") " +
                    oldValue + " → " + newValue);
            System.out.println("Empty cells remaining: " + currentGame.getEmptyCellCount());

        } catch (Exception e) {
            System.err.println("❌ Failed to save move: " + e.getMessage());
            currentGame.setCell(x, y, oldValue);
        }
    }

    public void undoMove() {
        if (currentGame == null) {
            System.out.println("❌ No game loaded!");
            return;
        }

        try {
            Move undone = persistenceManager.undoLastMove();
            if (undone != null) {
                currentGame.setCell(undone.getX(), undone.getY(), undone.getOldValue());
                persistenceManager.saveCurrentGame(currentGame);

                System.out.println("↩️ Undo: (" + undone.getX() + "," + undone.getY() + ") " +
                        undone.getNewValue() + " → " + undone.getOldValue());
                System.out.println("Empty cells: " + currentGame.getEmptyCellCount());
            } else {
                System.out.println("⚠️ No moves to undo!");
            }
        } catch (Exception e) {
            System.err.println("❌ Undo failed: " + e.getMessage());
        }
    }

    public void redoMove() {
        if (currentGame == null) {
            System.out.println("❌ No game loaded!");
            return;
        }

        try {
            Move redone = persistenceManager.redoLastMove();
            if (redone != null) {
                currentGame.setCell(redone.getX(), redone.getY(), redone.getNewValue());
                persistenceManager.saveCurrentGame(currentGame);

                System.out.println("↪️ Redo: (" + redone.getX() + "," + redone.getY() + ") " +
                        redone.getOldValue() + " → " + redone.getNewValue());
                System.out.println("Empty cells: " + currentGame.getEmptyCellCount());
            } else {
                System.out.println("⚠️ No moves to redo!");
            }
        } catch (Exception e) {
            System.err.println("❌ Redo failed: " + e.getMessage());
        }
    }

    public void validateCurrentGame() {
        if (currentGame == null) {
            System.out.println("❌ No game loaded!");
            return;
        }

        validator.validate(currentGame.getGrid());
    }

    public void solveGame() {
        if (currentGame == null) {
            System.out.println("❌ No game loaded!");
            return;
        }

        int emptyCells = currentGame.getEmptyCellCount();
        if (emptyCells != 5) {
            System.out.println("❌ Solver only works with exactly 5 empty cells!");
            System.out.println("Current empty cells: " + emptyCells);
            return;
        }

        System.out.println("🔍 Solving game with 5 empty cells...");
        // TODO: Integrate solver from Person 1
        System.out.println("Solver not implemented yet (Person 1's task)");

        // Once solved:
        currentGame.setComplete(true);
        try {
            persistenceManager.deleteCurrentGame();
            persistenceManager.deleteGame(currentGame.getDifficulty(), currentGame.getGameId());
            System.out.println("🎉 Game solved and cleaned up!");
        } catch (Exception e) {
            System.err.println("Failed to clean up solved game: " + e.getMessage());
        }
    }

    public void showBoard() {
        if (currentGame == null) {
            System.out.println("❌ No game loaded!");
            return;
        }

        int[][] board = currentGame.getGrid();
        System.out.println("\n=== Current Sudoku Board ===");
        System.out.println("Game ID: " + currentGame.getGameId());
        System.out.println("Difficulty: " + currentGame.getDifficulty());
        System.out.println("Empty cells: " + currentGame.getEmptyCellCount());
        System.out.println("Undo moves available: " + persistenceManager.getUndoCount());
        System.out.println("Redo moves available: " + persistenceManager.getRedoCount());
        System.out.println();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int val = board[i][j];
                System.out.print((val == 0 ? "." : val) + " ");
                if ((j + 1) % 3 == 0 && j != 8) System.out.print("| ");
            }
            System.out.println();
            if ((i + 1) % 3 == 0 && i != 8) {
                System.out.println("------+-------+------");
            }
        }
    }

    public void checkSolveButtonStatus() {
        if (currentGame == null) {
            System.out.println("❌ No game loaded!");
            return;
        }

        int emptyCells = currentGame.getEmptyCellCount();
        if (emptyCells == 5) {
            System.out.println("✅ SOLVE button should be ENABLED (5 empty cells)");
        } else {
            System.out.println("❌ SOLVE button should be DISABLED (" + emptyCells + " empty cells)");
        }
    }

    public void saveGameToDifficulty() {
        if (currentGame == null) {
            System.out.println("❌ No game loaded!");
            return;
        }

        try {
            persistenceManager.saveGame(currentGame.getDifficulty(), currentGame);
            System.out.println("✅ Game saved to " + currentGame.getDifficulty() + " folder");
        } catch (Exception e) {
            System.err.println("❌ Failed to save game: " + e.getMessage());
        }
    }

    // Getters
    public Game getCurrentGame() { return currentGame; }
    public Catalog getCatalog() { return catalog; }
    public PersistenceManager getPersistenceManager() { return persistenceManager; }
}