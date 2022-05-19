package edu.hitsz.application;

public class Config {
    public final static double BOMB_PROP_GENERATION_SIMPLE = 0.33;
    public final static double BLOOD_PROP_GENERATION_SIMPLE = 0.33;
    public final static double BULLET_PROP_GENERATION_SIMPLE = 0.33;
    public final static double BOMB_PROP_GENERATION_MEDIUM = 0.3;
    public final static double BLOOD_PROP_GENERATION_MEDIUM = 0.3;
    public final static double BULLET_PROP_GENERATION_MEDIUM = 0.3;
    public final static double BOMB_PROP_GENERATION_DIFFICULT = 0.2;
    public final static double BLOOD_PROP_GENERATION_DIFFICULT = 0.2;
    public final static double BULLET_PROP_GENERATION_DIFFICULT = 0.3;
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

    public static int getScore() {
        return score;
    }

    public static void setScore(int score) {
        Config.score = score;
    }

    public static boolean getEnableAudio() {
        return Config.enableAudio;
    }

    public static void setEnableAudio(boolean enableAudio) {
        Config.enableAudio = enableAudio;
    }
}
