package dao;

import dto.Reservation;
import util.DB_Connect;
import util.DB_Close;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {
    private static ReservationDAO instance = new ReservationDAO();

    private ReservationDAO() {
    }

    public static ReservationDAO getInstance() {
        return instance;
    }

    public void makeReservation(Reservation reservation) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DB_Connect.getConnection();
            String sql = "INSERT INTO reservations (reservation_payment, reservation_status, reservation_amount, customer_id, reservation_date) VALUES (?, ?, ?, ?, ?)";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, reservation.getReservationPayment());
            pstmt.setBoolean(2, reservation.isReservationStatus());
            pstmt.setInt(3, reservation.getReservationAmount());
            pstmt.setInt(4, reservation.getCustomerId());
            pstmt.setString(5, reservation.getReservationDate());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB_Close.close(pstmt, con);
        }
    }

    public List<Reservation> getAllReservations() {
        List<Reservation> reservations = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DB_Connect.getConnection();
            String sql = "SELECT * FROM reservations";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Reservation reservation = new Reservation();
                reservation.setReservationNumber(rs.getInt("reservation_number"));
                reservation.setReservationPayment(rs.getString("reservation_payment"));
                reservation.setReservationStatus(rs.getBoolean("reservation_status"));
                reservation.setReservationAmount(rs.getInt("reservation_amount"));
                reservation.setCustomerId(rs.getInt("customer_id"));
                reservation.setReservationDate(rs.getString("reservation_date"));
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB_Close.close(rs, pstmt, con);
        }

        return reservations;
    }
}