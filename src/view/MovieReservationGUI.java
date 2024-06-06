package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

import dao.*;
import dto.*;
import util.DB_Connect;

public class MovieReservationGUI extends JFrame {
    private JPanel mainPanel;
    private JComboBox<String> movieComboBox;
    private JComboBox<String> theaterComboBox;
    private JComboBox<String> scheduleComboBox;
    private JComboBox<String> seatComboBox;
    private JButton reserveButton;

    public MovieReservationGUI() {
        initializeUI();
        populateMovieComboBox();
    }

    private void initializeUI() {
        setTitle("Movie Reservation");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(5, 2, 10, 10));

        JLabel movieLabel = new JLabel("Select Movie:");
        mainPanel.add(movieLabel);

        movieComboBox = new JComboBox<>();
        mainPanel.add(movieComboBox);

        JLabel theaterLabel = new JLabel("Select Theater:");
        mainPanel.add(theaterLabel);

        theaterComboBox = new JComboBox<>();
        mainPanel.add(theaterComboBox);

        JLabel scheduleLabel = new JLabel("Select Schedule:");
        mainPanel.add(scheduleLabel);

        scheduleComboBox = new JComboBox<>();
        mainPanel.add(scheduleComboBox);

        JLabel seatLabel = new JLabel("Select Seat:");
        mainPanel.add(seatLabel);

        seatComboBox = new JComboBox<>();
        mainPanel.add(seatComboBox);

        reserveButton = new JButton("Reserve");
        reserveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                reserveMovie();
            }
        });
        mainPanel.add(reserveButton);

        add(mainPanel);
        setLocationRelativeTo(null);
    }

    private void populateMovieComboBox() {
        List<Movie> movies = MovieDAO.getInstance().getAllMovies();
        for (Movie movie : movies) {
            movieComboBox.addItem(movie.getTitle());
        }
    }

    private void populateTheaterComboBox(int movieId) {
        List<Schedule> schedules = ScheduleDAO.getInstance().getSchedulesByMovieId(movieId);
        for (Schedule schedule : schedules) {
            Theater theater = TheaterDAO.getInstance().getTheaterByScheduleId(schedule.getScheduleId());
            theaterComboBox.addItem("Theater " + theater.getTheaterId());
        }
    }

    private void populateScheduleComboBox(int movieId, int theaterId) {
        List<Schedule> schedules = ScheduleDAO.getInstance().getSchedulesByMovieIdAndTheaterId(movieId, theaterId);
        for (Schedule schedule : schedules) {
            scheduleComboBox.addItem(schedule.getStartTime());
        }
    }

    private void populateSeatComboBox(int theaterId) {
        List<Seat> seats = SeatDAO.getInstance().getSeatsByTheaterId(theaterId);
        for (Seat seat : seats) {
            seatComboBox.addItem("Row: " + seat.getSeatRow() + ", Col: " + seat.getSeatCol());
        }
    }

    private void reserveMovie() {
        // Get selected movie, theater, schedule, and seat
        String selectedMovie = (String) movieComboBox.getSelectedItem();
        String selectedTheater = (String) theaterComboBox.getSelectedItem();
        String selectedSchedule = (String) scheduleComboBox.getSelectedItem();
        String selectedSeat = (String) seatComboBox.getSelectedItem();

        // You can proceed with the reservation logic here
    }

    public static void main(String[] args) throws SQLException {
        // Ensure database connection
        DB_Connect.getConnection();

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MovieReservationGUI().setVisible(true);
            }
        });
    }
}
