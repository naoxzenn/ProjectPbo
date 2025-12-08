package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // ⚠️ UBAH SESUAI KONFIGURASI MYSQL ANDA
    private static final String URL = "jdbc:mysql://localhost:3306/pelaporan?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASSWORD = ""; // ISI PASSWORD MYSQL ANDA

    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            if (connection == null || connection.isClosed()) {
                System.out.println("Mencoba koneksi ke database...");
                System.out.println("URL: " + URL);
                System.out.println("User: " + USER);

                connection = DriverManager.getConnection(URL, USER, PASSWORD);

                System.out.println("✓ Database connected successfully!");
                return connection;
            }

            return connection;

        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL JDBC Driver tidak ditemukan!");
            System.err.println("Pastikan file mysql-connector-java-x.x.xx.jar ada di classpath");
            e.printStackTrace();
            return null;

        } catch (SQLException e) {
            System.err.println("❌ Koneksi ke database gagal!");
            System.err.println("Error Code: " + e.getErrorCode());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Pesan: " + e.getMessage());

            // Diagnosis error umum
            if (e.getMessage().contains("Access denied")) {
                System.err.println("\n🔧 SOLUSI: Username atau password salah!");
                System.err.println("   Cek di DatabaseConnection.java line 11-12");
            } else if (e.getMessage().contains("Communications link failure")) {
                System.err.println("\n🔧 SOLUSI: MySQL Server tidak berjalan!");
                System.err.println("   Jalankan: net start MySQL80");
            } else if (e.getMessage().contains("Unknown database")) {
                System.err.println("\n🔧 SOLUSI: Database belum dibuat!");
                System.err.println("   Jalankan: CREATE DATABASE pelaporan;");
            }

            e.printStackTrace();
            return null;
        }
    }

    // Method untuk test koneksi
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✓ Test koneksi berhasil!");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("❌ Test koneksi gagal!");
            e.printStackTrace();
        }
        return false;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Database connection closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}