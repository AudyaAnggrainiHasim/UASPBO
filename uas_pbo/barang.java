package uas_pbo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;

public class barang extends JFrame {
    private JTextField txtIdBarang, txtNamaBarang, txtHarga;
    private JTable tableBarang;
    private DefaultTableModel model;
    private Connection connection;

    public barang() {
        connectToDatabase(); // Koneksi ke database

        // Frame
        setTitle("CRUD Data Barang");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        // Komponen Input
        JLabel lblId = new JLabel("ID Barang:");
        lblId.setBounds(20, 20, 100, 25);
        add(lblId);

        txtIdBarang = new JTextField();
        txtIdBarang.setBounds(130, 20, 200, 25);
        add(txtIdBarang);

        JLabel lblNama = new JLabel("Nama Barang:");
        lblNama.setBounds(20, 60, 100, 25);
        add(lblNama);

        txtNamaBarang = new JTextField();
        txtNamaBarang.setBounds(130, 60, 200, 25);
        add(txtNamaBarang);

        JLabel lblHarga = new JLabel("Harga:");
        lblHarga.setBounds(20, 100, 100, 25);
        add(lblHarga);

        txtHarga = new JTextField();
        txtHarga.setBounds(130, 100, 200, 25);
        add(txtHarga);

        // Tombol Aksi
        JButton btnTambah = new JButton("Tambah");
        btnTambah.setBounds(20, 140, 100, 25);
        add(btnTambah);

        JButton btnEdit = new JButton("Edit");
        btnEdit.setBounds(130, 140, 100, 25);
        add(btnEdit);

        JButton btnHapus = new JButton("Hapus");
        btnHapus.setBounds(240, 140, 100, 25);
        add(btnHapus);

        // Tabel
        model = new DefaultTableModel();
        model.addColumn("ID Barang");
        model.addColumn("Nama Barang");
        model.addColumn("Harga");

        tableBarang = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tableBarang);
        scrollPane.setBounds(20, 180, 540, 150);
        add(scrollPane);

        // Event Listener
        btnTambah.addActionListener(e -> tambahBarang());
        btnEdit.addActionListener(e -> editBarang());
        btnHapus.addActionListener(e -> hapusBarang());
        tableBarang.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tableBarang.getSelectedRow();
                txtIdBarang.setText(model.getValueAt(row, 0).toString());
                txtNamaBarang.setText(model.getValueAt(row, 1).toString());
                txtHarga.setText(model.getValueAt(row, 2).toString());
            }
        });

        loadData();
        setVisible(true);
    }

    private void connectToDatabase() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/toko", "root", "");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Koneksi database gagal!");
        }
    }

    private void loadData() {
        model.setRowCount(0);
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM data_barang");
            while (resultSet.next()) {
                model.addRow(new Object[]{
                        resultSet.getString("id_barang"),
                        resultSet.getString("nama_barang"),
                        resultSet.getDouble("harga")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data barang!");
        }
    }

    private void tambahBarang() {
        try {
            String sql = "INSERT INTO data_barang (id_barang, nama_barang, harga) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, txtIdBarang.getText());
            ps.setString(2, txtNamaBarang.getText());
            ps.setDouble(3, Double.parseDouble(txtHarga.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data barang berhasil ditambahkan!");
            loadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menambahkan data barang!");
        }
    }

    private void editBarang() {
        try {
            String sql = "UPDATE data_barang SET nama_barang = ?, harga = ? WHERE id_barang = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, txtNamaBarang.getText());
            ps.setDouble(2, Double.parseDouble(txtHarga.getText()));
            ps.setString(3, txtIdBarang.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data barang berhasil diupdate!");
            loadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal mengupdate data barang!");
        }
    }

    private void hapusBarang() {
        try {
            String sql = "DELETE FROM data_barang WHERE id_barang = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, txtIdBarang.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data barang berhasil dihapus!");
            loadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus data barang!");
        }
    }

    public static void main(String[] args) {
        new barang();
    }
}