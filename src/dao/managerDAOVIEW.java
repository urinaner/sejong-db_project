package dao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class managerDAOVIEW extends JFrame {

    private JTable table;
    private JScrollPane scrollPane;
    private JPanel panel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                managerDAOVIEW databaseViewer = new managerDAOVIEW();
                databaseViewer.setVisible(true);
            }
        });
    }

    public managerDAOVIEW() {
        setTitle("Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 400);
        setLocationRelativeTo(null);

        panel = new JPanel();

        JButton showTableButton = new JButton("Show All Tables");
        showTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTable();
            }
        });

        JButton addMovieButton = new JButton("Add Movie");
        addMovieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMovieData();
            }
        });

        JButton addScheduleButton = new JButton("Add Schedule");
        addScheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addScheduleData();
            }
        });

        JButton addTheaterButton = new JButton("Add Theater");
        addTheaterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addTheaterData();
            }
        });

        JButton addSeatsButton = new JButton("Add Seats");
        addSeatsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addSeatsData();
            }
        });

        panel.add(showTableButton);
        panel.add(addMovieButton);
        panel.add(addScheduleButton);
        panel.add(addTheaterButton);
        panel.add(addSeatsButton);

        add(panel, BorderLayout.NORTH);

        // Initialize table
        table = new JTable();
        scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void showTable() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db1?serverTimezone=UTC", "root", "1234");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SHOW TABLES");

            DefaultTableModel model = new DefaultTableModel();
            model.addColumn("Table Name");

            while (rs.next()) {
                model.addRow(new Object[]{rs.getString(1)});
            }

            table.setModel(model);

            rs.close();
            stmt.close();
            conn.close();

            JPanel buttonPanel = new JPanel();
            for (int i = 0; i < model.getRowCount(); i++) {
                final String tableName = (String) model.getValueAt(i, 0);
                JButton tableButton = new JButton(tableName + " Table");
                tableButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        showTableMoviesData(tableName);
                    }
                });
                buttonPanel.add(tableButton);
            }

            add(buttonPanel, BorderLayout.SOUTH);
            revalidate();
            repaint();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addMovieData() {
    }

    private void addScheduleData() {
    }

    private void addTheaterData() {
    }

    private void addSeatsData() {
    }

    private void showTableMoviesData(String tableName) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db1?serverTimezone=UTC", "root", "1234");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);

            // Populate table model with data from ResultSet
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            DefaultTableModel model = new DefaultTableModel();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                model.addColumn(metaData.getColumnLabel(columnIndex));
            }

            while (rs.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    rowData[i] = rs.getObject(i + 1);
                }
                model.addRow(rowData);
            }

            table.setModel(model);

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
