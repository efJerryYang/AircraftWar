package edu.hitsz.aircraft;

import edu.hitsz.subscriber.BombSubscriber;

/**
 * 普通敌机
 * 不可射击
 *
 * @author Jerry Yang
 */
public class MobEnemy extends AbstractEnemy implements BombSubscriber {
    public MobEnemy(int locationX, int locationY, int speedX, int speedY, int hp, int score, String type) {
        super(locationX, locationY, speedX, speedY, hp, score, type);
    }

    @Override
    public void bombExplode() {
        this.decreaseHp(this.getMaxHp());
    }
}
