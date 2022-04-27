package edu.hitsz.gui;

import edu.hitsz.application.Config;
import edu.hitsz.record.Record;
import edu.hitsz.record.RecordDAOImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;

public class RankingPanel {
    private JPanel topPanel;
    private JTable rankingTable;
    private JScrollPane scrollPane;
    private JButton deleteButton;
    private RecordDAOImpl recordDAO;
    private DefaultTableModel model;

    public RankingPanel(int level) {
        recordDAO = new RecordDAOImpl(level);

        deleteButton.addActionListener(new ActionListener() {
            /**
             * Invoked when an action occurs.
             *
             * @param e the event to be processed
             */
            @Override
            public void actionPerformed(ActionEvent e) {
                int opt = JOptionPane.showConfirmDialog(null, "确认删除所选记录？");
                int row = rankingTable.getSelectedRow();
                System.out.println(row);
                if (row != -1 && opt == 0) {
                    model.removeRow(row);
                    recordDAO.deleteByRank(row + 1);
                    recordDAO.saveRecord();
                }
            }
        });
    }

    public JPanel getTopPanel() {
        return topPanel;
    }

    public void addRecord() {
        model = new DefaultTableModel(recordDAO.getHeader(), 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        String username = JOptionPane.showInputDialog(null, "您的得分为：" + Config.getScore() + "\n请输入您的姓名以添加记录：",
                "Game Over!", JOptionPane.QUESTION_MESSAGE);
        if (username != null) {
            try {
                Record record = new Record(username, Config.getScore(),Config.getGameLevel());
                recordDAO.addRecord(record);
                recordDAO.saveRecord();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
    }

    public void refreshTable() {
        model = new DefaultTableModel(recordDAO.getHeader(), 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        for (Record record : recordDAO.getAllRecords()) {
            Object[] objects = new Object[]{record.getRank(), record.getUsername(), record.getlevel(), record.getScore(), record.getDatetime().substring(5, 16)};
            model.addRow(objects);
        }
        rankingTable.setModel(model);
        scrollPane.setViewportView(rankingTable);
    }
}
