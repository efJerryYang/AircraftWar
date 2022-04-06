package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;

import java.util.LinkedList;
import java.util.List;

/**
 * @author JerryYang
 */
public class BossEnemy extends EliteEnemy {

    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp, int score, String type) {
        super(locationX, locationY, speedX, speedY, hp, score, type);
        this.setShootNum(this.getShootNum() * 5);
//        this.setPower();
    }

    @Override
    public List<BaseBullet> shoot() {
        List<BaseBullet> res = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() + this.getDirection() * 2;
        int speedX = 1;
        int speedY = this.getSpeedY() + this.getDirection() * 5;
        BaseBullet baseBullet;
        for (int i = 0; i < this.getShootNum(); i++) {
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            int center = i * 2 - this.getShootNum() + 1;
            int xloc = x + center;
            int yloc = y - center * center + 10;
            baseBullet = new EnemyBullet(xloc, yloc, center != 0 ? speedX * center : 0, speedY, this.getPower());
            res.add(baseBullet);
        }
        return res;
    }

}
