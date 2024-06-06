package view;

import util.DB_Connect;
import util.Init;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class ManagerFrame extends JFrame {

    private JTable table;
    private JScrollPane scrollPane;
    private JPanel panel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ManagerFrame databaseViewer = new ManagerFrame();
                databaseViewer.setVisible(true);
            }
        });
    }

    public ManagerFrame() {
        setTitle("Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 450);
        setLocationRelativeTo(null);

        panel = new JPanel();
        
        JButton initTableButton = new JButton("Init table");
        initTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Init.run();
            }
        });

        JButton showTableButton = new JButton("Show All Tables");
        showTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showTable();
            }
        });

        JButton addDeleteUpdateButton = new JButton("Insert/Delete/Update");
        addDeleteUpdateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ADU();
            }
        });

        panel.add(initTableButton);
        panel.add(showTableButton);
        panel.add(addDeleteUpdateButton);
        add(panel, BorderLayout.NORTH);

        table = new JTable();
        scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void showTable() {
        clearContent();
        
        try {
            Connection conn = DB_Connect.getConnection();
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
    
    private void showTableMoviesData(String tableName) {
        try {
            Connection conn = DB_Connect.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName);

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
    
    private void ADU() {
        clearContent();
        
        try {
            Connection conn = DB_Connect.getConnection();
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
            buttonPanel.setLayout(new GridLayout(0, 3));

            for (int i = 0; i < model.getRowCount(); i++) {
                final String tableName = (String) model.getValueAt(i, 0);

                JButton addButton = new JButton(tableName + " Insert");
                addButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                    	insert(tableName);
                    }
                });

                JButton deleteButton = new JButton(tableName + " Delete");
                deleteButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        delete(tableName);
                    }
                });

                JButton updateButton = new JButton(tableName + " Update");
                updateButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        update(tableName);
                    }
                });

                buttonPanel.add(addButton);
                buttonPanel.add(deleteButton);
                buttonPanel.add(updateButton);
            }

            add(buttonPanel, BorderLayout.SOUTH);
            revalidate();
            repaint();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void clearContent() {
        Container contentPane = getContentPane();
        Component[] components = contentPane.getComponents();
        for (Component component : components) {
            if (component != panel && component != scrollPane) {
                contentPane.remove(component);
            }
        }
    }
    
    private void insert(String tableName) {
        try {
            Connection conn = DB_Connect.getConnection();
            Statement stmt = conn.createStatement();

            JPanel panel = new JPanel(new GridLayout(0, 2));
            JTextField queryField = new JTextField(10);

            panel.add(new JLabel("Input values for " + tableName + " (e.g., (2, 'value1', 'value2', 'value3') ) :"));
            panel.add(queryField);

            int result = JOptionPane.showConfirmDialog(null, panel, "Insert Values", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String values = queryField.getText().trim();
                if (values.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Values cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    String query = "INSERT INTO " + tableName + " VALUES " + values;
                    stmt.executeUpdate(query);
                    JOptionPane.showMessageDialog(null, "Query executed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }

            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error executing query: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void delete(String tableName) {
        String afterwhere = JOptionPane.showInputDialog("Enter the after WHERE:");

        if (afterwhere!= null && !afterwhere.isEmpty()) {
            try {
                Connection conn = DB_Connect.getConnection();
                Statement stmt = conn.createStatement();

                String query = "DELETE FROM " + tableName + " WHERE " + afterwhere;

                stmt.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Query executed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

                stmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error executing query: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private void update(String tableName) {
    	String afterwhere = JOptionPane.showInputDialog("Enter set to where:");

        if (afterwhere!= null && !afterwhere.isEmpty()) {
            try {
                Connection conn = DB_Connect.getConnection();
                Statement stmt = conn.createStatement();

                String query = "UPDATE " + tableName + " "+afterwhere;
                System.out.println(query);
                
                stmt.executeUpdate(query);
                JOptionPane.showMessageDialog(null, "Query executed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

                stmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error executing query: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
