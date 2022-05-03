package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.factory.BulletFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * @author JerryYang
 */
public class ScatterShoot implements ShootStrategy {

    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();
        BulletFactory bulletFactory = new BulletFactory();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + aircraft.getDirection() * 2;
        int speedX = 1;
        int speedY = aircraft.getSpeedY() + aircraft.getDirection() * 5;
        BaseBullet baseBullet;
        int shootNum = aircraft.getShootNum();
        int power = aircraft.getPower();
        for (int i = 0; i < shootNum; i++) {
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            boolean isHeroInstance = aircraft instanceof HeroAircraft;
            int center = i * 2 - shootNum + 1;
            int xloc = isHeroInstance ? x + center * 10 : x + center;
            int yloc = isHeroInstance ? y + center * center * 2 - 5 : y - center * center + 10;
            baseBullet = bulletFactory.createBullet(
                    aircraft, xloc, yloc,
                    center != 0 ? speedX * center : 0, isHeroInstance ? speedY * 2 : speedY, power);
            res.add(baseBullet);
        }
        return res;
    }
}
