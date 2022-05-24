package com.efjerryyang.defendthefrontline.factory;

import com.efjerryyang.defendthefrontline.aircraft.AbstractEnemy;
import com.efjerryyang.defendthefrontline.aircraft.MobEnemy;
import com.efjerryyang.defendthefrontline.application.ImageManager;
import com.efjerryyang.defendthefrontline.application.MainActivity;

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
    public AbstractEnemy createEnemy(double level) {
        locationX = (int) (Math.random() * (MainActivity.screenWidth - ImageManager.MOB_ENEMY_IMAGE.getWidth()));
        locationY = (int) (Math.random() * MainActivity.screenHeight * 0.2);
        int val =(int ) (speedY * Math.sqrt(level));
        return new MobEnemy(locationX, locationY, speedX, Math.min(val, 15), hp, score);
    }
}
