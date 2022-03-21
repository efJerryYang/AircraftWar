package edu.hitsz.aircraft;

public class BossEnemy extends EliteEnemy {

    public BossEnemy(int locationX, int locationY, int speedX, int speedY, int hp, int score, String type) {
        super(locationX, locationY, speedX, speedY, hp, score, type);
        this.setShootNum(this.getShootNum() * 5);
//        this.setPower();
    }

}
