package a38;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ManageUsers extends JFrame {

    JTable table;

    DefaultTableModel model;

    JButton deleteBtn, refreshBtn;

    public ManageUsers() {

        setTitle("Manage Users");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        panel.setBackground(Theme.BACKGROUND);

        // ================= HEADING =================

        JLabel heading = new JLabel(
                "MANAGE USERS",
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

        panel.add(heading, BorderLayout.NORTH);

        // ================= TABLE =================

        String columns[] = {
            "ID",
            "Name",
            "Email",
            "Phone",
            "Role"
        };

        model = new DefaultTableModel(columns, 0);

        table = new JTable(model);

        table.setRowHeight(35);

        table.setFont(
                new Font("Segoe UI",
                        Font.PLAIN,
                        14));

        table.getTableHeader().setFont(
                new Font("Segoe UI",
                        Font.BOLD,
                        15));

        JScrollPane scrollPane =
                new JScrollPane(table);

        panel.add(scrollPane, BorderLayout.CENTER);

        // ================= BUTTON PANEL =================

        JPanel bottomPanel = new JPanel();

        refreshBtn = new JButton("Refresh");

        deleteBtn = new JButton("Delete User");

        refreshBtn.setBackground(
                Theme.BUTTON);

        refreshBtn.setForeground(
                Color.WHITE);

        deleteBtn.setBackground(
                Color.RED);

        deleteBtn.setForeground(
                Color.WHITE);

        bottomPanel.add(refreshBtn);

        bottomPanel.add(deleteBtn);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        add(panel);

        // ================= LOAD DATA =================

        loadUsers();

        // ================= REFRESH =================

        refreshBtn.addActionListener(e -> {

            loadUsers();

        });

        // ================= DELETE =================

        deleteBtn.addActionListener(e -> {

            deleteUser();

        });
    }

    // =====================================================
    // LOAD USERS FROM DATABASE
    // =====================================================

    private void loadUsers() {

        try {

            model.setRowCount(0);

            Connection con =
                    DBConnection.getConnection();

            String query =
                    "SELECT * FROM users";

            PreparedStatement pst =
                    con.prepareStatement(query);

            ResultSet rs =
                    pst.executeQuery();

            while (rs.next()) {

                model.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("role")
                });
            }

            con.close();

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Error Loading Users");
        }
    }

    // =====================================================
    // DELETE USER
    // =====================================================

    private void deleteUser() {

        try {

            int row = table.getSelectedRow();

            if (row == -1) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please Select User");

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
                            model.getValueAt(row, 0).toString());

            Connection con =
                    DBConnection.getConnection();

            String query =
                    "DELETE FROM users WHERE id=?";

            PreparedStatement pst =
                    con.prepareStatement(query);

            pst.setInt(1, id);

            int result =
                    pst.executeUpdate();

            if (result > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "User Deleted Successfully");

                loadUsers();

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

            new ManageUsers().setVisible(true);

        });
    }
}