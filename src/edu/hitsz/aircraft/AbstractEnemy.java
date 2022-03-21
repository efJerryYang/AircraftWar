package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractEnemy extends AbstractAircraft {
    private int score;
    private String type;

    public AbstractEnemy(int locationX, int locationY, int speedX, int speedY, int hp, int score, String type) {
        super(locationX, locationY, speedX, speedY, hp);
        this.score = score;
        this.type = type;
    }

    @Override
    public void forward() {
        super.forward();
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

    public String getType() {
        return type;
    }
}
