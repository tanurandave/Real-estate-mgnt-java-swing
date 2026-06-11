package a38;

import java.awt.*;
import javax.swing.*;
import java.sql.*;

public class LoginPage extends JFrame {

    JTextField txtUser;
    JPasswordField txtPass;
    JComboBox<String> roleBox;
    JButton loginBtn, backBtn;

    public LoginPage() {

        setTitle("Login");
        setSize(550, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new ImagePanel("src\\a38\\images\\loginbg1.png");
        panel.setLayout(null);

        JLabel heading = new JLabel("USER LOGIN");
        heading.setBounds(170, 40, 250, 40);
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JLabel lblUser = new JLabel("Email");
        lblUser.setBounds(70, 130, 100, 30);
        lblUser.setForeground(Color.WHITE);

        txtUser = new JTextField();
        txtUser.setBounds(200, 130, 220, 35);

        JLabel lblPass = new JLabel("Password");
        lblPass.setBounds(70, 190, 100, 30);
        lblPass.setForeground(Color.WHITE);

        txtPass = new JPasswordField();
        txtPass.setBounds(200, 190, 220, 35);

        JLabel lblRole = new JLabel("Role");
        lblRole.setBounds(70, 250, 100, 30);
        lblRole.setForeground(Color.WHITE);

        roleBox = new JComboBox<>(
                new String[]{
                    "Admin",
                    "Owner",
                    "Tenant"
                });

        roleBox.setBounds(200, 250, 220, 35);

        loginBtn = new JButton("LOGIN");
        loginBtn.setBounds(120, 340, 120, 45);

        backBtn = new JButton("BACK");
        backBtn.setBounds(290, 340, 120, 45);

        loginBtn.setBackground(Theme.BUTTON);
        loginBtn.setForeground(Color.WHITE);

        backBtn.setBackground(Color.RED);
        backBtn.setForeground(Color.WHITE);

        panel.add(heading);
        panel.add(lblUser);
        panel.add(txtUser);
        panel.add(lblPass);
        panel.add(txtPass);
        panel.add(lblRole);
        panel.add(roleBox);
        panel.add(loginBtn);
        panel.add(backBtn);

        add(panel);

        // LOGIN BUTTON ACTION

        loginBtn.addActionListener(e -> {

    String email = txtUser.getText();
    String password = new String(txtPass.getPassword());
    String role = roleBox.getSelectedItem().toString();

    try {

        Connection con = DBConnection.getConnection();

        // ================= ADMIN LOGIN =================

        if (role.equals("Admin")) {

            String adminQuery =
                    "SELECT * FROM users WHERE email=? AND role='Admin'";

            PreparedStatement adminPst =
                    con.prepareStatement(adminQuery);

            adminPst.setString(1, email);

            ResultSet adminRs = adminPst.executeQuery();

            if (adminRs.next()) {

                String dbPassword =
                        adminRs.getString("password");

                // STATIC PASSWORD CHECK
                if (password.equals(dbPassword)
                        && password.equals("123")) {

                    JOptionPane.showMessageDialog(
                            this,
                            "Admin Login Successful");

                    new AdminDashboard().setVisible(true);

                    dispose();

                } else {

                    JOptionPane.showMessageDialog(
                            this,
                            "Invalid Admin Password");
                }

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Admin Not Found");
            }

        }

        // ============== OWNER / TENANT LOGIN ==============

        else {

            String query =
                    "SELECT * FROM users WHERE email=? AND password=? AND role=?";

            PreparedStatement pst =
                    con.prepareStatement(query);

            pst.setString(1, email);
            pst.setString(2, password);
            pst.setString(3, role);

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {

                JOptionPane.showMessageDialog(
                        this,
                        role + " Login Successful");

                // OWNER DASHBOARD
                if (role.equals("Owner")) {

                    new OwnerDashboard().setVisible(true);
                }

                // TENANT DASHBOARD
                else if (role.equals("Tenant")) {

                    new TenantDashboard().setVisible(true);
                }

                dispose();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid Email, Password or Role");
            }
        }

        con.close();

    } catch (Exception ex) {

        ex.printStackTrace();

        JOptionPane.showMessageDialog(
                this,
                "Database Connection Error");
    }

});
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new LoginPage().setVisible(true);

        });
    }
}