package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractEnemy;

import java.util.List;

public class BombProp extends AbstractProp {

    public BombProp(int locationX, int locationY, int speedX, int speedY, String type) {
        super(locationX, locationY, speedX, speedY, type);
    }

    @Override
    public void activate(List<AbstractEnemy> abstractPropList) {
        System.out.println("BombSupply active!");
    }
}
