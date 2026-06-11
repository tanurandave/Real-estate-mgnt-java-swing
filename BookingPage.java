package a38;

import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class BookingPage extends JFrame {

    JTextField txtTenant;
    JTextField txtDuration;
    JTextField txtAdvance;

    JComboBox<String> propertyDropdown;

    JButton btnBook, btnRefresh;

    public BookingPage() {

        setTitle("Book Property");
        setSize(700, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(null);
        panel.setBackground(Theme.BACKGROUND);

        // =====================================================
        // HEADING
        // =====================================================

        JLabel heading = new JLabel("BOOK PROPERTY");
        heading.setBounds(190, 30, 350, 40);
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 30));

        panel.add(heading);

        // =====================================================
        // TENANT NAME
        // =====================================================

        JLabel l1 = new JLabel("Tenant Name");
        l1.setBounds(70, 110, 150, 30);
        l1.setForeground(Color.WHITE);
        l1.setFont(new Font("Segoe UI", Font.BOLD, 16));

        txtTenant = new JTextField();
        txtTenant.setBounds(250, 110, 280, 38);
        txtTenant.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        // =====================================================
        // PROPERTY DROPDOWN
        // =====================================================

        JLabel l2 = new JLabel("Select Property");
        l2.setBounds(70, 180, 150, 30);
        l2.setForeground(Color.WHITE);
        l2.setFont(new Font("Segoe UI", Font.BOLD, 16));

        propertyDropdown = new JComboBox<>();
        propertyDropdown.setBounds(250, 180, 280, 38);
        propertyDropdown.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        propertyDropdown.setBackground(Color.WHITE);

        // =====================================================
        // DURATION
        // =====================================================

        JLabel l3 = new JLabel("Duration (Months)");
        l3.setBounds(70, 250, 170, 30);
        l3.setForeground(Color.WHITE);
        l3.setFont(new Font("Segoe UI", Font.BOLD, 16));

        txtDuration = new JTextField();
        txtDuration.setBounds(250, 250, 280, 38);
        txtDuration.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        // =====================================================
        // ADVANCE AMOUNT
        // =====================================================

        JLabel l4 = new JLabel("Advance Amount");
        l4.setBounds(70, 320, 170, 30);
        l4.setForeground(Color.WHITE);
        l4.setFont(new Font("Segoe UI", Font.BOLD, 16));

        txtAdvance = new JTextField();
        txtAdvance.setBounds(250, 320, 280, 38);
        txtAdvance.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        // =====================================================
        // BUTTONS
        // =====================================================

        btnBook = new JButton("Book Property");
        btnBook.setBounds(180, 410, 170, 45);
        btnBook.setBackground(Theme.BUTTON);
        btnBook.setForeground(Color.WHITE);
        btnBook.setFocusPainted(false);
        btnBook.setFont(new Font("Segoe UI", Font.BOLD, 16));

        btnRefresh = new JButton("Refresh List");
        btnRefresh.setBounds(380, 410, 150, 45);
        btnRefresh.setBackground(new Color(50, 50, 50));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 16));

        // =====================================================
        // ADD COMPONENTS
        // =====================================================

        panel.add(l1);
        panel.add(txtTenant);

        panel.add(l2);
        panel.add(propertyDropdown);

        panel.add(l3);
        panel.add(txtDuration);

        panel.add(l4);
        panel.add(txtAdvance);

        panel.add(btnBook);
        panel.add(btnRefresh);

        add(panel);

        // =====================================================
        // LOAD PROPERTY LIST
        // =====================================================

        loadProperties();

        // =====================================================
        // BUTTON ACTIONS
        // =====================================================

        btnBook.addActionListener(e -> bookProperty());

        btnRefresh.addActionListener(e -> loadProperties());
    }

    // =====================================================
    // LOAD ONLY VACANT PROPERTIES
    // =====================================================

    private void loadProperties() {

        try {

            propertyDropdown.removeAllItems();

            propertyDropdown.addItem("Select Property");

            Connection con = DBConnection.getConnection();

            // ONLY VACANT PROPERTIES
            String query =
                    "SELECT property_name FROM properties " +
                    "WHERE status='Vacant'";

            PreparedStatement pst =
                    con.prepareStatement(query);

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {

                propertyDropdown.addItem(
                        rs.getString("property_name"));
            }

            con.close();

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(this,
                    "Failed To Load Properties");
        }
    }

    // =====================================================
    // BOOK PROPERTY
    // =====================================================

    private void bookProperty() {

        String tenant = txtTenant.getText().trim();

        String property =
                propertyDropdown.getSelectedItem().toString();

        String duration = txtDuration.getText().trim();

        String advance = txtAdvance.getText().trim();

        // =====================================================
        // VALIDATION
        // =====================================================

        if (tenant.isEmpty()
                || property.equals("Select Property")
                || duration.isEmpty()
                || advance.isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Please Fill All Fields");

            return;
        }

        try {

            Connection con = DBConnection.getConnection();

            // =====================================================
            // CHECK PROPERTY STATUS AGAIN
            // =====================================================

            String checkQuery =
                    "SELECT status FROM properties " +
                    "WHERE property_name=?";

            PreparedStatement pst1 =
                    con.prepareStatement(checkQuery);

            pst1.setString(1, property);

            ResultSet rs = pst1.executeQuery();

            // =====================================================
            // IF PROPERTY ALREADY OCCUPIED
            // =====================================================

            if (rs.next()) {

                String status = rs.getString("status");

                if (status.equalsIgnoreCase("Occupied")) {

                    JOptionPane.showMessageDialog(this,
                            "This Property Is Already Booked By Another Tenant");

                    // REMOVE FROM DROPDOWN
                    loadProperties();

                    return;
                }
            }

            // =====================================================
            // INSERT BOOKING
            // =====================================================

            String query =
                    "INSERT INTO bookings " +
                    "(tenant_name, property_name, duration, " +
                    "advance_amount, booking_date) " +
                    "VALUES (?, ?, ?, ?, NOW())";

            PreparedStatement pst =
                    con.prepareStatement(query);

            pst.setString(1, tenant);
            pst.setString(2, property);
            pst.setString(3, duration);
            pst.setDouble(4, Double.parseDouble(advance));

            int result = pst.executeUpdate();

            // =====================================================
            // SUCCESS
            // =====================================================

          if (result > 0) {

    // =====================================================
    // UPDATE PROPERTY STATUS
    // VACANT -> OCCUPIED
    // =====================================================

    String updateQuery =
            "UPDATE properties " +
            "SET status=? " +
            "WHERE property_name=?";

    PreparedStatement pst2 =
            con.prepareStatement(updateQuery);

    pst2.setString(1, "Occupied");

    pst2.setString(2, property);

    int updated = pst2.executeUpdate();

    // =====================================================
    // CHECK STATUS UPDATED
    // =====================================================

    if (updated > 0) {

        JOptionPane.showMessageDialog(this,
                "Property Booked Successfully\n"
                + "Status Changed To OCCUPIED");

    } else {

        JOptionPane.showMessageDialog(this,
                "Booking Saved But Status Not Updated");
    }

    clearFields();

    // =====================================================
    // RELOAD PROPERTY LIST
    // =====================================================

    loadProperties();

} else {

    JOptionPane.showMessageDialog(this,
            "Booking Failed");
}

            con.close();

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(this,
                    "Advance Amount Must Be Numeric");

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(this,
                    "Database Error");
        }
    }

    // =====================================================
    // CLEAR FIELDS
    // =====================================================

    private void clearFields() {

        txtTenant.setText("");

        propertyDropdown.setSelectedIndex(0);

        txtDuration.setText("");

        txtAdvance.setText("");
    }

    // =====================================================
    // MAIN METHOD
    // =====================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new BookingPage().setVisible(true);
        });
    }
}