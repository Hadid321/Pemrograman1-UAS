package config;
import java.sql.*;
import javax.swing.JOptionPane;

public class Database {
    private static final String URL  = "jdbc:mysql://localhost:3306/db_zenithstore?useSSL=false&serverTimezone=Asia/Jakarta&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASS = "";

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Koneksi database gagal!\n" + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
}