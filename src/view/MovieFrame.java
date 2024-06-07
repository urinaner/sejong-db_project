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
    private JTextField titleField, directorField, actorsField, genreField;
    private int selectedRow = -1;
    private int selectedMovieId;

    public MovieFrame() {
        setTitle("Movie List");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        add(panel);

        JPanel searchPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        searchPanel.add(new JLabel("Title:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        titleField = new JTextField(15);
        searchPanel.add(titleField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        searchPanel.add(new JLabel("Director:"), gbc);

        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        directorField = new JTextField(15);
        searchPanel.add(directorField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        searchPanel.add(new JLabel("Actors:"), gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        actorsField = new JTextField(15);
        searchPanel.add(actorsField, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        searchPanel.add(new JLabel("Genre:"), gbc);

        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        genreField = new JTextField(15);
        searchPanel.add(genreField, gbc);

        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            //@Override
            public void actionPerformed(ActionEvent e) {
                searchMovies();
            }
        });
        searchPanel.add(searchButton, gbc);

        panel.add(searchPanel, BorderLayout.NORTH);

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

        JButton selectButton = new JButton("Select");
        selectButton.addActionListener(new ActionListener() {
            //@Override
            public void actionPerformed(ActionEvent e) {
                if (selectedRow >= 0) {
                    openScheduleFrame(selectedMovieId);
                }
            }
        });
        panel.add(selectButton, BorderLayout.SOUTH);

        populateTable();

        setVisible(true);
    }

    private void populateTable() {
        MovieDAO movieDAO = MovieDAO.getInstance();
        List<Movie> movies = movieDAO.getAllMovies();

        model.setRowCount(0); // Clear existing data
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

    private void searchMovies() {
        String title = titleField.getText();
        String director = directorField.getText();
        String actors = actorsField.getText();
        String genre = genreField.getText();

        MovieDAO movieDAO = MovieDAO.getInstance();
        List<Movie> movies = movieDAO.searchMovies(title, director, actors, genre);

        model.setRowCount(0); // Clear existing data
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

    private void openScheduleFrame(int movieId) {
        ScheduleFrame scheduleFrame = new ScheduleFrame(movieId);
        scheduleFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MovieFrame();
            }
        });
    }
}
