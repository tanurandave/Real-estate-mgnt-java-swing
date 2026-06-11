package a38;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ViewProperty extends JFrame {

    JTable table;
    DefaultTableModel model;

    JLabel imageLabel;

    JButton refreshBtn, deleteBtn, updateBtn;

    public ViewProperty() {

        setTitle("View Properties");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BACKGROUND);

        // ================= HEADING =================

        JLabel heading = new JLabel(
                "PROPERTY LIST",
                SwingConstants.CENTER);

        heading.setFont(
                new Font("Segoe UI", Font.BOLD, 30));

        heading.setForeground(Color.WHITE);

        panel.add(heading, BorderLayout.NORTH);

        // ================= TABLE =================

        String columns[] = {
            "ID",
            "Name",
            "Type",
            "Rent",
            "Address",
            "Description",
            "Status"
        };

        model = new DefaultTableModel(columns, 0);

        table = new JTable(model);

        table.setRowHeight(35);

        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        table.getTableHeader().setFont(
                new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane sp =
                new JScrollPane(table);

        panel.add(sp, BorderLayout.CENTER);

        // ================= RIGHT IMAGE PANEL =================

        JPanel rightPanel = new JPanel(null);

        rightPanel.setPreferredSize(
                new Dimension(260, 650));

        rightPanel.setBackground(
                new Color(35, 35, 35));

        JLabel imgTitle = new JLabel(
                "PROPERTY IMAGE");

        imgTitle.setBounds(40, 20, 200, 30);

        imgTitle.setForeground(Color.WHITE);

        imgTitle.setFont(
                new Font("Segoe UI",
                        Font.BOLD,
                        20));

        imageLabel = new JLabel();

        imageLabel.setBounds(20, 70, 220, 220);

        imageLabel.setBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2));

        rightPanel.add(imgTitle);

        rightPanel.add(imageLabel);

        panel.add(rightPanel, BorderLayout.EAST);

        // ================= BUTTON PANEL =================

        JPanel bottomPanel = new JPanel();

        refreshBtn = new JButton("Refresh");
        deleteBtn = new JButton("Delete");
        updateBtn = new JButton("Update");

        refreshBtn.setBackground(Theme.BUTTON);
        refreshBtn.setForeground(Color.WHITE);

        updateBtn.setBackground(Color.BLUE);
        updateBtn.setForeground(Color.WHITE);

        deleteBtn.setBackground(Color.RED);
        deleteBtn.setForeground(Color.WHITE);

        bottomPanel.add(refreshBtn);
        bottomPanel.add(updateBtn);
        bottomPanel.add(deleteBtn);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);

        // ================= LOAD DATA =================

        loadProperties();

        // ================= TABLE CLICK =================

        table.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                showSelectedImage();
            }
        });

        updateBtn.addActionListener(e ->{
            new UpdateProperty().setVisible(true);
            this.dispose();
        });
        // ================= REFRESH =================

        refreshBtn.addActionListener(e -> {

            loadProperties();
        });

        // ================= DELETE =================

        deleteBtn.addActionListener(e -> {

            deleteProperty();
        });

    }

    // =====================================================
    // LOAD PROPERTY DATA
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
    // SHOW SELECTED IMAGE
    // =====================================================

    private void showSelectedImage() {

    try {

        int row = table.getSelectedRow();

        if (row == -1) {
            return;
        }

        int id = Integer.parseInt(
                model.getValueAt(row, 0).toString());

        Connection con =
                DBConnection.getConnection();

        String query =
                "SELECT property_image FROM properties WHERE id=?";

        PreparedStatement pst =
                con.prepareStatement(query);

        pst.setInt(1, id);

        ResultSet rs = pst.executeQuery();

        if (rs.next()) {

            byte[] imageBytes =
                    rs.getBytes("property_image");

            if (imageBytes != null) {

                ImageIcon icon =
                        new ImageIcon(imageBytes);

                Image img =
                        icon.getImage().getScaledInstance(
                                220,
                                220,
                                Image.SCALE_SMOOTH);

                imageLabel.setIcon(
                        new ImageIcon(img));

            } else {

                imageLabel.setIcon(null);

                JOptionPane.showMessageDialog(
                        this,
                        "Image Not Found");
            }
        }

        con.close();

    } catch (Exception ex) {

        ex.printStackTrace();

        JOptionPane.showMessageDialog(
                this,
                "Image Loading Failed");
    }
}

    // =====================================================
    // DELETE PROPERTY
    // =====================================================

    private void deleteProperty() {

        try {

            int row = table.getSelectedRow();

            if (row == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Select Property First");

                return;
            }

            int confirm =
                    JOptionPane.showConfirmDialog(
                            this,
                            "Are you sure you want to delete?",
                            "Confirm",
                            JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            int id =
                    Integer.parseInt(
                            model.getValueAt(row, 0).toString());

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
    // MAIN METHOD
    // =====================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new ViewProperty().setVisible(true);

        });
    }
}