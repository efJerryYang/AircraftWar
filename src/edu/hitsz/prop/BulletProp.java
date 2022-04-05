package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;

import java.util.List;

public class BulletProp extends AbstractProp {
    public BulletProp(int locationX, int locationY, int speedX, int speedY, int score, String type) {
        super(locationX, locationY, speedX, speedY, score, type);
    }


    @Override
    public void activate(HeroAircraft heroAircraft, List<AbstractEnemy> abstractEnemyList, List<BaseBullet> heroBullets, int time) {
        System.out.println("BulletSupply active!");
        if (heroAircraft.getShootNum() < 4) {
            heroAircraft.setShootNum(heroAircraft.getShootNum() * 2);
        } else if (heroAircraft.getShootNum() == 4) {
            heroAircraft.setBulletSpeedUp(true);
            heroAircraft.setShootNum(heroAircraft.getShootNum() + 1);
        } else {
            heroAircraft.setBulletSpeedUp(true);
            heroAircraft.setShootNum(Math.min(5, heroAircraft.getShootNum()));
        }


    }

}
