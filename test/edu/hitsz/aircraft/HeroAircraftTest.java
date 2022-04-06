package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.factory.*;
import edu.hitsz.prop.AbstractProp;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class HeroAircraftTest {


    private List<AbstractEnemy> enemyAircrafts;
    private List<BaseBullet> heroBullets;
    private List<BaseBullet> enemyBullets;
    private EnemyFactory enemyFactory;
    private BulletPropFactory bulletPropFactory;
    private BloodPropFactory bloodPropFactory;
    private BombPropFactory bombPropFactory;
    private HeroAircraft heroAircraft;
    private MobFactory mobFactory;
    private EliteFactory eliteFactory;
    private BossFactory bossFactory;
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
        this.mobFactory = new MobFactory();
        this.eliteFactory = new EliteFactory();
        this.bossFactory = new BossFactory();
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

    @Test
    void getHeroAircraft() {
        heroAircraft = HeroAircraft.getHeroAircraft();
        assertNotEquals(null, heroAircraft);
    }

    @Test
    void getDirection() {
        assertEquals(-1, heroAircraft.getDirection());
    }

    @Test
    void setDirection() {
        heroAircraft.setDirection(1);
        assertEquals(1, heroAircraft.getDirection());
    }

    @Disabled
    @Test
    void forward() {

    }

    @ParameterizedTest
//    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    @ValueSource(ints = {8})
// 根据要求只运行一个测试用例
    void shoot(int x) {
        System.out.println("\tTest: HeroAircraft.shoot()");
        time = 0;
        AbstractProp bulletProp = bulletPropFactory.createProp(100, 100, "bullet");
        System.out.println("\tactivate times: " + x);
        for (int i = 0; i < x; i++) {
            System.out.print("\tactivate number: " + (i + 1) + "\t");
            bulletProp.activate(heroAircraft, enemyAircrafts, heroBullets, time);
        }

        List<BaseBullet> res = heroAircraft.shoot();
        int sumX = 0;
        for (int i = 0; i < heroAircraft.getShootNum(); i++) {
            BaseBullet bullet = res.get(i);
            int center = i * 2 - heroAircraft.getShootNum() + 1;
            assertEquals(heroAircraft.getLocationY() + heroAircraft.getDirection() * 2 + center * center,
                    bullet.getLocationY());
            sumX += bullet.getLocationX();
            System.out.println("\tx_offset: " + center + " \ty_offset: " + center * center);
        }
        assertEquals(heroAircraft.getLocationX(), sumX / res.size());
    }

    @Test
    void getShootNum() {
        assertEquals(1, heroAircraft.getShootNum());
    }

    @Test
    void setShootNum() {
        heroAircraft.setShootNum(10);
        assertEquals(10, heroAircraft.getShootNum());
    }

    @Test
    void getPower() {
        assertEquals(30, heroAircraft.getPower());
    }

    @Test
    void setPower() {
        heroAircraft.setPower(100);
        assertEquals(100, heroAircraft.getPower());
    }

    @Disabled
    @Test
    void isBulletValid() {
    }

    @Disabled
    @Test
    void setBulletValid() {
    }

    @Disabled
    @Test
    void isShieldValid() {
    }

    @Disabled
    @Test
    void setShieldValid() {
    }
}