package edu.hitsz.application;

import edu.hitsz.record.Record;
import edu.hitsz.record.RecordDAOImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import java.util.Vector;

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
        int gameLevel = 1; // gameLevel [1] [2 3] [4 5]
        boolean enableAudio = true;  // Todo: add other audio needed
        gameFrame.showWelcome();
//        RankingFrame rankingFrame = new RankingFrame();
    }
}

class GameFrame extends JFrame {
    private final RecordDAOImpl recordDAOImpl;
    private int gameLevel;
    private boolean gameAudio = true;
    private JPanel startup;
    private Game game;
    private JPanel ranking;

    public GameFrame() {
        super("Aircraft War");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        recordDAOImpl = new RecordDAOImpl();
    }

    public void showWelcome() {
        var simpleButton = new JButton("SIMPLE");
        var mediumButton = new JButton("MEDIUM");
        var hardButton = new JButton("HARD");

        var audioCombo = new JComboBox<String>();
        audioCombo.addItem("是");
        audioCombo.addItem("否");
        audioCombo.addActionListener(e ->
                audioCombo.getItemAt(audioCombo.getSelectedIndex())
        );  // Todo:
        startup = new JPanel();
        startup.add(simpleButton);
        startup.add(mediumButton);
        startup.add(hardButton);
        startup.add(audioCombo);
        startup.setLayout(new FlowLayout(FlowLayout.CENTER, 2000, 100));
        add(startup);
        var simpleAction = new LevelController(1);
        var mediumAction = new LevelController(2);
        var hardAction = new LevelController(4);
        simpleButton.addActionListener(simpleAction);
        mediumButton.addActionListener(mediumAction);
        hardButton.addActionListener(hardAction);

        setVisible(true);
    }

    public void runGame(int level, boolean enableAudio) {
        remove(startup);
//        removeAll();
//        revalidate();
//        repaint();
        game = new Game(level, enableAudio);
        add(game);
        setVisible(true);
        game.action();
//        if (game.isGameOverFlag()) {
//            showRanking();
//        }
    }

    public void showRanking() {
        Vector<Vector<Object>> ls2d = new Vector<>();
        Vector<Object> x = new Vector<>();
//        removeAll();
        remove(game);
        ranking = new JPanel();
        JTable table = new JTable();

        JScrollPane scrollPane = new JScrollPane();
        DefaultTableModel model = new DefaultTableModel(recordDAOImpl.getHeader(), 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        ranking.add(table.getTableHeader());
        ranking.add(scrollPane.getVerticalScrollBar());
        model.addRow(recordDAOImpl.getHeader());
        for (Record record : recordDAOImpl.getAllRecords()) {
            Object[] objects = new Object[]{record.getRank(), record.getUsername(), record.getlevel(), record.getScore(), record.getDatetime().substring(5, 16)};
            model.addRow(objects);
        }
        table.setModel(model);
        scrollPane.setViewportView(table);
        ranking.add(table);
        ranking.add(scrollPane);
        add(ranking);
        setVisible(true);
        try {
            Record record = new Record("Anonymous", Config.getScore());
            recordDAOImpl.addRecord(record);
            recordDAOImpl.saveRecord();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private class AudioController implements ActionListener {
        private boolean enable;

        public AudioController(boolean enableAudio) {
            this.enable = enableAudio;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // Todo:
        }
    }

    private class LevelController implements ActionListener {
        private int level;

        public LevelController(int level) {
            this.level = level;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Config.setGameLevel(level);
            runGame(Config.getGameLevel(), Config.isEnableAudio());
        }
    }
}


class RankingFrame extends JFrame {
    public RankingFrame() {
        super("Aircraft War");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        showRanking();
//        inputUsername();
        var dialog = new InputDialog(this);
//        dialog.setVisible(true);
        if (dialog == null) // first time
            dialog = new InputDialog(this);
        dialog.setVisible(true);
    }

    public void showRanking() {

    }

}

class InputDialog extends JDialog {
    public InputDialog(JFrame owner) {
        super(owner, "about dialog", true);
        add(new JLabel("hello world"), BorderLayout.CENTER);
        var panel = new JPanel();
        var ok = new JButton("OK");

        ok.addActionListener(event -> setVisible(false));
        panel.add(ok);
        add(panel, BorderLayout.SOUTH);
        setSize(250, 150);
        JOptionPane.showInputDialog(this, "Title", JOptionPane.QUESTION_MESSAGE);

    }
}

class MyComponent extends JComponent {
    @Override
    public void paintComponent(Graphics g) {
        // code for drawing
    }
}
