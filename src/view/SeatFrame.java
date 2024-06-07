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

        seatPanel = new JPanel();
        seatPanel.setLayout(new GridLayout(0, 10));
        add(seatPanel, BorderLayout.CENTER);

        populateSeatButtons(theaterId);
    }

    private void populateSeatButtons(int theaterId) {
        SeatDAO seatDAO = SeatDAO.getInstance();
        List<Seat> seats = seatDAO.getSeatsByTheaterId(theaterId);

        for (Seat seat : seats) {
            JButton seatButton = new JButton(seat.getSeatRow() + "-" + seat.getSeatCol());
            seatButton.setPreferredSize(new Dimension(40, 40));

            if (seat.isAvailable()) {
                seatButton.setBackground(Color.GREEN);
            } else {
                seatButton.setBackground(Color.RED);
            }

            seatPanel.add(seatButton);
        }
    }
}