package edu.hitsz.factory;

import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.BombProp;

/**
 * @author JerryYang
 */
public class BombPropFactory implements PropFactory{
    private int speedX = 0;
    private int speedY = 1;
    private int score = 30;

    @Override
    public AbstractProp createProp(int locationX, int locationY) {
        return new BombProp(locationX, locationY, speedX, speedY, score, "bomb");
    }
}
