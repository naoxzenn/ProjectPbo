package view;

import Model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class dashboardUser extends JFrame {

    private JTable tblLaporan;
    private JButton btnBuatLaporan;
    private JButton btnRefresh;
    private JButton btnLogout;
    private JPanel DashboardUser;
    private User user;

    public dashboardUser(User user) {
        this.user = user;
        initComponents();
        setVisible(true);
    }

    private void initComponents() {
        setTitle("Dashboard Model.User");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        DashboardUser = new JPanel(new BorderLayout());

        // ===== PANEL ATAS (TOMBOL) =====
        JPanel panelAtas = new JPanel(new FlowLayout());

        btnBuatLaporan = new JButton("Buat Model.Laporan");
        btnRefresh = new JButton("Refresh");
        btnLogout = new JButton("Logout");

        panelAtas.add(btnBuatLaporan);
        panelAtas.add(btnRefresh);
        panelAtas.add(btnLogout);

        // ===== TABEL =====
        String[] kolom = {"ID", "Judul", "Kategori", "Lokasi", "Status"};
        DefaultTableModel model = new DefaultTableModel(kolom, 0);
        tblLaporan = new JTable(model);

        JScrollPane scrollPane = new JScrollPane(tblLaporan);

        // ===== TAMBAH KE PANEL UTAMA =====
        DashboardUser.add(panelAtas, BorderLayout.NORTH);
        DashboardUser.add(scrollPane, BorderLayout.CENTER);

        add(DashboardUser);

        // ===== FUNGSI TOMBOL =====
        btnBuatLaporan.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Fungsi Buat Model.Laporan dipanggil");
        });

        btnRefresh.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Data direfresh");
        });

        btnLogout.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Logout berhasil");
            dispose();
            new LoginPage(null); // kembali ke login
        });
    }
}
