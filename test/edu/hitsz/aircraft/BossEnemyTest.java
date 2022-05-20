package edu.hitsz.aircraft;

import edu.hitsz.application.ShootContext;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.factory.*;
import edu.hitsz.strategy.ScatterShoot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BossEnemyTest {
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

    @ParameterizedTest
//    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    @ValueSource(ints = {8})
        // 根据要求只运行一个测试用例
    void shoot(int x) {
        System.out.println("\tTest: BossEnemy.shoot()");
        time = 0;
        for (int i = 0; i < x; i++) {
            if (i > 5) {
                break;
            }
            int difficulty = Math.max(1, i);
            System.out.println("\tDifficulty: " + difficulty);
            BossEnemy boss = (BossEnemy) bossFactory.createEnemy(difficulty);
            ShootContext shootContext = new ShootContext(new ScatterShoot());
            List<BaseBullet> res = shootContext.executeShootStrategy(boss);
            int sumX = 0;
            for (int j = 0; j < boss.getShootNum(); j++) {
                BaseBullet bullet = res.get(j);
                int center = j * 2 - boss.getShootNum() + 1;
//                System.out.print(boss.getLocationY() + boss.getDirection() * 2 - center * center + 10);
                assertEquals(boss.getLocationY() + boss.getDirection() * 2 - center * center + 10, bullet.getLocationY());
                sumX += bullet.getLocationX();
                System.out.println("\tx_offset: " + center + " \ty_offset: " + center * center);
            }
            assertEquals(boss.getLocationX(), sumX / res.size());
//            System.out.print(boss.getLocationX());
            System.out.println();
        }

    }

    @ParameterizedTest
//    @ValueSource(ints = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10})
    @ValueSource(ints = {8})
//  根据要求只运行一个测试用例
    void getHp(int x) {
        System.out.println("\tTest: BossEnemy.getHp()");
        int difficulty = Math.max(1, x);
        BossEnemy boss = (BossEnemy) bossFactory.createEnemy(difficulty);
        System.out.println("\tMaxHp: " + boss.getHp());
        assertEquals(500 * difficulty, boss.getHp());
    }

    @Test
    void getShootNum() {
        assertEquals(5, ((BossEnemy) bossFactory.createEnemy(1)).getShootNum());
    }

    @Test
    void setShootNum() {
        BossEnemy boss = (BossEnemy) bossFactory.createEnemy(1);
        boss.setShootNum(10);
        assertEquals(10, boss.getShootNum());
    }

    @Test
    void getPower() {
        BossEnemy boss = (BossEnemy) bossFactory.createEnemy(1);
        assertEquals(30, boss.getPower());
    }

    @Test
    void setPower() {
        BossEnemy boss = (BossEnemy) bossFactory.createEnemy(1);
        boss.setPower(10);
        assertEquals(10, boss.getPower());
    }


}