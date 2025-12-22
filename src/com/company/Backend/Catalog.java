package com.company.Backend;

import com.company.Backend.*;

public class Catalog {
    private PersistenceManager persistenceManager;

    public Catalog(PersistenceManager persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

    public boolean hasUnfinished() {
        try {
            return persistenceManager.hasUnfinishedGame();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean hasEasyMediumHard() {
        try {
            return persistenceManager.hasGamesForAllLevels();
        } catch (Exception e) {
            return false;
        }
    }

    public String getStatus() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== Game Catalog Status ===\n");
        sb.append("Unfinished game: ").append(hasUnfinished() ? "YES" : "NO").append("\n");
        sb.append("Easy games available: ").append(persistenceManager.countGamesInDir(GameConstants.EASY_DIR)).append("\n");
        sb.append("Medium games available: ").append(persistenceManager.countGamesInDir(GameConstants.MEDIUM_DIR)).append("\n");
        sb.append("Hard games available: ").append(persistenceManager.countGamesInDir(GameConstants.HARD_DIR)).append("\n");
        sb.append("All levels available: ").append(hasEasyMediumHard() ? "YES" : "NO");
        return sb.toString();
    }
}