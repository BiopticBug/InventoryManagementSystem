package com.inventory.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL ="jdbc:mysql://localhost:3306/shop?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "SQL80";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static void main(String[] args) {
        try (Connection c = getConnection()) {
            System.out.println("Connected to DB: " + c.getMetaData().getURL());
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("DB connection failed: " + e.getMessage());
        }
    }
}
