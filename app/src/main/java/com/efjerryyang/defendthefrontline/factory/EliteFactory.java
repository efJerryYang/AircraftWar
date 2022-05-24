package com.efjerryyang.defendthefrontline.factory;

import com.efjerryyang.defendthefrontline.aircraft.AbstractEnemy;
import com.efjerryyang.defendthefrontline.aircraft.EliteEnemy;
import com.efjerryyang.defendthefrontline.application.ImageManager;
import com.efjerryyang.defendthefrontline.application.MainActivity;

/**
 * @author JerryYang
 */
public class EliteFactory implements EnemyFactory {
    private int locationX;
    private int locationY;
    private int speedX;
    private int speedY = 3;
    private int hp = 60;
    private int score = 50;
    private String type = "elite";

    @Override
    public AbstractEnemy createEnemy(double level) {
        locationX = (int) (Math.random() * (MainActivity.screenWidth - ImageManager.BOSS_ENEMY_IMAGE.getWidth()));
        locationY = (int) (Math.random() * MainActivity.screenHeight * 0.2);
        boolean moveRight = Math.random() < 0.5;
        speedX = moveRight ? 10 : -10;
        int val = (int) (speedY );//* Math.sqrt(level));
        return new EliteEnemy(locationX, locationY, speedX, Math.min(val, 5), (int) (hp * level), score);
    }
}
