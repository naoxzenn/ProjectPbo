package view;

import Model.Laporan;
import Model.User;
import controller.LaporanController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
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

        System.out.println("Dashboard Admin dibuka untuk: " + currentUser.getNama());

        initComponents();
        setupTable();
        setupEventListeners();
        loadLaporan();

        setVisible(true);
    }

    private void initComponents() {
        setTitle("Dashboard Admin - " + currentUser.getNama());
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Buat panel utama
        Adminpane = new JPanel(new BorderLayout(10, 10));
        Adminpane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        Adminpane.setBackground(Color.WHITE);

        // Panel Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);

        JLabel lblTitle = new JLabel("Dashboard Admin - " + currentUser.getNama());
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        headerPanel.add(lblTitle, BorderLayout.WEST);

        btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(238, 75, 43));
        btnLogout.setForeground(Color.WHITE);
        btnLogout.setFocusPainted(false);
        headerPanel.add(btnLogout, BorderLayout.EAST);

        // Panel Button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        btnStatus = new JButton("Ubah Status");
        btnStatus.setFont(new Font("Arial", Font.BOLD, 14));
        btnStatus.setBackground(new Color( 55, 0, 255));
        btnStatus.setForeground(Color.WHITE);
        btnStatus.setFocusPainted(false);
        btnStatus.setPreferredSize(new Dimension(150, 35));

        btnSelesai = new JButton("Tandai Selesai");
        btnSelesai.setFont(new Font("Arial", Font.BOLD, 14));
        btnSelesai.setBackground(new Color(0, 255, 4));
        btnSelesai.setForeground(Color.WHITE);
        btnSelesai.setFocusPainted(false);
        btnSelesai.setPreferredSize(new Dimension(170, 35));

        buttonPanel.add(btnStatus);
        buttonPanel.add(btnSelesai);

        // Panel Tabel
        tblLaporan = new JTable();
        JScrollPane scrollPane = new JScrollPane(tblLaporan);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Semua Laporan"));

        // Assembly
        Adminpane.add(headerPanel, BorderLayout.NORTH);
        Adminpane.add(buttonPanel, BorderLayout.SOUTH);
        Adminpane.add(scrollPane, BorderLayout.CENTER);

        setContentPane(Adminpane);
    }

    private void setupTable() {
        // Setup table model
        String[] kolom = {"ID", "User", "Judul", "Kategori", "Lokasi", "Status", "Tanggal"};
        tableModel = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblLaporan.setModel(tableModel);
        tblLaporan.setRowHeight(25);
        tblLaporan.setFont(new Font("Arial", Font.PLAIN, 12));
        tblLaporan.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tblLaporan.getTableHeader().setBackground(new Color(220, 20, 60));
        tblLaporan.getTableHeader().setForeground(Color.WHITE);
        tblLaporan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Set column widths
        tblLaporan.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblLaporan.getColumnModel().getColumn(1).setPreferredWidth(120);
        tblLaporan.getColumnModel().getColumn(2).setPreferredWidth(200);
        tblLaporan.getColumnModel().getColumn(3).setPreferredWidth(120);
        tblLaporan.getColumnModel().getColumn(4).setPreferredWidth(150);
        tblLaporan.getColumnModel().getColumn(5).setPreferredWidth(100);
        tblLaporan.getColumnModel().getColumn(6).setPreferredWidth(150);
    }

    private void setupEventListeners() {
        // Event handlers
        btnStatus.addActionListener(e -> updateStatus());
        btnSelesai.addActionListener(e -> markAsSelesai());
        btnLogout.addActionListener(e -> handleLogout());

        // Double click untuk lihat detail
        tblLaporan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) {
                    viewDetailLaporan();
                }
            }
        });
    }

    private void loadLaporan() {
        System.out.println("📊 Loading semua laporan...");

        tableModel.setRowCount(0);
        List<Laporan> laporanList = laporanController.getAllLaporan();

        System.out.println("📋 Jumlah laporan ditemukan: " + laporanList.size());

        if (laporanList.isEmpty()) {
            System.out.println("⚠️ Belum ada laporan dalam database");
        }

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
            System.out.println("✅ Laporan ditambahkan: ID=" + laporan.getLaporanId() + ", " + laporan.getJudul());
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
            JOptionPane.showMessageDialog(this,
                    "Pilih laporan yang ingin diupdate!",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);
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
                    JOptionPane.showMessageDialog(this,
                            "Status berhasil diupdate!",
                            "Sukses",
                            JOptionPane.INFORMATION_MESSAGE);
                    loadLaporan();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Gagal mengupdate status!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void markAsSelesai() {
        int selectedRow = tblLaporan.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih laporan yang ingin ditandai selesai!",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);
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
                JOptionPane.showMessageDialog(this,
                        "Laporan ditandai selesai!",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
                loadLaporan();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Gagal mengupdate status!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void viewDetailLaporan() {
        int selectedRow = tblLaporan.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Pilih laporan yang ingin dilihat!",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int laporanId = (int) tableModel.getValueAt(selectedRow, 0);
        Laporan laporan = laporanController.getLaporanById(laporanId);

        if (laporan != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

            // Panel utama dengan BorderLayout
            JPanel detailPanel = new JPanel(new BorderLayout(10, 10));
            detailPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // Text area untuk detail
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
            textArea.setFont(new Font("Arial", Font.PLAIN, 13));
            textArea.setBackground(new Color(245, 245, 245));
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 250));

            detailPanel.add(scrollPane, BorderLayout.CENTER);

            // Tambahkan preview foto jika ada
            if (laporan.getFotoPath() != null && !laporan.getFotoPath().isEmpty()) {
                System.out.println("🖼️ Mencoba load foto dari: " + laporan.getFotoPath());

                try {
                    File fotoFile = new File(laporan.getFotoPath());

                    if (fotoFile.exists()) {
                        System.out.println("✅ File foto ditemukan: " + fotoFile.getAbsolutePath());

                        ImageIcon icon = new ImageIcon(laporan.getFotoPath());
                        Image img = icon.getImage();

                        // Hitung proporsi untuk resize
                        int maxWidth = 500;
                        int maxHeight = 350;
                        int imgWidth = icon.getIconWidth();
                        int imgHeight = icon.getIconHeight();

                        if (imgWidth > 0 && imgHeight > 0) {
                            double scale = Math.min(
                                    (double) maxWidth / imgWidth,
                                    (double) maxHeight / imgHeight
                            );

                            int scaledWidth = (int) (imgWidth * scale);
                            int scaledHeight = (int) (imgHeight * scale);

                            Image scaledImg = img.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);

                            JLabel lblFoto = new JLabel(new ImageIcon(scaledImg));
                            lblFoto.setBorder(BorderFactory.createTitledBorder("Foto Kerusakan"));
                            lblFoto.setHorizontalAlignment(SwingConstants.CENTER);

                            JPanel fotoPanel = new JPanel(new BorderLayout());
                            fotoPanel.add(lblFoto, BorderLayout.CENTER);
                            fotoPanel.setPreferredSize(new Dimension(500, 370));

                            detailPanel.add(fotoPanel, BorderLayout.SOUTH);

                            System.out.println("✅ Foto berhasil ditampilkan");
                        } else {
                            System.err.println("❌ Ukuran gambar tidak valid");
                        }
                    } else {
                        System.err.println("❌ File foto tidak ditemukan: " + fotoFile.getAbsolutePath());

                        JLabel lblNoFoto = new JLabel("⚠️ Foto tidak ditemukan di: " + laporan.getFotoPath());
                        lblNoFoto.setHorizontalAlignment(SwingConstants.CENTER);
                        lblNoFoto.setForeground(Color.RED);
                        lblNoFoto.setBorder(BorderFactory.createTitledBorder("Foto Kerusakan"));
                        detailPanel.add(lblNoFoto, BorderLayout.SOUTH);
                    }
                } catch (Exception ex) {
                    System.err.println("❌ Error loading foto: " + ex.getMessage());
                    ex.printStackTrace();

                    JLabel lblError = new JLabel("❌ Error memuat foto: " + ex.getMessage());
                    lblError.setHorizontalAlignment(SwingConstants.CENTER);
                    lblError.setForeground(Color.RED);
                    lblError.setBorder(BorderFactory.createTitledBorder("Foto Kerusakan"));
                    detailPanel.add(lblError, BorderLayout.SOUTH);
                }
            } else {
                System.out.println("⚠️ Laporan tidak memiliki foto");

                JLabel lblNoFoto = new JLabel("📷 Tidak ada foto untuk laporan ini");
                lblNoFoto.setHorizontalAlignment(SwingConstants.CENTER);
                lblNoFoto.setForeground(Color.GRAY);
                lblNoFoto.setBorder(BorderFactory.createTitledBorder("Foto Kerusakan"));
                detailPanel.add(lblNoFoto, BorderLayout.SOUTH);
            }

            // Tampilkan dialog dengan ukuran yang sesuai
            JDialog dialog = new JDialog(this, "Detail Laporan", true);
            dialog.setContentPane(new JScrollPane(detailPanel));
            dialog.setSize(550, 680);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);
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
            System.out.println("👋 Logout admin...");
            dispose();
            new LoginPage(null);
        }
    }
}