package view;

import Model.User;
import controller.UserController;

import javax.swing.*;
import java.awt.*;

public class LoginPage extends JDialog {

    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JButton btnLogin;
    private JButton btnRegister;
    private JPanel loginPanel;

    private UserController userController;
    public static User user; // user login aktif

    public LoginPage(JFrame parent) {
        super(parent);
        userController = new UserController();

        setTitle("Login");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(450, 450));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // ===== BUTTON LOGIN =====
        btnLogin.addActionListener(e -> {
            String emailOrUsername = tfEmail.getText().trim();
            String password = String.valueOf(pfPassword.getPassword()).trim();

            // ✅ VALIDASI FORM KOSONG
            if (emailOrUsername.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Email/Username dan Password wajib diisi!",
                        "Peringatan",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // ✅ LOGIN MENGGUNAKAN CONTROLLER
            user = userController.login(emailOrUsername, password);

            if (user != null) {
                dispose(); // ✅ tutup login setelah sukses

                // ✅ ARAHKAN SESUAI ROLE
                if (user.getRole().equalsIgnoreCase("admin")) {
                    new dashboardAdmin(user);
                } else {
                    new dashboardUser(user);
                }

            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Email/Username atau password salah!",
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

    // ===== MAIN =====
    public static void main(String[] args) {
        new LoginPage(null); // ✅ HARUS start dari sini
    }
}