package com.efjerryyang.defendthefrontline.prop;

import com.efjerryyang.defendthefrontline.aircraft.AbstractEnemy;
import com.efjerryyang.defendthefrontline.aircraft.BossEnemy;
import com.efjerryyang.defendthefrontline.aircraft.HeroAircraft;
import com.efjerryyang.defendthefrontline.bullet.BaseBullet;
import com.efjerryyang.defendthefrontline.bullet.EnemyBullet;
import com.efjerryyang.defendthefrontline.subscriber.BombSubscriber;

import java.util.LinkedList;
import java.util.List;

/**
 * @author JerryYang
 */
public class BombProp extends AbstractProp {

    public final List<BombSubscriber> subscribers;

    public BombProp(int locationX, int locationY, int speedX, int speedY, int score, String type) {
        super(locationX, locationY, speedX, speedY, score, type);
        subscribers = new LinkedList<>();
    }

    @Override
    public void activate(HeroAircraft heroAircraft, List<AbstractEnemy> abstractEnemyList, List<BaseBullet> heroBullets, List<BaseBullet> enemyBullets, int time) {
        System.out.println("BombSupply active!");
        for (AbstractEnemy enemy : abstractEnemyList) {
            subscribe(enemy);
        }
        for (BaseBullet bullet : enemyBullets) {
            subscribe(bullet);
        }
        notifyAllSubscribers();
//        for (AbstractEnemy enemy : abstractEnemyList) {
//            if (BossEnemy.class.equals(enemy.getClass())) {
//                continue;
//            }
//            this.setScore(this.getScore() + enemy.getScore());
//            enemy.vanish();
//        }
//        for (BaseBullet enemyBullet: enemyBullets){
//            enemyBullet.vanish();
//        }
    }

    public void subscribe(BombSubscriber subscriber) {
//        System.exit(0);
        subscribers.add(subscriber);
    }


    public void unsubscribe(BombSubscriber subscriber) {
        subscribers.remove(subscriber);
    }
    public void unsubscribe(List<BombSubscriber> subscriberList) {
        subscribers.removeAll(subscriberList);
    }

    public void notifyAllSubscribers() {
        for (BombSubscriber subscriber : subscribers) {
            subscriber.bombExplode();
            if (subscriber.getClass().equals(BossEnemy.class)) {
                continue;
            }
            if(subscriber.getClass().equals(EnemyBullet.class) ) {
//                this.setScore(this.getScore() + ((EnemyBullet) subscriber).getScore());
                ((EnemyBullet) subscriber).vanish();
            }else{
                this.setScore(this.getScore() + ((AbstractEnemy) subscriber).getScore());
                ((AbstractEnemy) subscriber).vanish();
            }
        }
        unsubscribe(subscribers);
    }
}
