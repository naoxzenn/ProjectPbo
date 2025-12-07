package view;

import Model.User;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginPage extends JDialog {

    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JButton btnLogin;
    private JButton btnRegister;
    private JPanel loginPanel;

    public static User user; // user login aktif

    public LoginPage(JFrame parent) {
        super(parent);
        setTitle("Login");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(450, 450));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // ===== BUTTON LOGIN =====
        btnLogin.addActionListener(e -> {

            String email = tfEmail.getText().trim();
            String password = String.valueOf(pfPassword.getPassword()).trim();

            // ✅ VALIDASI FORM KOSONG
            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Email dan Password wajib diisi!",
                        "Peringatan",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            user = getAuthenticatedUser(email, password);

            if (user != null) {
                dispose(); // ✅ tutup login setelah sukses

                // ✅ ARAHKAN SESUAI ROLE
                if (user.role.equalsIgnoreCase("admin")) {
                    new dashboardAdmin(user);
                } else {
                    new dashboardUser(user);
                }

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Email atau password salah!",
                        "Login Gagal",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // ===== BUTTON REGISTER =====
        btnRegister.addActionListener(e -> {
            dispose();
            new registerPage(null);
        });

        setVisible(true);
    }

    // ===== AUTH DATABASE =====
    private User getAuthenticatedUser(String email, String password) {

        User user = null;

        final String DB_URL = "jdbc:mysql://localhost/pelaporan?serverTimezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = "";

        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // ✅ SET PARAMETER QUERY
            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.user_id = rs.getInt("user_id");
                user.username = rs.getString("username");
                user.password = rs.getString("password");
                user.nama = rs.getString("nama");
                user.email = rs.getString("email");
                user.no_telp = rs.getString("no_telp");
                user.role = rs.getString("role");
                user.created_at = rs.getString("created_at");
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Koneksi database gagal!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        return user;
    }

    // ===== MAIN =====
    public static void main(String[] args) {
        new LoginPage(null); // ✅ HARUS start dari sini
    }
}
