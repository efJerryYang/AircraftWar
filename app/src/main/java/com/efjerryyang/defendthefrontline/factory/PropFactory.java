package com.efjerryyang.defendthefrontline.factory;


import com.efjerryyang.defendthefrontline.prop.AbstractProp;

/**
 * @author JerryYang
 */
public interface PropFactory {
    /**
     * create prop
     *
     * @param locationX prop generated location x
     * @param locationY prop generated location y
     * @return          return a prop class instance
     */
    AbstractProp createProp(int locationX, int locationY);
}
