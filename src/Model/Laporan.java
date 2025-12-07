package Model;

public class Laporan {
    private int id;
    private String user;
    private String judul;
    private String status;

    public Laporan(int id, String user, String judul, String status) {
        this.id = id;
        this.user = user;
        this.judul = judul;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getUser() {
        return user;
    }

    public String getJudul() {
        return judul;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
