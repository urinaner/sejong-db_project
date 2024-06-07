package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import dao.ScheduleDAO;
import dto.Schedule;

public class ScheduleFrame extends JFrame {
    private JTable scheduleTable;
    private DefaultTableModel scheduleModel;
    private int selectedRow = -1;
    private int selectedScheduleId;

    public ScheduleFrame(int movieId) {
        setTitle("Schedules");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        String[] columnNames = {"Schedule ID", "Movie ID", "Theater ID", "Start Date", "Day of Week", "Show Number", "Start Time"};
        scheduleModel = new DefaultTableModel(columnNames, 0);
        scheduleTable = new JTable(scheduleModel);
        scheduleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scheduleTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = scheduleTable.rowAtPoint(e.getPoint());
                if (row >= 0 && row != selectedRow) {
                    selectedRow = row;
                    selectedScheduleId = Integer.parseInt(scheduleModel.getValueAt(row, 0).toString());
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton selectButton = new JButton("Select");
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedRow >= 0) {
                    openTheaterFrame(selectedScheduleId);
                }
            }
        });
        panel.add(selectButton, BorderLayout.SOUTH);

        populateScheduleTable(movieId);
    }

    private void populateScheduleTable(int movieId) {
        ScheduleDAO scheduleDAO = ScheduleDAO.getInstance();
        List<Schedule> schedules = scheduleDAO.getSchedulesByMovieId(movieId);

        for (Schedule schedule : schedules) {
            Object[] rowData = {
                    schedule.getScheduleId(),
                    schedule.getMovieId(),
                    schedule.getTheaterId(),
                    schedule.getStartDate(),
                    schedule.getDayOfWeek(),
                    schedule.getShowNumber(),
                    schedule.getStartTime()
            };
            scheduleModel.addRow(rowData);
        }
    }

    private void openTheaterFrame(int scheduleId) {
        TheaterFrame theaterFrame = new TheaterFrame(scheduleId);
        theaterFrame.setVisible(true);
    }
}