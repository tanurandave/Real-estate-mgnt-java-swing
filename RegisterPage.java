package a38;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class RegisterPage extends JFrame {

    JTextField txtName, txtEmail, txtPhone;
    JPasswordField txtPassword;
    JComboBox<String> roleBox;

    public RegisterPage() {

        setTitle("Register");
        setSize(550, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(null);
        panel.setBackground(Theme.BACKGROUND);

        JLabel title = new JLabel("REGISTER");
        title.setBounds(180, 30, 200, 40);
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(Color.WHITE);

        panel.add(title);

        JLabel l1 = new JLabel("Full Name");
        l1.setBounds(70, 100, 100, 30);
        l1.setForeground(Color.WHITE);

        txtName = new JTextField();
        txtName.setBounds(220, 100, 220, 35);

        JLabel l2 = new JLabel("Email");
        l2.setBounds(70, 160, 100, 30);
        l2.setForeground(Color.WHITE);

        txtEmail = new JTextField();
        txtEmail.setBounds(220, 160, 220, 35);

        JLabel l3 = new JLabel("Phone");
        l3.setBounds(70, 220, 100, 30);
        l3.setForeground(Color.WHITE);

        txtPhone = new JTextField();
        txtPhone.setBounds(220, 220, 220, 35);

        JLabel l4 = new JLabel("Password");
        l4.setBounds(70, 280, 100, 30);
        l4.setForeground(Color.WHITE);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(220, 280, 220, 35);

        JLabel l5 = new JLabel("Role");
        l5.setBounds(70, 340, 100, 30);
        l5.setForeground(Color.WHITE);

        roleBox = new JComboBox<>(
                new String[]{"Owner", "Tenant"});

        roleBox.setBounds(220, 340, 220, 35);

        JButton registerBtn = new JButton("REGISTER");
        registerBtn.setBounds(180, 430, 160, 45);

        registerBtn.setBackground(Theme.BUTTON);
        registerBtn.setForeground(Color.WHITE);

        panel.add(l1);
        panel.add(txtName);

        panel.add(l2);
        panel.add(txtEmail);

        panel.add(l3);
        panel.add(txtPhone);

        panel.add(l4);
        panel.add(txtPassword);

        panel.add(l5);
        panel.add(roleBox);

        panel.add(registerBtn);

        add(panel);

        // REGISTER BUTTON ACTION

        registerBtn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {

                String name = txtName.getText();
                String email = txtEmail.getText();
                String phone = txtPhone.getText();
                String password = String.valueOf(txtPassword.getPassword());
                String role = roleBox.getSelectedItem().toString();

                try {

                    Connection con = DBConnection.getConnection();

                    String query = "INSERT INTO users(name,email,phone,password,role) VALUES(?,?,?,?,?)";

                    PreparedStatement pst = con.prepareStatement(query);

                    pst.setString(1, name);
                    pst.setString(2, email);
                    pst.setString(3, phone);
                    pst.setString(4, password);
                    pst.setString(5, role);

                    int rows = pst.executeUpdate();

                    if (rows > 0) {

                        JOptionPane.showMessageDialog(null,
                                "Registration Successful");

                        txtName.setText("");
                        txtEmail.setText("");
                        txtPhone.setText("");
                        txtPassword.setText("");

                    } else {

                        JOptionPane.showMessageDialog(null,
                                "Registration Failed");
                    }

                    con.close();

                } catch (Exception ex) {

                    ex.printStackTrace();

                    JOptionPane.showMessageDialog(null,
                            "Database Error");
                }
            }
        });
    }
}