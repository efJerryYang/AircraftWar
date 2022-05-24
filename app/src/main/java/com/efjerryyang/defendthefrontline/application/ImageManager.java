package com.efjerryyang.defendthefrontline.application;


import android.graphics.Bitmap;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 综合管理图片的加载，访问
 * 提供图片的静态访问方法
 *
 * @author JerryYang
 */
public class ImageManager {

    /**
     * 类名-图片 映射，存储各基类的图片 <br>
     * 可使用 CLASSNAME_IMAGE_MAP.get( obj.getClass().getName() ) 获得 obj 所属基类对应的图片
     */
    public static final Map<String, Bitmap> CLASSNAME_IMAGE_MAP = new HashMap<>();

    public static Bitmap BACKGROUND_IMAGE_LEVEL1;
    public static Bitmap BACKGROUND_IMAGE_LEVEL2;
    public static Bitmap BACKGROUND_IMAGE_LEVEL3;
    public static Bitmap BACKGROUND_IMAGE_LEVEL4;
    public static Bitmap BACKGROUND_IMAGE_LEVEL5;
    public static Bitmap HERO_IMAGE;
    public static Bitmap HERO_BULLET_IMAGE;
    public static Bitmap ENEMY_BULLET_IMAGE;
    public static Bitmap MOB_ENEMY_IMAGE;

    public static Bitmap ELITE_ENEMY_IMAGE;
    public static Bitmap BOSS_ENEMY_IMAGE;

    public static Bitmap BLOOD_PROP_IMAGE, BOMB_PROP_IMAGE, BULLET_PROP_IMAGE;
    public static Bitmap SHIELD_IMAGE;

    public static Bitmap get(String className) {
        return CLASSNAME_IMAGE_MAP.get(className);
    }

    public static Bitmap get(Object obj) {
        if (obj == null) {
            return null;
        }
        return get(obj.getClass().getName());
    }

}
