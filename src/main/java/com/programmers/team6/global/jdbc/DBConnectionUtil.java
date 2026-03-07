package com.programmers.team6.global.jdbc;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;

import static com.programmers.team6.global.jdbc.ConnectionConst.*;


@Slf4j
public class DBConnectionUtil {

    //connection util
    public static Connection getConnection()  {
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            log.info("get connection={}, class={}", connection, connection.getClass());
            return connection;

        } catch (SQLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    //close util
    public static void close(Connection conn, Statement stmt, ResultSet rs){
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                log.info("error", e);
            }
        }
    }


}