package a38;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ManageProperties extends JFrame {

    JTable table;

    DefaultTableModel model;

    JButton updateBtn, deleteBtn, refreshBtn;

    JLabel imageLabel;

    public ManageProperties() {

        setTitle("Manage Properties");
        setSize(1200, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel =
                new JPanel(new BorderLayout());

        mainPanel.setBackground(Theme.BACKGROUND);

        // ================= HEADING =================

        JLabel heading =
                new JLabel(
                        "MANAGE PROPERTIES",
                        SwingConstants.CENTER);

        heading.setFont(
                new Font("Segoe UI",
                        Font.BOLD,
                        30));

        heading.setForeground(Color.WHITE);

        heading.setOpaque(true);

        heading.setBackground(Theme.SIDEBAR);

        heading.setPreferredSize(
                new Dimension(100, 60));

        mainPanel.add(heading, BorderLayout.NORTH);

        // ================= TABLE =================

        String columns[] = {
            "ID",
            "Property",
            "Type",
            "Rent",
            "Address",
            "Description",
            "Status"
        };

        model =
                new DefaultTableModel(columns, 0);

        table =
                new JTable(model);

        table.setRowHeight(35);

        table.setFont(
                new Font("Segoe UI",
                        Font.PLAIN,
                        14));

        table.getTableHeader().setFont(
                new Font("Segoe UI",
                        Font.BOLD,
                        14));

        JScrollPane sp =
                new JScrollPane(table);

        mainPanel.add(sp, BorderLayout.CENTER);

        // ================= RIGHT IMAGE PANEL =================

        JPanel rightPanel =
                new JPanel(null);

        rightPanel.setPreferredSize(
                new Dimension(260, 650));

        rightPanel.setBackground(
                new Color(35, 35, 35));

        JLabel imgTitle =
                new JLabel("PROPERTY IMAGE");

        imgTitle.setBounds(30, 20, 220, 30);

        imgTitle.setForeground(Color.WHITE);

        imgTitle.setFont(
                new Font("Segoe UI",
                        Font.BOLD,
                        20));

        imageLabel =
                new JLabel();

        imageLabel.setBounds(20, 70, 220, 220);

        imageLabel.setBorder(
                BorderFactory.createLineBorder(
                        Color.WHITE,
                        2));

        rightPanel.add(imgTitle);

        rightPanel.add(imageLabel);

        mainPanel.add(rightPanel, BorderLayout.EAST);

        // ================= BOTTOM PANEL =================

        JPanel bottom =
                new JPanel();

        refreshBtn =
                new JButton("Refresh");

        updateBtn =
                new JButton("Update");

        deleteBtn =
                new JButton("Delete");

        refreshBtn.setBackground(
                Theme.BUTTON);

        refreshBtn.setForeground(
                Color.WHITE);

        updateBtn.setBackground(
                Color.BLUE);

        updateBtn.setForeground(
                Color.WHITE);

        deleteBtn.setBackground(
                Color.RED);

        deleteBtn.setForeground(
                Color.WHITE);

        bottom.add(refreshBtn);

        bottom.add(updateBtn);

        bottom.add(deleteBtn);

        mainPanel.add(bottom, BorderLayout.SOUTH);

        add(mainPanel);

        // ================= LOAD DATA =================

        loadProperties();

        // ================= TABLE CLICK =================

        table.getSelectionModel()
                .addListSelectionListener(e -> {

                    if (!e.getValueIsAdjusting()) {

                        showSelectedImage();
                    }
                });

        // ================= REFRESH =================

        refreshBtn.addActionListener(e -> {

            loadProperties();

        });

        // ================= DELETE =================

        deleteBtn.addActionListener(e -> {

            deleteProperty();

        });

        // ================= UPDATE =================

        updateBtn.addActionListener(e -> {

            updatePropertyStatus();

        });
    }

    // =====================================================
    // LOAD PROPERTIES
    // =====================================================

    private void loadProperties() {

        try {

            model.setRowCount(0);

            Connection con =
                    DBConnection.getConnection();

            String query =
                    "SELECT * FROM properties";

            PreparedStatement pst =
                    con.prepareStatement(query);

            ResultSet rs =
                    pst.executeQuery();

            while (rs.next()) {

                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("property_name"),
                    rs.getString("property_type"),
                    rs.getDouble("rent_amount"),
                    rs.getString("address"),
                    rs.getString("description"),
                    rs.getString("status")
                });
            }

            con.close();

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Error Loading Properties");
        }
    }

    // =====================================================
    // SHOW IMAGE
    // =====================================================

    private void showSelectedImage() {

        try {

            int row =
                    table.getSelectedRow();

            if (row == -1) {
                return;
            }

            int id =
                    Integer.parseInt(
                            model.getValueAt(
                                    row,
                                    0).toString());

            Connection con =
                    DBConnection.getConnection();

            String query =
                    "SELECT property_image FROM properties WHERE id=?";

            PreparedStatement pst =
                    con.prepareStatement(query);

            pst.setInt(1, id);

            ResultSet rs =
                    pst.executeQuery();

            if (rs.next()) {

                byte[] imageBytes =
                        rs.getBytes("property_image");

                if (imageBytes != null) {

                    ImageIcon icon =
                            new ImageIcon(imageBytes);

                    Image img =
                            icon.getImage()
                                    .getScaledInstance(
                                            220,
                                            220,
                                            Image.SCALE_SMOOTH);

                    imageLabel.setIcon(
                            new ImageIcon(img));

                } else {

                    imageLabel.setIcon(null);
                }
            }

            con.close();

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Image Loading Error");
        }
    }

    // =====================================================
    // DELETE PROPERTY
    // =====================================================

    private void deleteProperty() {

        try {

            int row =
                    table.getSelectedRow();

            if (row == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Select Property First");

                return;
            }

            int confirm =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Are You Sure Want To Delete?",
                            "Confirm",
                            JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION) {

                return;
            }

            int id =
                    Integer.parseInt(
                            model.getValueAt(
                                    row,
                                    0).toString());

            Connection con =
                    DBConnection.getConnection();

            String query =
                    "DELETE FROM properties WHERE id=?";

            PreparedStatement pst =
                    con.prepareStatement(query);

            pst.setInt(1, id);

            int result =
                    pst.executeUpdate();

            if (result > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Property Deleted Successfully");

                loadProperties();

                imageLabel.setIcon(null);

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Delete Failed");
            }

            con.close();

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error");
        }
    }

    // =====================================================
    // UPDATE STATUS
    // =====================================================

    private void updatePropertyStatus() {

        try {

            int row =
                    table.getSelectedRow();

            if (row == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Select Property First");

                return;
            }

            int id =
                    Integer.parseInt(
                            model.getValueAt(
                                    row,
                                    0).toString());

            String currentStatus =
                    model.getValueAt(
                            row,
                            6).toString();

            String newStatus =
                    JOptionPane.showInputDialog(
                            this,
                            "Enter New Status",
                            currentStatus);

            if (newStatus == null
                    || newStatus.trim().isEmpty()) {

                return;
            }

            Connection con =
                    DBConnection.getConnection();

            String query =
                    "UPDATE properties SET status=? WHERE id=?";

            PreparedStatement pst =
                    con.prepareStatement(query);

            pst.setString(1, newStatus);

            pst.setInt(2, id);

            int result =
                    pst.executeUpdate();

            if (result > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Property Updated");

                loadProperties();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Update Failed");
            }

            con.close();

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error");
        }
    }

    // =====================================================
    // MAIN METHOD
    // =====================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new ManageProperties().setVisible(true);

        });
    }
}