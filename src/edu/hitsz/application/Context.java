package edu.hitsz.application;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.BossEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.strategy.ScatterShoot;
import edu.hitsz.strategy.ShootStrategy;
import edu.hitsz.strategy.StraightShoot;

import java.util.List;

public class Context {
    private ShootStrategy shootStrategy;

    public Context(ShootStrategy shootStrategy) {
        this.shootStrategy = shootStrategy;
    }

    public List<BaseBullet> executeShootStrategy(AbstractAircraft aircraft) {
        if (aircraft instanceof HeroAircraft && ((HeroAircraft) aircraft).isBulletSpeedUp()) {
            setShootStrategy(new ScatterShoot());
        } else if (aircraft instanceof BossEnemy) {
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
