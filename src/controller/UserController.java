package controller;

import Model.User;
import util.DatabaseConnection;

import java.sql.*;

public class UserController {

    // Method untuk registrasi user baru (DENGAN no_telp)
    public boolean register(User user) {
        String sql = "INSERT INTO users (username, password, nama, email, no_telp, role) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getNama());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getNoTelp());
            pstmt.setString(6, user.getRole());

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("✅ Register berhasil! Rows affected: " + rowsAffected);
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("❌ Error saat register:");
            e.printStackTrace();
            return false;
        }
    }

    // Method untuk login (FIXED - return user lengkap)
    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE (username = ? OR email = ?) AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, username); // bisa email atau username
            pstmt.setString(3, password);

            System.out.println("🔍 Mencoba login dengan: " + username);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setNama(rs.getString("nama"));
                user.setEmail(rs.getString("email"));
                user.setNoTelp(rs.getString("no_telp"));
                user.setRole(rs.getString("role"));
                user.setCreatedAt(rs.getTimestamp("created_at"));

                System.out.println("✅ Login berhasil! User ID: " + user.getUserId() + ", Role: " + user.getRole());
                return user;
            } else {
                System.out.println("❌ Login gagal! Username/password salah.");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error saat login:");
            e.printStackTrace();
        }

        return null;
    }

    // Method untuk cek username sudah ada atau belum
    public boolean isUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Method untuk cek email sudah ada atau belum
    public boolean isEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Method untuk update profil user
    public boolean updateProfile(User user) {
        String sql = "UPDATE users SET nama = ?, email = ?, no_telp = ? WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getNama());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getNoTelp());
            pstmt.setInt(4, user.getUserId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method untuk get user by ID
    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setNama(rs.getString("nama"));
                user.setEmail(rs.getString("email"));
                user.setNoTelp(rs.getString("no_telp"));
                user.setRole(rs.getString("role"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}