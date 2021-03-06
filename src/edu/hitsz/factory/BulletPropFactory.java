package edu.hitsz.factory;

import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.BulletProp;

/**
 * @author JerryYang
 */
public class BulletPropFactory implements PropFactory {
    private int speedX = 0;
    private int speedY = 1;
    private int score = 30;
    @Override
    public AbstractProp createProp(int locationX, int locationY) {
        return new BulletProp(locationX, locationY, speedX, speedY, score,"bullet");
    }
}
