package a38;

import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class ReceiptPage extends JFrame {

    JTextArea area;

    public ReceiptPage(int paymentId) {

        setTitle("Rent Receipt");
        setSize(600, 500);
        setLocationRelativeTo(null);

        area = new JTextArea();
        area.setFont(new Font("Monospaced", Font.PLAIN, 16));

        add(new JScrollPane(area));

        loadReceipt(paymentId);
    }

    // =====================================================
    // LOAD RECEIPT FROM DATABASE
    // =====================================================
    private void loadReceipt(int id) {

        try {

            Connection con = DBConnection.getConnection();

            String query = "SELECT * FROM payments WHERE id=?";

            PreparedStatement pst = con.prepareStatement(query);

            pst.setInt(1, id);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                String tenant = rs.getString("tenant_name");
                String property = rs.getString("property_name");
                double amount = rs.getDouble("amount");
                String mode = rs.getString("payment_mode");
                String date = rs.getString("payment_date");

                area.setText(
                        "\n========= RENT RECEIPT =========\n\n"
                      + "Tenant Name   : " + tenant + "\n"
                      + "Property      : " + property + "\n"
                      + "Amount Paid   : ₹" + amount + "\n"
                      + "Payment Mode  : " + mode + "\n"
                      + "Date          : " + date + "\n\n"
                      + "Status        : SUCCESS\n\n"
                      + "================================"
                );
            }

            con.close();

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Receipt Load Error");
        }
    }
}