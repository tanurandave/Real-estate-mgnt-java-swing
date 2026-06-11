package a38;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TenantDashboard extends JFrame {

    JLabel totalPropertiesLabel;
    JLabel vacantLabel;
    JLabel occupiedLabel;
    JLabel bookingLabel;
    JLabel paymentLabel;

    JTable bookingTable;
    DefaultTableModel bookingModel;

    public TenantDashboard() {

        setTitle("Tenant Dashboard");
        setSize(1200, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(Theme.BACKGROUND);

        // ================= SIDEBAR =================
        JPanel sidebar = new JPanel(null);
        sidebar.setBounds(0, 0, 220, 650);
        sidebar.setBackground(Theme.SIDEBAR);

        JLabel title = new JLabel("TENANT PANEL");
        title.setBounds(20, 30, 180, 40);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JButton searchBtn = createButton("Search Property");
        searchBtn.setBounds(20, 120, 170, 40);

        JButton bookingBtn = createButton("Bookings");
        bookingBtn.setBounds(20, 190, 170, 40);

        JButton paymentBtn = createButton("Payments");
        paymentBtn.setBounds(20, 260, 170, 40);

        JButton refreshBtn = createButton("Refresh");
        refreshBtn.setBounds(20, 330, 170, 40);

        JButton logoutBtn = createButton("Logout");
        logoutBtn.setBounds(20, 520, 170, 40);

        sidebar.add(title);
        sidebar.add(searchBtn);
        sidebar.add(bookingBtn);
        sidebar.add(paymentBtn);
        sidebar.add(refreshBtn);
        sidebar.add(logoutBtn);

        // ================= WELCOME =================
        JLabel welcome = new JLabel("Welcome Tenant Dashboard");
        welcome.setBounds(350, 20, 500, 40);
        welcome.setForeground(Color.WHITE);
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 28));

        // ================= CARDS =================
        JPanel card1 = createCard("Total Properties");
        card1.setBounds(300, 80, 200, 120);

        JPanel card2 = createCard("Vacant");
        card2.setBounds(530, 80, 200, 120);

        JPanel card3 = createCard("Occupied");
        card3.setBounds(760, 80, 200, 120);

        JPanel card4 = createCard("My Bookings");
        card4.setBounds(990, 80, 180, 120);

        JPanel card5 = createCard("Payments");
        card5.setBounds(300, 220, 200, 120);

        totalPropertiesLabel = (JLabel) card1.getComponent(1);
        vacantLabel = (JLabel) card2.getComponent(1);
        occupiedLabel = (JLabel) card3.getComponent(1);
        bookingLabel = (JLabel) card4.getComponent(1);
        paymentLabel = (JLabel) card5.getComponent(1);

        // ================= BOOKED PROPERTY TABLE =================
        JLabel tableTitle = new JLabel("BOOKED PROPERTIES");
        tableTitle.setBounds(300, 370, 300, 30);
        tableTitle.setForeground(Color.WHITE);
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

        String[] cols = {
                "ID",
                "Tenant",
                "Property",
                "Duration",
                "Advance",
                "Date"
        };

        bookingModel = new DefaultTableModel(cols, 0);
        bookingTable = new JTable(bookingModel);

        bookingTable.setRowHeight(30);
        bookingTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JScrollPane sp = new JScrollPane(bookingTable);
        sp.setBounds(300, 410, 850, 180);

        // ================= ADD COMPONENTS =================
        mainPanel.add(sidebar);
        mainPanel.add(welcome);
        mainPanel.add(card1);
        mainPanel.add(card2);
        mainPanel.add(card3);
        mainPanel.add(card4);
        mainPanel.add(card5);
        mainPanel.add(tableTitle);
        mainPanel.add(sp);

        add(mainPanel);

        // ================= LOAD DATA =================
        loadDashboardData();
        loadBookedProperties();

        // ================= BUTTON ACTIONS =================
        searchBtn.addActionListener(e -> new SearchProperty().setVisible(true));

        bookingBtn.addActionListener(e -> new BookingPage().setVisible(true));

        paymentBtn.addActionListener(e -> new PaymentPage().setVisible(true));

        refreshBtn.addActionListener(e -> {
            loadDashboardData();
            loadBookedProperties();
            JOptionPane.showMessageDialog(this, "Dashboard Refreshed");
        });

        logoutBtn.addActionListener(e -> {
            new LoginPage().setVisible(true);
            dispose();
        });

        // AUTO REFRESH
        Timer timer = new Timer(10000, e -> {
            loadDashboardData();
            loadBookedProperties();
        });
        timer.start();
    }

    // =====================================================
    // LOAD DASHBOARD COUNTS
    // =====================================================
    private void loadDashboardData() {

        try {

            Connection con = DBConnection.getConnection();

            PreparedStatement p1 = con.prepareStatement("SELECT COUNT(*) FROM properties");
            ResultSet r1 = p1.executeQuery();
            if (r1.next()) totalPropertiesLabel.setText(r1.getString(1));

            PreparedStatement p2 = con.prepareStatement("SELECT COUNT(*) FROM properties WHERE status='Vacant'");
            ResultSet r2 = p2.executeQuery();
            if (r2.next()) vacantLabel.setText(r2.getString(1));

            PreparedStatement p3 = con.prepareStatement("SELECT COUNT(*) FROM properties WHERE status='Occupied'");
            ResultSet r3 = p3.executeQuery();
            if (r3.next()) occupiedLabel.setText(r3.getString(1));

            PreparedStatement p4 = con.prepareStatement("SELECT COUNT(*) FROM bookings");
            ResultSet r4 = p4.executeQuery();
            if (r4.next()) bookingLabel.setText(r4.getString(1));

            PreparedStatement p5 = con.prepareStatement("SELECT COUNT(*) FROM payments");
            ResultSet r5 = p5.executeQuery();
            if (r5.next()) paymentLabel.setText(r5.getString(1));

            con.close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // =====================================================
    // LOAD BOOKED PROPERTIES FROM DB
    // =====================================================
    private void loadBookedProperties() {

        try {

            bookingModel.setRowCount(0);

            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM bookings";

            PreparedStatement pst = con.prepareStatement(query);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                bookingModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("tenant_name"),
                        rs.getString("property_name"),
                        rs.getString("duration"),
                        rs.getDouble("advance_amount"),
                        rs.getString("booking_date")
                });
            }

            con.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error Loading Bookings");
        }
    }

    // =====================================================
    // CARD DESIGN
    // =====================================================
    private JPanel createCard(String title) {

        JPanel panel = new JPanel(new GridLayout(2, 1));

        panel.setBackground(Theme.SIDEBAR);
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE));

        JLabel l1 = new JLabel(title, SwingConstants.CENTER);
        l1.setForeground(Color.WHITE);
        l1.setFont(new Font("Segoe UI", Font.BOLD, 14));

        JLabel l2 = new JLabel("0", SwingConstants.CENTER);
        l2.setForeground(Color.WHITE);
        l2.setFont(new Font("Segoe UI", Font.BOLD, 26));

        panel.add(l1);
        panel.add(l2);

        return panel;
    }

    // =====================================================
    // BUTTON STYLE
    // =====================================================
    private JButton createButton(String text) {

        JButton btn = new JButton(text);
        btn.setBackground(Theme.BUTTON);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));

        return btn;
    }

    public static void main(String[] args) {
        new TenantDashboard().setVisible(true);
    }
}