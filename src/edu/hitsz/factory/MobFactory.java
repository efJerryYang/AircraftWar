package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;

/**
 * @author JerryYang
 */
public class MobFactory implements EnemyFactory {
    private int locationX;
    private int locationY;
    private int speedX = 0;
    private int speedY = 5;

    private int hp = 30;
    private int score = 10;
    private String type = "mob";

    @Override
    public AbstractEnemy createEnemy(int level) {
        locationX = (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth()));
        locationY = (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2);
        int val = speedY + (level - 1);
        return new MobEnemy(locationX, locationY, speedX, Math.min(val, 15), hp, score, type);
    }
}
