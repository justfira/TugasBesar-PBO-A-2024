import javax.swing.*;
import java.util.*;
//Hashset Menyimpan daftar Mata Kuliah
class MataKuliah extends Mahasiswa { //SubClass dari Class Mahasiswa
    private HashSet<String> daftarMataKuliah;

    public MataKuliah(String nama, String nim, String jurusan, String fakultas, HashSet<String> daftarMataKuliah) {
        super(nama, nim, jurusan, fakultas);
        this.daftarMataKuliah = daftarMataKuliah;
    }

    @Override
    public void tampilkanData() {
        super.tampilkanData();
        StringBuilder sb = new StringBuilder("Daftar Mata Kuliah:\n");
        for (String matkul : daftarMataKuliah) {
            sb.append("- ").append(matkul).append("\n");
        }
        JOptionPane.showMessageDialog(null, sb.toString(), "Mata Kuliah", JOptionPane.INFORMATION_MESSAGE);
    }
}
