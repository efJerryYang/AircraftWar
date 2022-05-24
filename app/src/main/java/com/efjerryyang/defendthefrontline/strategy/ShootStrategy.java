package com.efjerryyang.defendthefrontline.strategy;

import com.efjerryyang.defendthefrontline.aircraft.AbstractEnemy;
import com.efjerryyang.defendthefrontline.aircraft.HeroAircraft;
import com.efjerryyang.defendthefrontline.bullet.BaseBullet;

import java.util.List;

/**
 * @author JerryYang
 */
public interface ShootStrategy {
    List<BaseBullet> shoot(AbstractEnemy aircraft);
    List<BaseBullet> shoot(HeroAircraft aircraft);
}
