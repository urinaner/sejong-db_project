package dao;

import entity.Theater;
import util.DB_Connect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TheaterDAO {
    private static TheaterDAO instance = new TheaterDAO();

    private TheaterDAO() {
    }

    public static TheaterDAO getInstance() {
        return instance;
    }

    // Method to get all theaters
    public List<Theater> getAllTheaters() {
        List<Theater> theaters = new ArrayList<>();
        String sql = "SELECT * FROM theaters";

        try (Connection conn = DB_Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Theater theater = new Theater();
                theater.setTheaterId(rs.getInt("theater_id"));
                theater.setSeatCount(rs.getInt("seat_count"));
                theater.setAvailable(rs.getBoolean("is_available"));
                theater.setSeatRows(rs.getInt("seat_rows"));
                theater.setSeatColumns(rs.getInt("seat_columns"));
                theaters.add(theater);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return theaters;
    }

    // Method to get theaters by movie ID
    public List<Theater> getTheatersByMovieId(int movieId) {
        List<Theater> theaters = new ArrayList<>();
        String sql = "SELECT t.* FROM theaters t " +
                "JOIN schedules s ON t.theater_id = s.theater_id " +
                "WHERE s.movie_id = ?";

        try (Connection conn = DB_Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, movieId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Theater theater = new Theater();
                    theater.setTheaterId(rs.getInt("theater_id"));
                    theater.setSeatCount(rs.getInt("seat_count"));
                    theater.setAvailable(rs.getBoolean("is_available"));
                    theater.setSeatRows(rs.getInt("seat_rows"));
                    theater.setSeatColumns(rs.getInt("seat_columns"));
                    theaters.add(theater);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return theaters;
    }
}