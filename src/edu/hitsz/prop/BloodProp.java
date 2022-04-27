package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;

import java.util.List;

/**
 * @author JerryYang
 */
public class BloodProp extends AbstractProp {

    private int speedX = 0;
    private int speedY = 1;

    public BloodProp(int locationX, int locationY, int speedX, int speedY, int score, String type) {
        super(locationX, locationY, speedX, speedY, score, type);
    }


    @Override
    public void activate(HeroAircraft heroAircraft, List<AbstractEnemy> abstractEnemyList, List<BaseBullet> heroBullets,List<BaseBullet> enemyBullets, int time) {
        System.out.println("BloodSupply active!");
        if (heroAircraft.getHp() > 0) {
            heroAircraft.increaseHp(100);
        } else {
            //  my Todo: add shield(blood sup)
            heroAircraft.increaseHp(0);
        }
    }
}
