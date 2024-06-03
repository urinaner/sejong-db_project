package util;

import java.sql.*;

public class Init {
    private static Connection con;

    public static void run() {
        try {
            con = DB_Connect.getConnection();
            System.out.println("데이터베이스 연결 성공");

            // 테이블 생성
            createTables();

            // 예제 데이터 주입
            insertExampleData();

            System.out.println("테이블 생성 및 예제 데이터 주입 완료");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void createTables() throws SQLException {
        Statement stmt = con.createStatement();

        // 기존 테이블 삭제
        stmt.execute("DROP TABLE IF EXISTS customer");
        stmt.execute("DROP TABLE IF EXISTS tickets");
        stmt.execute("DROP TABLE IF EXISTS seats");
        stmt.execute("DROP TABLE IF EXISTS schedules");
        stmt.execute("DROP TABLE IF EXISTS movies");
        stmt.execute("DROP TABLE IF EXISTS theaters");

        // 새로운 테이블 생성
        stmt.execute("CREATE TABLE IF NOT EXISTS theaters (" +
                "theater_id INT PRIMARY KEY," +
                "seat_count INT," +
                "is_available BOOLEAN," +
                "seat_rows INT," +
                "seat_columns INT" +
                ")");

        stmt.execute("CREATE TABLE IF NOT EXISTS movies (" +
                "movie_id INT PRIMARY KEY," +
                "title VARCHAR(100) NOT NULL," +
                "running_time INT," +
                "rating VARCHAR(20)," +
                "director VARCHAR(50)," +
                "actors VARCHAR(200)," +
                "genre VARCHAR(50)," +
                "introduce VARCHAR(300)," +
                "open_date DATE," +
                "rating_score INT" +
                ")");

        stmt.execute("CREATE TABLE IF NOT EXISTS schedules (" +
                "schedule_id INT PRIMARY KEY," +
                "movie_id INT," +
                "theater_id INT," +
                "start_date DATE," +
                "day_of_week VARCHAR(10)," +
                "show_number INT," +
                "start_time TIME," +
                "FOREIGN KEY (movie_id) REFERENCES movies(movie_id)," +
                "FOREIGN KEY (theater_id) REFERENCES theaters(theater_id)" +
                ")");

        stmt.execute("CREATE TABLE IF NOT EXISTS seats (" +
                "seat_id INT PRIMARY KEY," +
                "theater_id INT," +
                "is_available BOOLEAN," +
                "FOREIGN KEY (theater_id) REFERENCES theaters(theater_id)" +
                ")");

        stmt.execute("CREATE TABLE IF NOT EXISTS customer (" +
                "customer_id INT NOT NULL," +
                "customer_name VARCHAR(45) NULL DEFAULT NULL," +
                "customer_mobilenumber VARCHAR(20) NOT NULL," +
                "customer_emailadress VARCHAR(45) NULL DEFAULT NULL," +
                "PRIMARY KEY (customer_id)" +
                ")");

        stmt.execute("CREATE TABLE IF NOT EXISTS reservation (" +
                "reservation_number INT NOT NULL," +
                "reservation_payment VARCHAR(45)," +
                "reservation_status BOOLEAN NOT NULL," +
                "reservation_amount INT," +
                "customer_ID INT NOT NULL," +
                "reservation_date DATE," +
                "PRIMARY KEY (reservation_number)," +
                "FOREIGN KEY (customer_ID) REFERENCES customer(customer_id)" +
                ")");

        stmt.execute("CREATE TABLE IF NOT EXISTS tickets (" +
                "ticket_id INT PRIMARY KEY," +
                "theater_id INT," +
                "movie_id INT," +
                "seat_id INT," +
                "reservation_number INT," +
                "is_purchase BOOLEAN," +
                "how_much INT," +
                "schedule_id INT," +
                "FOREIGN KEY (schedule_id) REFERENCES schedules(schedule_id)," +
                "FOREIGN KEY (theater_id) REFERENCES theaters(theater_id)," +
                "FOREIGN KEY (seat_id) REFERENCES seats(seat_id)," +
                "FOREIGN KEY (reservation_number) REFERENCES reservation(reservation_number)" +
                ")");
    }

    private static void insertExampleData() throws SQLException {
        Statement stmt = con.createStatement();

        // theaters 테이블에 예제 데이터 삽입
        stmt.execute("INSERT INTO theaters (theater_id, seat_count, is_available, seat_rows, seat_columns) " +
                "VALUES (1, 100, true, 10, 10), (2, 80, true, 8, 10)");

        // movies 테이블에 예제 데이터 삽입
        stmt.execute("INSERT INTO movies (movie_id, title, running_time, rating, director, actors, genre, introduce, open_date, rating_score) " +
                "VALUES (1, '영화 제목 1', 120, '15세 관람가', '감독 1', '배우 1, 배우 2', '액션', '영화 소개 1', '2023-06-01', 8), " +
                "(2, '영화 제목 2', 105, '12세 관람가', '감독 2', '배우 3, 배우 4', '코미디', '영화 소개 2', '2023-06-15', 7)");

        // schedules 테이블에 예제 데이터 삽입
        stmt.execute("INSERT INTO schedules (schedule_id, movie_id, theater_id, start_date, day_of_week, show_number, start_time) " +
                "VALUES (1, 1, 1, '2023-06-10', '토요일', 1, '10:00:00'), (2, 2, 2, '2023-06-20', '화요일', 2, '14:30:00')");

        // seats 테이블에 예제 데이터 삽입
        stmt.execute("INSERT INTO seats (seat_id, theater_id, is_available) " +
                "VALUES (1, 1, true), (2, 1, false)");

        // customer 테이블에 예제 데이터 삽입
        stmt.execute("INSERT INTO customer (customer_id, customer_name, customer_mobilenumber, customer_emailadress) " +
                "VALUES (3, 'janschedulesg', '1234567890', 'customer1@example.com'), (4, '고객4', '9876543210', 'customer2@example.com')");

        // reservation 테이블에 예제 데이터 삽입
        stmt.execute("INSERT INTO reservation (reservation_number, reservation_payment, reservation_status, reservation_amount, customer_ID, reservation_date) " +
                "VALUES (1, '신용카드', true, 20000, 3, '2023-06-09'), (2, '현금', false, 15000, 4, '2023-06-19')");

        // tickets 테이블에 예제 데이터 삽입
        stmt.execute("INSERT INTO tickets (ticket_id, theater_id, movie_id, seat_id, reservation_number, is_purchase, how_much, schedule_id) " +
                "VALUES (1, 1, 1, 1, 1, true, 10000, 1), (2, 2, 2, 2, 2, false, 8000, 2)");
    }
}