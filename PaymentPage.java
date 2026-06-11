package a38;

import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class PaymentPage extends JFrame {

    JTextField txtTenant;
    JTextField txtAmount;

    JComboBox<String> propertyDropdown;
    JComboBox<String> paymentMode;

    JButton btnPay, btnRefresh;

    int lastPaymentId = -1;

    public PaymentPage() {

        setTitle("Payment");
        setSize(650, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(null);
        panel.setBackground(Theme.BACKGROUND);

        // =====================================================
        // HEADING
        // =====================================================

        JLabel heading = new JLabel("PAYMENT");
        heading.setBounds(220, 30, 250, 40);
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 30));

        panel.add(heading);

        // =====================================================
        // TENANT
        // =====================================================

        JLabel l1 = new JLabel("Tenant Name");
        l1.setBounds(70, 120, 140, 30);
        l1.setForeground(Color.WHITE);
        l1.setFont(new Font("Segoe UI", Font.BOLD, 15));

        txtTenant = new JTextField();
        txtTenant.setBounds(240, 120, 250, 38);
        txtTenant.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        // =====================================================
        // PROPERTY DROPDOWN
        // =====================================================

        JLabel l2 = new JLabel("Select Property");
        l2.setBounds(70, 190, 140, 30);
        l2.setForeground(Color.WHITE);
        l2.setFont(new Font("Segoe UI", Font.BOLD, 15));

        propertyDropdown = new JComboBox<>();
        propertyDropdown.setBounds(240, 190, 250, 38);
        propertyDropdown.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        // =====================================================
        // AMOUNT
        // =====================================================

        JLabel l3 = new JLabel("Amount");
        l3.setBounds(70, 260, 140, 30);
        l3.setForeground(Color.WHITE);
        l3.setFont(new Font("Segoe UI", Font.BOLD, 15));

        txtAmount = new JTextField();
        txtAmount.setBounds(240, 260, 250, 38);
        txtAmount.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        // =====================================================
        // PAYMENT MODE
        // =====================================================

        JLabel l4 = new JLabel("Payment Mode");
        l4.setBounds(70, 330, 140, 30);
        l4.setForeground(Color.WHITE);
        l4.setFont(new Font("Segoe UI", Font.BOLD, 15));

        paymentMode = new JComboBox<>(
                new String[]{"Cash", "UPI", "Card"});

        paymentMode.setBounds(240, 330, 250, 38);

        paymentMode.setFont(
                new Font("Segoe UI", Font.PLAIN, 15));

        // =====================================================
        // BUTTONS
        // =====================================================

        btnPay = new JButton("Pay Now");

        btnPay.setBounds(170, 420, 150, 45);

        btnPay.setBackground(Theme.BUTTON);

        btnPay.setForeground(Color.WHITE);

        btnPay.setFocusPainted(false);

        btnPay.setFont(new Font("Segoe UI", Font.BOLD, 16));

        btnRefresh = new JButton("Refresh");

        btnRefresh.setBounds(350, 420, 140, 45);

        btnRefresh.setBackground(Color.DARK_GRAY);

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
        panel.add(txtAmount);

        panel.add(l4);
        panel.add(paymentMode);

        panel.add(btnPay);
        panel.add(btnRefresh);

        add(panel);

        // =====================================================
        // LOAD PROPERTIES
        // =====================================================

        loadVacantProperties();

        // =====================================================
        // BUTTON ACTIONS
        // =====================================================

        btnPay.addActionListener(e -> makePayment());

        btnRefresh.addActionListener(e -> loadVacantProperties());
    }

    // =====================================================
    // LOAD ONLY VACANT PROPERTIES
    // =====================================================

    private void loadVacantProperties() {

        try {

            propertyDropdown.removeAllItems();

            propertyDropdown.addItem("Select Property");

            Connection con = DBConnection.getConnection();

            // =====================================================
            // ONLY SHOW VACANT PROPERTIES
            // =====================================================

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

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(this,
                    "Error Loading Properties");
        }
    }

    // =====================================================
    // SAVE PAYMENT
    // =====================================================

    private void makePayment() {

        String tenant = txtTenant.getText().trim();

        String property =
                propertyDropdown.getSelectedItem().toString();

        String amount = txtAmount.getText().trim();

        String mode =
                paymentMode.getSelectedItem().toString();

        // =====================================================
        // VALIDATION
        // =====================================================

        if (tenant.isEmpty()
                || property.equals("Select Property")
                || amount.isEmpty()) {

            JOptionPane.showMessageDialog(this,
                    "Please Fill All Fields");

            return;
        }

        try {

            Connection con = DBConnection.getConnection();

            // =====================================================
            // CHECK PROPERTY STATUS
            // =====================================================

            String checkQuery =
                    "SELECT status FROM properties " +
                    "WHERE property_name=?";

            PreparedStatement checkPst =
                    con.prepareStatement(checkQuery);

            checkPst.setString(1, property);

            ResultSet rs = checkPst.executeQuery();

            if (rs.next()) {

                String status = rs.getString("status");

                // =====================================================
                // IF OCCUPIED THEN BLOCK PAYMENT
                // =====================================================

                if (status.equalsIgnoreCase("Occupied")) {

                    JOptionPane.showMessageDialog(this,
                            "This Property Is Already Booked");

                    loadVacantProperties();

                    return;
                }
            }

            // =====================================================
            // INSERT PAYMENT
            // =====================================================

            String query =
                    "INSERT INTO payments " +
                    "(tenant_name, property_name, amount, " +
                    "payment_mode, payment_date) " +
                    "VALUES (?, ?, ?, ?, NOW())";

            PreparedStatement pst =
                    con.prepareStatement(
                            query,
                            Statement.RETURN_GENERATED_KEYS);

            pst.setString(1, tenant);

            pst.setString(2, property);

            pst.setDouble(3,
                    Double.parseDouble(amount));

            pst.setString(4, mode);

            int result = pst.executeUpdate();

            // =====================================================
            // SUCCESS
            // =====================================================

            if (result > 0) {

                ResultSet keyResult =
                        pst.getGeneratedKeys();

                if (keyResult.next()) {

                    lastPaymentId =
                            keyResult.getInt(1);
                }

                JOptionPane.showMessageDialog(this,
                        "Payment Successful");

                // =====================================================
                // OPEN RECEIPT
                // =====================================================

                new ReceiptPage(lastPaymentId)
                        .setVisible(true);

                clearFields();

            } else {

                JOptionPane.showMessageDialog(this,
                        "Payment Failed");
            }

            con.close();

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(this,
                    "Amount Must Be Numeric");

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

        txtAmount.setText("");

        paymentMode.setSelectedIndex(0);
    }

    // =====================================================
    // MAIN
    // =====================================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new PaymentPage().setVisible(true);
        });
    }
}