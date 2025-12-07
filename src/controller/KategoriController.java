package controller;

import com.model.Kategori;
import com.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KategoriController {

    // Method untuk get all kategori
    public List<Kategori> getAllKategori() {
        List<Kategori> kategoriList = new ArrayList<>();
        String sql = "SELECT * FROM kategori ORDER BY nama_kategori";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Kategori kategori = new Kategori();
                kategori.setKategoriId(rs.getInt("kategori_id"));
                kategori.setNamaKategori(rs.getString("nama_kategori"));
                kategori.setDeskripsi(rs.getString("deskripsi"));
                kategoriList.add(kategori);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return kategoriList;
    }

    // Method untuk get kategori by ID
    public Kategori getKategoriById(int kategoriId) {
        String sql = "SELECT * FROM kategori WHERE kategori_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, kategoriId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Kategori kategori = new Kategori();
                kategori.setKategoriId(rs.getInt("kategori_id"));
                kategori.setNamaKategori(rs.getString("nama_kategori"));
                kategori.setDeskripsi(rs.getString("deskripsi"));
                return kategori;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Method untuk create kategori baru
    public boolean createKategori(Kategori kategori) {
        String sql = "INSERT INTO kategori (nama_kategori, deskripsi) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, kategori.getNamaKategori());
            pstmt.setString(2, kategori.getDeskripsi());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method untuk update kategori
    public boolean updateKategori(Kategori kategori) {
        String sql = "UPDATE kategori SET nama_kategori = ?, deskripsi = ? WHERE kategori_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, kategori.getNamaKategori());
            pstmt.setString(2, kategori.getDeskripsi());
            pstmt.setInt(3, kategori.getKategoriId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method untuk delete kategori
    public boolean deleteKategori(int kategoriId) {
        String sql = "DELETE FROM kategori WHERE kategori_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, kategoriId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}