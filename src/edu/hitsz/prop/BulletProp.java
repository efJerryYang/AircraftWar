package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;

public class BulletProp extends AbstractProp {
    public BulletProp(int locationX, int locationY, int speedX, int speedY, int score, String type) {
        super(locationX, locationY, speedX, speedY, score, type);
    }

    public void activate(HeroAircraft heroAircraft) {
        System.out.println("BombSupply active!");
        heroAircraft.setShootNum(heroAircraft.getShootNum() + 1);
    }
}
