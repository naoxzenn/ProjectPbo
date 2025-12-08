package view;

import Model.User;
import controller.UserController;

import javax.swing.*;
import java.awt.*;

public class registerPage extends JDialog {

    private JPanel registerPane;
    private JTextField tfUsername;
    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField tfNomor;
    private JPasswordField pfPassword;
    private JPasswordField pfRepeatPassword;
    private JButton btnConfirm;
    private JButton btnCancle;

    private UserController userController;

    public registerPage(JFrame parent) {
        super(parent);
        userController = new UserController();

        setTitle("Register");
        setContentPane(registerPane);
        setMinimumSize(new Dimension(450, 500));
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // ✅ TOMBOL REGISTER
        btnConfirm.addActionListener(e -> {

            String username = tfUsername.getText().trim();
            String nama = tfName.getText().trim();
            String email = tfEmail.getText().trim();
            String nomor = tfNomor.getText().trim();
            String password = String.valueOf(pfPassword.getPassword());
            String repeat = String.valueOf(pfRepeatPassword.getPassword());

            // ✅ VALIDASI INPUT KOSONG
            if (username.isEmpty() || nama.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Username, nama, email, dan password wajib diisi!",
                        "Validasi Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // ✅ VALIDASI USERNAME MIN 5 KARAKTER
            if (username.length() < 5) {
                JOptionPane.showMessageDialog(this,
                        "Username minimal 5 karakter!",
                        "Validasi Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // ✅ VALIDASI PASSWORD MATCH
            if (!password.equals(repeat)) {
                JOptionPane.showMessageDialog(this,
                        "Password tidak sama!",
                        "Validasi Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // ✅ VALIDASI PASSWORD MIN 6 KARAKTER
            if (password.length() < 6) {
                JOptionPane.showMessageDialog(this,
                        "Password minimal 6 karakter!",
                        "Validasi Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // ✅ VALIDASI EMAIL FORMAT
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                JOptionPane.showMessageDialog(this,
                        "Format email tidak valid!",
                        "Validasi Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // ✅ CEK USERNAME SUDAH ADA
            if (userController.isUsernameExists(username)) {
                JOptionPane.showMessageDialog(this,
                        "Username sudah digunakan!",
                        "Validasi Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // ✅ CEK EMAIL SUDAH ADA
            if (userController.isEmailExists(email)) {
                JOptionPane.showMessageDialog(this,
                        "Email sudah terdaftar!",
                        "Validasi Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // ✅ BUAT OBJECT USER (DENGAN no_telp)
            User user = new User(username, password, nama, email, nomor);

            // ✅ REGISTER MENGGUNAKAN CONTROLLER
            boolean success = userController.register(user);

            if (success) {
                JOptionPane.showMessageDialog(this,
                        "Registrasi BERHASIL! Silakan login.",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new LoginPage(null);
            } else {
                JOptionPane.showMessageDialog(this,
                        "Registrasi gagal! Silakan coba lagi.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // ✅ TOMBOL CANCEL → KEMBALI KE LOGIN
        btnCancle.addActionListener(e -> {
            dispose();
            new LoginPage(null);
        });

        setVisible(true);
    }
}