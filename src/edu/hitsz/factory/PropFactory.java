package edu.hitsz.factory;

import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.BloodProp;

public interface PropFactory {
    public abstract AbstractProp createProp(int locationX, int locationY, int speedX, int speedY, int score, String type);
}
