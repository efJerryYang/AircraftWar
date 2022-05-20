package edu.hitsz.application;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.strategy.ScatterShoot;
import edu.hitsz.strategy.ShootStrategy;
import edu.hitsz.strategy.StraightShoot;

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
