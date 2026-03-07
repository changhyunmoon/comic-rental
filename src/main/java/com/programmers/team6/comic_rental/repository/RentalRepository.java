package com.programmers.team6.comic_rental.repository;

import com.programmers.team6.comic_rental.entity.Rental;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import static com.programmers.team6.global.jdbc.DBConnectionUtil.close;
import static com.programmers.team6.global.jdbc.DBConnectionUtil.getConnection;

public class RentalRepository {

    public Rental save(Rental rental) throws SQLException {

        String sql = "insert into rental(rent_date, return_date, " +
                "created_date, updated_date, comic_id, member_id) " +
                "values(?, ?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);

            // 파라미터 바인딩
            pstmt.setTimestamp(1, Timestamp.valueOf(rental.getRentDate()));
            pstmt.setTimestamp(2, Timestamp.valueOf(rental.getReturnDate()));
            pstmt.setTimestamp(3, Timestamp.valueOf(rental.getCreatedDate()));
            pstmt.setTimestamp(4, Timestamp.valueOf(rental.getUpdatedDate()));
            pstmt.setLong(5, rental.getComicId());
            pstmt.setLong(6, rental.getMemberId());

            pstmt.executeUpdate();
            return rental;

        } catch (SQLException e) {
            // 로거 대신 표준 에러 출력 사용
            System.err.println("[Error] Rental 저장 중 DB 오류 발생: " + e.getMessage());
            e.printStackTrace();
            throw e;

        } finally {
            // 리소스 반납
            close(con, pstmt, null);
        }
    }
}