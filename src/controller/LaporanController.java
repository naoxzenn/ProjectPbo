package controller;

import Model.Laporan;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LaporanController {

    // Method untuk create laporan baru
    public boolean createLaporan(Laporan laporan) {
        String sql = "INSERT INTO laporan (user_id, kategori_id, judul, deskripsi, lokasi, foto_path, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, laporan.getUserId());
            pstmt.setInt(2, laporan.getKategoriId());
            pstmt.setString(3, laporan.getJudul());
            pstmt.setString(4, laporan.getDeskripsi());
            pstmt.setString(5, laporan.getLokasi());
            pstmt.setString(6, laporan.getFotoPath());
            pstmt.setString(7, laporan.getStatus());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method untuk get laporan by user ID
    public List<Laporan> getLaporanByUserId(int userId) {
        List<Laporan> laporanList = new ArrayList<>();
        String sql = "SELECT l.*, k.nama_kategori FROM laporan l " +
                "JOIN kategori k ON l.kategori_id = k.kategori_id " +
                "WHERE l.user_id = ? ORDER BY l.tanggal_lapor DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Laporan laporan = extractLaporanFromResultSet(rs);
                laporanList.add(laporan);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return laporanList;
    }

    // Method untuk get all laporan (untuk admin)
    public List<Laporan> getAllLaporan() {
        List<Laporan> laporanList = new ArrayList<>();
        String sql = "SELECT l.*, u.nama as nama_user, k.nama_kategori FROM laporan l " +
                "JOIN users u ON l.user_id = u.user_id " +
                "JOIN kategori k ON l.kategori_id = k.kategori_id " +
                "ORDER BY l.tanggal_lapor DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Laporan laporan = extractLaporanFromResultSet(rs);
                laporan.setNamaUser(rs.getString("nama_user"));
                laporanList.add(laporan);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return laporanList;
    }

    // Method untuk get laporan by status
    public List<Laporan> getLaporanByStatus(String status) {
        List<Laporan> laporanList = new ArrayList<>();
        String sql = "SELECT l.*, u.nama as nama_user, k.nama_kategori FROM laporan l " +
                "JOIN users u ON l.user_id = u.user_id " +
                "JOIN kategori k ON l.kategori_id = k.kategori_id " +
                "WHERE l.status = ? ORDER BY l.tanggal_lapor DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Laporan laporan = extractLaporanFromResultSet(rs);
                laporan.setNamaUser(rs.getString("nama_user"));
                laporanList.add(laporan);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return laporanList;
    }

    // Method untuk update status laporan
    public boolean updateStatus(int laporanId, String status) {
        String sql = "UPDATE laporan SET status = ? WHERE laporan_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, laporanId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Method untuk get laporan by ID
    public Laporan getLaporanById(int laporanId) {
        String sql = "SELECT l.*, u.nama as nama_user, k.nama_kategori FROM laporan l " +
                "JOIN users u ON l.user_id = u.user_id " +
                "JOIN kategori k ON l.kategori_id = k.kategori_id " +
                "WHERE l.laporan_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, laporanId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Laporan laporan = extractLaporanFromResultSet(rs);
                laporan.setNamaUser(rs.getString("nama_user"));
                return laporan;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    // Method untuk count laporan by user dan status
    public int countLaporanByUserAndStatus(int userId, String status) {
        String sql = "SELECT COUNT(*) FROM laporan WHERE user_id = ? AND status = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            pstmt.setString(2, status);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // Method untuk count laporan by status (untuk admin)
    public int countLaporanByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM laporan WHERE status = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // Method untuk search laporan
    public List<Laporan> searchLaporan(String keyword) {
        List<Laporan> laporanList = new ArrayList<>();
        String sql = "SELECT l.*, u.nama as nama_user, k.nama_kategori FROM laporan l " +
                "JOIN users u ON l.user_id = u.user_id " +
                "JOIN kategori k ON l.kategori_id = k.kategori_id " +
                "WHERE l.judul LIKE ? OR l.lokasi LIKE ? OR u.nama LIKE ? " +
                "ORDER BY l.tanggal_lapor DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Laporan laporan = extractLaporanFromResultSet(rs);
                laporan.setNamaUser(rs.getString("nama_user"));
                laporanList.add(laporan);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return laporanList;
    }

    // Helper method untuk extract laporan dari ResultSet
    private Laporan extractLaporanFromResultSet(ResultSet rs) throws SQLException {
        Laporan laporan = new Laporan();
        laporan.setLaporanId(rs.getInt("laporan_id"));
        laporan.setUserId(rs.getInt("user_id"));
        laporan.setKategoriId(rs.getInt("kategori_id"));
        laporan.setJudul(rs.getString("judul"));
        laporan.setDeskripsi(rs.getString("deskripsi"));
        laporan.setLokasi(rs.getString("lokasi"));
        laporan.setFotoPath(rs.getString("foto_path"));
        laporan.setStatus(rs.getString("status"));
        laporan.setTanggalLapor(rs.getTimestamp("tanggal_lapor"));
        laporan.setTanggalUpdate(rs.getTimestamp("tanggal_update"));
        laporan.setNamaKategori(rs.getString("nama_kategori"));
        return laporan;
    }
}