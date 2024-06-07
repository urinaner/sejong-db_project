package view;
import util.DB_Connect;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieReservationGUI extends JFrame {
    private JTable movieTable, scheduleTable, reservationTable, ticketTable ;
    private DefaultTableModel movieModel, scheduleModel, reservationModel ;
    private static DefaultTableModel ticketModel;
    private JTextField titleField, directorField, actorsField, genreField;
    private int selectedMovieId = -1;
    private int selectedScheduleId = -1;
    private int selectedTheaterId = -1;
    private JPanel seatPanel;
    private JButton reserveButton;
    private Seat selectedSeat = null;

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
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        searchPanel.add(new JLabel("Director:"), gbc);

        gbc.gridx = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        directorField = new JTextField(15);
        searchPanel.add(directorField, gbc);

        // Actors label and text field
        gbc.gridx = 4;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        searchPanel.add(new JLabel("Actors:"), gbc);

        gbc.gridx = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        actorsField = new JTextField(15);
        searchPanel.add(actorsField, gbc);

        // Genre label and text field
        gbc.gridx = 6;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.fill = GridBagConstraints.NONE;
        searchPanel.add(new JLabel("Genre:"), gbc);

        gbc.gridx = 7;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        genreField = new JTextField(15);
        searchPanel.add(genreField, gbc);

        // Search button
        gbc.gridx = 8;
        gbc.gridy = 0;
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

    public MovieReservationGUI() {
        setTitle("Movie Reservation System");
        setSize(2000, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create panel for movie and schedule
        JPanel moviePanelandschedulePanel = new JPanel(new GridLayout(2, 1));
        JPanel moviePanel = createMoviePanel();
        JPanel schedulePanel = createSchedulePanel();
        moviePanelandschedulePanel.add(moviePanel);
        moviePanelandschedulePanel.add(schedulePanel);

        // Create seat panel
        JPanel seatPanel = createSeatPanel();

        // Create panel for ticket and reservation
        JPanel ticketAndReservePanel = new JPanel(new GridLayout(2, 1));
        JPanel ticketPanel = createTicketPanel();
        JPanel reservePanel = createReservationPanel();
        ticketAndReservePanel.add(ticketPanel);
        ticketAndReservePanel.add(reservePanel);


        // Create search panel
        JPanel searchPanel = createSearchPanel();

        // Add panels to mainPanel
        mainPanel.add(searchPanel, BorderLayout.NORTH); // Search panel at the top
        JPanel contentPanel = new JPanel(new GridLayout(1, 3));
        contentPanel.add(moviePanelandschedulePanel);
        contentPanel.add(seatPanel);
        contentPanel.add(ticketAndReservePanel);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Set mainPanel as the content pane
        setContentPane(mainPanel);

        populateMovieTable();
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
                    selectedTheaterId = Integer.parseInt(scheduleModel.getValueAt(row, 2).toString());
                    populateSeatTable(selectedTheaterId);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createSeatPanel() {
        seatPanel = new JPanel(new BorderLayout());
        seatPanel.add(new JLabel("Select a schedule to view available seats."), BorderLayout.CENTER);

        return seatPanel;
    }



    private void populateMovieTable() {
        try {
            Connection conn = DB_Connect.getConnection();

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM movies");

            movieModel.setRowCount(0);
            while (rs.next()) {
                Object[] rowData = {
                        rs.getInt("movie_id"),
                        rs.getString("title"),
                        rs.getTime("running_time"),
                        rs.getString("rating"),
                        rs.getString("director"),
                        rs.getString("actors"),
                        rs.getString("genre"),
                        rs.getString("introduce"),
                        rs.getDate("open_date"),
                        rs.getFloat("rating_score")
                };
                movieModel.addRow(rowData);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateScheduleTable(int movieId) {
        try {
            Connection conn = DB_Connect.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM schedules WHERE movie_id = ?");
            pstmt.setInt(1, movieId);
            ResultSet rs = pstmt.executeQuery();

            scheduleModel.setRowCount(0);
            while (rs.next()) {
                Object[] rowData = {
                        rs.getInt("schedule_id"),
                        rs.getInt("movie_id"),
                        rs.getInt("theater_id"),
                        rs.getDate("start_date"),
                        rs.getString("day_of_week"),
                        rs.getInt("show_number"),
                        rs.getTime("start_time")
                };
                scheduleModel.addRow(rowData);
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateSeatTable(int theaterId) {
        try {
            Connection conn = DB_Connect.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM seats WHERE theater_id = ?");
            pstmt.setInt(1, theaterId);
            ResultSet rs = pstmt.executeQuery();

            seatPanel.removeAll();
            seatPanel.setLayout(new BorderLayout());

            // Legend panel
            JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            legendPanel.add(new JLabel("Front"));
            legendPanel.add(Box.createHorizontalStrut(20));
            legendPanel.add(createColorLegend(Color.BLUE, "Available"));
            legendPanel.add(Box.createHorizontalStrut(10));
            legendPanel.add(createColorLegend(Color.RED, "Unavailable"));
            legendPanel.add(Box.createHorizontalStrut(10));
            legendPanel.add(createColorLegend(Color.YELLOW, "Selected"));
            seatPanel.add(legendPanel, BorderLayout.NORTH);

            // Seats panel
            JPanel seatsContainer = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(2, 2, 2, 2);
            gbc.fill = GridBagConstraints.BOTH;

            final JButton[][] seatButtons = new JButton[15][15]; // Assuming 10x10 grid for simplicity
            while (rs.next()) {
                final int seatId = rs.getInt("seat_id");
                final int seatRow = rs.getInt("seat_row");
                final int seatCol = rs.getInt("seat_col");
                final boolean isAvailable = rs.getBoolean("is_available");
                final JButton seatButton = new JButton();
                seatButton.setPreferredSize(new Dimension(30, 30));
                seatButton.setOpaque(true);
                seatButton.setBorderPainted(false);

                // Set background color based on availability
                if (isAvailable) {
                    seatButton.setBackground(Color.BLUE);
                } else {
                    seatButton.setBackground(Color.RED);
                }

                seatButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (selectedSeat != null && selectedSeat.isAvailable()) {
                            seatButtons[selectedSeat.getSeatRow()][selectedSeat.getSeatCol()].setBackground(Color.BLUE);
                        }
                        selectedSeat = new Seat(seatId, seatRow, seatCol, isAvailable);
                        if (isAvailable) {
                            seatButton.setBackground(Color.YELLOW);
                            reserveButton.setEnabled(true);
                        } else {
                            JOptionPane.showMessageDialog(null, "This seat is already reserved.");
                            reserveButton.setEnabled(false);
                        }
                    }
                });

                seatButtons[seatRow][seatCol] = seatButton;
                gbc.gridx = seatCol;
                gbc.gridy = seatRow;
                seatsContainer.add(seatButton, gbc);
            }

            reserveButton = new JButton("Reserve");
            reserveButton.setEnabled(false);
            reserveButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (selectedSeat != null && selectedSeat.isAvailable()) {
                        try {
                            Connection conn = DB_Connect.getConnection();
                            // Update the seat availability to false
                            PreparedStatement pstmtUpdateSeat = conn.prepareStatement("UPDATE seats SET is_available = false WHERE seat_id = ?");
                            pstmtUpdateSeat.setInt(1, selectedSeat.getSeatId());
                            pstmtUpdateSeat.executeUpdate();
                            pstmtUpdateSeat.close();

                            // Get the next reservation number
                            Statement stmtReservationNumber = conn.createStatement();
                            ResultSet rsReservationNumber = stmtReservationNumber.executeQuery("SELECT COALESCE(MAX(reservation_number), 0) + 1 FROM reservations");
                            rsReservationNumber.next();
                            int reservationNumber = rsReservationNumber.getInt(1);
                            rsReservationNumber.close();
                            stmtReservationNumber.close();

                            Statement stmtTicketId = conn.createStatement();
                            ResultSet rsTicketId = stmtTicketId.executeQuery("SELECT COALESCE(MAX(ticket_id), 0) + 1 FROM tickets");
                            rsTicketId.next();
                            int ticketId = rsTicketId.getInt(1);
                            rsTicketId.close();
                            stmtTicketId.close();

                            // Insert a new reservation
                            PreparedStatement pstmtInsertReservation = conn.prepareStatement("INSERT INTO reservations (reservation_number, reservation_payment, reservation_status, reservation_amount, customer_ID, reservation_date) VALUES (?, ?, ?, ?, ?, ?)");
                            pstmtInsertReservation.setInt(1, reservationNumber);
                            pstmtInsertReservation.setString(2, "Credit Card");
                            pstmtInsertReservation.setBoolean(3, true);
                            pstmtInsertReservation.setInt(4, 20000);
                            pstmtInsertReservation.setInt(5, 1);
                            pstmtInsertReservation.setDate(6, new java.sql.Date(System.currentTimeMillis()));
                            pstmtInsertReservation.executeUpdate();
                            pstmtInsertReservation.close();

                            // Insert a new ticket
                            PreparedStatement pstmtInsertTicket = conn.prepareStatement("INSERT INTO tickets (ticket_id, schedule_id, theater_id, seat_id, reservation_number, is_purchase, standard_price, selling_price) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                            pstmtInsertTicket.setInt(1, ticketId);
                            pstmtInsertTicket.setInt(2, selectedScheduleId);
                            pstmtInsertTicket.setInt(3, selectedTheaterId);
                            pstmtInsertTicket.setInt(4, selectedSeat.getSeatId());
                            pstmtInsertTicket.setInt(5, reservationNumber);
                            pstmtInsertTicket.setBoolean(6, true);
                            pstmtInsertTicket.setInt(7, 20000);
                            pstmtInsertTicket.setInt(8, 20000);
                            pstmtInsertTicket.executeUpdate();
                            pstmtInsertTicket.close();

                            // Refresh the seat table
                            populateSeatTable(selectedTheaterId);

                            // Refresh the ticket table
                            populateTicketTable();
                            // Refresh the reservation table
                            populateReservationTable();

                            JOptionPane.showMessageDialog(null, "Seat " + selectedSeat.getSeatId() + " in theater " + selectedTheaterId + " has been reserved.");
                            selectedSeat = null;
                            reserveButton.setEnabled(false);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null, "Failed to reserve the seat.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Please select an available seat to reserve.");
                    }
                }
            });

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(reserveButton);
            seatPanel.add(buttonPanel, BorderLayout.SOUTH);

            seatPanel.add(seatsContainer, BorderLayout.CENTER);
            seatPanel.add(legendPanel, BorderLayout.NORTH);
            seatPanel.add(buttonPanel, BorderLayout.SOUTH);
            seatPanel.revalidate();
            seatPanel.repaint();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private JPanel createTicketPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Create table model for tickets
        String[] columnNames = {"Ticket ID", "Schedule ID", "Theater ID", "Seat ID", "Reservation Number", "Is Purchase", "Standard Price", "Selling Price"};
        ticketModel = new DefaultTableModel(columnNames, 0);

        // Create table and add the table model
        ticketTable = new JTable(ticketModel);

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(ticketTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Populate the table with ticket data
        populateTicketTable();

        return panel;
    }

    private void changeMovie() {
        deleteTicket(1);

    }

    private void changeSchedule() {
        deleteTicket(2);
    }

    private void populateReservationTable() {
        try {
            Connection conn = DB_Connect.getConnection();
            String query = "SELECT r.reservation_number, m.title, s.start_date AS schedule_date, t.theater_id, " +
                    "CONCAT(se.seat_row, '-', se.seat_col) AS seat_number, ti.selling_price " +
                    "FROM reservations r " +
                    "JOIN tickets ti ON r.reservation_number = ti.reservation_number " +
                    "JOIN schedules s ON ti.schedule_id = s.schedule_id " +
                    "JOIN movies m ON s.movie_id = m.movie_id " +
                    "JOIN theaters t ON s.theater_id = t.theater_id " +
                    "JOIN seats se ON ti.seat_id = se.seat_id " +
                    "WHERE r.customer_ID = ?"; // Assuming customer_ID is the current user's ID
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, 1); // Replace with the actual customer ID
            ResultSet rs = pstmt.executeQuery();

            reservationModel.setRowCount(0);
            while (rs.next()) {
                Object[] rowData = {
                        rs.getInt("reservation_number"),
                        rs.getString("title"),
                        rs.getDate("schedule_date"),
                        rs.getInt("theater_id"),
                        rs.getString("seat_number"),
                        rs.getInt("selling_price")
                };
                reservationModel.addRow(rowData);
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private JPanel createReservationPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Create table model for reservations
        String[] columnNames = {"Reservation Number", "Title", "Schedule Date", "Theater ID", "Seat Number", "Selling Price"};
        reservationModel = new DefaultTableModel(columnNames, 0);

        // Create table and add the table model
        reservationTable = new JTable(reservationModel);
        reservationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        reservationTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = reservationTable.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    int reservationNumber = (int) reservationModel.getValueAt(row, 0);
                    showReservationDetails(reservationNumber);
                }
            }
        });

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(reservationTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Populate the table with reservation data
        populateReservationTable();


        // Add buttons for ticket management
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Use FlowLayout for button panel
        JButton deleteButton = new JButton("Delete Ticket");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteTicket(0);
            }
        });
        buttonPanel.add(deleteButton);

        JButton changeMovieButton = new JButton("Change Movie");
        changeMovieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeMovie();
            }
        });
        buttonPanel.add(changeMovieButton);

        JButton changeScheduleButton = new JButton("Change Schedule");
        changeScheduleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeSchedule();
            }
        });
        buttonPanel.add(changeScheduleButton);

        // Add the button panel to the main panel
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private static void showReservationDetails(int reservationNumber) {
        try {
            Connection conn = DB_Connect.getConnection();
            String query = "SELECT m.title, s.start_date AS schedule_date, t.theater_id, " +
                    "CONCAT(se.seat_row, '-', se.seat_col) AS seat_number, ti.selling_price " +
                    "FROM tickets ti " +
                    "JOIN schedules s ON ti.schedule_id = s.schedule_id " +
                    "JOIN movies m ON s.movie_id = m.movie_id " +
                    "JOIN theaters t ON s.theater_id = t.theater_id " +
                    "JOIN seats se ON ti.seat_id = se.seat_id " +
                    "WHERE ti.reservation_number = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, reservationNumber);
            ResultSet rs = pstmt.executeQuery();

            // Create a string to display the reservation details
            StringBuilder details = new StringBuilder();
            while (rs.next()) {
                details.append("Title: ").append(rs.getString("title")).append("\n");
                details.append("Schedule Date: ").append(rs.getDate("schedule_date")).append("\n");
                details.append("Theater ID: ").append(rs.getInt("theater_id")).append("\n");
                details.append("Seat Number: ").append(rs.getString("seat_number")).append("\n");
                details.append("Selling Price: ").append(rs.getInt("selling_price")).append("\n\n");
            }

            rs.close();
            pstmt.close();
            conn.close();

            // Show the details in a dialog with a "Show More Detail" button
            JButton showMoreButton = new JButton("Show More Detail");
            showMoreButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showTicketDetails(reservationNumber);
                }
            });

            JPanel panel = new JPanel(new BorderLayout());
            JTextArea textArea = new JTextArea(details.toString());
            textArea.setEditable(false);
            panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
            panel.add(showMoreButton, BorderLayout.SOUTH);

            JOptionPane.showMessageDialog(null, panel, "Reservation Details", JOptionPane.INFORMATION_MESSAGE);
            populateTicketTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private static void showTicketDetails(int reservationNumber) {
        try {
            Connection conn = DB_Connect.getConnection();
            String query = "SELECT * FROM tickets WHERE reservation_number = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, reservationNumber);
            ResultSet rs = pstmt.executeQuery();

            ticketModel.setRowCount(0);
            while (rs.next()) {
                Object[] rowData = {
                        rs.getInt("ticket_id"),
                        rs.getInt("schedule_id"),
                        rs.getInt("theater_id"),
                        rs.getInt("seat_id"),
                        rs.getInt("reservation_number"),
                        rs.getBoolean("is_purchase"),
                        rs.getInt("standard_price"),
                        rs.getInt("selling_price")
                };
                ticketModel.addRow(rowData);
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    private static void populateTicketTable() {
        try {
            Connection conn = DB_Connect.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM tickets");

            ticketModel.setRowCount(0);
            while (rs.next()) {
                Object[] rowData = {
                        rs.getInt("ticket_id"),
                        rs.getInt("schedule_id"),
                        rs.getInt("theater_id"),
                        rs.getInt("seat_id"),
                        rs.getInt("reservation_number"),
                        rs.getBoolean("is_purchase"),
                        rs.getInt("standard_price"),
                        rs.getInt("selling_price")
                };
                ticketModel.addRow(rowData);
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteTicket(int flag) {
        int selectedRow = reservationTable.getSelectedRow();
        if (selectedRow >= 0) {
            int ticketId = (int) ticketModel.getValueAt(selectedRow, 0);
            int seatId = (int) ticketModel.getValueAt(selectedRow, 3); // Get the seat ID from the selected ticket
            int reservationId = (int) ticketModel.getValueAt(selectedRow, 4);

            try {
                Connection conn = DB_Connect.getConnection();
                conn.setAutoCommit(false); // Start transaction

                // Retrieve movie ID associated with the selected reservation
                PreparedStatement pstmtGetMovieId = conn.prepareStatement(
                        "SELECT s.movie_id FROM tickets t " +
                                "JOIN schedules s ON t.schedule_id = s.schedule_id " +
                                "WHERE t.reservation_number = ?");
                pstmtGetMovieId.setInt(1, reservationId);
                ResultSet rsMovieId = pstmtGetMovieId.executeQuery();
                int movieId = 0;
                if (rsMovieId.next()) {
                    movieId = rsMovieId.getInt("movie_id");
                }
                pstmtGetMovieId.close();

                // Check if the ticket exists and its reservation number
                PreparedStatement pstmtCheckTicket = conn.prepareStatement("SELECT * FROM tickets WHERE ticket_id = ?");
                pstmtCheckTicket.setInt(1, ticketId);
                ResultSet rsTicket = pstmtCheckTicket.executeQuery();
                int ticketReservationId = 0;
                if (rsTicket.next()) {
                    ticketReservationId = rsTicket.getInt("reservation_number");
                }
                pstmtCheckTicket.close();

                // If the ticket reservation number matches the selected reservation, delete the ticket
                if (ticketReservationId == reservationId) {
                    // Update the seat availability to true
                    PreparedStatement pstmtUpdateSeat = conn.prepareStatement("UPDATE seats SET is_available = true WHERE seat_id = ?");
                    pstmtUpdateSeat.setInt(1, seatId);
                    pstmtUpdateSeat.executeUpdate();
                    pstmtUpdateSeat.close();

                    // Delete the ticket
                    PreparedStatement pstmtDeleteTicket = conn.prepareStatement("DELETE FROM tickets WHERE ticket_id = ?");
                    pstmtDeleteTicket.setInt(1, ticketId);
                    int rowsAffectedTicket = pstmtDeleteTicket.executeUpdate();
                    pstmtDeleteTicket.close();

                    if (rowsAffectedTicket > 0 && flag == 0) {
                        ticketModel.removeRow(selectedRow);
                        JOptionPane.showMessageDialog(null, "Ticket deleted successfully.");
                    } else if (flag == 0) {
                        JOptionPane.showMessageDialog(null, "Failed to delete ticket.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Selected ticket does not match the reservation.");
                }

                // Delete reservation if all tickets referencing it are deleted
                PreparedStatement pstmtCountTickets = conn.prepareStatement("SELECT COUNT(*) AS num_tickets FROM tickets WHERE reservation_number = ?");
                pstmtCountTickets.setInt(1, reservationId);
                ResultSet rsCountTickets = pstmtCountTickets.executeQuery();
                rsCountTickets.next();
                int numTickets = rsCountTickets.getInt("num_tickets");
                pstmtCountTickets.close();

                if (numTickets == 0) {
                    PreparedStatement pstmtDeleteReservation = conn.prepareStatement("DELETE FROM reservations WHERE reservation_number = ?");
                    pstmtDeleteReservation.setInt(1, reservationId);
                    int rowsAffectedReservation = pstmtDeleteReservation.executeUpdate();
                    pstmtDeleteReservation.close();

                    if (rowsAffectedReservation > 0 && flag == 0) {
                        JOptionPane.showMessageDialog(null, "Reservation deleted successfully.");
                    } else if (flag == 0) {
                        JOptionPane.showMessageDialog(null, "Failed to delete reservation.");
                    }
                }

                conn.commit(); // Commit transaction
                conn.setAutoCommit(true); // Restore auto-commit mode
                conn.close();

                if (flag == 0) {
                    populateTicketTable();
                    populateReservationTable();
                } else if (flag == 1) {
                    JOptionPane.showMessageDialog(null, "Please select another movie, schedule and seat.");
                    populateMovieTable();
                } else if (flag == 2) {
                    JOptionPane.showMessageDialog(null, "Please select a new schedule for the selected movie.");
                    populateScheduleTable(movieId);
                }
                populateSeatTable(selectedTheaterId);

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "An error occurred while deleting ticket: " + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Please select a ticket to delete.");
        }
    }


    private void searchMovies() {
        String title = titleField.getText();
        String director = directorField.getText();
        String actors = actorsField.getText();
        String genre = genreField.getText();

        try {
            Connection conn = DB_Connect.getConnection();
            StringBuilder sb = new StringBuilder("SELECT * FROM movies WHERE 1=1");
            List<String> params = new ArrayList<>();

            if (!title.isEmpty()) {
                sb.append(" AND title LIKE ?");
                params.add("%" + title + "%");
            }
            if (!director.isEmpty()) {
                sb.append(" AND director LIKE ?");
                params.add("%" + director + "%");
            }
            if (!actors.isEmpty()) {
                sb.append(" AND actors LIKE ?");
                params.add("%" + actors + "%");
            }
            if (!genre.isEmpty()) {
                sb.append(" AND genre LIKE ?");
                params.add("%" + genre + "%");
            }

            PreparedStatement pstmt = conn.prepareStatement(sb.toString());
            for (int i = 0; i < params.size(); i++) {
                pstmt.setString(i + 1, params.get(i));
            }
            ResultSet rs = pstmt.executeQuery();

            movieModel.setRowCount(0);
            while (rs.next()) {
                Object[] rowData = {
                        rs.getInt("movie_id"),
                        rs.getString("title"),
                        rs.getTime("running_time"),
                        rs.getString("rating"),
                        rs.getString("director"),
                        rs.getString("actors"),
                        rs.getString("genre"),
                        rs.getString("introduce"),
                        rs.getDate("open_date"),
                        rs.getFloat("rating_score")
                };
                movieModel.addRow(rowData);
            }

            rs.close();
            pstmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MovieReservationGUI gui = new MovieReservationGUI();
            gui.setVisible(true);
        });
    }

    private JPanel createColorLegend(Color color, String text) {
        JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        JLabel colorLabel = new JLabel();
        colorLabel.setOpaque(true);
        colorLabel.setBackground(color);
        colorLabel.setPreferredSize(new Dimension(15, 15));
        legend.add(colorLabel);
        legend.add(new JLabel(text));
        return legend;
    }

    private class Seat {
        private int seatId;
        private int seatRow;
        private int seatCol;
        private boolean isAvailable;

        public Seat(int seatId, int seatRow, int seatCol, boolean isAvailable) {
            this.seatId = seatId;
            this.seatRow = seatRow;
            this.seatCol = seatCol;
            this.isAvailable = isAvailable;
        }

        public int getSeatId() {
            return seatId;
        }

        public int getSeatRow() {
            return seatRow;
        }

        public int getSeatCol() {
            return seatCol;
        }

        public boolean isAvailable() {
            return isAvailable;
        }
    }
}