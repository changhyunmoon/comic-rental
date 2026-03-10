package com.programmers.team6.comic_rental.repository;

import com.programmers.team6.comic_rental.entity.Rental;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

// ✅ 새로 추가한 import
// ResultSet: DB 조회 결과를 받아오는 객체 (SELECT 할 때 필요)
// Types: null을 DB에 넣을 때 타입 지정용 (setNull에 필요)
// LocalDateTime: 반납일 파라미터 타입
// ArrayList, List: 목록 조회 결과를 담는 컬렉션
import java.sql.ResultSet;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.programmers.team6.global.jdbc.DBConnectionUtil.close;
import static com.programmers.team6.global.jdbc.DBConnectionUtil.getConnection;

public class RentalRepository {

    // ==============================
    // 창현님 코드 기반 (버그 1개 수정)
    // ==============================
    public Rental save(Rental rental) throws SQLException {

        String sql = "insert into rental(rent_date, return_date, " +
                "created_date, updated_date, comic_id, member_id) " +
                "values(?, ?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            // 코드 그대로
            pstmt.setTimestamp(1, Timestamp.valueOf(rental.getRentDate()));

            // ✅ 버그 수정: returnDate null 처리
            // 창현님 코드: pstmt.setTimestamp(2, Timestamp.valueOf(rental.getReturnDate()));
            // → 문제: 대여 시점엔 returnDate가 null인데
            //         Timestamp.valueOf(null) 하면 NullPointerException 오류 터짐
            // 수정: setNull()로 DB에 null 직접 삽입
            //       return_date가 null = 아직 반납 안 됨을 의미
            pstmt.setNull(2, Types.TIMESTAMP);

            pstmt.setTimestamp(3, Timestamp.valueOf(rental.getCreatedDate()));
            pstmt.setTimestamp(4, Timestamp.valueOf(rental.getUpdatedDate()));
            pstmt.setLong(5, rental.getComicId());
            pstmt.setLong(6, rental.getMemberId());

            int savedRows = pstmt.executeUpdate();

            if (savedRows == 0) {
                throw new SQLException("[Error] Rental 저장 실패");
            }

            return rental;

        } catch (SQLException e) {
            System.err.println("[Error] Rental 저장 중 DB 오류 발생: " + e.getMessage());
            e.printStackTrace();
            throw e;

        } finally {
            close(con, pstmt, null);
        }
    }

    // ==============================
    // ✅ 새로 추가: 반납 처리
    // 이유: 코드에 반납 기능이 없었음
    // 흐름: rentalId로 해당 대여건 찾아서 return_date를 지금 시간으로 업데이트
    //       return_date가 null → 반납일 입력 = 반납 완료
    // ==============================
    public void returnRental(Long rentalId, LocalDateTime returnDate) throws SQLException {
        String sql = "UPDATE rental SET return_date = ?, updated_date = ? WHERE rental_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setTimestamp(1, Timestamp.valueOf(returnDate));
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setLong(3, rentalId);

            int rows = pstmt.executeUpdate();
            // 업데이트된 행이 0이면 해당 rentalId가 DB에 없다는 뜻
            if (rows == 0) {
                throw new SQLException("해당 대여 ID가 없습니다: " + rentalId);
            }
        } catch (SQLException e) {
            System.err.println("[Error] 반납 처리 실패: " + e.getMessage());
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }

    // ==============================
    // ✅ 새로 추가: 전체 대여 목록 조회
    // 이유: 코드에 목록 조회 기능이 없었음
    // 흐름: DB rental 테이블 전체 SELECT → Rental 객체 리스트로 변환해서 반환
    //       return_date가 null인 것 = 미반납, 값 있는 것 = 반납 완료
    // ==============================
    public List<Rental> findAll() throws SQLException {
        String sql = "SELECT rental_id, comic_id, member_id, rent_date, return_date, " +
                "created_date, updated_date FROM rental ORDER BY rental_id";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Rental> list = new ArrayList<>();

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                // return_date는 null일 수 있어서 따로 꺼내서 null 체크
                // null이면 그대로 null, 값 있으면 LocalDateTime으로 변환
                Timestamp returnTs = rs.getTimestamp("return_date");
                Rental rental = new Rental(
                        rs.getLong("rental_id"),
                        rs.getLong("comic_id"),
                        rs.getLong("member_id"),
                        rs.getTimestamp("rent_date").toLocalDateTime(),
                        returnTs != null ? returnTs.toLocalDateTime() : null,
                        rs.getTimestamp("created_date").toLocalDateTime(),
                        rs.getTimestamp("updated_date").toLocalDateTime()
                );
                list.add(rental);
            }
            return list;
        } catch (SQLException e) {
            System.err.println("[Error] 대여 목록 조회 실패: " + e.getMessage());
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    // ==============================
    // ✅ 새로 추가: 단건 조회
    // 이유: 반납할 때 해당 rentalId가 실제로 존재하는지,
    //       그리고 이미 반납된 건인지 확인하려면 단건 조회가 필요함
    // 흐름: rentalId로 DB 조회 → 없으면 오류, 있으면 Rental 객체 반환
    // ==============================
    public Rental findById(Long rentalId) throws SQLException {
        String sql = "SELECT rental_id, comic_id, member_id, rent_date, return_date, " +
                "created_date, updated_date FROM rental WHERE rental_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, rentalId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                Timestamp returnTs = rs.getTimestamp("return_date");
                return new Rental(
                        rs.getLong("rental_id"),
                        rs.getLong("comic_id"),
                        rs.getLong("member_id"),
                        rs.getTimestamp("rent_date").toLocalDateTime(),
                        returnTs != null ? returnTs.toLocalDateTime() : null,
                        rs.getTimestamp("created_date").toLocalDateTime(),
                        rs.getTimestamp("updated_date").toLocalDateTime()
                );
            } else {
                // 조회 결과가 없으면 = 해당 rentalId가 DB에 없다는 뜻
                throw new SQLException("해당 대여 ID가 없습니다: " + rentalId);
            }
        } catch (SQLException e) {
            System.err.println("[Error] 단건 조회 실패: " + e.getMessage());
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }

    // ==============================
    // ✅ 새로 추가: 대여중 여부 확인
    // 이유: 같은 만화책을 두 명이 동시에 빌리는 걸 막기 위해 필요
    // 흐름: comicId로 조회했을 때 return_date가 null인 행이 있으면
    //       = 아직 반납 안 된 대여가 있다 = 대여중
    //       COUNT(*)가 0보다 크면 true(대여중), 0이면 false(대여가능)
    // ==============================
    public boolean isRented(Long comicId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM rental WHERE comic_id = ? AND return_date IS NULL";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, comicId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("[Error] 대여 여부 확인 실패: " + e.getMessage());
            throw e;
        } finally {
            close(con, pstmt, rs);
        }
    }
}