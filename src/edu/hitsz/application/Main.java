package edu.hitsz.application;

import edu.hitsz.game.*;
import edu.hitsz.gui.RankingPanel;
import edu.hitsz.gui.WelcomePanel;

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
    public static GameFrame gameFrame;

    public static void main(String[] args) {
        System.out.println("Hello Aircraft War");
        gameFrame = new GameFrame();
        WelcomePanel welcomePanel = new WelcomePanel();
        gameFrame.setContentPane(welcomePanel.getWelcomePanel());
        gameFrame.setVisible(true);
        synchronized (Main.class) {
            try {
                (Main.class).wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Config.getGameLevel());
        AbstractGame game;
        if (Config.getGameLevel() == 1) {
            game = new SimpleGame(Config.getGameLevel(), Config.getEnableAudio());
        } else if (Config.getGameLevel() == 2) {
            game = new MediumGame(Config.getGameLevel(), Config.getEnableAudio());
        } else if (Config.getGameLevel() == 4) {
            game = new DifficultGame(Config.getGameLevel(), Config.getEnableAudio());
        } else {
            game = new Game(Config.getGameLevel(), Config.getEnableAudio());
        }
        gameFrame.remove(welcomePanel.getWelcomePanel());
        gameFrame.setContentPane(game);
        gameFrame.setVisible(true);
        game.action();

        synchronized (Main.class) {
            try {
                (Main.class).wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        gameFrame.remove(game);
        RankingPanel rankingPanel = new RankingPanel(Config.getGameLevel());
        gameFrame.setContentPane(rankingPanel.getTopPanel());

        rankingPanel.refreshTable();
        gameFrame.setVisible(true);
        rankingPanel.addRecord();
        rankingPanel.refreshTable();
        gameFrame.setVisible(true);

    }
}

class GameFrame extends JFrame {

    public GameFrame() {
        super("Aircraft War");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
