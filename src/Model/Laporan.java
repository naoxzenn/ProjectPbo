package Model;

import java.sql.Timestamp;

public class Laporan {
    private int laporanId;
    private int userId;
    private int kategoriId;
    private String judul;
    private String deskripsi;
    private String lokasi;
    private String fotoPath;
    private String status;
    private Timestamp tanggalLapor;
    private Timestamp tanggalUpdate;

    // Data tambahan untuk join
    private String namaUser;
    private String namaKategori;

    // Constructor kosong
    public Laporan() {
        this.status = "Pending";
    }

    // Constructor dengan parameter
    public Laporan(int userId, int kategoriId, String judul, String deskripsi,
                   String lokasi, String fotoPath) {
        this.userId = userId;
        this.kategoriId = kategoriId;
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.lokasi = lokasi;
        this.fotoPath = fotoPath;
        this.status = "Pending";
    }

    // Getters and Setters
    public int getLaporanId() {
        return laporanId;
    }

    public void setLaporanId(int laporanId) {
        this.laporanId = laporanId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getKategoriId() {
        return kategoriId;
    }

    public void setKategoriId(int kategoriId) {
        this.kategoriId = kategoriId;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getFotoPath() {
        return fotoPath;
    }

    public void setFotoPath(String fotoPath) {
        this.fotoPath = fotoPath;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getTanggalLapor() {
        return tanggalLapor;
    }

    public void setTanggalLapor(Timestamp tanggalLapor) {
        this.tanggalLapor = tanggalLapor;
    }

    public Timestamp getTanggalUpdate() {
        return tanggalUpdate;
    }

    public void setTanggalUpdate(Timestamp tanggalUpdate) {
        this.tanggalUpdate = tanggalUpdate;
    }

    public String getNamaUser() {
        return namaUser;
    }

    public void setNamaUser(String namaUser) {
        this.namaUser = namaUser;
    }

    public String getNamaKategori() {
        return namaKategori;
    }

    public void setNamaKategori(String namaKategori) {
        this.namaKategori = namaKategori;
    }

    @Override
    public String toString() {
        return "Laporan{" +
                "laporanId=" + laporanId +
                ", judul='" + judul + '\'' +
                ", status='" + status + '\'' +
                ", tanggalLapor=" + tanggalLapor +
                '}';
    }
}