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
    // Komponen dari .form file Anda
    private User currentUser;
    private JTextField tfJudul;
    private JTextField tfAlamat; // ini sebenarnya lokasi
    private JComboBox<Kategori> cbxKategori;
    private JTextArea tfDeskripsi;
    private JTextField tfFoto;
    private JButton btnCariGambar;
    private JButton btnKonfirmasi;
    private JPanel fotoPane;
    private JLabel lblPreview;

    private File selectedFile;
    private LaporanController laporanController;
    private KategoriController kategoriController;

    public LaporForm(User user) {
        this.currentUser = user;
        this.laporanController = new LaporanController();
        this.kategoriController = new KategoriController();

        // initComponents(); // dari .form file Anda
        setupManual(); // setup tambahan
        loadKategori();
    }

    // Setup manual untuk komponen yang belum ada di .form
    private void setupManual() {
        setTitle("Form Lapor Kerusakan Infrastruktur");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Setup label preview di fotoPane
        if (fotoPane != null) {
            lblPreview = new JLabel("No image selected");
            lblPreview.setHorizontalAlignment(SwingConstants.CENTER);
            lblPreview.setVerticalAlignment(SwingConstants.CENTER);
            lblPreview.setPreferredSize(new Dimension(200, 150));
            lblPreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            lblPreview.setOpaque(true);
            lblPreview.setBackground(Color.LIGHT_GRAY);
            fotoPane.add(lblPreview);
        }

        // Setup event listeners
        if (btnCariGambar != null) {
            btnCariGambar.addActionListener(e -> browseFoto());
        }

        if (btnKonfirmasi != null) {
            btnKonfirmasi.addActionListener(e -> handleSubmit());
        }
    }

    private void loadKategori() {
        List<Kategori> kategoriList = kategoriController.getAllKategori();
        cbxKategori.removeAllItems(); // Hapus item lama
        for (Kategori k : kategoriList) {
            cbxKategori.addItem(k);
        }
    }

    private void browseFoto() {
        JFileChooser fileChooser = new JFileChooser();
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

            // Tampilkan preview
            try {
                ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
                lblPreview.setIcon(new ImageIcon(img));
                lblPreview.setText("");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal memuat preview foto!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void handleSubmit() {
        // Ambil nilai input
        String judul = tfJudul.getText().trim();
        String lokasi = tfAlamat.getText().trim(); // tfAlamat = lokasi
        String deskripsi = tfDeskripsi.getText().trim();
        Kategori kategori = (Kategori) cbxKategori.getSelectedItem();

        // Validasi
        if (judul.isEmpty() || lokasi.isEmpty() || deskripsi.isEmpty() || kategori == null) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Validasi Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih foto kerusakan!", "Validasi Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (selectedFile.length() > 5 * 1024 * 1024) {
            JOptionPane.showMessageDialog(this, "Ukuran file terlalu besar! Maksimal 5MB.", "Validasi Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Simpan foto ke folder uploads
        String fotoPath = null;
        try {
            File uploadsDir = new File("uploads");
            if (!uploadsDir.exists()) {
                uploadsDir.mkdir();
            }

            String fileName = System.currentTimeMillis() + "_" + selectedFile.getName();
            Path destination = Paths.get("uploads", fileName);
            Files.copy(selectedFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
            fotoPath = "uploads/" + fileName;

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan foto!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Buat object Laporan
        Laporan laporan = new Laporan(
                currentUser.getUserId(),
                kategori.getKategoriId(),
                judul,
                deskripsi,
                lokasi,
                fotoPath
        );

        // Simpan ke database
        boolean success = laporanController.createLaporan(laporan);

        if (success) {
            JOptionPane.showMessageDialog(this, "Laporan berhasil dikirim!\nStatus: Pending", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            dispose();
            // Refresh dashboard jika ada
        } else {
            JOptionPane.showMessageDialog(this, "Gagal mengirim laporan! Silakan coba lagi.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}