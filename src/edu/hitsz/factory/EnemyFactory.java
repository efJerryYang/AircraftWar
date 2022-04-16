package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractEnemy;

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
    public abstract AbstractEnemy createEnemy(int level);
}
