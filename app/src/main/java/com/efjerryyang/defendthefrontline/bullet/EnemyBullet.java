package com.efjerryyang.defendthefrontline.bullet;

import com.efjerryyang.defendthefrontline.subscriber.BombSubscriber;

/**
 * @Author JerryYang
 */
public class EnemyBullet extends BaseBullet implements BombSubscriber {

    private int score = 1;
    public EnemyBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY, power);
    }

    @Override
    public void bombExplode() {
        this.vanish();
    }
}
