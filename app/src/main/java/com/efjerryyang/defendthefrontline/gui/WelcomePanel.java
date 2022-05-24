//package edu.hitsz.gui;
//
//import edu.hitsz.application.Config;
//import edu.hitsz.application.Main;
//
//import javax.swing.*;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//public class WelcomePanel {
//    private JButton simpleButton;
//    private JButton mediumButton;
//    private JButton difficultButton;
//    private JComboBox audioCombo;
//    private JPanel topPanel;
//    private JLabel Audio;
//    private JPanel jp1;
//    private JPanel jp2;
//    private JPanel jp3;
//    private JPanel jp4;
//
//    public WelcomePanel() {
//
//        simpleButton.addActionListener(new ActionListener() {
//            /**
//             * Invoked when an action occurs.
//             *
//             * @param e the event to be processed
//             */
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                Config.setGameLevel(1);
//                synchronized (Main.class) {
//                    (Main.class).notify();
//                }
//            }
//        });
//        mediumButton.addActionListener(new ActionListener() {
//            /**
//             * Invoked when an action occurs.
//             *
//             * @param e the event to be processed
//             */
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                Config.setGameLevel(2);
//                synchronized (Main.class) {
//                    (Main.class).notify();
//                }
//            }
//        });
//        difficultButton.addActionListener(new ActionListener() {
//            /**
//             * Invoked when an action occurs.
//             *
//             * @param e the event to be processed
//             */
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                Config.setGameLevel(4);
//                synchronized (Main.class) {
//                    (Main.class).notify();
//                }
//            }
//        });
//        audioCombo.addActionListener(new ActionListener() {
//            /**
//             * Invoked when an action occurs.
//             *
//             * @param e the event to be processed
//             */
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String audioOnOff = (String) audioCombo.getItemAt(audioCombo.getSelectedIndex());
//                if("OFF".equals(audioOnOff)){
//                    Config.setEnableAudio(false);
//                }else if("ON".equals(audioOnOff)){
//                    Config.setEnableAudio(true);
//                }
//            }
//        });
//    }
//
//    public JPanel getWelcomePanel() {
//        return topPanel;
//    }
//}
