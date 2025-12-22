package com.company.Backend;

import javax.sql.rowset.spi.SyncResolver;
import java.io.IOException;

public class GameController implements Viewable {
    private PersistenceManager PM;
    private SudokuSolver sudokuSolver;
    public GameController(){
        this.PM = new PersistenceManager();
        this.sudokuSolver = new SudokuSolver();

        try{
            PM.initializeFolders();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public Catalog getCatalog() {
        return new Catalog(PM);
    }

    @Override
    public Game getGame(GameConstants.Difficulty level) throws NotFoundException {
        try {
            Game game = PM.loadRandomGame(level);
            if(game == null){
                throw new NotFoundException("No games found in this difficulty" + level);
            }
            PM.saveCurrentGame(game);
            return game;
        }catch (IOException e){
            throw new NotFoundException("ERROR loading game: " + e.getMessage());
        }
    }

    @Override
    public void driveGames(Game sourceGame) throws SolutionInvalidException {
        String verif = verifyGame(sourceGame);
        if(!verif.equals("valid")){
            throw new SolutionInvalidException("Source game is not valid: " + verif);
        }
        try{
            RandomPairs randomPairs = new RandomPairs();
            int[][] sourceGrid = sourceGame.getGrid();

            int[][] easyGrid = copyGrid(sourceGrid);
            for(int[] pair : randomPairs.generateDistinctPairs(10)){
                easyGrid[pair[0]][pair[1]] = 0;
            }
            Game easyGame = new Game(easyGrid, GameConstants.Difficulty.EASY);
            PM.saveGame(GameConstants.Difficulty.EASY, easyGame);

            int[][] mediumGrid = copyGrid(sourceGrid);
            for(int[] pair : randomPairs.generateDistinctPairs(20)){
                mediumGrid[pair[0]][pair[1]] = 0;
            }
            Game mediumGame = new Game(easyGrid, GameConstants.Difficulty.MEDIUM);
            PM.saveGame(GameConstants.Difficulty.MEDIUM, mediumGame);

            int[][] hardGrid = copyGrid(sourceGrid);
            for(int[] pair : randomPairs.generateDistinctPairs(25)){
                hardGrid[pair[0]][pair[1]] = 0;
            }
            Game hardGame = new Game(easyGrid, GameConstants.Difficulty.HARD);
            PM.saveGame(GameConstants.Difficulty.HARD, hardGame);

        } catch (IOException e) {
            throw new SolutionInvalidException("Error generating games: " + e.getMessage());
        }
    }

    @Override
    public String verifyGame(Game game) {
        runSingleThread validator = new runSingleThread();
        boolean isValid = validator.validate(game.getGrid());

        if(!isValid){
            return "invalid";
        }
        for(int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){
                if(game.getGrid()[i][j] == 0){
                    return "incomplete";
                }
            }
        }
        return "valid";
    }

    @Override
    public int[] solveGame(Game game) throws InvalidGame {
        try{
            int[][] solution = sudokuSolver.solveGame(game);
            int[] result = new int[solution.length];
            for(int i = 0; i < solution.length; i++){
                result[i] = solution[i][2];
            }
            return result;
        }catch (SudokuSolver.InvalidGame e){
            throw new InvalidGame(e.getMessage());
        }
    }

    @Override
    public void logUserAction(String userAction) throws IOException {
        String clean = userAction.substring(1, userAction.length() - 1);
        String[] parts = clean.split(",");
        if(parts.length == 4){
            int x = Integer.parseInt(parts[0]);
            int y = Integer.parseInt(parts[1]);
            int newValue = Integer.parseInt(parts[2]);
            int oldValue = Integer.parseInt(parts[3]);
            PM.logMove(x, y, newValue, oldValue);
        }
    }
    private int[][] copyGrid(int[][]original){
        int[][] copy = new int[9][9];
        for (int i = 0; i < 9; i++){
            System.arraycopy(original[i], 0, copy[i], 0, 9);
        }
        return copy;
    }
}
