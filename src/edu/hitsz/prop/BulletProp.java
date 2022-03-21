package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.HeroAircraft;

import java.util.List;

public class BulletProp extends AbstractProp {
    public BulletProp(int locationX, int locationY, int speedX, int speedY, String type) {
        super(locationX, locationY, speedX, speedY, type);
    }
    public void activate(HeroAircraft heroAircraft) {
        System.out.println("BombSupply active!");
    }
}
