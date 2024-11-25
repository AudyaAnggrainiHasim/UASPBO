package uas_pbo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;

public class konsumen extends JFrame {
    private JTextField txtIdKonsumen, txtNamaKonsumen;
    private JTable tableKonsumen;
    private DefaultTableModel model;
    private Connection connection;

    public konsumen() {
        connectToDatabase(); // Koneksi ke database

        // Frame
        setTitle("CRUD Data Konsumen");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        // Komponen Input
        JLabel lblId = new JLabel("ID Konsumen:");
        lblId.setBounds(20, 20, 100, 25);
        add(lblId);

        txtIdKonsumen = new JTextField();
        txtIdKonsumen.setBounds(130, 20, 200, 25);
        add(txtIdKonsumen);

        JLabel lblNama = new JLabel("Nama Konsumen:");
        lblNama.setBounds(20, 60, 100, 25);
        add(lblNama);

        txtNamaKonsumen = new JTextField();
        txtNamaKonsumen.setBounds(130, 60, 200, 25);
        add(txtNamaKonsumen);

        // Tombol Aksi
        JButton btnTambah = new JButton("Tambah");
        btnTambah.setBounds(20, 100, 100, 25);
        add(btnTambah);

        JButton btnEdit = new JButton("Edit");
        btnEdit.setBounds(130, 100, 100, 25);
        add(btnEdit);

        JButton btnHapus = new JButton("Hapus");
        btnHapus.setBounds(240, 100, 100, 25);
        add(btnHapus);

        // Tabel
        model = new DefaultTableModel();
        model.addColumn("ID Konsumen");
        model.addColumn("Nama Konsumen");

        tableKonsumen = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(tableKonsumen);
        scrollPane.setBounds(20, 140, 540, 200);
        add(scrollPane);

        // Event Listener
        btnTambah.addActionListener(e -> tambahKonsumen());
        btnEdit.addActionListener(e -> editKonsumen());
        btnHapus.addActionListener(e -> hapusKonsumen());
        tableKonsumen.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tableKonsumen.getSelectedRow();
                txtIdKonsumen.setText(model.getValueAt(row, 0).toString());
                txtNamaKonsumen.setText(model.getValueAt(row, 1).toString());
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
            ResultSet resultSet = statement.executeQuery("SELECT * FROM data_konsumen");
            while (resultSet.next()) {
                model.addRow(new Object[]{
                        resultSet.getString("id_konsumen"),
                        resultSet.getString("nama_konsumen")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat data konsumen!");
        }
    }

    private void tambahKonsumen() {
        try {
            String sql = "INSERT INTO data_konsumen (id_konsumen, nama_konsumen) VALUES (?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, txtIdKonsumen.getText());
            ps.setString(2, txtNamaKonsumen.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data konsumen berhasil ditambahkan!");
            loadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menambahkan data konsumen!");
        }
    }

    private void editKonsumen() {
        try {
            String sql = "UPDATE data_konsumen SET nama_konsumen = ? WHERE id_konsumen = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, txtNamaKonsumen.getText());
            ps.setString(2, txtIdKonsumen.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data konsumen berhasil diupdate!");
            loadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal mengupdate data konsumen!");
        }
    }

    private void hapusKonsumen() {
        try {
            String sql = "DELETE FROM data_konsumen WHERE id_konsumen = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, txtIdKonsumen.getText());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Data konsumen berhasil dihapus!");
            loadData();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus data konsumen!");
        }
    }

    public static void main(String[] args) {
        new konsumen();
    }
}