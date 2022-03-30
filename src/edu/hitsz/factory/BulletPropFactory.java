package edu.hitsz.factory;

import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.BulletProp;

public class BulletPropFactory implements PropFactory {

    @Override
    public AbstractProp createProp(int locationX, int locationY, int speedX, int speedY, int score, String type) {
        return new BulletProp(locationX, locationY, speedX, speedY, score, type);
    }
}
