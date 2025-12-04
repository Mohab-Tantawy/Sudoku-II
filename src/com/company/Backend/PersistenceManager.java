package com.company.Backend;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class PersistenceManager {
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
                if(files.size() != 0 && files.size() != 2){
                    for(Path file : files){
                        Files.deleteIfExists(file);
                    }
                }
            }
        }

    }
}
