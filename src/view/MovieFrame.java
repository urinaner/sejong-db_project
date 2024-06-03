package view;

import dao.MovieDAO;
import dao.TheaterDAO;
import entity.Movie;
import entity.Theater;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MovieFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private int selectedRow = -1;

    public MovieFrame() {
        setTitle("Movie List");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create a panel to hold the table
        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        // Create a table model with column names
        String[] columnNames = {"ID", "Title", "Running Time", "Rating", "Director", "Actors", "Genre", "Introduce", "Open Date", "Rating Score"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0 && row != selectedRow) {
                    selectedRow = row;
                    int movieId = Integer.parseInt(model.getValueAt(row, 0).toString());
                    openTheaterFrame(movieId);
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Populate table with movie data
        populateTable();

        setVisible(true);
    }

    // Method to populate the table with movie data
    private void populateTable() {
        // Get movie data from the database using MovieDAO
        MovieDAO movieDAO = MovieDAO.getInstance();
        List<Movie> movies = movieDAO.getAllMovies();

        // Add each movie to the table model
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
            model.addRow(rowData);
        }
    }

    // Method to open the theater frame for the selected movie
    private void openTheaterFrame(int movieId) {
        // Get theaters showing the selected movie from the database using TheaterDAO
        TheaterDAO theaterDAO = TheaterDAO.getInstance();
        List<Theater> theaters = theaterDAO.getTheatersByMovieId(movieId);

        // Create a new frame to display the theaters
        JFrame theaterFrame = new JFrame("Theaters");
        theaterFrame.setSize(400, 300);
        theaterFrame.setLocationRelativeTo(null);
        theaterFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create a panel to hold the theater list
        JPanel panel = new JPanel(new BorderLayout());
        theaterFrame.add(panel);

        // Create a table model with column names for theaters
        String[] columnNames = {"Theater ID", "Seat Count", "Available", "Seat Rows", "Seat Columns"};
        DefaultTableModel theaterModel = new DefaultTableModel(columnNames, 0);

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

        // Create a JTable with the theater table model
        JTable theaterTable = new JTable(theaterModel);
        JScrollPane scrollPane = new JScrollPane(theaterTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        theaterFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MovieFrame();
            }
        });
    }
}