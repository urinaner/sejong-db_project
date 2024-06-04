package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.Schedule;
import util.DB_Connect;

public class ScheduleDAO {
    private static ScheduleDAO instance = new ScheduleDAO();

    private ScheduleDAO() {
    }

    public static ScheduleDAO getInstance() {
        return instance;
    }

    // Method to get schedules by movie ID
    public List<Schedule> getSchedulesByMovieId(int movieId) {
        List<Schedule> schedules = new ArrayList<>();
        String sql = "SELECT * FROM schedules WHERE movie_id = ?";

        try (Connection conn = DB_Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, movieId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Schedule schedule = new Schedule();
                    schedule.setScheduleId(rs.getInt("schedule_id"));
                    schedule.setMovieId(rs.getInt("movie_id"));
                    schedule.setTheaterId(rs.getInt("theater_id"));
                    schedule.setStartDate(String.valueOf(rs.getDate("start_date")));
                    schedule.setDayOfWeek(rs.getString("day_of_week"));
                    schedule.setShowNumber(rs.getInt("show_number"));
                    schedule.setStartTime(String.valueOf(rs.getTime("start_time")));
                    schedules.add(schedule);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return schedules;
    }
}