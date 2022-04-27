package edu.hitsz.application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.Date;

/**
 * A frame that contains settings for selecting various option dialogs.
 */
public class OptionDialogFrame extends JFrame {
    private ButtonPanel typePanel;
    private ButtonPanel messagePanel;
    private ButtonPanel messageTypePanel;
    private ButtonPanel optionTypePanel;
    private ButtonPanel optionsPanel;
    private ButtonPanel inputPanel;
    private String messageString = "Message";
    private Icon messageIcon = new ImageIcon("blue-ball.gif");
    private Object messageObject = new Date();
    private Component messageComponent = new SampleComponent();

    public OptionDialogFrame() {
        var gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(2, 3));
//
        typePanel = new ButtonPanel("Type", "Message", "Confirm", "Option", "Input");
        messageTypePanel = new ButtonPanel("Message Type", "ERROR_MESSAGE", "INFORMATION_MESSAGE",
                "WARNING_MESSAGE", "QUESTION_MESSAGE", "PLAIN_MESSAGE");
        messagePanel = new ButtonPanel("Message", "String", "Icon", "Component", "Other",
                "Object[]");
        optionTypePanel = new ButtonPanel("Confirm", "DEFAULT_OPTION", "YES_NO_OPTION",
                "YES_NO_CANCEL_OPTION", "OK_CANCEL_OPTION");
//        optionsPanel = new ButtonPanel("Option", "String[]", "Icon[]", "Object[]");
        inputPanel = new ButtonPanel("Input", "Text field", "Combo box");

        gridPanel.add(typePanel);
        gridPanel.add(messageTypePanel);
        gridPanel.add(messagePanel);
        gridPanel.add(optionTypePanel);
//        gridPanel.add(optionsPanel);
        gridPanel.add(inputPanel);

        // add a panel with a Show button

        var showPanel = new JPanel();
        var showButton = new JButton("Show");
        showButton.addActionListener(new ShowAction());
        showPanel.add(showButton);

        add(gridPanel, BorderLayout.CENTER);
        add(showPanel, BorderLayout.SOUTH);
        pack();
    }

    /**
     * Gets the currently selected message.
     *
     * @return a string, icon, component, or object array, depending on the Message panel selection
     */
    public Object getMessage() {
        String s = messagePanel.getSelection();
        if (s.equals("String")) return messageString;
        else if (s.equals("Icon")) return messageIcon;
        else if (s.equals("Component")) return messageComponent;
        else if (s.equals("Object[]")) return new Object[]{messageString, messageIcon,
                messageComponent, messageObject};
        else if (s.equals("Other")) return messageObject;
        else return null;
    }

    /**
     * Gets the currently selected options.
     *
     * @return an array of strings, icons, or objects, depending on the Option panel selection
     */
    public Object[] getOptions() {
        String s = optionsPanel.getSelection();
        if (s.equals("String[]")) return new String[]{"Yellow", "Blue", "Red"};
        else if (s.equals("Icon[]")) return new Icon[]{new ImageIcon("yellow-ball.gif"),
                new ImageIcon("blue-ball.gif"), new ImageIcon("red-ball.gif")};
        else if (s.equals("Object[]")) return new Object[]{messageString, messageIcon,
                messageComponent, messageObject};
        else return null;
    }

    private class ShowAction implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            JOptionPane.showInputDialog(
                    OptionDialogFrame.this, getMessage(), "Title", JOptionPane.QUESTION_MESSAGE);
        }
    }
}

/**
 * A component with a painted surface
 */

class SampleComponent extends JComponent {
    public void paintComponent(Graphics g) {
        var g2 = (Graphics2D) g;
        var rect = new Rectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1);
        g2.setPaint(Color.YELLOW);
        g2.fill(rect);
        g2.setPaint(Color.BLUE);
        g2.draw(rect);
    }

    public Dimension getPreferredSize() {
        return new Dimension(10, 10);
    }
}
