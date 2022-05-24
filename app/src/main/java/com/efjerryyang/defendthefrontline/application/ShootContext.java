package com.efjerryyang.defendthefrontline.application;

import com.efjerryyang.defendthefrontline.aircraft.AbstractEnemy;
import com.efjerryyang.defendthefrontline.aircraft.BossEnemy;
import com.efjerryyang.defendthefrontline.aircraft.HeroAircraft;
import com.efjerryyang.defendthefrontline.bullet.BaseBullet;
import com.efjerryyang.defendthefrontline.strategy.ScatterShoot;
import com.efjerryyang.defendthefrontline.strategy.ShootStrategy;
import com.efjerryyang.defendthefrontline.strategy.StraightShoot;

import java.util.List;

public class ShootContext {
    private ShootStrategy shootStrategy;

    public ShootContext(ShootStrategy shootStrategy) {
        this.shootStrategy = shootStrategy;
    }

    public List<BaseBullet> executeShootStrategy(AbstractEnemy aircraft) {
        if (aircraft.getClass().equals(BossEnemy.class)) {
            setShootStrategy(new ScatterShoot());
        } else {
            setShootStrategy(new StraightShoot());
        }
        return shootStrategy.shoot(aircraft);
    }
    public List<BaseBullet> executeShootStrategy(HeroAircraft aircraft) {
        if (aircraft.getBulletPropStage() > 0) {
            setShootStrategy(new ScatterShoot());
        } else {
            setShootStrategy(new StraightShoot());
        }
        return shootStrategy.shoot(aircraft);
    }

    public ShootStrategy getShootStrategy() {
        return shootStrategy;
    }

    public void setShootStrategy(ShootStrategy shootStrategy) {
        this.shootStrategy = shootStrategy;
    }
}
