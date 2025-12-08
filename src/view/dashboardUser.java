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
    private JPanel Userpane;

    private User currentUser;
    private LaporanController laporanController;
    private DefaultTableModel tableModel;

    public dashboardUser(User user) {
        this.currentUser = user;
        this.laporanController = new LaporanController();

        System.out.println("🚀 Dashboard User dibuka untuk: " + currentUser.getNama());
        System.out.println("📝 User ID: " + currentUser.getUserId());

        initComponents();
        setupTable();
        setupEventListeners();
        loadLaporan();

        setVisible(true);
    }

    private void initComponents() {
        setTitle("Dashboard User - " + currentUser.getNama());
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Buat panel utama jika belum ada dari .form
        if (Userpane == null) {
            Userpane = new JPanel(new BorderLayout(10, 10));
            Userpane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            Userpane.setBackground(Color.WHITE);
        }

        setContentPane(Userpane);
    }

    private void setupTable() {
        // Setup table model
        String[] kolom = {"ID", "Judul", "Kategori", "Lokasi", "Status", "Tanggal"};
        tableModel = new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Setup tabel jika belum ada dari .form
        if (tblLaporan == null) {
            tblLaporan = new JTable();
        }

        tblLaporan.setModel(tableModel);
        tblLaporan.setRowHeight(25);
        tblLaporan.setFont(new Font("Arial", Font.PLAIN, 12));
        tblLaporan.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tblLaporan.getTableHeader().setBackground(new Color(70, 130, 180));
        tblLaporan.getTableHeader().setForeground(Color.WHITE);
        tblLaporan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Set column widths
        tblLaporan.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblLaporan.getColumnModel().getColumn(1).setPreferredWidth(250);
        tblLaporan.getColumnModel().getColumn(2).setPreferredWidth(120);
        tblLaporan.getColumnModel().getColumn(3).setPreferredWidth(180);
        tblLaporan.getColumnModel().getColumn(4).setPreferredWidth(100);
        tblLaporan.getColumnModel().getColumn(5).setPreferredWidth(150);
    }

    private void setupEventListeners() {
        // Setup buttons jika belum ada dari .form
        if (btnBuatLaporan == null) {
            btnBuatLaporan = new JButton("+ Buat Laporan");
        }
        if (btnRefresh == null) {
            btnRefresh = new JButton("Refresh");
        }
        if (btnLogout == null) {
            btnLogout = new JButton("Logout");
        }

        // Event handlers
        btnBuatLaporan.addActionListener(e -> {
            System.out.println("📝 Membuka form laporan...");
            new LaporForm(currentUser).setVisible(true);
        });

        btnRefresh.addActionListener(e -> {
            System.out.println("🔄 Refresh data...");
            loadLaporan();
            JOptionPane.showMessageDialog(this,
                    "Data berhasil direfresh!",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Yakin ingin logout?",
                    "Logout",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                System.out.println("👋 Logout...");
                dispose();
                new LoginPage(null);
            }
        });

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
        System.out.println("📊 Loading laporan untuk user ID: " + currentUser.getUserId());

        tableModel.setRowCount(0);
        List<Laporan> laporanList = laporanController.getLaporanByUserId(currentUser.getUserId());

        System.out.println("📋 Jumlah laporan ditemukan: " + laporanList.size());

        if (laporanList.isEmpty()) {
            System.out.println("⚠️ Tidak ada laporan untuk user ini");
            System.out.println("💡 Silakan buat laporan baru dengan klik 'Buat Laporan'");
        }

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
            System.out.println("✅ Laporan ditambahkan: " + laporan.getJudul());
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
            textArea.setFont(new Font("Arial", Font.PLAIN, 13));
            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 300));

            JOptionPane.showMessageDialog(this,
                    scrollPane,
                    "Detail Laporan",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}