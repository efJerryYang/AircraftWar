package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

/**
 * @author JerryYang
 */
public class BossFactory implements EnemyFactory {
    private int locationX;
    private int locationY;
    private int speedX;
    private int hp = 2000;
    private int speedY = 0;
    private int score = 500;
    private String type = "boss";

    @Override
    public AbstractEnemy createEnemy(double level) {
        locationX = (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.BOSS_ENEMY_IMAGE.getWidth()));
        locationY = (int) (Math.random() * Main.WINDOW_HEIGHT * 0.1);
        boolean moveRight = Math.random() < 0.5;
        speedX = moveRight ? 10 : -10;
        return new BossEnemy(locationX, locationY, speedX, speedY, (int)(hp * level), score);
    }
}
