package edu.hitsz.aircraft;

import edu.hitsz.subscriber.BombSubscriber;

/**
 * 普通敌机
 * 不可射击
 *
 * @author Jerry Yang
 */
public class MobEnemy extends AbstractEnemy implements BombSubscriber {
    private int direction=1;

    public MobEnemy(int locationX, int locationY, int speedX, int speedY, int hp, int score) {
        super(locationX, locationY, speedX, speedY, hp, score);
    }

    @Override
    public void bombExplode() {
        this.decreaseHp(this.getMaxHp());
    }

    @Override
    public int getDirection() {
        return direction;
    }

    @Override
    public void setDirection(int direction) {
        this.direction = direction;
    }
}
