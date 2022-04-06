package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractEnemy;

/**
 * @author JerryYang
 */


public interface EnemyFactory {
    /**
     * create enemy
     *
     * @param difficulty game difficulty;
     * @return return an enemy class instance
     */
    public abstract AbstractEnemy createEnemy(int difficulty);
}
