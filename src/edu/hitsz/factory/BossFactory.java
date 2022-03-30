package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

public class BossFactory implements EnemyFactory {
    private int locationX;
    private int locationY;
    private int speedX;
    private int hp = 300;
    private int speedY = 1;
    private int score = 50;
    private String type = "boss";

    @Override
    public AbstractEnemy createEnemy(int difficulty) {
        locationX = (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth()));
        locationY = (int) (Math.random() * Main.WINDOW_HEIGHT * 0.1);
        boolean moveRight = Math.random() < 0.5;
        speedX = moveRight ? 10 : -10;
        return new BossEnemy(locationX, locationY, speedX, speedY, hp * difficulty, score, type);
    }
}
