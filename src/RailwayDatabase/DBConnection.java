package RailwayDatabase;

import java.sql.Connection;

import java.sql.DriverManager;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/isai";

    private static final String USER = "root";

    // Change this to your MySQL password
    private static final String PASSWORD = "Isai@123";

    public static Connection getConnection() throws Exception {

        Class.forName("com.mysql.cj.jdbc.Driver");

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
