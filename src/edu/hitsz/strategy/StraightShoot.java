package edu.hitsz.strategy;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.factory.BulletFactory;

import java.util.LinkedList;
import java.util.List;

public class StraightShoot implements ShootStrategy {


    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();
        BulletFactory bulletFactory = new BulletFactory();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + aircraft.getDirection() * 2;
        int speedX = 0;
        int speedY = aircraft.getSpeedY() + aircraft.getDirection() * 5;
        BaseBullet baseBullet;
        int shootNum = aircraft.getShootNum();
        int power = aircraft.getPower();
        for (int i = 0; i < shootNum; i++) {
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            int center = i * 2 - shootNum + 1;
            int xloc = x + center * 13;
            int yloc = aircraft instanceof HeroAircraft ? y + center * center * 3 : y;
            baseBullet = bulletFactory.createBullet(
                    aircraft, xloc, yloc,
                    speedX, speedY, power);
            res.add(baseBullet);
        }
        return res;
    }
}
