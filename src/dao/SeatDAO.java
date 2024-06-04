package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.Seat;
import util.DB_Connect;

public class SeatDAO {
    private static SeatDAO instance = new SeatDAO();

    private SeatDAO() {
    }

    public static SeatDAO getInstance() {
        return instance;
    }

    // Method to get seats by theater ID
    public List<Seat> getSeatsByTheaterId(int theaterId) {
        List<Seat> seats = new ArrayList<>();
        String sql = "SELECT * FROM seats WHERE theater_id = ?";

        try (Connection conn = DB_Connect.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, theaterId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Seat seat = new Seat();
                    seat.setSeatId(rs.getInt("seat_id"));
                    seat.setTheaterId(rs.getInt("theater_id"));
                    seat.setAvailable(rs.getBoolean("is_available"));
                    seat.setSeatRow(rs.getInt("seat_row"));
                    seat.setSeatCol(rs.getInt("seat_col"));
                    seats.add(seat);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return seats;
    }
}