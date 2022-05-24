package com.efjerryyang.defendthefrontline.prop;

import static com.efjerryyang.defendthefrontline.aircraft.HeroAircraft.SCATTERING_SHOOTNUM;

import com.efjerryyang.defendthefrontline.aircraft.AbstractEnemy;
import com.efjerryyang.defendthefrontline.aircraft.HeroAircraft;
import com.efjerryyang.defendthefrontline.bullet.BaseBullet;

import java.util.List;


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
