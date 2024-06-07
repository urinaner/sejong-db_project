package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import dao.TheaterDAO;
import dto.Theater;

public class TheaterFrame extends JFrame {
    private JTable theaterTable;
    private DefaultTableModel theaterModel;
    private int selectedRow = -1;
    private int selectedTheaterId;

    public TheaterFrame(int scheduleId) {
        setTitle("Theaters");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        String[] columnNames = {"Theater ID", "Seat Count", "Available", "Seat Rows", "Seat Columns"};
        theaterModel = new DefaultTableModel(columnNames, 0);
        theaterTable = new JTable(theaterModel);
        theaterTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        theaterTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = theaterTable.rowAtPoint(e.getPoint());
                if (row >= 0 && row != selectedRow) {
                    selectedRow = row;
                    selectedTheaterId = Integer.parseInt(theaterModel.getValueAt(row, 0).toString());
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(theaterTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton selectButton = new JButton("Select");
        selectButton.addActionListener(new ActionListener() {
            //@Override
            public void actionPerformed(ActionEvent e) {
                if (selectedRow >= 0) {
                    openSeatFrame(selectedTheaterId);
                }
            }
        });
        panel.add(selectButton, BorderLayout.SOUTH);

        populateTheaterTable(scheduleId);
    }

    private void populateTheaterTable(int scheduleId) {
        TheaterDAO theaterDAO = TheaterDAO.getInstance();
        Theater theater = theaterDAO.getTheaterByScheduleId(scheduleId);

        Object[] rowData = {
                theater.getTheaterId(),
                theater.getSeatCount(),
                theater.isAvailable(),
                theater.getSeatRows(),
                theater.getSeatColumns()
        };
        theaterModel.addRow(rowData);
    }

    private void openSeatFrame(int theaterId) {
        SeatFrame seatFrame = new SeatFrame(theaterId);
        seatFrame.setVisible(true);
    }
}