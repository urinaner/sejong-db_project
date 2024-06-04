package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import dao.TheaterDAO;
import dto.Theater;

public class TheaterFrame extends JFrame {
    private JTable theaterTable;
    private DefaultTableModel theaterModel;

    public TheaterFrame(int movieId) {
        setTitle("Theaters");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create a panel to hold the theater list and button
        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        // Create a table model with column names for theaters
        String[] columnNames = {"Theater ID", "Seat Count", "Available", "Seat Rows", "Seat Columns"};
        theaterModel = new DefaultTableModel(columnNames, 0);
        theaterTable = new JTable(theaterModel);
        JScrollPane scrollPane = new JScrollPane(theaterTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create a button to select the theater
        JButton selectButton = new JButton("Select");
        selectButton.addActionListener(new ActionListener() {
            //@Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = theaterTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int theaterId = Integer.parseInt(theaterModel.getValueAt(selectedRow, 0).toString());
                    // TODO: Open the seat selection frame for the selected theater
                    System.out.println("Selected Theater ID: " + theaterId);
                }
            }
        });
        panel.add(selectButton, BorderLayout.SOUTH);

        populateTheaterTable(movieId);
    }

    private void populateTheaterTable(int movieId) {
        // Get theaters showing the selected movie from the database using TheaterDAO
        TheaterDAO theaterDAO = TheaterDAO.getInstance();
        List<Theater> theaters = theaterDAO.getTheatersByMovieId(movieId);

        // Add each theater to the table model
        for (Theater theater : theaters) {
            Object[] rowData = {
                    theater.getTheaterId(),
                    theater.getSeatCount(),
                    theater.isAvailable(),
                    theater.getSeatRows(),
                    theater.getSeatColumns()
            };
            theaterModel.addRow(rowData);
        }
    }
}