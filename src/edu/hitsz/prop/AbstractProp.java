package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Main;
import edu.hitsz.basic.FlyingObject;

import java.util.List;

public abstract class AbstractProp extends AbstractEnemy {


    public AbstractProp(int locationX, int locationY, int speedX, int speedY, int score, String type) {
        super(locationX, locationY, speedX, speedY, 0, score,type);
    }

    public void activate(HeroAircraft heroAircraft) {

    }

    public void activate(List<AbstractEnemy> abstractEnemyList) {

    }

}
