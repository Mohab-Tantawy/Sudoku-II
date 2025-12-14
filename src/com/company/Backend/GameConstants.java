package com.company.Backend;

public class GameConstants {
    public static final String SAVE_DIR = "saved_games";
    public static final String EASY_DIR = SAVE_DIR + "/easy";
    public static final String MEDIUM_DIR = SAVE_DIR + "/medium";
    public static final String HARD_DIR = SAVE_DIR + "/hard";
    public static final String CURRENT_DIR = SAVE_DIR + "/current";
    public static final String CURRENT_GAME_FILE = CURRENT_DIR + "/game.sdk";
    public static final String LOG_FILE = CURRENT_DIR + "/log.txt";

    public enum Difficulty{
        EASY, MEDIUM, HARD
    }
    public enum GameState{
        VALID, INVALID, INCOMPLETE
    }
}
