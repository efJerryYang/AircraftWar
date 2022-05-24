package com.efjerryyang.defendthefrontline.bullet;


import com.efjerryyang.defendthefrontline.application.MainActivity;
import com.efjerryyang.defendthefrontline.basic.AbstractFlyingObject;
import com.efjerryyang.defendthefrontline.subscriber.BombSubscriber;

/**
 * 子弹类。
 * 也可以考虑不同类型的子弹
 *
 * @author JerryYang
 */
public class BaseBullet extends AbstractFlyingObject implements BombSubscriber {

    private int power = 10;

    public BaseBullet(int locationX, int locationY, int speedX, int speedY, int power) {
        super(locationX, locationY, speedX, speedY);
        this.power = power;
    }

    @Override
    public void forward() {
        super.forward();

        // 判定 x 轴出界
        if (locationX <= 0 || locationX >= MainActivity.screenWidth) {
            vanish();
        }

    }

    public int getPower() {
        return power;
    }

    @Override
    public void bombExplode() {

    }
}
