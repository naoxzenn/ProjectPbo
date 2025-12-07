import view.LoginPage;

import javax.swing.*;

/**
 * Main Class - Entry Point untuk Aplikasi Sistem Pelaporan Infrastruktur
 *
 * @version 1.0
 * @since 2025
 */
public class Main {

    public static void main(String[] args) {
        // Set Look and Feel ke System default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Jalankan aplikasi di Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Buka Login Form
            new LoginPage(null);
        });
    }
}