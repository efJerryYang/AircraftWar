package com.efjerryyang.defendthefrontline.factory;

import com.efjerryyang.defendthefrontline.prop.AbstractProp;
import com.efjerryyang.defendthefrontline.prop.BombProp;


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
