package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBManager {
	private static final String URL = "jdbc:mysql://localhost:3306/product_management?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "Shoo2455";
    private static final String PASS = "Shoo0036";

    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASS);
    }
}