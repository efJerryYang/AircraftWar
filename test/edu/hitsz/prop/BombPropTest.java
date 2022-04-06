package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractEnemy;
import edu.hitsz.aircraft.HeroAircraft;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.factory.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BombPropTest {
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
        this.bombPropFactory = new BombPropFactory();
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
        this.bombPropFactory = null;

    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    void activate(int x) {
        System.out.println("\ttest: BombProp.activate()");
        time = 0;
        int difficulty = 2;
        BombProp bombProp = (BombProp) bombPropFactory.createProp(100, 200, "bomb");
        for (int i = 1; i <= x; i++) {
            if (i > 5) {
                break;
            }
            enemyAircrafts.add(mobFactory.createEnemy(difficulty));
            enemyAircrafts.add(eliteFactory.createEnemy(difficulty));
            enemyAircrafts.add(bossFactory.createEnemy(difficulty));
        }
        bombProp.activate(heroAircraft, enemyAircrafts, heroBullets, time);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);  // boss enemy will not be removed
        assertEquals(Math.min(x, 5), enemyAircrafts.size());
    }

    @Test
    void getScore() {
        System.out.println("\ttest: BombProp.getScore()");
        BombProp bombProp = (BombProp) bombPropFactory.createProp(100, 200, "bomb");
        assertEquals(30, bombProp.getScore());
    }
}