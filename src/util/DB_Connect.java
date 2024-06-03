package util;
import java.sql.*;

public class DB_Connect {
    public static void main(String[] args) {
        Statement stmt;
        Connection conn;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn=DriverManager.getConnection("jdbc:mysql://localhost:3306/TEST?serverTimezone=UTC","root","wkddudwo1!");
            System.out.println("DB connection complete");

            stmt = conn.createStatement();

        } catch (ClassNotFoundException e) {
            System.out.println("JDBC drive road error");
        } catch (SQLException e) {
            System.out.println("DB connection error");
        }
    }
}