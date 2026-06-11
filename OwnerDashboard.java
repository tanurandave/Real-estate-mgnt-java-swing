package a38;

import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class OwnerDashboard extends JFrame {

    JLabel totalLabel;
    JLabel occupiedLabel;
    JLabel vacantLabel;

    public OwnerDashboard() {

        setTitle("Owner Dashboard");
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(Theme.BACKGROUND);

        // ================= SIDEBAR =================

        JPanel sidebar = new JPanel(null);
        sidebar.setBounds(0, 0, 220, 650);
        sidebar.setBackground(Theme.SIDEBAR);

        JLabel logo = new JLabel("OWNER PANEL");
        logo.setBounds(25, 40, 180, 40);
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JButton addBtn = createButton("Add Property");
        addBtn.setBounds(20, 140, 170, 45);

        JButton viewBtn = createButton("View Property");
        viewBtn.setBounds(20, 210, 170, 45);

        JButton refreshBtn = createButton("Refresh");
        refreshBtn.setBounds(20, 280, 170, 45);

        JButton logoutBtn = createButton("Logout");
        logoutBtn.setBounds(20, 500, 170, 45);

        sidebar.add(logo);
        sidebar.add(addBtn);
        sidebar.add(viewBtn);
        sidebar.add(refreshBtn);
        sidebar.add(logoutBtn);

        // ================= HEADING =================

        JLabel heading = new JLabel(
                "Welcome To Real Estate Dashboard");

        heading.setBounds(300, 60, 600, 40);

        heading.setForeground(Color.WHITE);

        heading.setFont(
                new Font("Segoe UI",
                        Font.BOLD,
                        30));

        // ================= CARDS =================

        JPanel card1 = createCard("Total Properties");
        card1.setBounds(300, 150, 220, 130);

        JPanel card2 = createCard("Occupied");
        card2.setBounds(560, 150, 220, 130);

        JPanel card3 = createCard("Vacant");
        card3.setBounds(820, 150, 220, 130);

        totalLabel =
                (JLabel) card1.getComponent(1);

        occupiedLabel =
                (JLabel) card2.getComponent(1);

        vacantLabel =
                (JLabel) card3.getComponent(1);

        // ================= ADD COMPONENTS =================

        mainPanel.add(sidebar);

        mainPanel.add(heading);

        mainPanel.add(card1);
        mainPanel.add(card2);
        mainPanel.add(card3);

        add(mainPanel);

        // ================= LOAD DASHBOARD DATA =================

        loadDashboardData();

        // ================= BUTTON ACTIONS =================

        logoutBtn.addActionListener(e -> {

            new LoginPage().setVisible(true);

            dispose();

        });

        addBtn.addActionListener(e -> {

            new AddProperty().setVisible(true);

        });

        viewBtn.addActionListener(e -> {

            new ViewProperty().setVisible(true);

        });

        refreshBtn.addActionListener(e -> {

            loadDashboardData();

        });
    }

    // ================= LOAD DATA =================

    private void loadDashboardData() {

        try {

            Connection con =
                    DBConnection.getConnection();

            // TOTAL PROPERTIES

            String totalQuery =
                    "SELECT COUNT(*) FROM properties";

            PreparedStatement pst1 =
                    con.prepareStatement(totalQuery);

            ResultSet rs1 =
                    pst1.executeQuery();

            if (rs1.next()) {

                totalLabel.setText(
                        rs1.getString(1));
            }

            // OCCUPIED

            String occupiedQuery =
                    "SELECT COUNT(*) FROM properties WHERE status='Occupied'";

            PreparedStatement pst2 =
                    con.prepareStatement(occupiedQuery);

            ResultSet rs2 =
                    pst2.executeQuery();

            if (rs2.next()) {

                occupiedLabel.setText(
                        rs2.getString(1));
            }

            // VACANT

            String vacantQuery =
                    "SELECT COUNT(*) FROM properties WHERE status='Vacant'";

            PreparedStatement pst3 =
                    con.prepareStatement(vacantQuery);

            ResultSet rs3 =
                    pst3.executeQuery();

            if (rs3.next()) {

                vacantLabel.setText(
                        rs3.getString(1));
            }

            con.close();

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error");
        }
    }

    // ================= BUTTON =================

    private JButton createButton(String text) {

        JButton btn = new JButton(text);

        btn.setBackground(Theme.BUTTON);

        btn.setForeground(Color.WHITE);

        btn.setFocusPainted(false);

        btn.setFont(
                new Font("Segoe UI",
                        Font.BOLD,
                        16));

        return btn;
    }

    // ================= CARD =================

    private JPanel createCard(String title) {

        JPanel panel = new JPanel();

        panel.setLayout(
                new GridLayout(2, 1));

        panel.setBackground(Theme.SIDEBAR);

        panel.setBorder(
                BorderFactory.createLineBorder(Color.WHITE));

        JLabel l1 = new JLabel(
                title,
                SwingConstants.CENTER);

        l1.setForeground(Color.WHITE);

        l1.setFont(
                new Font("Segoe UI",
                        Font.BOLD,
                        18));

        JLabel l2 = new JLabel(
                "0",
                SwingConstants.CENTER);

        l2.setForeground(Color.WHITE);

        l2.setFont(
                new Font("Segoe UI",
                        Font.BOLD,
                        40));

        panel.add(l1);

        panel.add(l2);

        return panel;
    }

    public static void main(String[] args) {

        new OwnerDashboard().setVisible(true);
    }
}