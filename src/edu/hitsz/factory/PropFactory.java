package edu.hitsz.factory;

import edu.hitsz.prop.AbstractProp;

/**
 * @author JerryYang
 */
public interface PropFactory {
    /**
     * create prop
     *
     * @param locationX prop generated location x
     * @param locationY prop generated location y
     * @param type      prop type
     * @return          return a prop class instance
     */
    AbstractProp createProp(int locationX, int locationY, String type);
}
