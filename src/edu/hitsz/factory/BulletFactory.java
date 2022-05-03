package edu.hitsz.factory;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.bullet.HeroBullet;

/**
 * @author JerryYang
 */
public class BulletFactory {
    /**
     * The method is used for generating different types of bullet (enemy & hero)
     *
     * @param aircraft  an instance of abstractAircraft
     * @param locationX location to generate bullet
     * @param locationY location to generate bullet
     * @param speedX    usage for scatter shoot
     * @param speedY    basic shoot direction
     * @param power     bullet power
     * @return return a bullet instance
     */
    public BaseBullet createBullet(AbstractAircraft aircraft, int locationX, int locationY, int speedX, int speedY, int power) {
        if (aircraft instanceof HeroAircraft) {
            if (((HeroAircraft) aircraft).getBulletPropStage() == 0) {
                return new HeroBullet(locationX, locationY, speedX, speedY, power);
            } else {
                return new HeroBullet(locationX, locationY, speedX, speedY, power * 2);
            }
        } else if (aircraft instanceof AbstractEnemy) {
            return new EnemyBullet(locationX, locationY, speedX, speedY, power);
        } else {
            return null;
        }
    }
}
