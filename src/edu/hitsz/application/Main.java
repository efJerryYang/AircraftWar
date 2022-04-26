package edu.hitsz.application;

import javax.swing.*;
import java.awt.*;

import static edu.hitsz.application.Main.WINDOW_HEIGHT;
import static edu.hitsz.application.Main.WINDOW_WIDTH;

/**
 * 程序入口
 *
 * @author JerryYang
 */
public class Main {

    public static final int WINDOW_WIDTH = 512;
    public static final int WINDOW_HEIGHT = 768;

    public static void main(String[] args) {
        System.out.println("Hello Aircraft War");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        GameFrame gameFrame = new GameFrame(screenSize);
        int gameLevel = 1;
        gameFrame.runGame(gameLevel);
    }
}

class GameFrame extends JFrame {
    public GameFrame(Dimension screenSize) {
        super("Aircraft War");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0,
                WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void runGame(int level) {
        Game game = new Game(level);
        add(game);
        game.action();
    }
}
