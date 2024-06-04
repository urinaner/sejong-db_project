package view;

import dao.MovieDAO;
import dto.Movie;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MovieFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private int selectedRow = -1;
    private int selectedMovieId;

    public MovieFrame() {
        setTitle("Movie List");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create a panel to hold the table and button
        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        // Create a table model with column names
        String[] columnNames = {"ID", "Title", "Running Time", "Rating", "Director", "Actors", "Genre", "Introduce", "Open Date", "Rating Score"};
        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.addMouseListener(new MouseAdapter() {
            //@Override
            public void mousePressed(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0 && row != selectedRow) {
                    selectedRow = row;
                    selectedMovieId = Integer.parseInt(model.getValueAt(row, 0).toString());
                }
            }
        });
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Create a button to select the movie
        JButton selectButton = new JButton("Select");
        selectButton.addActionListener(new ActionListener() {
            //@override
        	public void actionPerformed(ActionEvent e) {
                if (selectedRow >= 0) {
                    openTheaterFrame(selectedMovieId);
                }
            }
        });
        panel.add(selectButton, BorderLayout.SOUTH);

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
        TheaterFrame theaterFrame = new TheaterFrame(movieId);
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