package util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Init {
    private static Connection con;

    public static void run() {
        try {
            con = DB_Connect.getConnection();
            System.out.println("데이터베이스 연결 성공");

            // 테이블 생성 및 데이터 주입
            createTablesAndInsertData();

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

    private static void createTablesAndInsertData() throws SQLException {
        Statement stmt = con.createStatement();

        // 기존 테이블 삭제
        stmt.executeUpdate("SET FOREIGN_KEY_CHECKS=0");
        stmt.execute("DROP TABLE IF EXISTS tickets");
        stmt.execute("DROP TABLE IF EXISTS reservations");
        stmt.execute("DROP TABLE IF EXISTS customers");
        stmt.execute("DROP TABLE IF EXISTS seats");
        stmt.execute("DROP TABLE IF EXISTS schedules");
        stmt.execute("DROP TABLE IF EXISTS movies");
        stmt.execute("DROP TABLE IF EXISTS theaters");
        stmt.executeUpdate("SET FOREIGN_KEY_CHECKS=1");

        // 새로운 테이블 생성 및 데이터 주입
        createTables(stmt);
        insertExampleData(stmt);
    }

    private static void createTables(Statement stmt) throws SQLException {
        // theaters 테이블 생성
        stmt.execute("CREATE TABLE IF NOT EXISTS theaters (" +
                "theater_id INT PRIMARY KEY," +
                "seat_count INT," +
                "is_available BOOLEAN," +
                "seat_row INT," +
                "seat_column INT" +
                ")");

        // movies 테이블 생성
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
                "rating_score FLOAT" +
                ")");

        // schedules 테이블 생성
        stmt.execute("CREATE TABLE IF NOT EXISTS schedules (" +
                "schedule_id INT PRIMARY KEY," +
                "movie_id INT NOT NULL," +
                "theater_id INT NOT NULL," +
                "start_date DATE," +
                "day_of_week VARCHAR(10)," +
                "show_number INT," +
                "start_time TIME," +
                "FOREIGN KEY (movie_id) REFERENCES movies(movie_id)," +
                "FOREIGN KEY (theater_id) REFERENCES theaters(theater_id)" +
                ")");

        // seats 테이블 생성
        stmt.execute("CREATE TABLE IF NOT EXISTS seats (" +
                "seat_id INT PRIMARY KEY," +
                "seat_row INT NOT NULL," +
                "seat_col INT NOT NULL," +
                "theater_id INT NOT NULL," +
                "is_available BOOLEAN NOT NULL," +
                "FOREIGN KEY (theater_id) REFERENCES theaters(theater_id)" +
                ")");

        // customers 테이블 생성
        stmt.execute("CREATE TABLE IF NOT EXISTS customers (" +
                "customer_id INT PRIMARY KEY," +
                "customer_name VARCHAR(45) NOT NULL," +
                "customer_mobilenumber VARCHAR(20) NOT NULL," +
                "customer_emailaddress VARCHAR(45) NULL" +
                ")");

        // reservations 테이블 생성
        stmt.execute("CREATE TABLE IF NOT EXISTS reservations (" +
                "reservation_number INT PRIMARY KEY," +
                "reservation_payment VARCHAR(45)," +
                "reservation_status BOOLEAN NOT NULL," +
                "reservation_amount INT," +
                "customer_ID INT NOT NULL," +
                "reservation_date DATE," +
                "FOREIGN KEY (customer_ID) REFERENCES customers(customer_id)" +
                ")");

        // tickets 테이블 생성
        stmt.execute("CREATE TABLE IF NOT EXISTS tickets (" +
                "ticket_id INT PRIMARY KEY," +
                "schedule_id INT NOT NULL," +
                "theater_id INT NOT NULL," +
                "seat_id INT NOT NULL," +
                "reservation_number INT NOT NULL," +
                "is_purchase BOOLEAN NOT NULL," +
                "standard_price INT," +
                "selling_price INT," +
                "FOREIGN KEY (schedule_id) REFERENCES schedules(schedule_id)," +
                "FOREIGN KEY (theater_id) REFERENCES theaters(theater_id)," +
                "FOREIGN KEY (seat_id) REFERENCES seats(seat_id)," +
                "FOREIGN KEY (reservation_number) REFERENCES reservations(reservation_number)" +
                ")");
    }

    private static void insertExampleData(Statement stmt) throws SQLException {
        Statement stmt1 = con.createStatement();
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
        stmt1.executeUpdate(insertMoviesData);

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
        stmt1.executeUpdate(insertTheatersData);

        String insertCustomersData = "INSERT INTO customers (customer_id, customer_name, customer_mobilenumber, customer_emailaddress) VALUES "
                + "(1, 'Kim', '01012345678', 'Kim@example.com')";
        stmt1.executeUpdate(insertCustomersData);


        String insertSchedulesData = "INSERT INTO schedules (schedule_id, movie_id, theater_id, start_date, day_of_week, show_number, start_time) VALUES "
                + "(1, 1, 1, '2024-01-01', 'Monday', 1, '10:00:00'),"
                + "(2, 1, 2, '2024-02-01', 'Tuesday', 2, '14:00:00'),"
                + "(3, 2, 3, '2024-03-02', 'Wednesday', 1, '12:00:00'),"
                + "(4, 2, 4, '2024-04-02', 'Thursday', 2, '16:00:00'),"
                + "(5, 3, 5, '2024-05-03', 'Wednesday', 1, '11:00:00'),"
                + "(6, 3, 6, '2024-06-03', 'Friday', 2, '15:00:00'),"
                + "(7, 4, 7, '2024-07-04', 'Thursday', 1, '13:00:00'),"
                + "(8, 4, 8, '2024-08-04', 'Saturday', 2, '17:00:00'),"
                + "(9, 5, 9, '2024-09-05', 'Friday', 1, '10:30:00'),"
                + "(10, 5, 10, '2024-10-05', 'Sunday', 2, '14:30:00'),"
                + "(11, 6, 11, '2024-11-06', 'Saturday', 1, '11:30:00'),"
                + "(12, 6, 12, '2024-12-06', 'Monday', 2, '15:30:00'),"
                + "(13, 7, 1, '2024-01-07', 'Sunday', 1, '09:00:00'),"
                + "(14, 7, 2, '2024-02-07', 'Tuesday', 2, '13:00:00'),"
                + "(15, 8, 3, '2024-03-08', 'Monday', 1, '12:30:00'),"
                + "(16, 8, 4, '2024-04-08', 'Wednesday', 2, '16:30:00'),"
                + "(17, 9, 5, '2024-05-09', 'Tuesday', 1, '11:00:00'),"
                + "(18, 9, 6, '2024-06-09', 'Thursday', 2, '15:00:00'),"
                + "(19, 10, 7, '2024-07-10', 'Wednesday', 1, '10:15:00'),"
                + "(20, 10, 8, '2024-08-10', 'Friday', 2, '14:15:00'),"
                + "(21, 11, 9, '2024-09-11', 'Thursday', 1, '12:45:00'),"
                + "(22, 11, 10, '2024-10-11', 'Saturday', 2, '16:45:00'),"
                + "(23, 12, 11, '2024-11-12', 'Friday', 1, '11:15:00'),"
                + "(24, 12, 12, '2024-12-12', 'Sunday', 2, '15:15:00')";
        stmt1.executeUpdate(insertSchedulesData);

        String insertReservationsData = "INSERT INTO reservations (reservation_number, reservation_payment, reservation_status, reservation_amount, customer_id, reservation_date) VALUES "
                + "(1, 'Credit Card', 1, 15000, 1, '2024-06-01 15:30:00'),"
                + "(2, 'PayPal', 1, 20000, 1, '2024-06-02 10:45:00'),"
                + "(3, 'Credit Card', 1, 12000, 1, '2024-06-03 14:20:00'),"
                + "(4, 'Cash', 1, 18000, 1, '2024-06-04 13:00:00'),"
                + "(5, 'Credit Card', 1, 25000, 1, '2024-06-05 16:10:00'),"
                + "(6, 'Credit Card', 1, 30000, 1, '2024-06-06 11:55:00'),"
                + "(7, 'PayPal', 1, 21000, 1, '2024-06-07 09:30:00'),"
                + "(8, 'Cash', 1, 19000, 1, '2024-06-08 12:40:00'),"
                + "(9, 'Credit Card', 1, 22000, 1, '2024-06-09 15:15:00'),"
                + "(10, 'Credit Card', 1, 14000, 1, '2024-06-10 10:20:00'),"
                + "(11, 'Cash', 1, 17000, 1, '2024-06-11 14:00:00'),"
                + "(12, 'PayPal', 1, 16000, 1, '2024-06-12 11:25:00')";
        stmt1.executeUpdate(insertReservationsData);


        String inserSeatsData = "INSERT INTO seats (seat_id, seat_row, seat_col, theater_id, is_available) VALUES "
                //1관
                +"(1, 1, 1, 1, 1),"
                +"(2, 1, 2, 1, 1),"
                +"(3, 1, 3, 1, 1),"
                +"(4, 1, 4, 1, 1),"
                +"(5, 1, 5, 1, 1),"
                +"(6, 2, 1, 1, 1),"
                +"(7, 2, 2, 1, 1),"
                +"(8, 2, 3, 1, 1),"
                +"(9, 2, 4, 1, 1),"
                +"(10, 2, 5, 1, 1),"
                +"(11, 3, 1, 1, 1),"
                +"(12, 3, 2, 1, 1),"
                +"(13, 3, 3, 1, 1),"
                +"(14, 3, 4, 1, 1),"
                +"(15, 3, 5, 1, 1),"
                +"(16, 4, 1, 1, 1),"
                +"(17, 4, 2, 1, 1),"
                +"(18, 4, 3, 1, 1),"
                +"(19, 4, 4, 1, 1),"
                +"(20, 4, 5, 1, 1),"
                +"(21, 5, 1, 1, 1),"
                +"(22, 5, 2, 1, 1),"
                +"(23, 5, 3, 1, 1),"
                +"(24, 5, 4, 1, 1),"
                +"(25, 5, 5, 1, 1),"
                +"(26, 6, 1, 1, 1),"
                +"(27, 6, 2, 1, 1),"
                +"(28, 6, 3, 1, 1),"
                +"(29, 6, 4, 1, 1),"
                +"(30, 6, 5, 1, 1),"

                //2관
                +"(31, 1, 1, 2, 1),"
                +"(32, 1, 2, 2, 1),"
                +"(33, 1, 3, 2, 1),"
                +"(34, 2, 1, 2, 1),"
                +"(35, 2, 2, 2, 1),"
                +"(36, 2, 3, 2, 1),"
                +"(37, 3, 1, 2, 1),"
                +"(38, 3, 2, 2, 1),"
                +"(39, 3, 3, 2, 1),"
                +"(40, 4, 1, 2, 1),"
                +"(41, 4, 2, 2, 1),"
                +"(42, 4, 3, 2, 1),"

                //3관
                +"(43, 1, 1, 3, 1),"
                +"(44, 1, 2, 3, 1),"
                +"(45, 1, 3, 3, 1),"
                +"(46, 1, 4, 3, 1),"
                +"(47, 2, 1, 3, 1),"
                +"(48, 2, 2, 3, 1),"
                +"(49, 2, 3, 3, 1),"
                +"(50, 2, 4, 3, 1),"
                +"(51, 3, 1, 3, 1),"
                +"(52, 3, 2, 3, 1),"
                +"(53, 3, 3, 3, 1),"
                +"(54, 3, 4, 3, 1),"
                +"(55, 4, 1, 3, 1),"
                +"(56, 4, 2, 3, 1),"
                +"(57, 4, 3, 3, 1),"
                +"(58, 4, 4, 3, 1),"
                +"(59, 5, 1, 3, 1),"
                +"(60, 5, 2, 3, 1),"
                +"(61, 5, 3, 3, 1),"
                +"(62, 5, 4, 3, 1),"

                //4
                +"(63, 1, 1, 4, 1),"
                +"(64, 1, 2, 4, 1),"
                +"(65, 1, 3, 4, 1),"
                +"(66, 1, 4, 4, 1),"
                +"(67, 1, 5, 4, 1),"
                +"(68, 2, 1, 4, 1),"
                +"(69, 2, 2, 4, 1),"
                +"(70, 2, 3, 4, 1),"
                +"(71, 2, 4, 4, 1),"
                +"(72, 2, 5, 4, 1),"
                +"(73, 3, 1, 4, 1),"
                +"(74, 3, 2, 4, 1),"
                +"(75, 3, 3, 4, 1),"
                +"(76, 3, 4, 4, 1),"
                +"(77, 3, 5, 4, 1),"
                +"(78, 4, 1, 4, 1),"
                +"(79, 4, 2, 4, 1),"
                +"(80, 4, 3, 4, 1),"
                +"(81, 4, 4, 4, 1),"
                +"(82, 4, 5, 4, 1),"
                +"(83, 5, 1, 4, 1),"
                +"(84, 5, 2, 4, 1),"
                +"(85, 5, 3, 4, 1),"
                +"(86, 5, 4, 4, 1),"
                +"(87, 5, 5, 4, 1),"

                //5
                +"(88, 1, 1, 5, 1),"
                +"(89, 1, 2, 5, 1),"
                +"(90, 1, 3, 5, 1),"
                +"(91, 1, 4, 5, 1),"
                +"(92, 1, 5, 5, 1),"
                +"(93, 2, 1, 5, 1),"
                +"(94, 2, 2, 5, 1),"
                +"(95, 2, 3, 5, 1),"
                +"(96, 2, 4, 5, 1),"
                +"(97, 2, 5, 5, 1),"
                +"(98, 3, 1, 5, 1),"
                +"(99, 3, 2, 5, 1),"
                +"(100, 3, 3, 5, 1),"
                +"(101, 3, 4, 5, 1),"
                +"(102, 3, 5, 5, 1),"

                //6
                +"(103, 1, 1, 6, 1),"
                +"(104, 1, 2, 6, 1),"
                +"(105, 1, 3, 6, 1),"
                +"(106, 2, 1, 6, 1),"
                +"(107, 2, 2, 6, 1),"
                +"(108, 2, 3, 6, 1),"

                //7
                +"(109, 1, 1, 7, 1),"
                +"(110, 1, 2, 7, 1),"
                +"(111, 1, 3, 7, 1),"
                +"(112, 1, 4, 7, 1),"
                +"(113, 2, 1, 7, 1),"
                +"(114, 2, 2, 7, 1),"
                +"(115, 2, 3, 7, 1),"
                +"(116, 2, 4, 7, 1),"
                +"(117, 3, 1, 7, 1),"
                +"(118, 3, 2, 7, 1),"
                +"(119, 3, 3, 7, 1),"
                +"(120, 3, 4, 7, 1),"
                +"(121, 4, 1, 7, 1),"
                +"(122, 4, 2, 7, 1),"
                +"(123, 4, 3, 7, 1),"
                +"(124, 4, 4, 7, 1),"

                //8
                +"(125, 1, 1, 8, 1),"
                +"(126, 2, 1, 8, 1),"

                //9
                +"(127, 1, 1, 9, 1),"
                +"(128, 1, 2, 9, 1),"
                +"(129, 2, 1, 9, 1),"
                +"(130, 2, 2, 9, 1),"
                +"(131, 3, 1, 9, 1),"
                +"(132, 3, 2, 9, 1),"
                +"(133, 4, 1, 9, 1),"
                +"(134, 4, 2, 9, 1),"

                //10
                +"(135, 1, 1, 10, 1),"
                +"(136, 1, 2, 10, 1),"
                +"(137, 1, 3, 10, 1),"
                +"(138, 1, 4, 10, 1),"
                +"(139, 1, 5, 10, 1),"
                +"(140, 1, 6, 10, 1),"
                +"(141, 1, 7, 10, 1),"
                +"(142, 1, 8, 10, 1),"
                +"(143, 1, 9, 10, 1),"
                +"(144, 1, 10, 10, 1),"
                +"(145, 2, 1, 10, 1),"
                +"(146, 2, 2, 10, 1),"
                +"(147, 2, 3, 10, 1),"
                +"(148, 2, 4, 10, 1),"
                +"(149, 2, 5, 10, 1),"
                +"(150, 2, 6, 10, 1),"
                +"(151, 2, 7, 10, 1),"
                +"(152, 2, 8, 10, 1),"
                +"(153, 2, 9, 10, 1),"
                +"(154, 2, 10, 10, 1),"
                +"(155, 3, 1, 10, 1),"
                +"(156, 3, 2, 10, 1),"
                +"(157, 3, 3, 10, 1),"
                +"(158, 3, 4, 10, 1),"
                +"(159, 3, 5, 10, 1),"
                +"(160, 3, 6, 10, 1),"
                +"(161, 3, 7, 10, 1),"
                +"(162, 3, 8, 10, 1),"
                +"(163, 3, 9, 10, 1),"
                +"(164, 3, 10, 10, 1),"
                +"(165, 4, 1, 10, 1),"
                +"(166, 4, 2, 10, 1),"
                +"(167, 4, 3, 10, 1),"
                +"(168, 4, 4, 10, 1),"
                +"(169, 4, 5, 10, 1),"
                +"(170, 4, 6, 10, 1),"
                +"(171, 4, 7, 10, 1),"
                +"(172, 4, 8, 10, 1),"
                +"(173, 4, 9, 10, 1),"
                +"(174, 4, 10, 10, 1),"
                +"(175, 5, 1, 10, 1),"
                +"(176, 5, 2, 10, 1),"
                +"(177, 5, 3, 10, 1),"
                +"(178, 5, 4, 10, 1),"
                +"(179, 5, 5, 10, 1),"
                +"(180, 5, 6, 10, 1),"
                +"(181, 5, 7, 10, 1),"
                +"(182, 5, 8, 10, 1),"
                +"(183, 5, 9, 10, 1),"
                +"(184, 5, 10, 10, 1),"
                +"(185, 6, 1, 10, 1),"
                +"(186, 6, 2, 10, 1),"
                +"(187, 6, 3, 10, 1),"
                +"(188, 6, 4, 10, 1),"
                +"(189, 6, 5, 10, 1),"
                +"(190, 6, 6, 10, 1),"
                +"(191, 6, 7, 10, 1),"
                +"(192, 6, 8, 10, 1),"
                +"(193, 6, 9, 10, 1),"
                +"(194, 6, 10, 10, 1),"
                +"(195, 7, 1, 10, 1),"
                +"(196, 7, 2, 10, 1),"
                +"(197, 7, 3, 10, 1),"
                +"(198, 7, 4, 10, 1),"
                +"(199, 7, 5, 10, 1),"
                +"(200, 7, 6, 10, 1),"
                +"(201, 7, 7, 10, 1),"
                +"(202, 7, 8, 10, 1),"
                +"(203, 7, 9, 10, 1),"
                +"(204, 7, 10, 10, 1),"
                +"(205, 8, 1, 10, 1),"
                +"(206, 8, 2, 10, 1),"
                +"(207, 8, 3, 10, 1),"
                +"(208, 8, 4, 10, 1),"
                +"(209, 8, 5, 10, 1),"
                +"(210, 8, 6, 10, 1),"
                +"(211, 8, 7, 10, 1),"
                +"(212, 8, 8, 10, 1),"
                +"(213, 8, 9, 10, 1),"
                +"(214, 8, 10, 10, 1),"
                +"(215, 9, 1, 10, 1),"
                +"(216, 9, 2, 10, 1),"
                +"(217, 9, 3, 10, 1),"
                +"(218, 9, 4, 10, 1),"
                +"(219, 9, 5, 10, 1),"
                +"(220, 9, 6, 10, 1),"
                +"(221, 9, 7, 10, 1),"
                +"(222, 9, 8, 10, 1),"
                +"(223, 9, 9, 10, 1),"
                +"(224, 9, 10, 10, 1),"
                +"(225, 10, 1, 10, 1),"
                +"(226, 10, 2, 10, 1),"
                +"(227, 10, 3, 10, 1),"
                +"(228, 10, 4, 10, 1),"
                +"(229, 10, 5, 10, 1),"
                +"(230, 10, 6, 10, 1),"
                +"(231, 10, 7, 10, 1),"
                +"(232, 10, 8, 10, 1),"
                +"(233, 10, 9, 10, 1),"
                +"(234, 10, 10, 10, 1),"
                +"(235, 11, 1, 10, 1),"
                +"(236, 11, 2, 10, 1),"
                +"(237, 11, 3, 10, 1),"
                +"(238, 11, 4, 10, 1),"
                +"(239, 11, 5, 10, 1),"
                +"(240, 11, 6, 10, 1),"
                +"(241, 11, 7, 10, 1),"
                +"(242, 11, 8, 10, 1),"
                +"(243, 11, 9, 10, 1),"
                +"(244, 11, 10, 10, 1),"
                +"(245, 12, 1, 10, 1),"
                +"(246, 12, 2, 10, 1),"
                +"(247, 12, 3, 10, 1),"
                +"(248, 12, 4, 10, 1),"
                +"(249, 12, 5, 10, 1),"
                +"(250, 12, 6, 10, 1),"
                +"(251, 12, 7, 10, 1),"
                +"(252, 12, 8, 10, 1),"
                +"(253, 12, 9, 10, 1),"
                +"(254, 12, 10, 10, 1),"
                +"(255, 13, 1, 10, 1),"
                +"(256, 13, 2, 10, 1),"
                +"(257, 13, 3, 10, 1),"
                +"(258, 13, 4, 10, 1),"
                +"(259, 13, 5, 10, 1),"
                +"(260, 13, 6, 10, 1),"
                +"(261, 13, 7, 10, 1),"
                +"(262, 13, 8, 10, 1),"
                +"(263, 13, 9, 10, 1),"
                +"(264, 13, 10, 10, 1),"
                +"(265, 14, 1, 10, 1),"
                +"(266, 14, 2, 10, 1),"
                +"(267, 14, 3, 10, 1),"
                +"(268, 14, 4, 10, 1),"
                +"(269, 14, 5, 10, 1),"
                +"(270, 14, 6, 10, 1),"
                +"(271, 14, 7, 10, 1),"
                +"(272, 14, 8, 10, 1),"
                +"(273, 14, 9, 10, 1),"
                +"(274, 14, 10, 10, 1),"

                //11
                +"(275, 1, 1, 11, 1),"
                +"(276, 1, 2, 11, 1),"
                +"(277, 1, 3, 11, 1),"
                +"(278, 1, 4, 11, 1),"
                +"(279, 2, 1, 11, 1),"
                +"(280, 2, 2, 11, 1),"
                +"(281, 2, 3, 11, 1),"
                +"(282, 2, 4, 11, 1),"
                +"(283, 3, 1, 11, 1),"
                +"(284, 3, 2, 11, 1),"
                +"(285, 3, 3, 11, 1),"
                +"(286, 3, 4, 11, 1),"
                +"(287, 4, 1, 11, 1),"
                +"(288, 4, 2, 11, 1),"
                +"(289, 4, 3, 11, 1),"
                +"(290, 4, 4, 11, 1),"
                +"(291, 5, 1, 11, 1),"
                +"(292, 5, 2, 11, 1),"
                +"(293, 5, 3, 11, 1),"
                +"(294, 5, 4, 11, 1),"
                +"(295, 6, 1, 11, 1),"
                +"(296, 6, 2, 11, 1),"
                +"(297, 6, 3, 11, 1),"
                +"(298, 6, 4, 11, 1),"

                //12
                +"(299, 1, 1, 12, 1),"
                +"(300, 1, 2, 12, 1),"
                +"(301, 1, 3, 12, 1),"
                +"(302, 2, 1, 12, 1),"
                +"(303, 2, 2, 12, 1),"
                +"(304, 2, 3, 12, 1),"
                +"(305, 3, 1, 12, 1),"
                +"(306, 3, 2, 12, 1),"
                +"(307, 3, 3, 12, 1),"
                +"(308, 4, 1, 12, 1),"
                +"(309, 4, 2, 12, 1),"
                +"(310, 4, 3, 12, 1),"
                +"(311, 5, 1, 12, 1),"
                +"(312, 5, 2, 12, 1),"
                +"(313, 5, 3, 12, 1),"
                +"(314, 6, 1, 12, 1),"
                +"(315, 6, 2, 12, 1),"
                +"(316, 6, 3, 12, 1)";
        stmt1.executeUpdate(inserSeatsData);

        // Insert data into tickets table
        String insertTicketsData = "INSERT INTO tickets (ticket_id, schedule_id, theater_id, seat_id, reservation_number, is_purchase, standard_price, selling_price) VALUES "
                + "(1, 1, 1, 1, 1, 1, 10000, 9000),"
                + "(2, 1, 1, 2, 2, 1, 10000, 9500),"
                + "(3, 2, 1, 3, 3, 1, 10000, 10000),"
                + "(4, 2, 1, 4, 4, 1, 10000, 10500),"
                + "(5, 3, 2, 5, 5, 1, 12000, 11500),"
                + "(6, 3, 2, 6, 6, 1, 12000, 12000),"
                + "(7, 4, 2, 7, 7, 1, 12000, 12500),"
                + "(8, 4, 2, 8, 8, 1, 12000, 11000),"
                + "(9, 5, 3, 9, 9, 1, 11000, 10500),"
                + "(10, 5, 3, 10, 10, 1, 11000, 11500),"
                + "(11, 6, 3, 11, 11, 1, 11000, 11000),"
                + "(12, 6, 3, 12, 12, 1, 11000, 12000)";
        stmt1.executeUpdate(insertTicketsData);
    }

    public static void main(String[] args) {
        run();
    }
}
