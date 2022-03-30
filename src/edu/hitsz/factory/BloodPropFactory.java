package edu.hitsz.factory;

import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.BloodProp;

public class BloodPropFactory implements PropFactory {
    private int speedX = 0;
    private int speedY = 1;
    private int score = 30;

    @Override
    public AbstractProp createProp(int locationX, int locationY, String type) {
        return new BloodProp(locationX, locationY, speedX, speedY, score, type);
    }

}
