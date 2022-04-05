package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.factory.BloodPropFactory;
import edu.hitsz.factory.BombPropFactory;
import edu.hitsz.factory.BulletPropFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BloodPropTest {
    private List<AbstractEnemy> enemyAircrafts;
    private List<BaseBullet> heroBullets;
    private List<BaseBullet> enemyBullets;
    private BulletPropFactory bulletPropFactory;
    private BloodPropFactory bloodPropFactory;
    private BombPropFactory bombPropFactory;
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
        this.bloodPropFactory = new BloodPropFactory();
        this.time = 0;
    }

    @AfterEach
    void tearDown() {
        System.out.println("after each test");
        this.heroAircraft = null;
        this.enemyAircrafts = null;
        this.heroBullets = null;
        this.enemyBullets = null;
        this.bloodPropFactory = null;

    }

    @ParameterizedTest
    @ValueSource(ints = {300,0, 0, 0, 10, 20, 50, 100, 199, 200, 201, 250, 300})
    void activate(int x) {
        System.out.println("\ttest: BloodProp.activate()");
        time = 0;
        BloodProp bloodProp = (BloodProp) bloodPropFactory.createProp(100, 200, "blood");

        heroAircraft.decreaseHp(heroAircraft.getMaxHp() - x);
        System.out.print("\t");
        bloodProp.activate(heroAircraft, enemyAircrafts, heroBullets, time);
        int expected = x <= 0 ? 0 :
                Math.min(x + 100, 300);
        assertEquals(expected, heroAircraft.getHp());
    }

}