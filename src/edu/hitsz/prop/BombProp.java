package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;

import java.util.List;

/**
 * @author JerryYang
 */
public class BombProp extends AbstractProp {

    public BombProp(int locationX, int locationY, int speedX, int speedY, int score, String type) {
        super(locationX, locationY, speedX, speedY, score, type);
    }

    @Override
    public void activate(HeroAircraft heroAircraft, List<AbstractEnemy> abstractEnemyList, List<BaseBullet> heroBullets,List<BaseBullet> enemyBullets, int time) {
        System.out.println("BombSupply active!");
        for (AbstractEnemy enemy : abstractEnemyList) {
            if ("boss".equals(enemy.getType())) {
                continue;
            }
            this.setScore(this.getScore() + enemy.getScore());
            enemy.vanish();
        }
        for (BaseBullet enemyBullet: enemyBullets){
            enemyBullet.vanish();
        }
    }

}
