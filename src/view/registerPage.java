package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class registerPage extends JDialog {

    private JPanel registerPane;
    private JTextField tfUsername;
    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField tfNomor;
    private JPasswordField pfPassword;
    private JPasswordField pfRepeatPassword;
    private JButton btnConfirm;
    private JButton btnCancle;   // ✅ TOMBOL CANCEL

    public registerPage(JFrame parent) {
        super(parent);
        setTitle("Register");
        setContentPane(registerPane);
        setMinimumSize(new Dimension(450, 500));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // ✅ TOMBOL REGISTER
        btnConfirm.addActionListener(e -> {

            String username = tfUsername.getText();
            String nama = tfName.getText();
            String email = tfEmail.getText();
            String nomor = tfNomor.getText();
            String password = String.valueOf(pfPassword.getPassword());
            String repeat = String.valueOf(pfRepeatPassword.getPassword());

            if (username.isEmpty() || nama.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Semua field wajib diisi!");
                return;
            }

            if (!password.equals(repeat)) {
                JOptionPane.showMessageDialog(this, "Password tidak sama!");
                return;
            }

            if (registerUser(username, password, nama, email, nomor)) {
                dispose();           // tutup register
                new LoginPage(null); // kembali ke login
            }
        });
;

        // ✅ ✅ TOMBOL CANCEL → KEMBALI KE LOGIN
        btnCancle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();              // Tutup halaman register
                new LoginPage(null);    // Buka kembali LoginPage
            }
        });

        setVisible(true);
    }

    // ✅ SIMPAN USER KE DATABASE
    private boolean registerUser(String username, String password, String nama, String email, String nomor) {

        final String DB_URL = "jdbc:mysql://localhost/pelaporan?serverTimezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = "";

        String sql = "INSERT INTO users (username, password, nama, email, no_telp) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, nama);
            ps.setString(4, email);
            ps.setString(5, nomor);

            ps.executeUpdate();

            JOptionPane.showMessageDialog(
                    this,
                    "Registrasi BERHASIL! Silakan login.",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return true;

        } catch (SQLIntegrityConstraintViolationException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Username atau Email sudah digunakan!",
                    "Duplicate Data",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception e) {
            e.printStackTrace(); // WAJIB agar kita tahu error asli
            JOptionPane.showMessageDialog(
                    this,
                    "Error: " + e.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        return false;
    }

}
