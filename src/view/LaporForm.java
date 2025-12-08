package view;

import Model.User;
import Model.Kategori;
import Model.Laporan;
import controller.KategoriController;
import controller.LaporanController;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class LaporForm extends JFrame {
    // ✅ Komponen dari .form file (JANGAN UBAH NAMA INI)
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

        // ✅ Panggil initComponents() dari .form DULU
        // initComponents(); // Ini di-generate otomatis oleh IntelliJ

        // ✅ Setup tambahan SETELAH initComponents()
        setupAfterInit();
    }

    // ✅ Method ini dipanggil SETELAH initComponents() dari .form
    private void setupAfterInit() {
        setTitle("Form Lapor Kerusakan Infrastruktur");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // ✅ Setup preview foto di fotoPane (yang sudah ada dari .form)
        if (fotoPane != null) {
            fotoPane.removeAll(); // Hapus isi lama
            fotoPane.setLayout(new BorderLayout());

            lblPreview = new JLabel("Belum ada gambar dipilih");
            lblPreview.setHorizontalAlignment(SwingConstants.CENTER);
            lblPreview.setVerticalAlignment(SwingConstants.CENTER);
            lblPreview.setPreferredSize(new Dimension(250, 200));
            lblPreview.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
            lblPreview.setOpaque(true);
            lblPreview.setBackground(new Color(240, 240, 240));

            fotoPane.add(lblPreview, BorderLayout.CENTER);
            fotoPane.revalidate();
            fotoPane.repaint();
        }

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
            btnCariGambar.addActionListener(e -> browseFoto());
        }

        // Event untuk button konfirmasi
        if (btnKonfirmasi != null) {
            btnKonfirmasi.addActionListener(e -> handleSubmit());
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

            // Tampilkan preview
            try {
                ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(250, 200, Image.SCALE_SMOOTH);
                lblPreview.setIcon(new ImageIcon(img));
                lblPreview.setText("");
            } catch (Exception e) {
                System.err.println("❌ Error loading preview: " + e.getMessage());
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
        String lokasi = tfAlamat.getText().trim(); // tfAlamat = lokasi
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