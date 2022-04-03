package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;

import java.util.List;

public class BulletProp extends AbstractProp {
    public BulletProp(int locationX, int locationY, int speedX, int speedY, int score, String type) {
        super(locationX, locationY, speedX, speedY, score, type);
    }


    public void activate(HeroAircraft heroAircraft, List<AbstractEnemy> abstractEnemyList, List<BaseBullet> heroBullets, int time) {
        System.out.println("BombSupply active!");
        if (heroAircraft.getShootNum() < 4) {
            heroAircraft.setShootNum(heroAircraft.getShootNum() * 2);
        } else {
            for (BaseBullet bullet : heroBullets) {
                bullet.setSpeedX(bullet.getSpeedX());
//                my Todo: add shield(blood sup)
//                my Todo: add moving bullet(hero and boss)
            }
        }


    }

}
