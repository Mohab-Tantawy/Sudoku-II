package com.company.Backend;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

public class PersistenceManager {
    private Stack<Move> undoStack = new Stack<>();
    private Stack<Move> redoStack = new Stack<>();
    private boolean logFileLoaded = false;

    public void initializeFolders() throws IOException{
        String[] dirs = {
                GameConstants.SAVE_DIR,
                GameConstants.EASY_DIR,
                GameConstants.MEDIUM_DIR,
                GameConstants.HARD_DIR,
                GameConstants.CURRENT_DIR
        };
        for(String dir : dirs){
            Path path = Paths.get(dir);
            if(!Files.exists(path)){
                Files.createDirectories(path);
            }
        }
        cleanCurrentDirectory();
    }
    private void cleanCurrentDirectory() throws IOException{
        Path currentDir = Paths.get(GameConstants.CURRENT_DIR);
        if(Files.exists(currentDir)){
            try(DirectoryStream<Path> stream = Files.newDirectoryStream(currentDir)){
                List<Path> files = new ArrayList<>();
                for(Path entry : stream){
                    files.add(entry);
                }
                if(files.size()>2){
                    for(Path file : files){
                        Files.deleteIfExists(file);
                    }
                }
            }
        }

    }
    public void loadLogIntoStack() throws IOException{
        Path logPath = Paths.get(GameConstants.LOG_FILE);
        if(!Files.exists(logPath) || Files.size(logPath) == 0){
            undoStack.clear();
            return;
        }
        undoStack.clear();
        List<String> lines = Files.readAllLines(logPath);
        for(String line : lines){
            undoStack.push(Move.deserialize(line));
        }
        logFileLoaded = true;
    }
    private void rewriteLogFileFromStack() throws IOException{
        Path logPath = Paths.get(GameConstants.LOG_FILE);

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(String.valueOf(logPath)))){
            if(undoStack.isEmpty()){
                //Files.deleteIfExists(logPath);
                writer.write("");
                return;
            }
            List<Move> movesInOrder = new ArrayList<>(undoStack);
            for(Move move : movesInOrder){
                writer.write((move.serialize())+"\n");
            }
        }
    }
    public void saveGame(GameConstants.Difficulty level, Game game) throws IOException{
        String dir = getDirectoryForLevel(level);
        String filename = dir + "/" + game.getGameId() + ".sdk";

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(filename))){
            writer.write(game.serialize());
            writer.newLine();
            writer.write(level.toString());
        }
    }
    public Game loadRandomGame(GameConstants.Difficulty level) throws IOException{
        String dir = getDirectoryForLevel(level);
        Path dirPath = Paths.get(dir);
        if(!Files.exists(dirPath)) return null;

        List<Path> files = new ArrayList<>();
        try(DirectoryStream<Path> stream = Files.newDirectoryStream(dirPath,"*.sdk")){
            for(Path entry : stream){
                files.add(entry);
            }
        }
        if(files.isEmpty()) return null;

        Random rand = new Random();
        Path selectedFiles = files.get(rand.nextInt(files.size()));

        try(BufferedReader reader = new BufferedReader(new FileReader(selectedFiles.toFile()))){
            String gridData = reader.readLine();
            String levelStr = reader.readLine();
            return Game.deserialize(gridData, GameConstants.Difficulty.valueOf(levelStr));
        }
    }
    public void deleteGame(GameConstants.Difficulty level, String gameId) throws IOException{
        String filename = getDirectoryForLevel(level) + "/" + gameId + ".sdk";
        Files.deleteIfExists(Paths.get(filename));
    }
    public void saveCurrentGame(Game game) throws IOException{
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(GameConstants.CURRENT_GAME_FILE))){
            writer.write(game.serialize());
            writer.newLine();
            writer.write((game.getDifficulty().toString()));
        }
        if(!Files.exists(Paths.get(GameConstants.LOG_FILE)))
            Files.createFile(Paths.get(GameConstants.LOG_FILE));
        if(!logFileLoaded)
            loadLogIntoStack();
    }
    public Game loadCurrentGame() throws IOException{
        if(!Files.exists(Paths.get(GameConstants.CURRENT_GAME_FILE))){
            System.out.println("hi");
            return null;
        }
       // System.out.println("hi2");
        Game game;
        try(BufferedReader reader = new BufferedReader(new FileReader(String.valueOf(GameConstants.CURRENT_GAME_FILE)))){
            String gridData = reader.readLine();
            String levelStr = reader.readLine();
            game = Game.deserialize(gridData, GameConstants.Difficulty.valueOf(levelStr));
        }
        if(!logFileLoaded) loadLogIntoStack();
        return game;
    }
    public void deleteCurrentGame() throws IOException{
        undoStack.clear();
        logFileLoaded = false;
        Files.deleteIfExists(Paths.get(GameConstants.CURRENT_GAME_FILE));
        Files.deleteIfExists(Paths.get(GameConstants.LOG_FILE));
    }
    public void logMove(int x, int y, int newValue, int oldValue) throws IOException{
        Move move = new Move(x, y, newValue, oldValue);
        undoStack.push(move);
        redoStack.clear();

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(GameConstants.LOG_FILE, true))){
            writer.write(move.serialize());
            writer.newLine();
        }
    }
    public Move undoLastMove() throws IOException{
        if(undoStack.isEmpty()) return null;
        Move lastMove = undoStack.pop();
        rewriteLogFileFromStack();
        return lastMove;
    }
    public Move redoLastMove() throws IOException{
        if(redoStack.isEmpty())
            return null;
        Move redoMove = redoStack.pop();
        undoStack.push(redoMove);

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(GameConstants.LOG_FILE, true))){
            writer.write((redoMove.serialize()));
            writer.newLine();
        }
        return redoMove;
    }
    public boolean hasUnfinishedGame(){
        return Files.exists(Paths.get(GameConstants.CURRENT_GAME_FILE));
    }
    public boolean hasGamesForAllLevels(){
        return  countGamesInDir(GameConstants.EASY_DIR) > 0 &&
                countGamesInDir(GameConstants.MEDIUM_DIR) > 0 &&
                countGamesInDir(GameConstants.HARD_DIR) > 0;
    }
    public int countGamesInDir(String dirPath){
        Path path = Paths.get(dirPath);
        if(!Files.exists(path)) return 0;

        try(DirectoryStream<Path> stream = Files.newDirectoryStream(path, "*.sdk")){
            int count = 0;
            for(Path entry : stream)
                count++;
            return count;
        }catch (IOException e){
            return 0;
        }
    }
    private String getDirectoryForLevel(GameConstants.Difficulty level){
        switch (level){
            case EASY : return GameConstants.EASY_DIR;
            case MEDIUM: return GameConstants.MEDIUM_DIR;
            case HARD: return GameConstants.HARD_DIR;
            default: return GameConstants.EASY_DIR;
        }
    }
    public int getUndoCount(){
        return undoStack.size();
    }
    public int getRedoCount(){
        return redoStack.size();
    }
    public void clearStack(){
        undoStack.clear();
        redoStack.clear();
        logFileLoaded = false;
    }

}