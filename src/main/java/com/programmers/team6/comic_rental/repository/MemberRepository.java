package com.programmers.team6.comic_rental.repository;

import com.mysql.cj.x.protobuf.MysqlxPrepare;
import com.programmers.team6.comic_rental.entity.Member;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberRepository {
    private static final Dotenv dotenv = Dotenv.configure().load();;

    private static final String URL = dotenv.get("DB_URL");
    private static final String USER = dotenv.get("DB_USER");
    private static final String PASS = dotenv.get("DB_PASS");
    private Connection getConnection() throws java.sql.SQLException{
        return DriverManager.getConnection(URL, USER, PASS);
    }

    // 회원 등록
    public long save(String name, String phone) {
        String sql = "INSERT INTO member (name, phoneNumber, createDate, updateDate)" +
                "VALUES (?, ?, NOW(), NOW())";

        try(Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            pstmt.setString(1, name);
            pstmt.setString(2, phone);

            pstmt.executeUpdate();

            try(ResultSet rs = pstmt.getGeneratedKeys()){
                if(rs.next()){
                    return rs.getLong(1);
                }
            }
        }catch(SQLException e){
            System.out.println("에러 발생 : " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    // 특정 회원 조회
    public void findById(long id){
        String sql = "SELECT * FROM member WHERE user_id = ?";
        try(Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setLong(1, id);

            try(ResultSet rs = pstmt.executeQuery()){
                if(rs.next()){
                    System.out.println("조회 성공 : " + rs.getString("name"));
                }
            }
        }catch(SQLException e) {
            System.out.println("에러 발생 : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // 전체 회원 조회
    public List<Member> findAll() {
        String sql = "SELECT * FROM member";

        List<Member> members = new ArrayList<>();

        try(Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()){
                long id = rs.getLong("member_id");
                String name = rs.getString("name");
                String phone = rs.getString("phoneNumber");
                String createDate = rs.getDate("createDate").toLocalDate().toString();

                Member member = new Member();
                member.setId(id);
                member.setName(name);;
                member.setPhone(phone);
                member.setCreateDate(createDate);

                members.add(member);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return members;
    }

    // 회원정보 업데이트
    public int update(long id, String name, String phone){
        String sql = "UPDATE member SET name = ?, phoneNumber = ? WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.setLong(3, id);
            return pstmt.executeUpdate(); // 수정된 행의 개수 반환
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    // 회원 삭제
    public int delete(long id) {
        String sql = "DELETE FROM member WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, id);
            return pstmt.executeUpdate(); // 삭제된 행의 개수 반환
        } catch (SQLException e) { e.printStackTrace(); }
        return 0;
    }

    // 전화 번호 중복 체크를 위한 조회 로직
    public boolean isPhoneExists(String phone) {
        String sql = "SELECT COUNT(*) FROM member WHERE phoneNumber = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, phone);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}

