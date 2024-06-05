package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import dto.Theater;
import util.DB_Connect;

public class TheaterDAO {
    private static TheaterDAO instance = new TheaterDAO();

    private TheaterDAO() {
    }

    public static TheaterDAO getInstance() {
        return instance;
    }

    // Method to get a theater by schedule ID
    public Theater getTheaterByScheduleId(int scheduleId) {
        Theater theater = null;
        String sql = "SELECT t.* FROM theaters t " +
                "JOIN schedules s ON t.theater_id = s.theater_id " +
                "WHERE s.schedule_id = ?";

        try (Connection conn = DB_Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, scheduleId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    theater = new Theater();
                    theater.setTheaterId(rs.getInt("theater_id"));
                    theater.setSeatCount(rs.getInt("seat_count"));
                    theater.setAvailable(rs.getBoolean("is_available"));
                    theater.setSeatRows(rs.getInt("seat_row"));
                    theater.setSeatColumns(rs.getInt("seat_column"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return theater;
    }
}