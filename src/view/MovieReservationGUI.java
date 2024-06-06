package view;

import dao.MovieDAO;
import dao.ScheduleDAO;
import dao.SeatDAO;
import dao.TheaterDAO;
import dto.Movie;
import dto.Schedule;
import dto.Seat;
import dto.Theater;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MovieReservationGUI extends JFrame {
    private JTable movieTable, scheduleTable, seatTable;
    private DefaultTableModel movieModel, scheduleModel, seatModel;
    private JTextField titleField, directorField, actorsField, genreField;
    private int selectedMovieId = -1;
    private int selectedScheduleId = -1;
    private int selectedTheaterId = -1;

    public MovieReservationGUI() {
        setTitle("Movie Reservation System");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 3));

        // Movie panel
        JPanel moviePanel = createMoviePanel();

        // Schedule panel
        JPanel schedulePanel = createSchedulePanel();

        // Seat panel
        JPanel seatPanel = createSeatPanel();

        add(moviePanel);
        add(schedulePanel);
        add(seatPanel);
    }

    private JPanel createMoviePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Create table model and table
        String[] columnNames = {"ID", "Title", "Running Time", "Rating", "Director", "Actors", "Genre", "Introduce", "Open Date", "Rating Score"};
        movieModel = new DefaultTableModel(columnNames, 0);
        movieTable = new JTable(movieModel);
        movieTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        movieTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = movieTable.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    selectedMovieId = Integer.parseInt(movieModel.getValueAt(row, 0).toString());
                    populateScheduleTable(selectedMovieId);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(movieTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Add search panel
        panel.add(createSearchPanel(), BorderLayout.NORTH);

        return panel;
    }

    private JPanel createSchedulePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Create table model and table
        String[] columnNames = {"ID", "Movie ID", "Theater ID", "Start Date", "Day of Week", "Show Number", "Start Time"};
        scheduleModel = new DefaultTableModel(columnNames, 0);
        scheduleTable = new JTable(scheduleModel);
        scheduleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        scheduleTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = scheduleTable.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    selectedScheduleId = Integer.parseInt(scheduleModel.getValueAt(row, 0).toString());
                    int theaterId = Integer.parseInt(scheduleModel.getValueAt(row, 2).toString());
                    populateSeatTable(theaterId);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSeatPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Create table model and table
        String[] columnNames = {"Seat ID", "Theater ID", "Available", "Row", "Column"};
        seatModel = new DefaultTableModel(columnNames, 0);
        seatTable = new JTable(seatModel);

        JScrollPane scrollPane = new JScrollPane(seatTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Add reserve button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton reserveButton = new JButton("Reserve");
        reserveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle reservation process
                int selectedRow = seatTable.getSelectedRow();
                if (selectedRow >= 0) {
                    int seatId = (int) seatModel.getValueAt(selectedRow, 0);
                    int theaterId = (int) seatModel.getValueAt(selectedRow, 1);
                    boolean isAvailable = (boolean) seatModel.getValueAt(selectedRow, 2);
                    int seatRow = (int) seatModel.getValueAt(selectedRow, 3);
                    int seatCol = (int) seatModel.getValueAt(selectedRow, 4);

                    // Perform reservation logic here
                    // For example, you can use the selected seat information to book the seat

                    JOptionPane.showMessageDialog(null, "Seat " + seatId + " in theater " + theaterId + " has been reserved.");
                } else {
                    JOptionPane.showMessageDialog(null, "Please select a seat to reserve.");
                }
            }
        });
        buttonPanel.add(reserveButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Title label and text field
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        searchPanel.add(new JLabel("Title:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        titleField = new JTextField(15);
        searchPanel.add(titleField, gbc);

        // Director label and text field
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        searchPanel.add(new JLabel("Director:"), gbc);

        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        directorField = new JTextField(15);
        searchPanel.add(directorField, gbc);

        // Actors label and text field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        searchPanel.add(new JLabel("Actors:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        actorsField = new JTextField(15);
        searchPanel.add(actorsField, gbc);

        // Genre label and text field
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        searchPanel.add(new JLabel("Genre:"), gbc);

        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        genreField = new JTextField(15);
        searchPanel.add(genreField, gbc);

        // Search button
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchMovies();
            }
        });
        searchPanel.add(searchButton, gbc);

        return searchPanel;
    }

    private void populateMovieTable() {
        MovieDAO movieDAO = MovieDAO.getInstance();
        List<Movie> movies = movieDAO.getAllMovies();

        movieModel.setRowCount(0);
        for (Movie movie : movies) {
            Object[] rowData = {
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getRunningTime(),
                    movie.getRating(),
                    movie.getDirector(),
                    movie.getActors(),
                    movie.getGenre(),
                    movie.getIntroduce(),
                    movie.getOpenDate(),
                    movie.getRatingScore()
            };
            movieModel.addRow(rowData);
        }
    }

    private void populateScheduleTable(int movieId) {
        ScheduleDAO scheduleDAO = ScheduleDAO.getInstance();
        List<Schedule> schedules = scheduleDAO.getSchedulesByMovieId(movieId);

        scheduleModel.setRowCount(0);
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

    private void populateSeatTable(int theaterId) {
        SeatDAO seatDAO = SeatDAO.getInstance();
        List<Seat> seats = seatDAO.getSeatsByTheaterId(theaterId);

        seatModel.setRowCount(0);
        for (Seat seat : seats) {
            Object[] rowData = {
                    seat.getSeatId(),
                    seat.getTheaterId(),
                    seat.isAvailable(), // 수정된 부분
                    seat.getSeatRow(),
                    seat.getSeatCol()
            };
            seatModel.addRow(rowData);
        }
    }

    private void searchMovies() {
        String title = titleField.getText();
        String director = directorField.getText();
        String actors = actorsField.getText();
        String genre = genreField.getText();

        MovieDAO movieDAO = MovieDAO.getInstance();
        List<Movie> movies = movieDAO.searchMovies(title, director, actors, genre);

        movieModel.setRowCount(0);
        for (Movie movie : movies) {
            Object[] rowData = {
                    movie.getMovieId(),
                    movie.getTitle(),
                    movie.getRunningTime(),
                    movie.getRating(),
                    movie.getDirector(),
                    movie.getActors(),
                    movie.getGenre(),
                    movie.getIntroduce(),
                    movie.getOpenDate(),
                    movie.getRatingScore()
            };
            movieModel.addRow(rowData);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MovieReservationGUI().setVisible(true);
            }
        });
    }
}