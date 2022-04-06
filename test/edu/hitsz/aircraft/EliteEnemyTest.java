package edu.hitsz.aircraft;

import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.factory.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.LinkedList;
import java.util.List;

import static edu.hitsz.application.Main.WINDOW_HEIGHT;
import static edu.hitsz.application.Main.WINDOW_WIDTH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class EliteEnemyTest {
    private static String path;
    private List<AbstractEnemy> enemyAircrafts;
    private List<BaseBullet> heroBullets;
    private List<BaseBullet> enemyBullets;
    private EnemyFactory enemyFactory;
    private BulletPropFactory bulletPropFactory;
    private BloodPropFactory bloodPropFactory;
    private BombPropFactory bombPropFactory;
    private MobFactory mobFactory;
    private EliteFactory eliteFactory;
    private BossFactory bossFactory;
    private int time;

    static void getCurrentPath() {
        path = System.getProperty("user.dir");
    }

    @BeforeEach
    void setUp() {
        System.out.println("before each test");
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
        this.enemyAircrafts = null;
        this.heroBullets = null;
        this.enemyBullets = null;
        this.bulletPropFactory = null;
        this.mobFactory = null;
        this.eliteFactory = null;
        this.bossFactory = null;
        this.time = 0;
    }

    //https://stackoverflow.com/questions/60081577/what-is-best-way-of-reading-test-data-from-csv-file-with-junit-5
    @ParameterizedTest
//    @CsvFileSource(resources = "/data/forward_samples.csv", numLinesToSkip = 0)
    @ValueSource(ints = {8})
    // 根据要求只运行一个测试用例
    void forward(int x) {
        System.out.println("\tTest: EliteEnemy.forward()");
        EliteEnemy elite = (EliteEnemy) eliteFactory.createEnemy(x);
        for (int i = 0; i < x; i++) {
            elite.forward();
        }
        // 需要注意的是，边界条件和图像在画面中的呈现有关，左取右不取
        int xllim = -Math.abs(elite.getSpeedX());
        int xrlim = WINDOW_WIDTH + Math.abs(elite.getSpeedX());
        if (elite.isValid()) {
            assertTrue(elite.getLocationY() >= 0);
            assertTrue(elite.getLocationY() < WINDOW_HEIGHT);
            System.out.println("\tyloc: 0 <= " + elite.getLocationY() + " <= " + WINDOW_HEIGHT);
            assertTrue(elite.getLocationX() >= xllim);
            assertTrue(elite.getLocationX() < xrlim);
            System.out.println("\txloc: " + xllim + " <= " + elite.getLocationX() + " <= " + xrlim);
        } else {
            System.out.println("\tyloc: " + elite.getLocationY() + " out of bound!\n\tValid ylim: [0, " + WINDOW_HEIGHT + "]");
            assertTrue(elite.getLocationY() < 0 || elite.getLocationY() >= WINDOW_HEIGHT);
            System.out.println("\txloc: " + xllim + " <= " + elite.getLocationX() + " <= " + xrlim);
            assertTrue(elite.getLocationX() >= xllim);
            assertTrue(elite.getLocationX() < xrlim);
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    void shoot(int x) {
        System.out.println("\tTest: EliteEnemy.shoot()");
        time = 0;
        for (int i = 0; i < x; i++) {
            if (i > 5) {
                break;
            }
            EliteEnemy elite = (EliteEnemy) eliteFactory.createEnemy(i);
            List<BaseBullet> res = elite.shoot();
            for (int j = 0; j < elite.getShootNum(); j++) {
                BaseBullet bullet = res.get(j);
                int center = j * 2 - elite.getShootNum() + 1;
                assertEquals(elite.getLocationX() + center * 10, bullet.getLocationX());
                assertEquals(elite.getLocationY() + elite.getDirection() * 2, bullet.getLocationY());
            }
        }
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    void getHp(int x) {
        System.out.println("\tTest: EliteEnemy.getHp()");
        int difficulty = Math.max(1, x);
        EliteEnemy elite = (EliteEnemy) eliteFactory.createEnemy(difficulty);
        System.out.println("\tMaxHp: " + elite.getHp());
        assertEquals(60 * difficulty, elite.getHp());
    }
}