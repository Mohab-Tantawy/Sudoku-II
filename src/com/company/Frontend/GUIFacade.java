package com.company.Frontend;
import com.company.Backend.*;
import java.io.IOException;

public class GUIFacade implements Controllable{
    private Viewable viewableController;
    private GameController gameController;

    public GUIFacade(){
        this.gameController=new GameController();
        this.viewableController=gameController;

    }

    public GUIFacade(Viewable controller){
        this.viewableController=controller;
        this.gameController=(GameController) controller;

    }
    @Override
    public boolean[] getCatalog(){
        Catalog catalog= viewableController.getCatalog();
        boolean[] result=new boolean[2];
        result[0]=catalog.hasUnfinished();
        result[1]=catalog.hasEasyMediumHard();
        return result;

    }
    @Override
    public int[][] getGame(char level) throws NotFoundException{
        GameConstants.Difficulty difficulty;
        switch (Character.toUpperCase(level)){
            case 'E':
                difficulty= GameConstants.Difficulty.EASY;
                break;

            case 'M':
                difficulty= GameConstants.Difficulty.MEDIUM;
                break;

            case 'H':
                difficulty= GameConstants.Difficulty.HARD;
                break;

            default:
                throw new NotFoundException("Invalid Difficulty level: "+level);
        }
        Game game = viewableController.getGame(difficulty);
        return game.getGrid();
    }
    @Override
    public void driveGames(String sourcePath) throws SolutionInvalidException{
        int[][] grid=Grid.getGrid(sourcePath);
        Game sourceGame= new Game(grid, GameConstants.Difficulty.EASY);
        viewableController.driveGames(sourceGame);
    }
    @Override
    public boolean[][] verifyGame(int[][] game){
        Game gameobj= new Game(game, GameConstants.Difficulty.EASY);
        String result=viewableController.verifyGame(gameobj);
        boolean[][] cellValid=new boolean[9][9];

        if(result.equals("valid")){
            for(int i=0;i<9;i++){
                for(int j=0;j<9;j++){
                    cellValid[i][j]=true;
                }
            }
        }
        else if(result.equals("incomplete")){
            Check check =new Check(game);
            for(int i=0;i<9;i++){
                for(int j=0;j<9;j++){
                    if(game[i][j]==0){
                        cellValid[i][j]=false;
                    }
                    else{
                        cellValid[i][j]=true;
                    }
                }
            }

        }
        else{
            for(int i=0;i<9;i++){
                for(int j=0;j<9;j++){
                    cellValid[i][j]=false;
                }
            }

        }
        return cellValid;
    }

    @Override
    public int[][] solveGame(int[][] game) throws InvalidGame {

        Game gameObj = new Game(game, GameConstants.Difficulty.EASY);
        try {
            int[] values= viewableController.solveGame(gameObj);

            int emptyCount = 0;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (game[i][j] == 0) {
                        emptyCount++;
                    }
                }
            }
            int[][] result = new int[emptyCount][3];
            int index = 0;
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (game[i][j] == 0 && index < values.length){
                        result[index][0]=i;
                        result[index][1]=j;
                        result[index][1]=values[index];
                        index++;
                    }
                }
            }
            return result;
        }catch(com.company.Backend.InvalidGame e){
            throw new InvalidGame(e.getMessage());
        }
    }

    @Override
    public void logUserAction(UserAction userAction) throws IOException{
        String actionString = String.format("(%d,%d,%d,%d)"),userAction.getX(),userAction.getY(),userAction.getNewValue(),userAction.getOldValue());
    viewableController.logUserAction(actionString);
    }
}
