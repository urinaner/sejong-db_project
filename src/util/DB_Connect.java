package util;
import java.sql.*;

public class DB_Connect {
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("연결안됨");
            e.printStackTrace();
        }
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/db1?serverTimezone=UTC","root","wkddudwo1!");
    }

}