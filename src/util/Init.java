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
        stmt.executeUpdate("SET FOREIGN_KEY_CHECKS=0");
        stmt.execute("DROP TABLE IF EXISTS customers");
        stmt.execute("DROP TABLE IF EXISTS tickets");
        stmt.execute("DROP TABLE IF EXISTS seats");
        stmt.execute("DROP TABLE IF EXISTS schedules");
        stmt.execute("DROP TABLE IF EXISTS movies");
        stmt.execute("DROP TABLE IF EXISTS theaters");
        stmt.executeUpdate("SET FOREIGN_KEY_CHECKS=1");

        // 새로운 테이블 생성
        stmt.execute("CREATE TABLE IF NOT EXISTS theaters (" +
                "theater_id INT PRIMARY KEY," +
                "seat_count INT," +
                "is_available BOOLEAN," +
                "seat_row INT," +
                "seat_column INT" +
                ")");

        stmt.execute("CREATE TABLE IF NOT EXISTS movies (" +
                "movie_id INT PRIMARY KEY," +
                "title VARCHAR(100) NOT NULL," +
                "running_time TIME," +
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

        stmt.execute("CREATE TABLE IF NOT EXISTS customers (" +
                "customer_id INT NOT NULL," +
                "customer_name VARCHAR(45) NULL DEFAULT NULL," +
                "customer_mobilenumber VARCHAR(20) NOT NULL," +
                "customer_emailaddress VARCHAR(45) NULL DEFAULT NULL," +
                "PRIMARY KEY (customer_id)" +
                ")");

        stmt.execute("CREATE TABLE IF NOT EXISTS reservations (" +
                "reservation_number INT NOT NULL," +
                "reservation_payment VARCHAR(45)," +
                "reservation_status BOOLEAN NOT NULL," +
                "reservation_amount INT," +
                "customer_ID INT NOT NULL," +
                "reservation_date DATE," +
                "PRIMARY KEY (reservation_number)," +
                "FOREIGN KEY (customer_ID) REFERENCES customers(customer_id)" +
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
                "FOREIGN KEY (reservation_number) REFERENCES reservations(reservation_number)" +
                ")");
    }

    private static void insertExampleData() throws SQLException {
        Statement stmt = con.createStatement();
        // Insert data into movies table
        String insertMoviesData = "INSERT INTO movies (movie_id, title, running_time, rating, director, actors, genre, introduce, open_date, rating_score) VALUES "
                + "(1, 'Interstellar', '02:49:00', 'PG-13', 'Christopher Nolan', 'Matthew McConaughey, Anne Hathaway', 'Sci-Fi', 'A journey to save humanity', '2014-11-07', 8.6),"
                + "(2, 'Inception', '02:28:00', 'PG-13', 'Christopher Nolan', 'Leonardo DiCaprio, Joseph Gordon-Levitt', 'Sci-Fi', 'A mind-bending thriller', '2010-07-16', 8.8),"
                + "(3, 'Titanic', '03:14:00', 'PG-13', 'James Cameron', 'Leonardo DiCaprio, Kate Winslet', 'Romance', 'A romance blooms on the ill-fated Titanic', '1997-12-19', 7.8),"
                + "(4, 'Coco', '01:45:00', 'PG', 'Lee Unkrich, Adrian Molina', 'Anthony Gonzalez, Gael García Bernal', 'Animation', 'A boy journeys into the Land of the Dead', '2017-11-22', 8.4),"
                + "(5, 'Gravity', '01:31:00', 'PG-13', 'Alfonso Cuarón', 'Sandra Bullock, George Clooney', 'Sci-Fi', 'Two astronauts struggle to survive in space', '2013-10-04', 7.7),"
                + "(6, 'Toy Story', '01:21:00', 'G', 'John Lasseter', 'Tom Hanks, Tim Allen', 'Animation', 'A toy cowboy and a toy astronaut become friends', '1995-11-22', 8.3),"
                + "(7, 'Avengers: Endgame', '03:01:00', 'PG-13', 'Anthony Russo, Joe Russo', 'Robert Downey Jr., Chris Evans', 'Action', 'The epic conclusion of the Avengers saga', '2019-04-26', 8.4),"
                + "(8, 'American Sniper', '02:13:00', 'R', 'Clint Eastwood', 'Bradley Cooper, Sienna Miller', 'War', 'The story of the deadliest sniper in U.S. history', '2014-12-25', 7.3),"
                + "(9, 'Deadpool', '01:48:00', 'R', 'Tim Miller', 'Ryan Reynolds, Morena Baccarin', 'Action', 'A former special forces operative turned mercenary', '2016-02-12', 8.0),"
                + "(10, 'Annabelle', '01:39:00', 'R', 'John R. Leonetti', 'Annabelle Wallis, Ward Horton', 'Horror', 'A couple begins to experience terrifying events', '2014-10-03', 5.4),"
                + "(11, 'Frozen', '01:42:00', 'PG', 'Chris Buck, Jennifer Lee', 'Kristen Bell, Idina Menzel', 'Animation', 'A young woman must save her kingdom from eternal winter', '2013-11-27', 7.4),"
                + "(12, 'Transformers', '02:24:00', 'PG-13', 'Michael Bay', 'Shia LaBeouf, Megan Fox', 'Action', 'Humans caught in a war between two factions of robots', '2007-07-03', 7.0)";
        stmt.executeUpdate(insertMoviesData);

        // Insert data into theaters table
        String insertTheatersData = "INSERT INTO theaters (theater_id, seat_count, is_available, seat_row, seat_column) VALUES "
                + "(1, 30, 1, 6, 5),"
                + "(2, 12, 1, 4, 3),"
                + "(3, 20, 1, 5, 4),"
                + "(4, 25, 1, 5, 5),"
                + "(5, 15, 1, 3, 5),"
                + "(6, 6, 1, 2, 3),"
                + "(7, 16, 1, 4, 4),"
                + "(8, 2, 1, 2, 1),"
                + "(9, 8, 1, 4, 2),"
                + "(10, 40, 1, 4, 10),"
                + "(11, 24, 1, 6, 4),"
                + "(12, 18, 1, 6, 3)";
        stmt.executeUpdate(insertTheatersData);

        // Insert data into customers table
        String insertCustomersData = "INSERT INTO customers (customer_id, customer_name, customer_mobilenumber, customer_emailaddress) VALUES "
                + "(1, 'Kim', '01012345678', 'Kim@example.com')";
        stmt.executeUpdate(insertCustomersData);

        // Insert data into schedules table
        String insertSchedulesData = "INSERT INTO schedules (schedule_id, movie_id, theater_id, start_date, day_of_week, show_number, start_time) VALUES"

                + "(1, 1, 1, '2024-06-01', 'Friday', 1, '09:00:00'),"
                + "(2, 2, 1, '2024-06-01', 'Friday', 2, '12:00:00'),"
                + "(3, 3, 1, '2024-06-01', 'Friday', 3, '15:00:00')";
        stmt.executeUpdate(insertSchedulesData);
    }

    public static void main(String[] args) {
        run();
    }
}
