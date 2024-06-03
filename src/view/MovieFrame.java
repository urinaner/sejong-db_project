package view;

import dao.MovieDAO;
import entity.Movie;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MovieFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MovieFrame();
            }
        });
    }
}
