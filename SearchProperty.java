package a38;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class SearchProperty extends JFrame {

    JTextField txtSearch;
    JTable table;
    DefaultTableModel model;

    JButton btnSearch, btnRefresh;

    public SearchProperty() {

        setTitle("Search Property");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // ================= TOP PANEL =================

        JPanel topPanel = new JPanel();

        JLabel lblSearch = new JLabel("Property Name:");

        txtSearch = new JTextField(20);

        btnSearch = new JButton("Search");
        btnRefresh = new JButton("Refresh");

        topPanel.add(lblSearch);
        topPanel.add(txtSearch);
        topPanel.add(btnSearch);
        topPanel.add(btnRefresh);

        // ================= TABLE =================

        String columns[] = {
                "ID",
                "Property Name",
                "Type",
                "Rent",
                "Address",
                "Status"
        };

        model = new DefaultTableModel(columns, 0);

        table = new JTable(model);

        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // ================= LOAD ALL DATA INITIALLY =================
        loadAllProperties();

        // ================= SEARCH ACTION =================
        btnSearch.addActionListener(e -> searchProperty());

        // ================= REFRESH ACTION =================
        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            loadAllProperties();
        });
    }

    // =====================================================
    // LOAD ALL PROPERTIES
    // =====================================================
    private void loadAllProperties() {

        try {

            model.setRowCount(0);

            Connection con = DBConnection.getConnection();

            String query = "SELECT id, property_name, property_type, rent_amount, address, status FROM properties";

            PreparedStatement pst = con.prepareStatement(query);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("property_name"),
                        rs.getString("property_type"),
                        rs.getDouble("rent_amount"),
                        rs.getString("address"),
                        rs.getString("status")
                });
            }

            con.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error Loading Data");
        }
    }

    // =====================================================
    // SEARCH PROPERTY
    // =====================================================
    private void searchProperty() {

        String name = txtSearch.getText().trim();

        try {

            model.setRowCount(0);

            Connection con = DBConnection.getConnection();

            String query = "SELECT id, property_name, property_type, rent_amount, address, status "
                    + "FROM properties WHERE property_name LIKE ?";

            PreparedStatement pst = con.prepareStatement(query);

            pst.setString(1, "%" + name + "%");

            ResultSet rs = pst.executeQuery();

            boolean found = false;

            while (rs.next()) {

                found = true;

                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("property_name"),
                        rs.getString("property_type"),
                        rs.getDouble("rent_amount"),
                        rs.getString("address"),
                        rs.getString("status")
                });
            }

            con.close();

            if (!found) {
                JOptionPane.showMessageDialog(this, "No Property Found");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Search Error");
        }
    }

    // =====================================================
    // MAIN METHOD
    // =====================================================
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new SearchProperty().setVisible(true);
        });
    }
}