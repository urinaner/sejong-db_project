package dao;

import util.DB_Close;
import util.DB_Connect;

import java.sql.*;

public class CustomerDAO {

    private static CustomerDAO instance = new CustomerDAO();

    private CustomerDAO() {
    }

    public static CustomerDAO getInstance() {
        return instance;
    }

    private static Connection con;
    private static PreparedStatement pstmt;
    private static ResultSet rs;

    public void connect() {
        try {
            con = DB_Connect.getConnection();
            System.out.println("데이터베이스 연결 성공");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 로그인
    // 성공 : 0 실패 : 전화번호 오류 : -1, 등록되지 않은 이름 : -2 | 데이터베이스 오류 : -3;
    public int login(String name, String phoneNumber) {
        connect();
        String sql = "SELECT * FROM customers WHERE customer_name = ?";

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();

            // 이름 확인
            if (rs.next()) {
                System.out.println("name chekced.");
                String dbPhoneNumber = rs.getString("customer_mobilenumber"); // 실제 전화번호

                // 디버그용 출력
                System.out.println("입력된 전화번호: " + phoneNumber);
                System.out.println("데이터베이스 전화번호: " + dbPhoneNumber);

                // 1) 전화번호가 맞는 경우.
                if (phoneNumber.equals(dbPhoneNumber)) {
                    System.out.println("login complete");
                    return 0;
                }
                // 2) 전화번호가 틀린 경우
                else {
                    return -1;
                }
            }
            // 등록되지 않은 이름
            else {
                return -2;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB_Close.close(rs, pstmt, con);
        }
        return -3; // 데이터베이스 오류
    }
}
