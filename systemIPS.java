import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.sql.*;
import java.util.*;

public class systemIPS {
//Mengkonekkan Database dengan Program
    private static final String URL = "jdbc:mysql://localhost:3307/systemIPS";
    private static final String USER = "root"; // Ubah sesuai konfigurasi MySQL Anda
    private static final String PASSWORD = ""; // Ubah sesuai konfigurasi MySQL Anda

    public static void main(String[] args) {
    //mengubah tampilan dari POP-Up
        UIManager.put("Panel.background", new ColorUIResource(255, 165, 0)); // Latar belakang Oren SI
        UIManager.put("OptionPane.messageForeground", new ColorUIResource(0, 0, 0)); // Teks hitam
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            JOptionPane.showMessageDialog(null, "Koneksi ke database berhasil!", "Informasi", JOptionPane.INFORMATION_MESSAGE);

        // Bagian Login
            boolean loginBerhasil = false;
            while (!loginBerhasil) {
                loginBerhasil = login();
            }

        // Fitur CRUD tambahan setelah login
            boolean exit = false;
            while (!exit) {
                String[] options = {"Create", "Read", "Update", "Delete", "Exit"};
                int choice = JOptionPane.showOptionDialog(null, "Pilih Menu yang ingin Dilakukan :", "Menu Beranda",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

                switch (choice) {
                    case 0 -> createMahasiswa(conn); // Create Data Mahasiswa
                    case 1 -> readMahasiswa(conn); // Read Data Mahasiswa
                    case 2 -> updateMahasiswa(conn); // Update Data Mahasiswa
                    case 3 -> deleteMahasiswa(conn); // Delete Data Mahasiswa
                    case 4 -> exit = true; // Exit Menu Beranda
                    default -> JOptionPane.showMessageDialog(null, "Pilihan tidak valid.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

            private static boolean login() {
                // Membuat angka random untuk CAPTCHA
                    Random random = new Random();
                    int captchaValue = 1000 + random.nextInt(9000);

                // Membuat panel untuk login
                    JPanel panel = new JPanel();
                    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                    JTextField usernameField = new JTextField(15);
                    JPasswordField passwordField = new JPasswordField(15);
                    JTextField captchaField = new JTextField(15);

                // Tambahkan label dan input field ke panel
                    panel.add(new JLabel("Username:"));
                    panel.add(usernameField);
                    panel.add(new JLabel("Password:"));
                    panel.add(passwordField);
                    panel.add(new JLabel("Masukkan CAPTCHA: " + captchaValue));
                    panel.add(captchaField);

                // Tampilkan dialog login
                    int result = JOptionPane.showConfirmDialog(null, panel, "Login",
                            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (result == JOptionPane.OK_OPTION) {
                        String username = usernameField.getText().trim();
                        String password = new String(passwordField.getPassword()).trim();
                        String captchaInput = captchaField.getText().trim();

                    // Validasi input
                        if (username.isEmpty() || password.isEmpty() || captchaInput.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }

                    //Menggunakan Percabangan Untuk Capcha
                        if (!captchaInput.equals(String.valueOf(captchaValue))) {
                            JOptionPane.showMessageDialog(null, "CAPTCHA salah!", "Error", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }

                    // Validasi username dan password
                        if (username.equals("van") && password.equals("123")) {
                            JOptionPane.showMessageDialog(null, "Login berhasil!", "Login", JOptionPane.INFORMATION_MESSAGE);
                            return true;
                        } else {
                            JOptionPane.showMessageDialog(null, "Username atau password salah.", "Login", JOptionPane.ERROR_MESSAGE);
                            return false;
                        }
                        } else {
                            JOptionPane.showMessageDialog(null, "Login dibatalkan.", "Login", JOptionPane.WARNING_MESSAGE);
                            return false;
                    }
                }

    //Membuat Data Mahasiswa
            private static void createMahasiswa(Connection conn) {
                try {
            // Membuat panel untuk input data mahasiswa
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
                JTextField namaField = new JTextField(15);
                JTextField nimField = new JTextField(15);
                JTextField jurusanField = new JTextField(15);
                JTextField fakultasField = new JTextField(15);
        
            // Tambahkan label dan input field ke panel
                panel.add(new JLabel("Nama Mahasiswa:"));
                panel.add(namaField);
                panel.add(new JLabel("NIM (10 digit):"));
                panel.add(nimField);
                panel.add(new JLabel("Jurusan:"));
                panel.add(jurusanField);
                panel.add(new JLabel("Fakultas:"));
                panel.add(fakultasField);

                // Tampilkan dialog untuk input data mahasiswa
                int result = JOptionPane.showConfirmDialog(null, panel, "Input Data Mahasiswa",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                if (result == JOptionPane.OK_OPTION) {
                // Mengambil data dari input
                    String nama = namaField.getText().trim();
                    String nim = nimField.getText().trim();
                    String jurusan = jurusanField.getText().trim();
                    String fakultas = fakultasField.getText().trim();

                    if (nama.isEmpty() || nim.isEmpty() || jurusan.isEmpty() || fakultas.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Semua field harus diisi!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Input mata kuliah
                    Map<String, Map.Entry<Double, Integer>> mataKuliahData = new HashMap<>();
                    HashSet<String> daftarMataKuliah = new HashSet<>();
                    int jumlahMataKuliah = Integer.parseInt(JOptionPane.showInputDialog("Masukkan jumlah mata kuliah:"));

                    for (int i = 1; i <= jumlahMataKuliah; i++) {
                        JPanel matkulPanel = new JPanel();
                        matkulPanel.setLayout(new BoxLayout(matkulPanel, BoxLayout.Y_AXIS));

                        JTextField matkulField = new JTextField(15);
                        JTextField ipField = new JTextField(15);
                        JTextField sksField = new JTextField(15);

                        matkulPanel.add(new JLabel("Nama Mata Kuliah ke-" + i + ":"));
                        matkulPanel.add(matkulField);
                        matkulPanel.add(new JLabel("IP Mata Kuliah ke-" + i + ":"));
                        matkulPanel.add(ipField);
                        matkulPanel.add(new JLabel("SKS Mata Kuliah ke-" + i + ":"));
                        matkulPanel.add(sksField);

                        int matkulResult = JOptionPane.showConfirmDialog(null, matkulPanel, "Input Data Mata Kuliah",
                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                        if (matkulResult == JOptionPane.OK_OPTION) {
                            String matkul = matkulField.getText().trim();
                            double ip = Double.parseDouble(ipField.getText().trim());
                            int sks = Integer.parseInt(sksField.getText().trim());

                            if (matkul.isEmpty()) {
                                JOptionPane.showMessageDialog(null, "Nama mata kuliah tidak boleh kosong!", "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                            }

                            mataKuliahData.put(matkul, Map.entry(ip, sks));
                            daftarMataKuliah.add(matkul);
                        } else {
                            return; // Batalkan jika pengguna menekan Cancel
                        }
                    }

                    // Buat objek mahasiswa dengan daftar mata kuliah
                    MataKuliah mahasiswa = new MataKuliah(nama, nim, jurusan, fakultas, daftarMataKuliah);
                    mahasiswa.hitungIPS(mataKuliahData);

                    // Simpan data ke database
                    String query = "INSERT INTO mahasiswa (nim, nama, jurusan, fakultas, totalSKS, ips) VALUES (?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        pstmt.setString(1, nim);
                        pstmt.setString(2, nama);
                        pstmt.setString(3, jurusan);
                        pstmt.setString(4, fakultas);
                        pstmt.setInt(5, mahasiswa.getTotalSKS());
                        pstmt.setDouble(6, mahasiswa.getIps());
                        pstmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "Data berhasil ditambahkan!", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                    }

                    // Langsung tampilkan data setelah berhasil disimpan
                    mahasiswa.tampilkanData();
                }

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Input tidak valid. Harap masukkan data yang benar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    // Read data mahasiswa
            private static void readMahasiswa(Connection conn) {
                try {
                    String query = "SELECT * FROM mahasiswa";
                    try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                        StringBuilder data = new StringBuilder("Data Mahasiswa:\n");
                        while (rs.next()) {
                            data.append("NIM: ").append(rs.getString("nim"))
                                .append(", Nama: ").append(rs.getString("nama"))
                                .append(", Jurusan: ").append(rs.getString("jurusan"))
                                .append(", Fakultas: ").append(rs.getString("fakultas"))
                                .append(", Total SKS: ").append(rs.getInt("totalSKS"))
                                .append(", IPS: ").append(rs.getDouble("ips"))
                                .append(", Dibuat pada: ").append(rs.getString("created_at"))   
                                .append("\n");
                        }
                        JOptionPane.showMessageDialog(null, data.toString(), "Data Mahasiswa", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
    //update Data Mahasiswa (Matkul, IP, SKS, IPS)
            private static void updateMahasiswa(Connection conn) {
                try {
            // Input NIM mahasiswa yang akan diupdate
                    String nim = JOptionPane.showInputDialog("Masukkan NIM Mahasiswa yang akan diupdate:");

                // Query untuk mengambil data mahasiswa berdasarkan NIM
                    String query = "SELECT * FROM mahasiswa WHERE nim = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        pstmt.setString(1, nim);
                        try (ResultSet rs = pstmt.executeQuery()) {
                            if (rs.next()) {
                            // Menampilkan informasi mahasiswa yang ada
                                String nama = rs.getString("nama");
                                String jurusan = rs.getString("jurusan");
                                String fakultas = rs.getString("fakultas");
                                int totalSKS = rs.getInt("totalSKS");

                            // Menampilkan data mahasiswa yang akan diupdate
                                JOptionPane.showMessageDialog(null, "Data Mahasiswa: \n" +
                                    "Nama: " + nama + "\n" +
                                    "Jurusan: " + jurusan + "\n" +
                                    "Fakultas: " + fakultas + "\n" +
                                    "Total SKS: " + totalSKS, "Info Mahasiswa", JOptionPane.INFORMATION_MESSAGE);

                            // Input ulang untuk mata kuliah
                                Map<String, Map.Entry<Double, Integer>> mataKuliahData = new HashMap<>();
                                int jumlahMataKuliah = Integer.parseInt(JOptionPane.showInputDialog("Masukkan jumlah mata kuliah:"));
                                for (int i = 1; i <= jumlahMataKuliah; i++) {
                                    String matkul = JOptionPane.showInputDialog("Masukkan Nama Mata Kuliah ke-" + i + ":");
                                    double ip = Double.parseDouble(JOptionPane.showInputDialog("Masukkan IP Mata Kuliah ke-" + i + ":"));
                                    int sks = Integer.parseInt(JOptionPane.showInputDialog("Masukkan SKS Mata Kuliah ke-" + i + ":"));
                                    mataKuliahData.put(matkul, Map.entry(ip, sks));
                                }

                            // Hitung ulang IPS dan total SKS berdasarkan mata kuliah yang baru
                                Mahasiswa mahasiswa = new Mahasiswa(nama, nim, jurusan, fakultas);
                                mahasiswa.hitungIPS(mataKuliahData);
                                double ipsBaru = mahasiswa.getIps();
                                int totalSKSBaru = mahasiswa.getTotalSKS();

                                String updateQuery = "UPDATE mahasiswa SET ips = ?, totalSKS = ? WHERE nim = ?";
                            // Update nilai IPS dan Total SKS ke dalam database
                                try (PreparedStatement updatePstmt = conn.prepareStatement(updateQuery)) {
                                    updatePstmt.setDouble(1, ipsBaru); // IPS baru yang dihitung
                                    updatePstmt.setInt(2, totalSKSBaru); // Total SKS baru yang dihitung
                                    updatePstmt.setString(3, nim);
                                    int rowsAffected = updatePstmt.executeUpdate();

                                    if (rowsAffected > 0) {
                                        JOptionPane.showMessageDialog(null, "IPS dan Total SKS berhasil dihitung ulang dan data berhasil diupdate!", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                                    } else {
                                        JOptionPane.showMessageDialog(null, "Data dengan NIM tersebut tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
                                    }
                                }

                            } else {
                                JOptionPane.showMessageDialog(null, "Mahasiswa dengan NIM tersebut tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }

                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

    // Delete data mahasiswa
            private static void deleteMahasiswa(Connection conn) {
                try {
                    String nim = JOptionPane.showInputDialog("Masukkan NIM Mahasiswa yang akan dihapus:");
                    String query = "DELETE FROM mahasiswa WHERE nim = ?";
                    try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                        pstmt.setString(1, nim);
                        int rowsAffected = pstmt.executeUpdate();

                        if (rowsAffected > 0) {
                            JOptionPane.showMessageDialog(null, "Data berhasil dihapus!", "Informasi", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "Data tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } catch (SQLException e) {
                    JOptionPane.showMessageDialog(null, "Error menghapus data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }