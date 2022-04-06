package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;

import java.util.List;

public class BombProp extends AbstractProp {

    public BombProp(int locationX, int locationY, int speedX, int speedY, int score, String type) {
        super(locationX, locationY, speedX, speedY, score, type);
    }

    @Override
    public void activate(HeroAircraft heroAircraft, List<AbstractEnemy> abstractEnemyList, List<BaseBullet> heroBullets,int time) {
        System.out.println("BombSupply active!");
        for (AbstractEnemy enemy : abstractEnemyList
        ) {
            if ("boss".equals(enemy.getType())) {
                continue;
            }
            this.setScore(this.getScore() + enemy.getScore());
            enemy.vanish();
        }
    }

}
