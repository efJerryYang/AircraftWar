package edu.hitsz.prop;

import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.factory.PropFactory;

public class BloodProp extends AbstractProp{

    public BloodProp(int locationX, int locationY, int speedX, int speedY, int score, String type) {
        super(locationX, locationY, speedX, speedY, score, type);
    }


    @Override
    public void activate(HeroAircraft heroAircraft) {
        System.out.println("BloodSupply active!");
        heroAircraft.increaseHp(100);
    }
}
