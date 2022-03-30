package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.factory.PropFactory;

import java.util.List;

public class BombProp extends AbstractProp{

    public BombProp(int locationX, int locationY, int speedX, int speedY, int score, String type) {
        super(locationX, locationY, speedX, speedY, score, type);
    }

    @Override
    public void activate(List<AbstractEnemy> enemyList) {
        System.out.println("BombSupply active!");
        for (AbstractEnemy enemy : enemyList
        ) {
            if (enemy.getType().equals("boss")) {
                continue;
            }
            this.setScore(this.getScore() + enemy.getScore());
            enemy.vanish();
        }
    }

}
