package model.entity;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionManager {
    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url  = "jdbc:mysql://localhost:3306/product_management"
                    + "?useUnicode=true"
                    + "&characterEncoding=UTF-8"
                    + "&serverTimezone=Asia/Tokyo"
                    + "&useSSL=false"
                    + "&allowPublicKeyRetrieval=true";
        String user = "Shoo2455";
        String pass = "Shoo0036";
        return DriverManager.getConnection(url, user, pass);
    }
}