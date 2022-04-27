package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;

import java.util.List;

import static edu.hitsz.aircraft.HeroAircraft.SCATTERING_SHOOTNUM;

/**
 * @author JerryYang
 */
public class BulletProp extends AbstractProp {
    public BulletProp(int locationX, int locationY, int speedX, int speedY, int score, String type) {
        super(locationX, locationY, speedX, speedY, score, type);
    }


    @Override
    public void activate(HeroAircraft heroAircraft, List<AbstractEnemy> abstractEnemyList, List<BaseBullet> heroBullets, List<BaseBullet> enemyBullets, int time) {
        System.out.println("BulletSupply active!");
        if (heroAircraft.getShootNum() < SCATTERING_SHOOTNUM) {
            heroAircraft.setBulletSpeedUp(false);
            heroAircraft.setShootNum(heroAircraft.getShootNum() * 2);
        } else if (heroAircraft.getShootNum() == SCATTERING_SHOOTNUM) {
            heroAircraft.setBulletSpeedUp(true);
            heroAircraft.setShootNum(heroAircraft.getShootNum() + 1);
        } else {
            heroAircraft.setBulletSpeedUp(true);
            heroAircraft.setShootNum(Math.min(5, heroAircraft.getShootNum()));
        }


    }

}
