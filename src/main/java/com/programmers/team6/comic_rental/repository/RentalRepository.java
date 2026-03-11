package com.programmers.team6.comic_rental.repository;

import com.programmers.team6.comic_rental.entity.Rental;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RentalRepository {

    private static final Dotenv dotenv = Dotenv.configure().load();
    private static final String URL = dotenv.get("DB_URL");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASSWORD = dotenv.get("DB_PASS");

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * 대여 등록 - rental 테이블에 새 행 INSERT
     * return_date는 반납 전이므로 NULL로 저장
     */
    public long save(long comicId, long memberId) {
        String sql = """
                INSERT INTO rental (comic_id, member_id, rent_date, return_date, created_date, updated_date)
                VALUES (?, ?, NOW(), NULL, NOW(), NOW())
                """;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setLong(1, comicId);
            pstmt.setLong(2, memberId);
            pstmt.executeUpdate();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);   // 생성된 rental_id 반환
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("rental save 오류: " + e.getMessage(), e);
        }

        throw new RuntimeException("rental_id를 가져오지 못했습니다.");
    }

    /**
     * 반납 처리 - return_date를 현재 시각으로 UPDATE
     */
    public boolean returnRental(long rentalId) {
        String sql = """
                UPDATE rental
                SET return_date = NOW(), updated_date = NOW()
                WHERE rental_id = ?
                """;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, rentalId);
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("returnRental 오류: " + e.getMessage(), e);
        }
    }

    /**
     * 특정 rental_id로 단건 조회
     */
    public Rental findById(long rentalId) {
        String sql = """
                SELECT rental_id, comic_id, member_id, rent_date, return_date, created_date, updated_date
                FROM rental
                WHERE rental_id = ?
                """;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, rentalId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("findById 오류: " + e.getMessage(), e);
        }

        return null;
    }

    /**
     * 전체 대여 내역 조회 (반납 완료 포함)
     */
    public List<Rental> findAll() {
        String sql = """
                SELECT rental_id, comic_id, member_id, rent_date, return_date, created_date, updated_date
                FROM rental
                ORDER BY rental_id
                """;

        return executeQuery(sql);
    }

    /**
     * 미반납 대여만 조회 (return_date가 NULL인 것)
     */
    public List<Rental> findAllOpen() {
        String sql = """
                SELECT rental_id, comic_id, member_id, rent_date, return_date, created_date, updated_date
                FROM rental
                WHERE return_date IS NULL
                ORDER BY rental_id
                """;

        return executeQuery(sql);
    }

    /**
     * 연체 목록 조회
     * 미반납(return_date IS NULL) + 대여일 기준 7일 초과
     */
    public List<Rental> findAllOverdue() {
        String sql = """
                SELECT rental_id, comic_id, member_id, rent_date, return_date, created_date, updated_date
                FROM rental
                WHERE return_date IS NULL
                AND rent_date < DATE_SUB(NOW(), INTERVAL 7 DAY)
                ORDER BY rental_id
                """;

        return executeQuery(sql);
    }

    /**
     * 특정 회원의 미반납 대여 조회
     * 연체 여부 확인에 사용
     */
    public List<Rental> findOpenByMemberId(long memberId) {
        String sql = """
                SELECT rental_id, comic_id, member_id, rent_date, return_date, created_date, updated_date
                FROM rental
                WHERE member_id = ?
                AND return_date IS NULL
                ORDER BY rental_id
                """;

        List<Rental> rentals = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, memberId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    rentals.add(mapRow(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("findOpenByMemberId 오류: " + e.getMessage(), e);
        }

        return rentals;
    }

    // SQL 실행 + ResultSet → List<Rental> 변환 공통 메서드
    private List<Rental> executeQuery(String sql) {
        List<Rental> rentals = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                rentals.add(mapRow(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("rental 목록 조회 오류: " + e.getMessage(), e);
        }

        return rentals;
    }

    // ResultSet 한 행 → Rental 객체로 변환
    private Rental mapRow(ResultSet rs) throws SQLException {
        Rental rental = new Rental();
        rental.setRentalId(rs.getLong("rental_id"));
        rental.setComicId(rs.getLong("comic_id"));
        rental.setMemberId(rs.getLong("member_id"));

        Timestamp rentDate = rs.getTimestamp("rent_date");
        if (rentDate != null) rental.setRentDate(rentDate.toLocalDateTime());

        Timestamp returnDate = rs.getTimestamp("return_date");
        if (returnDate != null) rental.setReturnDate(returnDate.toLocalDateTime());

        Timestamp created = rs.getTimestamp("created_date");
        if (created != null) rental.setCreatedDate(created.toLocalDateTime());

        Timestamp updated = rs.getTimestamp("updated_date");
        if (updated != null) rental.setUpdatedDate(updated.toLocalDateTime());

        return rental;
    }
}