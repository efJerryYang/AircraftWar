package edu.hitsz.strategy;

import edu.hitsz.aircraft.*;
import edu.hitsz.bullet.BaseBullet;

import java.util.List;

/**
 * @author JerryYang
 */
public interface ShootStrategy {
    List<BaseBullet> shoot(AbstractEnemy aircraft);
    List<BaseBullet> shoot(HeroAircraft aircraft);
}
