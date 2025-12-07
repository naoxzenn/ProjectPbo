package view;

import Model.Laporan;
import Model.User;
import controller.LaporanController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class dashboardAdmin extends JFrame {

    private JTable tblLaporan;
    private JButton btnStatus;
    private JButton btnLogout;
    private JButton btnSelesai;
    private JPanel Adminpane;

    private User currentUser;
    private LaporanController laporanController;
    private DefaultTableModel tableModel;

    public dashboardAdmin(User user) {
        this.currentUser = user;
        this.laporanController = new LaporanController();

        // initComponents(); // dari .form file
        setupManual();
        loadLaporan();
        setVisible(true);

        System.out.println("Admin Login: " + currentUser.getNama());
    }

    private void setupManual() {
        setTitle("Dashboard Admin - " + currentUser.getNama());
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Setup table model
        String[] kolom = {"ID", "User", "Judul", "Kategori", "Lokasi", "Status", "Tanggal"};
        tableModel = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        if (tblLaporan != null) {
            tblLaporan.setModel(tableModel);
            tblLaporan.setRowHeight(25);
            tblLaporan.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        }

        // ===== EVENT BUTTON =====
        if (btnStatus != null) {
            btnStatus.addActionListener(e -> updateStatus());
        }

        if (btnSelesai != null) {
            btnSelesai.addActionListener(e -> markAsSelesai());
        }

        if (btnLogout != null) {
            btnLogout.addActionListener(e -> handleLogout());
        }

        // Double click untuk lihat detail
        if (tblLaporan != null) {
            tblLaporan.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    if (evt.getClickCount() == 2) {
                        viewDetailLaporan();
                    }
                }
            });
        }
    }

    private void loadLaporan() {
        tableModel.setRowCount(0);
        List<Laporan> laporanList = laporanController.getAllLaporan();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        for (Laporan laporan : laporanList) {
            Object[] row = {
                    laporan.getLaporanId(),
                    laporan.getNamaUser(),
                    laporan.getJudul(),
                    laporan.getNamaKategori(),
                    laporan.getLokasi(),
                    laporan.getStatus(),
                    sdf.format(laporan.getTanggalLapor())
            };
            tableModel.addRow(row);
        }

        // Update statistik
        loadStatistik();
    }

    private void loadStatistik() {
        int total = laporanController.getAllLaporan().size();
        int pending = laporanController.countLaporanByStatus("Pending");
        int diproses = laporanController.countLaporanByStatus("Diproses");
        int selesai = laporanController.countLaporanByStatus("Selesai");

        System.out.println("=== STATISTIK LAPORAN ===");
        System.out.println("Total   : " + total);
        System.out.println("Pending : " + pending);
        System.out.println("Diproses: " + diproses);
        System.out.println("Selesai : " + selesai);
    }

    private void updateStatus() {
        int selectedRow = tblLaporan.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih laporan yang ingin diupdate!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int laporanId = (int) tableModel.getValueAt(selectedRow, 0);
        String currentStatus = (String) tableModel.getValueAt(selectedRow, 5);

        String[] statusOptions = {"Pending", "Diproses", "Selesai", "Ditolak"};
        String newStatus = (String) JOptionPane.showInputDialog(
                this,
                "Pilih status baru:",
                "Update Status Laporan",
                JOptionPane.QUESTION_MESSAGE,
                null,
                statusOptions,
                currentStatus
        );

        if (newStatus != null && !newStatus.equals(currentStatus)) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Apakah Anda yakin ingin mengubah status dari '" + currentStatus + "' ke '" + newStatus + "'?",
                    "Konfirmasi Update",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean success = laporanController.updateStatus(laporanId, newStatus);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Status berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    loadLaporan();
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal mengupdate status!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void markAsSelesai() {
        int selectedRow = tblLaporan.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih laporan yang ingin ditandai selesai!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int laporanId = (int) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Tandai laporan ini sebagai SELESAI?",
                "Konfirmasi",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = laporanController.updateStatus(laporanId, "Selesai");

            if (success) {
                JOptionPane.showMessageDialog(this, "Laporan ditandai selesai!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadLaporan();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mengupdate status!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewDetailLaporan() {
        int selectedRow = tblLaporan.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih laporan yang ingin dilihat!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int laporanId = (int) tableModel.getValueAt(selectedRow, 0);
        Laporan laporan = laporanController.getLaporanById(laporanId);

        if (laporan != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

            String detail = "=== DETAIL LAPORAN ===\n\n" +
                    "ID Laporan: " + laporan.getLaporanId() + "\n" +
                    "Dilaporkan oleh: " + laporan.getNamaUser() + "\n" +
                    "Judul: " + laporan.getJudul() + "\n" +
                    "Kategori: " + laporan.getNamaKategori() + "\n" +
                    "Lokasi: " + laporan.getLokasi() + "\n" +
                    "Status: " + laporan.getStatus() + "\n" +
                    "Tanggal Lapor: " + sdf.format(laporan.getTanggalLapor()) + "\n" +
                    "Terakhir Update: " + sdf.format(laporan.getTanggalUpdate()) + "\n\n" +
                    "Deskripsi:\n" + laporan.getDeskripsi();

            JTextArea textArea = new JTextArea(detail);
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 300));

            JOptionPane.showMessageDialog(this, scrollPane, "Detail Laporan", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void handleLogout() {
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