package view;

import Model.Laporan;
import Model.User;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class dashboardAdmin extends JFrame {

    private JTable tblLaporan;
    private JButton btnStatus;
    private JButton btnLogout;
    private JButton btnSelesai;
    private JPanel Adminpane;

    private User currentUser;
    private List<Laporan> laporanList; // Data laporan langsung di sini

    public dashboardAdmin(User user) {
        this.currentUser = user;

        // ===== DATA LAPORAN DUMMY =====
        laporanList = new ArrayList<>();
        laporanList.add(new Laporan(1, "User1", "Jalan Rusak", "Pending"));
        laporanList.add(new Laporan(2, "User2", "Lampu Mati", "Diproses"));

        // ===== FRAME BASIC =====
        setTitle("Dashboard Admin");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Adminpane = new JPanel();
        add(Adminpane);

        btnStatus = new JButton("Ubah Status");
        btnSelesai = new JButton("Selesai");
        btnLogout = new JButton("Logout");

        Adminpane.add(btnStatus);
        Adminpane.add(btnSelesai);
        Adminpane.add(btnLogout);

        // ===== ACTION BUTTON =====
        btnStatus.addActionListener(e -> {
            String idInput = JOptionPane.showInputDialog(this, "Masukkan ID Model.Laporan:");
            if (idInput != null) {
                int laporanId = Integer.parseInt(idInput);
                tombolUbahStatus(laporanId);
            }
        });

        btnSelesai.addActionListener(e -> {
            String idInput = JOptionPane.showInputDialog(this, "Masukkan ID Model.Laporan:");
            if (idInput != null) {
                int laporanId = Integer.parseInt(idInput);
                tombolSelesai(laporanId);
            }
        });

        btnLogout.addActionListener(e -> tombolLogout());

        setVisible(true);

        System.out.println("Admin Login: " + currentUser.nama);
        loadStatistik();
        loadLaporan();
    }

    // =========================
    // LOGIKA LAPORAN
    // =========================
    public void loadStatistik() {
        int total = laporanList.size();
        int pending = (int) laporanList.stream().filter(l -> l.getStatus().equalsIgnoreCase("Pending")).count();
        int diproses = (int) laporanList.stream().filter(l -> l.getStatus().equalsIgnoreCase("Diproses")).count();
        int selesai = (int) laporanList.stream().filter(l -> l.getStatus().equalsIgnoreCase("Selesai")).count();

        System.out.println("Total   : " + total);
        System.out.println("Pending : " + pending);
        System.out.println("Diproses: " + diproses);
        System.out.println("Selesai : " + selesai);
    }

    public void loadLaporan() {
        System.out.println("Daftar laporan:");
        for (Laporan l : laporanList) {
            System.out.println(l.getId() + " | " + l.getJudul() + " | " + l.getStatus());
        }
    }

    public void tombolUbahStatus(int laporanId) {
        Laporan laporan = findLaporanById(laporanId);
        if (laporan == null) {
            JOptionPane.showMessageDialog(this, "Model.Laporan tidak ditemukan!");
            return;
        }

        String[] statusOptions = {"Pending", "Diproses", "Selesai", "Ditolak"};
        String newStatus = (String) JOptionPane.showInputDialog(
                this,
                "Pilih Status Baru:",
                "Update Status",
                JOptionPane.QUESTION_MESSAGE,
                null,
                statusOptions,
                laporan.getStatus()
        );

        if (newStatus != null) {
            laporan.setStatus(newStatus);
            JOptionPane.showMessageDialog(this, "Status berhasil diubah!");
            refreshData();
        }
    }

    public void tombolSelesai(int laporanId) {
        Laporan laporan = findLaporanById(laporanId);
        if (laporan != null) {
            laporan.setStatus("Selesai");
            JOptionPane.showMessageDialog(this, "Model.Laporan diselesaikan!");
            refreshData();
        }
    }

    private Laporan findLaporanById(int id) {
        for (Laporan l : laporanList) {
            if (l.getId() == id) return l;
        }
        return null;
    }

    public void refreshData() {
        loadStatistik();
        loadLaporan();
        System.out.println("Data direfresh!");
    }

    public void tombolLogout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Yakin ingin logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginPage(null);
        }
    }
}

