package com.efjerryyang.defendthefrontline.basic;

import android.graphics.Bitmap;

import com.efjerryyang.defendthefrontline.aircraft.AbstractAircraft;
import com.efjerryyang.defendthefrontline.application.ImageManager;
import com.efjerryyang.defendthefrontline.application.MainActivity;

/**
 * 可飞行对象的父类
 *
 * @author JerryYang
 */
public abstract class AbstractFlyingObject{

    //locationX、locationY为图片中心位置坐标
    /**
     * x 轴坐标
     */
    protected int locationX;

    /**
     * y 轴坐标
     */
    protected int locationY;


    /**
     * x 轴移动速度
     */
    protected int speedX;
    /**
     * y 轴移动速度
     */
    protected int speedY;
    /**
     * 图片,
     * null 表示未设置
     */
    protected Bitmap image = null;
    /**
     * x 轴长度，根据图片尺寸获得
     * -1 表示未设置
     */
    protected int width = -1;
    /**
     * y 轴长度，根据图片尺寸获得
     * -1 表示未设置
     */
    protected int height = -1;
    /**
     * 有效（生存）标记，
     * 通常标记为 false的对象会再下次刷新时清除
     */
    protected boolean isValid = true;
    private int score = 10;

    public AbstractFlyingObject() {
    }

    public AbstractFlyingObject(int locationX, int locationY, int speedX, int speedY) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.speedX = speedX;
        this.speedY = speedY;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public int getSpeedX() {
        return speedX;
    }

    public int setSpeedX(int speedX) {
        this.speedX = speedX;
        return speedX;
    }

    /**
     * 可飞行对象根据速度移动
     * 若飞行对象触碰到横向边界，横向速度反向
     * 若飞行对象超出y轴表示范围，标记为出界
     */
    public void forward() {
        locationX += speedX;
        locationY += speedY;

        // 判定 y 轴出界
        if (speedY > 0 && locationY >= MainActivity.screenHeight) {
            // 向下飞行出界
            vanish();
        } else if (locationY <= 0) {
            // 向上飞行出界
            vanish();
        }
    }

    /**
     * 碰撞检测，当对方坐标进入我方范围，判定我方击中<br>
     * 对方与我方覆盖区域有交叉即判定撞击。
     * <br>
     * 非飞机对象区域：
     * 横向，[x - width/2, x + width/2]
     * 纵向，[y - height/2, y + height/2]
     * <br>
     * 飞机对象区域：
     * 横向，[x - width/2, x + width/2]
     * 纵向，[y - height/4, y + height/4]
     *
     * @param abstractFlyingObject 撞击对方
     * @return true: 我方被击中; false 我方未被击中
     */
    public boolean crash(AbstractFlyingObject abstractFlyingObject) {
        // 缩放因子，用于控制 y轴方向区域范围
        int factor = this instanceof AbstractAircraft ? 2 : 1;
        int fFactor = abstractFlyingObject instanceof AbstractAircraft ? 2 : 1;

        int x = abstractFlyingObject.getLocationX();
        int y = abstractFlyingObject.getLocationY();
        int fWidth = abstractFlyingObject.getWidth();
        int fHeight = abstractFlyingObject.getHeight();

        return x + (fWidth + this.getWidth()) / 2 > locationX && x - (fWidth + this.getWidth()) / 2 < locationX && y + (fHeight / fFactor + this.getHeight() / factor) / 2 > locationY && y - (fHeight / fFactor + this.getHeight() / factor) / 2 < locationY;
    }

    public int getLocationX() {
        return locationX;
    }

    public void setLocationX(int locationX) {
        this.locationX = locationX;
    }

    public int getLocationY() {
        return locationY;
    }

    public void setLocationY(int locationY) {
        this.locationY = locationY;
    }

    public void setLocation(double locationX, double locationY) {
        this.locationX = (int) locationX;
        this.locationY = (int) locationY;
    }

    public int getSpeedY() {
        return speedY;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }

    public Bitmap getImage() {
        if (image == null) {
            image = ImageManager.get(this);
        }
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public int getWidth() {
        if (width == -1) {
            // 若未设置，则查询图片宽度并设置
            width = ImageManager.get(this).getWidth();
        }
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        if (height == -1) {
            // 若未设置，则查询图片高度并设置
            height = ImageManager.get(this).getHeight();
        }
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean notValid() {
        return !this.isValid;
    }

    /**
     * 标记消失，
     * isValid = false.
     * notValid() => true.
     */
    public void vanish() {
        isValid = false;
    }

    public void explode() {
        // my Todo: load explode images (2-4) from other airwar repo
    }

    public int getScore() {
        return score;
    }
}

