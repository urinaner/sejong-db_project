package dao;

import dto.Movie;
import util.DB_Close;
import util.DB_Connect;

import java.sql.*;
import java.util.*;

public class MovieDAO {
    private static MovieDAO instance = new MovieDAO();

    private MovieDAO() {
    }

    public static MovieDAO getInstance() {
        return instance;
    }

    // 메소드 추가: 영화 데이터베이스에서 모든 영화를 가져오는 메서드
    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DB_Connect.getConnection();
            String sql = "SELECT * FROM movies";
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Movie movie = new Movie();
                movie.setMovieId(rs.getInt("movie_id"));
                movie.setTitle(rs.getString("title"));
                movie.setRunningTime(rs.getString("running_time"));
                movie.setRating(rs.getString("rating"));
                movie.setDirector(rs.getString("director"));
                movie.setActors(rs.getString("actors"));
                movie.setGenre(rs.getString("genre"));
                movie.setIntroduce(rs.getString("introduce"));
                movie.setOpenDate(rs.getString("open_date"));
                movie.setRatingScore(rs.getInt("rating_score"));

                movies.add(movie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DB_Close.close(rs, pstmt, con);
        }

        return movies;
    }
}
