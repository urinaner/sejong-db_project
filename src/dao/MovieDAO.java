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

    public List<Movie> getAllMovies() {
        List<Movie> movies = new ArrayList<>();

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
//            Connection conn = DB_Connect.getConnection();

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

    // 새로운 검색 메소드 추가
    public List<Movie> searchMovies(String title, String director, String actors, String genre) {
        List<Movie> movies = new ArrayList<>();
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DB_Connect.getConnection();
            StringBuilder sql = new StringBuilder("SELECT * FROM movies WHERE 1=1");
            if (title != null && !title.isEmpty()) sql.append(" AND title LIKE ?");
            if (director != null && !director.isEmpty()) sql.append(" AND director LIKE ?");
            if (actors != null && !actors.isEmpty()) sql.append(" AND actors LIKE ?");
            if (genre != null && !genre.isEmpty()) sql.append(" AND genre LIKE ?");

            pstmt = con.prepareStatement(sql.toString());
            int paramIndex = 1;
            if (title != null && !title.isEmpty()) pstmt.setString(paramIndex++, "%" + title + "%");
            if (director != null && !director.isEmpty()) pstmt.setString(paramIndex++, "%" + director + "%");
            if (actors != null && !actors.isEmpty()) pstmt.setString(paramIndex++, "%" + actors + "%");
            if (genre != null && !genre.isEmpty()) pstmt.setString(paramIndex++, "%" + genre + "%");

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
