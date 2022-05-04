package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.subscriber.BombSubscriber;

import java.util.LinkedList;
import java.util.List;

/**
 * 抽象敌人类
 * <p>
 * score用于记录击毁敌人所获得的分数，或者获取道具时获取的分数
 * type用于记录敌人种类
 *
 * @author JerryYang
 */
public abstract class AbstractEnemy extends AbstractAircraft implements BombSubscriber {
    private int score;

    public AbstractEnemy(int locationX, int locationY, int speedX, int speedY, int hp, int score) {
        super(locationX, locationY, speedX, speedY, hp);
        this.score = score;
    }

    @Override
    public void forward() {
        super.forward();
        if (locationX <= 0 || locationX >= Main.WINDOW_WIDTH) {
            // 横向超出边界后反向
            locationX = locationX <= 0 ? 1 : Main.WINDOW_WIDTH - 1;
            speedX = -speedX;
        }
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT) {
            vanish();
        }
    }

    @Override
    public List<BaseBullet> shoot() {
        return new LinkedList<>();
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void explode() { }
}
