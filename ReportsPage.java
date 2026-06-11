package a38;

import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class ReportsPage extends JFrame {

    JLabel totalUsersLabel;
    JLabel totalPropertiesLabel;
    JLabel occupiedLabel;
    JLabel vacantLabel;

    public ReportsPage() {

        setTitle("Reports");
        setSize(850, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(null);
        panel.setBackground(Theme.BACKGROUND);

        // ================= HEADING =================

        JLabel heading = new JLabel("SYSTEM REPORTS");
        heading.setBounds(250, 30, 400, 40);

        heading.setForeground(Color.WHITE);

        heading.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        32));

        panel.add(heading);

        // ================= CARDS =================

        JPanel card1 = createCard("Total Users");
        card1.setBounds(70, 130, 250, 130);

        JPanel card2 = createCard("Total Properties");
        card2.setBounds(380, 130, 250, 130);

        JPanel card3 = createCard("Occupied Properties");
        card3.setBounds(70, 320, 250, 130);

        JPanel card4 = createCard("Vacant Properties");
        card4.setBounds(380, 320, 250, 130);

        // GET VALUE LABELS

        totalUsersLabel =
                (JLabel) card1.getComponent(1);

        totalPropertiesLabel =
                (JLabel) card2.getComponent(1);

        occupiedLabel =
                (JLabel) card3.getComponent(1);

        vacantLabel =
                (JLabel) card4.getComponent(1);

        // ADD CARDS

        panel.add(card1);
        panel.add(card2);
        panel.add(card3);
        panel.add(card4);

        // ================= REFRESH BUTTON =================

        JButton refreshBtn = new JButton("Refresh");

        refreshBtn.setBounds(320, 500, 150, 45);

        refreshBtn.setBackground(Theme.BUTTON);

        refreshBtn.setForeground(Color.WHITE);

        refreshBtn.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        16));

        panel.add(refreshBtn);

        add(panel);

        // ================= LOAD DATA =================

        loadReportData();

        // ================= REFRESH ACTION =================

        refreshBtn.addActionListener(e -> {

            loadReportData();

        });
    }

    // ====================================================
    // LOAD REPORT DATA FROM DATABASE
    // ====================================================

    private void loadReportData() {

        try {

            Connection con =
                    DBConnection.getConnection();

            // ================= TOTAL USERS =================

            String userQuery =
                    "SELECT COUNT(*) FROM users";

            PreparedStatement pst1 =
                    con.prepareStatement(userQuery);

            ResultSet rs1 =
                    pst1.executeQuery();

            if (rs1.next()) {

                totalUsersLabel.setText(
                        rs1.getString(1));
            }

            // ================= TOTAL PROPERTIES =================

            String propertyQuery =
                    "SELECT COUNT(*) FROM properties";

            PreparedStatement pst2 =
                    con.prepareStatement(propertyQuery);

            ResultSet rs2 =
                    pst2.executeQuery();

            if (rs2.next()) {

                totalPropertiesLabel.setText(
                        rs2.getString(1));
            }

            // ================= OCCUPIED =================

            String occupiedQuery =
                    "SELECT COUNT(*) FROM properties WHERE status='Occupied'";

            PreparedStatement pst3 =
                    con.prepareStatement(occupiedQuery);

            ResultSet rs3 =
                    pst3.executeQuery();

            if (rs3.next()) {

                occupiedLabel.setText(
                        rs3.getString(1));
            }

            // ================= VACANT =================

            String vacantQuery =
                    "SELECT COUNT(*) FROM properties WHERE status='Vacant'";

            PreparedStatement pst4 =
                    con.prepareStatement(vacantQuery);

            ResultSet rs4 =
                    pst4.executeQuery();

            if (rs4.next()) {

                vacantLabel.setText(
                        rs4.getString(1));
            }

            con.close();

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error");
        }
    }

    // ====================================================
    // CARD DESIGN
    // ====================================================

    private JPanel createCard(
            String title) {

        JPanel panel = new JPanel();

        panel.setLayout(
                new GridLayout(2, 1));

        panel.setBackground(Theme.SIDEBAR);

        panel.setBorder(
                BorderFactory.createLineBorder(
                        Color.WHITE,
                        2));

        JLabel titleLabel = new JLabel(
                title,
                SwingConstants.CENTER);

        titleLabel.setForeground(Color.WHITE);

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        18));

        JLabel valueLabel = new JLabel(
                "0",
                SwingConstants.CENTER);

        valueLabel.setForeground(Color.WHITE);

        valueLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        38));

        panel.add(titleLabel);

        panel.add(valueLabel);

        return panel;
    }

    // ====================================================
    // MAIN METHOD
    // ====================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new ReportsPage().setVisible(true);

        });
    }
}