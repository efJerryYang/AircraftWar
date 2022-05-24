//package com.efjerryyang.defendthefrontline.application;
//
//import com.efjerryyang.defendthefrontline.aircraft.HeroAircraft;
//import com.efjerryyang.defendthefrontline.game.AbstractGame;
//
///**
// * 英雄机控制类
// * 监听鼠标，控制英雄机的移动
// *
// * @author JerryYang
// */
//public class HeroController {
//    private AbstractGame game;
//    private HeroAircraft heroAircraft;
//    private MouseAdapter mouseAdapter;
//
//    public HeroController(AbstractGame game, HeroAircraft heroAircraft) {
//        this.game = game;
//        this.heroAircraft = heroAircraft;
//
//        mouseAdapter = new MouseAdapter() {
//            // 同时使用mouseMoved和mouseDragged，完美解决瞬移bug
//            @Override
//            public void mouseMoved(MouseEvent e) {
//                super.mouseMoved(e);
//                int x = e.getX();
//                int y = e.getY();
//                if (x < 0 || x > Main.WINDOW_WIDTH || y < 0 || y > Main.WINDOW_HEIGHT) {
//                    // 防止超出边界
//                    return;
//                }
//                heroAircraft.setLocation(x, y);
//            }
//
//            @Override
//            public void mouseDragged(MouseEvent e) {
//                super.mouseDragged(e);
//                int x = e.getX();
//                int y = e.getY();
//                if (x < 0 || x > Main.WINDOW_WIDTH || y < 0 || y > Main.WINDOW_HEIGHT) {
//                    // 防止超出边界
//                    return;
//                }
//                heroAircraft.setLocation(x, y);
//            }
//        };
//        game.addMouseListener(mouseAdapter);
//        game.addMouseMotionListener(mouseAdapter);
//
//    }
//}
