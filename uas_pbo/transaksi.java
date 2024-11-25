package uas_pbo;

import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class transaksi extends JFrame {
    private JTextField txtIdTransaksi, txtIdKonsumen, txtIdBarang, txtQuantity, txtTotalBiaya;
    private JTable tableTransaksi;
    private DefaultTableModel modelTransaksi;
    private Connection conn;

    public transaksi() {
        setTitle("CRUD Transaksi");
        setSize(700, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        // Label dan TextField
        JLabel lblIdTransaksi = new JLabel("ID Transaksi:");
        lblIdTransaksi.setBounds(20, 20, 100, 30);
        add(lblIdTransaksi);
        txtIdTransaksi = new JTextField();
        txtIdTransaksi.setBounds(120, 20, 150, 30);
        txtIdTransaksi.setEditable(false); // ID otomatis
        add(txtIdTransaksi);

        JLabel lblIdKonsumen = new JLabel("ID Konsumen:");
        lblIdKonsumen.setBounds(20, 60, 100, 30);
        add(lblIdKonsumen);
        txtIdKonsumen = new JTextField();
        txtIdKonsumen.setBounds(120, 60, 150, 30);
        add(txtIdKonsumen);

        JLabel lblIdBarang = new JLabel("ID Barang:");
        lblIdBarang.setBounds(20, 100, 100, 30);
        add(lblIdBarang);
        txtIdBarang = new JTextField();
        txtIdBarang.setBounds(120, 100, 150, 30);
        add(txtIdBarang);

        JLabel lblQuantity = new JLabel("Quantity:");
        lblQuantity.setBounds(20, 140, 100, 30);
        add(lblQuantity);
        txtQuantity = new JTextField();
        txtQuantity.setBounds(120, 140, 150, 30);
        add(txtQuantity);

        JLabel lblTotalBiaya = new JLabel("Total Biaya:");
        lblTotalBiaya.setBounds(20, 180, 100, 30);
        add(lblTotalBiaya);
        txtTotalBiaya = new JTextField();
        txtTotalBiaya.setBounds(120, 180, 150, 30);
        add(txtTotalBiaya);

        // Tombol
        JButton btnTambah = new JButton("Tambah");
        btnTambah.setBounds(20, 220, 100, 30);
        add(btnTambah);

        JButton btnUbah = new JButton("Ubah");
        btnUbah.setBounds(130, 220, 100, 30);
        add(btnUbah);

        JButton btnHapus = new JButton("Hapus");
        btnHapus.setBounds(240, 220, 100, 30);
        add(btnHapus);

        // Tabel
        modelTransaksi = new DefaultTableModel(new String[]{"ID Transaksi", "ID Konsumen", "ID Barang", "Quantity", "Total Biaya"}, 0);
        tableTransaksi = new JTable(modelTransaksi);
        JScrollPane sp = new JScrollPane(tableTransaksi);
        sp.setBounds(20, 270, 650, 150);
        add(sp);

        // Koneksi ke database
        connectToDatabase();
        loadData();

        // Event Listener
        btnTambah.addActionListener(e -> tambahTransaksi());
        btnUbah.addActionListener(e -> ubahTransaksi());
        btnHapus.addActionListener(e -> hapusTransaksi());

        // Event Listener untuk memilih data dari tabel
        tableTransaksi.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = tableTransaksi.getSelectedRow();
            if (selectedRow != -1) {
                txtIdTransaksi.setText(modelTransaksi.getValueAt(selectedRow, 0).toString());
                txtIdKonsumen.setText(modelTransaksi.getValueAt(selectedRow, 1).toString());
                txtIdBarang.setText(modelTransaksi.getValueAt(selectedRow, 2).toString());
                txtQuantity.setText(modelTransaksi.getValueAt(selectedRow, 3).toString());
                txtTotalBiaya.setText(modelTransaksi.getValueAt(selectedRow, 4).toString());
            }
        });
    }

    // Metode untuk menghubungkan ke database
    private void connectToDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/toko", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Koneksi database gagal!");
        }
    }

    // Memuat data dari database ke tabel
    private void loadData() {
        modelTransaksi.setRowCount(0); // Hapus semua data di tabel
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM data_transaksi");
            while (rs.next()) {
                modelTransaksi.addRow(new Object[]{
                        rs.getInt("id_transaksi"),
                        rs.getInt("id_konsumen"),
                        rs.getInt("id_barang"),
                        rs.getInt("quantity"),
                        rs.getDouble("total_biaya")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Menambah data transaksi ke database
    private void tambahTransaksi() {
        try {
            int idKonsumen = Integer.parseInt(txtIdKonsumen.getText());
            int idBarang = Integer.parseInt(txtIdBarang.getText());
            int quantity = Integer.parseInt(txtQuantity.getText());
            double totalBiaya = Double.parseDouble(txtTotalBiaya.getText());

            String query = "INSERT INTO data_transaksi (id_konsumen, id_barang, quantity, total_biaya) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, idKonsumen);
            stmt.setInt(2, idBarang);
            stmt.setInt(3, quantity);
            stmt.setDouble(4, totalBiaya);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Transaksi berhasil ditambahkan!");
            loadData();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menambahkan transaksi.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Pastikan semua data terisi dengan format yang benar.");
        }
    }

    // Mengubah data transaksi di database
    private void ubahTransaksi() {
        try {
            int idTransaksi = Integer.parseInt(txtIdTransaksi.getText());
            int idKonsumen = Integer.parseInt(txtIdKonsumen.getText());
            int idBarang = Integer.parseInt(txtIdBarang.getText());
            int quantity = Integer.parseInt(txtQuantity.getText());
            double totalBiaya = Double.parseDouble(txtTotalBiaya.getText());

            String query = "UPDATE data_transaksi SET id_konsumen = ?, id_barang = ?, quantity = ?, total_biaya = ? WHERE id_transaksi = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, idKonsumen);
            stmt.setInt(2, idBarang);
            stmt.setInt(3, quantity);
            stmt.setDouble(4, totalBiaya);
            stmt.setInt(5, idTransaksi);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Transaksi berhasil diperbarui!");
            loadData();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal mengubah transaksi.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Pastikan semua data terisi dengan format yang benar.");
        }
    }

    // Menghapus data transaksi dari database
    private void hapusTransaksi() {
        try {
            int idTransaksi = Integer.parseInt(txtIdTransaksi.getText());

            String query = "DELETE FROM data_transaksi WHERE id_transaksi = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, idTransaksi);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Transaksi berhasil dihapus!");
            loadData();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Gagal menghapus transaksi.");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "ID Transaksi tidak valid.");
        }
    }

    // Mengosongkan semua field input
    private void clearFields() {
        txtIdTransaksi.setText("");
        txtIdKonsumen.setText("");
        txtIdBarang.setText("");
        txtQuantity.setText("");
        txtTotalBiaya.setText("");
    }

    // Main method untuk menjalankan aplikasi
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new transaksi().setVisible(true));
    }
}
