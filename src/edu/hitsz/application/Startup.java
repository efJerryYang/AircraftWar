package edu.hitsz.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static edu.hitsz.application.Main.WINDOW_HEIGHT;
import static edu.hitsz.application.Main.WINDOW_WIDTH;

public class Startup extends JFrame{
    private JPanel panel1;
    private JButton simpleButton;
    private JButton difficultButton;
    private JButton mediumButton;
    private JComboBox comboBox1;

    public Startup() {
        super("Aircraft War");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setBounds(((int) screenSize.getWidth() - WINDOW_WIDTH) / 2, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        simpleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Game game = new Game(1,true);
            }
        });
    }
}
