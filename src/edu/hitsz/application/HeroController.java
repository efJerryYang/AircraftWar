package edu.hitsz.application;

import edu.hitsz.aircraft.HeroAircraft;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 英雄机控制类
 * 监听鼠标，控制英雄机的移动
 *
 * @author hitsz
 */
public class HeroController {
    private Game game;
    private HeroAircraft heroAircraft;
    private MouseAdapter mouseAdapter;
//    private KeyListener keyListener;
//    private KeyAdapter keyAdapter;

    public HeroController(Game game, HeroAircraft heroAircraft) {
        this.game = game;
        this.heroAircraft = heroAircraft;

        mouseAdapter = new MouseAdapter() {
            // 同时使用mouseMoved和mouseDragged，完美解决瞬移bug
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                int x = e.getX();
                int y = e.getY();
                if (x < 0 || x > Main.WINDOW_WIDTH || y < 0 || y > Main.WINDOW_HEIGHT) {
                    // 防止超出边界
                    return;
                }
                heroAircraft.setLocation(x, y);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                int x = e.getX();
                int y = e.getY();
                if (x < 0 || x > Main.WINDOW_WIDTH || y < 0 || y > Main.WINDOW_HEIGHT) {
                    // 防止超出边界
                    return;
                }
                heroAircraft.setLocation(x, y);
            }
        };
//        keyAdapter = new KeyAdapter() {
//
//            @Override
//            public void keyPressed(KeyEvent e) {
//                int x = heroAircraft.getLocationX();
//                int y = heroAircraft.getLocationY();
//                super.keyPressed(e);
//                if (KeyEvent.VK_W == e.getKeyCode() || KeyEvent.VK_UP == e.getKeyCode()) {
//                    y = heroAircraft.getLocationY() + 10;
//                } else if (KeyEvent.VK_S == e.getKeyCode() || KeyEvent.VK_DOWN == e.getKeyCode()) {
//                    y = heroAircraft.getLocationY() - 10;
//                } else if (KeyEvent.VK_A == e.getKeyCode() || KeyEvent.VK_LEFT == e.getKeyCode()) {
//                    x = heroAircraft.getLocationX() - 10;
//                } else if (KeyEvent.VK_D == e.getKeyCode() || KeyEvent.VK_RIGHT == e.getKeyCode()) {
//                    x = heroAircraft.getLocationX() + 10;
//                }
//                heroAircraft.setLocation(x, y);
//
//            }
//
//            @Override
//            public void keyReleased(KeyEvent e) {
//                super.keyReleased(e);
//            }
//        };
        game.addMouseListener(mouseAdapter);
        game.addMouseMotionListener(mouseAdapter);
//        game.addKeyListener(keyAdapter);

    }
}
