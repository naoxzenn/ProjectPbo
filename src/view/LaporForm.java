package view;

import Model.User;
import Model.Kategori;
import Model.Laporan;
import controller.KategoriController;
import controller.LaporanController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class LaporForm extends JFrame {
    // ✅ Komponen dari .form file (HARUS SAMA PERSIS dengan nama di .form)
    private JTextField tfJudul;
    private JTextField tfAlamat;
    private JComboBox<Kategori> cbxKategori;
    private JTextField tfDeskripsi;
    private JTextField tfFoto;
    private JButton btnCariGambar;
    private JButton btnKonfirmasi;
    private JPanel fotoPane;

    // ✅ Komponen tambahan
    private User currentUser;
    private File selectedFile;
    private LaporanController laporanController;
    private KategoriController kategoriController;
    private JLabel lblPreview;

    public LaporForm(User user) {
        this.currentUser = user;
        this.laporanController = new LaporanController();
        this.kategoriController = new KategoriController();

        System.out.println("📝 Membuka form laporan untuk: " + currentUser.getNama());

        // ✅ Setup window properties
        setTitle("Form Lapor Kerusakan Infrastruktur");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 700);
        setLocationRelativeTo(null);

        // ✅ Inisialisasi komponen manual jika .form tidak ter-load
        initComponentsManual();

        // ✅ Setup tambahan SETELAH komponen dibuat
        setupAfterInit();

        setVisible(true);
    }

    // ✅ Method untuk inisialisasi komponen secara manual
    private void initComponentsManual() {
        // Panel utama
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Title
        JLabel lblTitle = new JLabel("Buat Laporan Kerusakan Infrastruktur");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(lblTitle, gbc);

        gbc.gridwidth = 1;

        // Judul
        gbc.gridy++;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Judul:"), gbc);
        gbc.gridx = 1;
        tfJudul = new JTextField(20);
        mainPanel.add(tfJudul, gbc);

        // Lokasi/Alamat
        gbc.gridy++;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Lokasi/Alamat:"), gbc);
        gbc.gridx = 1;
        tfAlamat = new JTextField(20);
        mainPanel.add(tfAlamat, gbc);

        // Kategori
        gbc.gridy++;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Kategori:"), gbc);
        gbc.gridx = 1;
        cbxKategori = new JComboBox<>();
        mainPanel.add(cbxKategori, gbc);

        // Deskripsi
        gbc.gridy++;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Deskripsi:"), gbc);
        gbc.gridx = 1;
        tfDeskripsi = new JTextField(20);
        mainPanel.add(tfDeskripsi, gbc);

        // Foto Path
        gbc.gridy++;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Foto:"), gbc);
        gbc.gridx = 1;
        JPanel fotoPanel = new JPanel(new BorderLayout(5, 0));
        tfFoto = new JTextField(15);
        tfFoto.setEditable(false);
        btnCariGambar = new JButton("Pilih Gambar");
        fotoPanel.add(tfFoto, BorderLayout.CENTER);
        fotoPanel.add(btnCariGambar, BorderLayout.EAST);
        mainPanel.add(fotoPanel, gbc);

        // Preview Foto
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        fotoPane = new JPanel();
        fotoPane.setLayout(new BorderLayout());
        fotoPane.setPreferredSize(new Dimension(400, 250));
        fotoPane.setBorder(BorderFactory.createTitledBorder("Preview Foto"));

        lblPreview = new JLabel("Belum ada gambar dipilih");
        lblPreview.setHorizontalAlignment(SwingConstants.CENTER);
        lblPreview.setVerticalAlignment(SwingConstants.CENTER);
        lblPreview.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
        lblPreview.setOpaque(true);
        lblPreview.setBackground(new Color(240, 240, 240));

        fotoPane.add(lblPreview, BorderLayout.CENTER);
        mainPanel.add(fotoPane, gbc);

        // Button Konfirmasi
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        btnKonfirmasi = new JButton("Kirim Laporan");
        btnKonfirmasi.setFont(new Font("Arial", Font.BOLD, 14));
        btnKonfirmasi.setBackground(new Color(70, 130, 180));
        btnKonfirmasi.setForeground(Color.WHITE);
        btnKonfirmasi.setFocusPainted(false);
        btnKonfirmasi.setPreferredSize(new Dimension(200, 40));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnKonfirmasi);
        mainPanel.add(buttonPanel, gbc);

        // Set content pane
        setContentPane(new JScrollPane(mainPanel));
    }

    // ✅ Method ini dipanggil SETELAH initComponents()
    private void setupAfterInit() {
        // ✅ Load kategori ke combobox
        loadKategori();

        // ✅ Setup event listeners
        setupEventListeners();
    }

    private void loadKategori() {
        if (cbxKategori != null) {
            cbxKategori.removeAllItems();
            List<Kategori> kategoriList = kategoriController.getAllKategori();

            System.out.println("📋 Loading " + kategoriList.size() + " kategori");

            for (Kategori k : kategoriList) {
                cbxKategori.addItem(k);
            }
        }
    }

    private void setupEventListeners() {
        // Event untuk button cari gambar
        if (btnCariGambar != null) {
            btnCariGambar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    browseFoto();
                }
            });
        }

        // Event untuk button konfirmasi
        if (btnKonfirmasi != null) {
            btnKonfirmasi.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    handleSubmit();
                }
            });
        }
    }

    private void browseFoto() {
        System.out.println("🖼️ Membuka file chooser...");

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Pilih Foto Kerusakan");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            public boolean accept(File f) {
                if (f.isDirectory()) return true;
                String name = f.getName().toLowerCase();
                return name.endsWith(".jpg") || name.endsWith(".jpeg") ||
                        name.endsWith(".png") || name.endsWith(".gif");
            }
            public String getDescription() {
                return "Image Files (*.jpg, *.jpeg, *.png, *.gif)";
            }
        });

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            tfFoto.setText(selectedFile.getName());

            System.out.println("✅ File dipilih: " + selectedFile.getName());

            // Tampilkan preview dengan resize yang lebih baik
            try {
                ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());
                Image img = icon.getImage();

                // Hitung proporsi untuk resize
                int previewWidth = 380;
                int previewHeight = 230;
                int imgWidth = icon.getIconWidth();
                int imgHeight = icon.getIconHeight();

                double scale = Math.min(
                        (double) previewWidth / imgWidth,
                        (double) previewHeight / imgHeight
                );

                int scaledWidth = (int) (imgWidth * scale);
                int scaledHeight = (int) (imgHeight * scale);

                Image scaledImg = img.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);
                lblPreview.setIcon(new ImageIcon(scaledImg));
                lblPreview.setText("");

                System.out.println("✅ Preview gambar berhasil ditampilkan");
            } catch (Exception e) {
                System.err.println("❌ Error loading preview: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                        "Gagal memuat preview foto!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleSubmit() {
        System.out.println("📤 Memproses submit laporan...");

        // Ambil nilai input
        String judul = tfJudul.getText().trim();
        String lokasi = tfAlamat.getText().trim();
        String deskripsi = tfDeskripsi.getText().trim();
        Kategori kategori = (Kategori) cbxKategori.getSelectedItem();

        // ✅ VALIDASI 1: Field kosong
        if (judul.isEmpty() || lokasi.isEmpty() || deskripsi.isEmpty() || kategori == null) {
            System.out.println("⚠️ Validasi gagal: Ada field yang kosong");
            JOptionPane.showMessageDialog(this,
                    "Semua field harus diisi!",
                    "Validasi Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // ✅ VALIDASI 2: Foto belum dipilih
        if (selectedFile == null) {
            System.out.println("⚠️ Validasi gagal: Foto belum dipilih");
            JOptionPane.showMessageDialog(this,
                    "Silakan pilih foto kerusakan!",
                    "Validasi Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // ✅ VALIDASI 3: Ukuran file
        if (selectedFile.length() > 5 * 1024 * 1024) {
            System.out.println("⚠️ Validasi gagal: File terlalu besar");
            JOptionPane.showMessageDialog(this,
                    "Ukuran file terlalu besar! Maksimal 5MB.",
                    "Validasi Error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        System.out.println("✅ Validasi berhasil");
        System.out.println("📝 Judul: " + judul);
        System.out.println("📍 Lokasi: " + lokasi);
        System.out.println("📂 Kategori: " + kategori.getNamaKategori());

        // ✅ Simpan foto ke folder uploads
        String fotoPath = null;
        try {
            File uploadsDir = new File("uploads");
            if (!uploadsDir.exists()) {
                uploadsDir.mkdir();
                System.out.println("📁 Folder uploads dibuat");
            }

            String fileName = System.currentTimeMillis() + "_" + selectedFile.getName();
            Path destination = Paths.get("uploads", fileName);
            Files.copy(selectedFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
            fotoPath = "uploads/" + fileName;

            System.out.println("✅ Foto berhasil disimpan: " + fotoPath);

        } catch (IOException e) {
            System.err.println("❌ Error menyimpan foto: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Gagal menyimpan foto!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ✅ Buat object Laporan
        Laporan laporan = new Laporan(
                currentUser.getUserId(),
                kategori.getKategoriId(),
                judul,
                deskripsi,
                lokasi,
                fotoPath
        );

        // ✅ Simpan ke database
        System.out.println("💾 Menyimpan ke database...");
        boolean success = laporanController.createLaporan(laporan);

        if (success) {
            System.out.println("✅ Laporan berhasil disimpan!");
            JOptionPane.showMessageDialog(this,
                    "Laporan berhasil dikirim!\nStatus: Pending",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            System.err.println("❌ Gagal menyimpan laporan ke database");
            JOptionPane.showMessageDialog(this,
                    "Gagal mengirim laporan! Silakan coba lagi.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}