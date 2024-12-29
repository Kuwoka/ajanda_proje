package ajanda_proje;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Window.Type;
import java.awt.Color;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import javax.swing.JToolBar;
import javax.swing.JInternalFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class tasarim extends JFrame {
	private static final long serialVersionUID = 1L;
	private JFrame frmAjanda;
    private JTextField txtTaskname;
    private JTextField txtTarih;
    private JTable table;
    private DefaultTableModel tableModel;
    private JCheckBox chckbxDurum;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
            	tasarim window = new tasarim();
                window.frmAjanda.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public tasarim() {
        initialize();
        loadTasks(); 
    }

    private void initialize() {
        frmAjanda = new JFrame();
        frmAjanda.setTitle("AJANDA");
        frmAjanda.setBounds(100, 100, 600, 400);
        frmAjanda.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmAjanda.getContentPane().setLayout(null);

        JLabel lblTaskname = new JLabel("Görev Adı:");
        lblTaskname.setBounds(10, 10, 70, 25);
        frmAjanda.getContentPane().add(lblTaskname);

        txtTaskname = new JTextField();
        txtTaskname.setBounds(90, 10, 200, 25);
        frmAjanda.getContentPane().add(txtTaskname);

        JLabel lblTarih = new JLabel("Tarih:");
        lblTarih.setBounds(10, 50, 70, 25);
        frmAjanda.getContentPane().add(lblTarih);

        txtTarih = new JTextField();
        txtTarih.setText("YYYY-AA-GG");
        txtTarih.setBounds(90, 50, 200, 25);
        frmAjanda.getContentPane().add(txtTarih);

        chckbxDurum = new JCheckBox("Tamamlandı");
        chckbxDurum.setBounds(90, 90, 200, 25);
        frmAjanda.getContentPane().add(chckbxDurum);

        JButton btnEkle = new JButton("Ekle");
        btnEkle.setBounds(10, 130, 80, 25);
        frmAjanda.getContentPane().add(btnEkle);

        JButton btnGuncelle = new JButton("Güncelle");
        btnGuncelle.setBounds(100, 130, 90, 25);
        frmAjanda.getContentPane().add(btnGuncelle);

        JButton btnSil = new JButton("Sil");
        btnSil.setBounds(200, 130, 80, 25);
        frmAjanda.getContentPane().add(btnSil);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(10, 170, 560, 180);
        frmAjanda.getContentPane().add(scrollPane);

        table = new JTable();
        tableModel = new DefaultTableModel(new Object[][]{},
                new String[]{"ID", "Görev Adı", "Tarih", "Durum"}) {
            
            /**
					 * 
					 */
					private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(tableModel);
        scrollPane.setViewportView(table);

     
        btnEkle.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                addTask();
            }
        });

        btnGuncelle.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                updateTask();
            }
        });

        btnSil.addActionListener(new ActionListener() {
            
            public void actionPerformed(ActionEvent e) {
                deleteTask();
            }
        });
    }

    private void loadTasks() {
        try (Connection connection = DatabaseHelper.connect();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM tasks")) {

            tableModel.setRowCount(0);
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("taskname"),
                        rs.getDate("tarih"),
                        rs.getBoolean("durum") ? "Tamamlandı" : "Tamamlanmadı"
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addTask() {
        String taskname = txtTaskname.getText();
        String tarih = txtTarih.getText();
        boolean durum = chckbxDurum.isSelected();

        if (taskname.isEmpty() || tarih.isEmpty()) {
            JOptionPane.showMessageDialog(frmAjanda, "Görev adı ve tarih boş bırakılamaz!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection connection = DatabaseHelper.connect();
             PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO tasks (taskname, tarih, durum) VALUES (?, ?, ?)")) {

            ps.setString(1, taskname);
            ps.setDate(2, java.sql.Date.valueOf(tarih));
            ps.setBoolean(3, durum);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(frmAjanda, "Görev başarıyla eklendi!");
            loadTasks(); 
            clearInputs();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frmAjanda, "Görev eklenirken bir hata oluştu!", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTask() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frmAjanda, "Güncellenecek bir görev seçmelisiniz!", "Hata", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String taskname = txtTaskname.getText();
        String tarih = txtTarih.getText();
        boolean durum = chckbxDurum.isSelected();

        try (Connection connection = DatabaseHelper.connect();
             PreparedStatement ps = connection.prepareStatement(
                     "UPDATE tasks SET taskname = ?, tarih = ?, durum = ? WHERE id = ?")) {

            ps.setString(1, taskname);
            ps.setDate(2, java.sql.Date.valueOf(tarih));
            ps.setBoolean(3, durum);
            ps.setInt(4, id);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(frmAjanda, "Görev başarıyla güncellendi!");
            loadTasks();
            clearInputs();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frmAjanda, "Güncelleme sırasında bir hata oluştu!", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteTask() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frmAjanda, "Silinecek bir görev seçmelisiniz!", "Hata", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tableModel.getValueAt(selectedRow, 0);

        try (Connection connection = DatabaseHelper.connect();
             PreparedStatement ps = connection.prepareStatement(
                     "DELETE FROM tasks WHERE id = ?")) {

            ps.setInt(1, id);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(frmAjanda, "Görev başarıyla silindi!");
            loadTasks();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frmAjanda, "Silme işlemi sırasında bir hata oluştu!", "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearInputs() {
        txtTaskname.setText("");
        txtTarih.setText("");
        chckbxDurum.setSelected(false);
    }
}
