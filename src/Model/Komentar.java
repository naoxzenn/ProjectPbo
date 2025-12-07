package com.infrastruktur.pelaporan.model;

import java.sql.Timestamp;

public class Komentar {
    private int komentarId;
    private int laporanId;
    private int adminId;
    private String isiKomentar;
    private Timestamp tanggal;

    // Data tambahan untuk join
    private String namaAdmin;

    // Constructor kosong
    public Komentar() {}

    // Constructor dengan parameter
    public Komentar(int laporanId, int adminId, String isiKomentar) {
        this.laporanId = laporanId;
        this.adminId = adminId;
        this.isiKomentar = isiKomentar;
    }

    // Getters and Setters
    public int getKomentarId() {
        return komentarId;
    }

    public void setKomentarId(int komentarId) {
        this.komentarId = komentarId;
    }

    public int getLaporanId() {
        return laporanId;
    }

    public void setLaporanId(int laporanId) {
        this.laporanId = laporanId;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getIsiKomentar() {
        return isiKomentar;
    }

    public void setIsiKomentar(String isiKomentar) {
        this.isiKomentar = isiKomentar;
    }

    public Timestamp getTanggal() {
        return tanggal;
    }

    public void setTanggal(Timestamp tanggal) {
        this.tanggal = tanggal;
    }

    public String getNamaAdmin() {
        return namaAdmin;
    }

    public void setNamaAdmin(String namaAdmin) {
        this.namaAdmin = namaAdmin;
    }

    @Override
    public String toString() {
        return "Komentar{" +
                "komentarId=" + komentarId +
                ", isiKomentar='" + isiKomentar + '\'' +
                ", tanggal=" + tanggal +
                '}';
    }
}