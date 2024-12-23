import javax.swing.*;
import java.sql.*;
import java.util.*;

// Interface untuk data mahasiswa
interface DataMahasiswa {
    void tampilkanData();
}

// Kelas Mahasiswa
class Mahasiswa implements DataMahasiswa { //Super Class Mahasiswa
    private String nama;
    private String nim;
    private String jurusan;
    private String fakultas;
    private int totalSKS;
    private double ips;

    // Objek Mahasiswa
    public Mahasiswa(String nama, String nim, String jurusan, String fakultas) {
        this.nama = nama;
        this.nim = nim;
        this.jurusan = jurusan;
        this.fakultas = fakultas;
        this.totalSKS = 0;
        this.ips = 0.0;
    }

    // Getter untuk totalSKS
    public int getTotalSKS() {
        return totalSKS;
    }

    // Getter untuk IPS
    public double getIps() {
        return ips;
    }

    // Method untuk menghitung IPS berdasarkan mata kuliah yang diinputkan
    public void hitungIPS(Map<String, Map.Entry<Double, Integer>> mataKuliahData) { //Collection Framework penggunaan Map
        try {
            double totalNilai = 0;
            totalSKS = 0;

            //Perulangan untuk Menghitung IPS
            for (Map.Entry<String, Map.Entry<Double, Integer>> entry : mataKuliahData.entrySet()) {
                double ip = entry.getValue().getKey();
                int sks = entry.getValue().getValue();

                //Exception Handling
                if (sks <= 0) {
                    throw new IllegalArgumentException("SKS harus berupa angka positif.");
                }
                totalSKS += sks;
                totalNilai += ip * sks;
            }

            if (totalSKS == 0) {
                throw new ArithmeticException("Total SKS tidak boleh nol.");
            }

            //Perhitungan Matematika untuk Menghitung IPS dari IP dan SKS yang diInput
            this.ips = Math.round((totalNilai / totalSKS) * 100.0) / 100.0;
        } catch (ArithmeticException e) {
            JOptionPane.showMessageDialog(null, "Kesalahan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method untuk menampilkan data mahasiswa
    @Override
    public void tampilkanData() {
        //Penggunaan Method Date
        String timestamp = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date());
        JOptionPane.showMessageDialog(null,
                "Data Mahasiswa:\n" +
                        "Tanggal dan Waktu: " + timestamp + "\n" +
                        "Nama Lengkap: " + nama + "\n" +
                        "NIM: " + nim + "\n" +
                        "Jurusan: " + jurusan + "\n" +
                        "Fakultas: " + fakultas + "\n" +
                        "Total SKS: " + totalSKS + "\n" +
                        "IPS: " + ips,
                "Informasi Mahasiswa", JOptionPane.INFORMATION_MESSAGE);
    }

    // Method untuk menyimpan data ke database
    public void simpanKeDatabase(Connection conn) throws SQLException {
        String query = "INSERT INTO mahasiswa (nim, nama, jurusan, fakultas, totalSKS, ips) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, nim);
            pstmt.setString(2, nama);
            pstmt.setString(3, jurusan);
            pstmt.setString(4, fakultas);
            pstmt.setInt(5, getTotalSKS()); // Menggunakan getter untuk totalSKS
            pstmt.setDouble(6, getIps());   // Menggunakan getter untuk ips
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Data berhasil disimpan ke database!", "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}