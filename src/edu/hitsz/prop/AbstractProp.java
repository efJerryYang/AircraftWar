package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.application.Main;
import edu.hitsz.basic.FlyingObject;

import java.util.List;

public abstract class AbstractProp extends FlyingObject {

    // my Todo: 这里需要重构，对于FlyingObject的分类是不完全合理的，敌方飞行物应当放在一类里面
    private String type = null;

    public AbstractProp(int locationX, int locationY, int speedX, int speedY, String type) {
        super(locationX, locationY, speedX, speedY);
        this.type = type;
    }

    @Override
    public void forward() {
        super.forward();

        // 判定 x 轴出界
        if (locationX <= 0 || locationX >= Main.WINDOW_WIDTH) {
            vanish();
        }

        // 判定 y 轴出界
        if (speedY > 0 && locationY >= Main.WINDOW_HEIGHT) {
            // 向下飞行出界
            vanish();
        } else if (locationY <= 0) {
            // 向上飞行出界
            vanish();
        }
    }

    public void activate(HeroAircraft heroAircraft){

    }
    public void activate(List<AbstractEnemy> abstractEnemyList) {

    }

}
