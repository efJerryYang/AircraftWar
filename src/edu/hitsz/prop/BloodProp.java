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
    public void activate(HeroAircraft heroAircraft, List<AbstractEnemy> abstractEnemyList, List<BaseBullet> heroBullets, List<BaseBullet> enemyBullets, int time) {
        System.out.println("BloodSupply active!");
        if (heroAircraft.getHp() > (heroAircraft.getMaxHp() / 3 * 2)) {
            //  my Todo: add shield(blood sup)
            heroAircraft.increaseHp(heroAircraft.getMaxHp() / 3);
            heroAircraft.setBloodPropStage(heroAircraft.getBloodPropStage() + 1);
        } else if (heroAircraft.getHp() > 0) {
            heroAircraft.increaseHp(heroAircraft.getMaxHp() / 3);
        } else {
            heroAircraft.increaseHp(0);
            heroAircraft.setBloodPropStage(0);
        }
    }

}
