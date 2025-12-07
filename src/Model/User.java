package Model;

import java.sql.Timestamp;

public class User {
    private int user_id;
    private String username;
    private String password;
    private String nama;
    private String email;
    private String role;
    private Timestamp created_at;

    // Constructor kosong
    public User() {
        this.role = "user"; // Default role
    }

    // Constructor dengan parameter (TANPA no_telp)
    public User(String username, String password, String nama, String email) {
        this.username = username;
        this.password = password;
        this.nama = nama;
        this.email = email;
        this.role = "user";
    }

    // Getters and Setters
    public int getUserId() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Timestamp getCreatedAt() {
        return created_at;
    }

    public void setCreatedAt(Timestamp created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + user_id +
                ", username='" + username + '\'' +
                ", nama='" + nama + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}