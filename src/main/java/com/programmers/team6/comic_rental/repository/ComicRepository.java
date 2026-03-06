package main.java.com.programmers.team6.comic_rental.repository;

import main.java.com.programmers.team6.comic_rental.entity.Comic;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ComicRepository {

    private static final String URL = "jdbc:mysql://localhost:3306/comic_rental?serverTimezone=Asia/Seoul";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC 드라이버를 찾을 수 없습니다.", e);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public long save(String title, String author) {
        String sql = """
                INSERT INTO comic (title, author, is_rented, created_date, updated_date)
                VALUES (?, ?, false, NOW(), NOW())
                """;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, title);
            pstmt.setString(2, author);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("만화책 등록 실패");
            }

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }

            throw new SQLException("생성된 comic_id를 가져오지 못했습니다.");

        } catch (SQLException e) {
            throw new RuntimeException("save 오류", e);
        }
    }

    public List<Comic> findAll() {
        String sql = """
                SELECT comic_id, title, author, is_rented, created_date, updated_date
                FROM comic
                ORDER BY comic_id
                """;

        List<Comic> comics = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                comics.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("findAll 오류", e);
        }

        return comics;
    }

    public Comic findById(long comicId) {
        String sql = """
                SELECT comic_id, title, author, is_rented, created_date, updated_date
                FROM comic
                WHERE comic_id = ?
                """;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, comicId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("findById 오류", e);
        }

        return null;
    }

    public boolean update(long comicId, String title, String author) {
        String sql = """
                UPDATE comic
                SET title = ?, author = ?, updated_date = NOW()
                WHERE comic_id = ?
                """;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setLong(3, comicId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("update 오류", e);
        }
    }

    public boolean delete(long comicId) {
        String sql = "DELETE FROM comic WHERE comic_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, comicId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("delete 오류", e);
        }
    }

    public boolean existsById(long comicId) {
        String sql = "SELECT 1 FROM comic WHERE comic_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, comicId);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new RuntimeException("existsById 오류", e);
        }
    }

    public boolean isRented(long comicId) {
        String sql = "SELECT is_rented FROM comic WHERE comic_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, comicId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("is_rented");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("isRented 오류", e);
        }

        throw new IllegalArgumentException("존재하지 않는 comic_id 입니다.");
    }

    public boolean updateRentalStatus(long comicId, boolean rented) {
        String sql = """
                UPDATE comic
                SET is_rented = ?, updated_date = NOW()
                WHERE comic_id = ?
                """;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBoolean(1, rented);
            pstmt.setLong(2, comicId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("updateRentalStatus 오류", e);
        }
    }

    private Comic mapRow(ResultSet rs) throws SQLException {
        Comic comic = new Comic();
        comic.setComicId(rs.getLong("comic_id"));
        comic.setTitle(rs.getString("title"));
        comic.setAuthor(rs.getString("author"));
        comic.setRented(rs.getBoolean("is_rented"));

        Timestamp created = rs.getTimestamp("created_date");
        if (created != null) {
            comic.setCreatedDate(created.toLocalDateTime());
        }

        Timestamp updated = rs.getTimestamp("updated_date");
        if (updated != null) {
            comic.setUpdatedDate(updated.toLocalDateTime());
        }

        return comic;
    }
}