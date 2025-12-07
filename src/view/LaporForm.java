import Model.User;
import com.controller.KategoriController;
import com.controller.LaporanController;
import com.Model.Kategori;
import com.Model.Laporan;
import com.Model

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
    private User currentUser;

    private JTextField tfJudul;
    private JTextField tfLokasi;
    private JTextArea tfDeskripsi;
    private JComboBox<Kategori> cbxKategori;
    private JTextField tfFoto;
    private JButton btnCariGambar;
    private JButton btnKonfirmasi;
    private JLabel lblPreview;

    private File selectedFile;

    private LaporanController laporanController;
    private KategoriController kategoriController;

    public LaporForm(User user) {
        this.currentUser = user;
        this.laporanController = new LaporanController();
        this.kategoriController = new KategoriController();
        initComponents();
        loadKategori();
    }

    private void initComponents() {
        setTitle("Form Lapor Kerusakan");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Judul
        JLabel lblJudul = new JLabel("Judul Laporan:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(lblJudul, gbc);

        tfJudul = new JTextField(25);
        gbc.gridx = 1;
        mainPanel.add(tfJudul, gbc);

        // Kategori
        JLabel lblKategori = new JLabel("Kategori:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(lblKategori, gbc);

        cbxKategori = new JComboBox<>();
        gbc.gridx = 1;
        mainPanel.add(cbxKategori, gbc);

        // Lokasi
        JLabel lblLokasi = new JLabel("Lokasi:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(lblLokasi, gbc);

        tfLokasi = new JTextField(25);
        gbc.gridx = 1;
        mainPanel.add(tfLokasi, gbc);

        // Deskripsi
        JLabel lblDeskripsi = new JLabel("Deskripsi:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(lblDeskripsi, gbc);

        tfDeskripsi = new JTextArea(5, 25);
        tfDeskripsi.setLineWrap(true);
        tfDeskripsi.setWrapStyleWord(true);
        JScrollPane scroll = new JScrollPane(tfDeskripsi);
        gbc.gridx = 1;
        mainPanel.add(scroll, gbc);

        // Foto
        JLabel lblFoto = new JLabel("Foto:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(lblFoto, gbc);

        JPanel fotoPanel = new JPanel(new BorderLayout(5, 5));
        fotoPanel.setBackground(Color.WHITE);

        tfFoto = new JTextField();
        tfFoto.setEditable(false);
        fotoPanel.add(tfFoto, BorderLayout.CENTER);

        btnCariGambar = new JButton("Browse");
        btnCariGambar.addActionListener(e -> browseFoto());
        fotoPanel.add(btnCariGambar, BorderLayout.EAST);

        gbc.gridx = 1;
        mainPanel.add(fotoPanel, gbc);

        // Preview
        lblPreview = new JLabel("No image selected");
        lblPreview.setHorizontalAlignment(SwingConstants.CENTER);
        lblPreview.setVerticalAlignment(SwingConstants.CENTER);
        lblPreview.setPreferredSize(new Dimension(200, 150));
        lblPreview.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblPreview.setOpaque(true);
        lblPreview.setBackground(Color.LIGHT_GRAY);

        gbc.gridx = 1;
        gbc.gridy = 5;
        mainPanel.add(lblPreview, gbc);

        // Button Konfirmasi
        btnKonfirmasi = new JButton("Submit");
        btnKonfirmasi.addActionListener(e -> handleSubmit());
        gbc.gridx = 1;
        gbc.gridy = 6;
        mainPanel.add(btnKonfirmasi, gbc);

        add(mainPanel);
    }

    private void loadKategori() {
        List<Kategori> kategoriList = kategoriController.getAllKategori();
        for (Kategori k : kategoriList) {
            cbxKategori.addItem(k);
        }
    }

    private void browseFoto() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            tfFoto.setText(selectedFile.getName());

            try {
                ImageIcon icon = new ImageIcon(selectedFile.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(200, 150, Image.SCALE_SMOOTH);
                lblPreview.setIcon(new ImageIcon(img));
                lblPreview.setText("");
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal memuat preview foto!");
            }
        }
    }

    private void handleSubmit() {
        // Ambil nilai input
        String judul = tfJudul.getText().trim();
        String lokasi = tfLokasi.getText().trim();
        String deskripsi = tfDeskripsi.getText().trim();
        Kategori kategori = (Kategori) cbxKategori.getSelectedItem();

        // Validasi
        if (judul.isEmpty() || lokasi.isEmpty() || deskripsi.isEmpty() || kategori == null) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi!", "Validasi Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Silakan pilih foto!", "Validasi Error", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (selectedFile.length() > 5 * 1024 * 1024) {
            JOptionPane.showMessageDialog(this, "Ukuran file maksimal 5MB!", "Validasi Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Simpan foto
        String fotoPath = null;
        try {
            File uploadsDir = new File("uploads");
            if (!uploadsDir.exists()) uploadsDir.mkdir();

            String fileName = System.currentTimeMillis() + "_" + selectedFile.getName();
            Path destination = Paths.get("uploads", fileName);
            Files.copy(selectedFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
            fotoPath = "uploads/" + fileName;
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menyimpan foto!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Buat Laporan
        Laporan laporan = new Laporan(
                currentUser.getUserId(),
                kategori.getKategoriId(),
                judul,
                deskripsi,
                lokasi,
                fotoPath
        );

        boolean success = laporanController.createLaporan(laporan);
        if (success) {
            JOptionPane.showMessageDialog(this, "Laporan berhasil dikirim!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal mengirim laporan!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
