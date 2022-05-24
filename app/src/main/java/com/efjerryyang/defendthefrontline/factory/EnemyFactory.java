package com.efjerryyang.defendthefrontline.factory;


import com.efjerryyang.defendthefrontline.aircraft.AbstractEnemy;

/**
 * @author JerryYang
 */


public interface EnemyFactory {
    /**
     * create enemy
     *
     * @param level game level;
     * @return return an enemy class instance
     */
    public abstract AbstractEnemy createEnemy(double level);
}
