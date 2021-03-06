package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.factory.BulletPropFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.pow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BulletPropTest {

    private List<AbstractEnemy> enemyAircrafts;
    private List<BaseBullet> heroBullets;
    private List<BaseBullet> enemyBullets;
    private BulletPropFactory bulletPropFactory;
    private BulletProp bulletProp;
    private HeroAircraft heroAircraft;
    private int time;

    @BeforeAll
    static void beforeAll() {
        System.out.println("before all methods in class");
    }

    @BeforeEach
    void setUp() {
        System.out.println("before each test");
        if (this.heroAircraft == null) {
            this.heroAircraft = HeroAircraft.getHeroAircraft();
        }
        this.heroAircraft.initialize();
        this.enemyAircrafts = new LinkedList<>();
        this.heroBullets = new LinkedList<>();
        this.enemyBullets = new LinkedList<>();
        this.bulletPropFactory = new BulletPropFactory();
        this.time = 0;
    }

    @AfterEach
    void tearDown() {
        System.out.println("after each test");
        this.heroAircraft = null;
        this.enemyAircrafts = null;
        this.heroBullets = null;
        this.enemyBullets = null;
        this.bulletPropFactory = null;
    }

    @ParameterizedTest
//    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    @ValueSource(ints = {8})
// 根据要求只运行一个测试用例
    void activate(int x) {
        System.out.println("\tTest: BulletProp.activate()");
        time = 0;
        AbstractProp bulletProp = bulletPropFactory.createProp(100, 100);
        System.out.println("\tbulletProp: " + x);
        for (int i = 1; i <= x; i++) {
            System.out.print("\tactivate number: " + i + "\t");
            bulletProp.activate(heroAircraft, enemyAircrafts, heroBullets, enemyBullets, time);
        }
        int p = (int) pow(2, x);
        assertEquals(Math.min(p, 5), heroAircraft.getShootNum());
    }

    @Test
    void getScore() {
        System.out.println("\tTest: BulletProp.getScore()");
        BulletProp bulletProp = (BulletProp) bulletPropFactory.createProp(100, 200);
        assertEquals(30, bulletProp.getScore());
    }
}