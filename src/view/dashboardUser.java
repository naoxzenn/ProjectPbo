package view;

import Model.User;
import Model.Laporan;
import controller.LaporanController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class dashboardUser extends JFrame {

    private JTable tblLaporan;
    private JButton btnBuatLaporan;
    private JButton btnRefresh;
    private JButton btnLogout;
    private JPanel DashboardUser;

    private User currentUser;
    private LaporanController laporanController;
    private DefaultTableModel tableModel;

    public dashboardUser(User user) {
        this.currentUser = user;
        this.laporanController = new LaporanController();

        // initComponents(); // dari .form file
        setupManual();
        loadLaporan();
        setVisible(true);
    }

    private void setupManual() {
        setTitle("Dashboard User - " + currentUser.getNama());
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Setup table model
        String[] kolom = {"ID", "Judul", "Kategori", "Lokasi", "Status", "Tanggal"};
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
        if (btnBuatLaporan != null) {
            btnBuatLaporan.addActionListener(e -> {
                new LaporForm(currentUser).setVisible(true);
            });
        }

        if (btnRefresh != null) {
            btnRefresh.addActionListener(e -> {
                loadLaporan();
                JOptionPane.showMessageDialog(this, "Data berhasil direfresh!", "Info", JOptionPane.INFORMATION_MESSAGE);
            });
        }

        if (btnLogout != null) {
            btnLogout.addActionListener(e -> {
                int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin logout?", "Logout", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    dispose();
                    new LoginPage(null);
                }
            });
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
        List<Laporan> laporanList = laporanController.getLaporanByUserId(currentUser.getUserId());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        for (Laporan laporan : laporanList) {
            Object[] row = {
                    laporan.getLaporanId(),
                    laporan.getJudul(),
                    laporan.getNamaKategori(),
                    laporan.getLokasi(),
                    laporan.getStatus(),
                    sdf.format(laporan.getTanggalLapor())
            };
            tableModel.addRow(row);
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
                    "ID: " + laporan.getLaporanId() + "\n" +
                    "Judul: " + laporan.getJudul() + "\n" +
                    "Kategori: " + laporan.getNamaKategori() + "\n" +
                    "Lokasi: " + laporan.getLokasi() + "\n" +
                    "Status: " + laporan.getStatus() + "\n" +
                    "Tanggal Lapor: " + sdf.format(laporan.getTanggalLapor()) + "\n\n" +
                    "Deskripsi:\n" + laporan.getDeskripsi();

            JTextArea textArea = new JTextArea(detail);
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 300));

            JOptionPane.showMessageDialog(this, scrollPane, "Detail Laporan", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}