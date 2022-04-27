package edu.hitsz.application;

public class Config {
    private static int gameLevel = 1;
    private static boolean enableAudio = true;
    private static int score = 0;

    public static int getGameLevel() {
        return gameLevel;
    }

    public static void setGameLevel(int gameLevel) {
        Config.gameLevel = gameLevel;
    }

    public static boolean isEnableAudio() {
        return enableAudio;
    }

    public static void setEnableAudio(boolean enableAudio) {
        Config.enableAudio = enableAudio;
    }

    public static int getScore() {
        return score;
    }

    public static void setScore(int score) {
        Config.score = score;
    }
}
