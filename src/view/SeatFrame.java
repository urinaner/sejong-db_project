package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import dao.SeatDAO;
import dto.Seat;

public class SeatFrame extends JFrame {
    private JPanel seatPanel;

    public SeatFrame(int theaterId) {
        setTitle("Seat Selection");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create a panel to hold the seat buttons
        seatPanel = new JPanel();
        seatPanel.setLayout(new GridLayout(0, 10)); // Adjust the number of columns as needed
        add(seatPanel, BorderLayout.CENTER);

        populateSeatButtons(theaterId);
    }

    private void populateSeatButtons(int theaterId) {
        // Get seats for the selected theater from the database using SeatDAO
        SeatDAO seatDAO = SeatDAO.getInstance();
        List<Seat> seats = seatDAO.getSeatsByTheaterId(theaterId);

        // Create seat buttons and add them to the seat panel
        for (Seat seat : seats) {
            JButton seatButton = new JButton(seat.getSeatRow() + "-" + seat.getSeatCol());
            seatButton.setPreferredSize(new Dimension(40, 40)); // Adjust the size of the seat buttons as needed

            if (seat.isAvailable()) {
                seatButton.setBackground(Color.GREEN);
            } else {
                seatButton.setBackground(Color.RED);
            }

            seatPanel.add(seatButton);
        }
    }
}