package a38;

import java.awt.*;
import javax.swing.*;

public class UpdateProperty extends JFrame {

    JTextField txtId;
    JTextField txtName;
    JTextField txtType;
    JTextField txtRent;
    JTextField txtAddress;

    public UpdateProperty() {

        setTitle("Update Property");
        setSize(600,550);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(Theme.BACKGROUND);

        JLabel heading = new JLabel("UPDATE PROPERTY");
        heading.setBounds(150,30,300,40);
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("Segoe UI",Font.BOLD,28));

        panel.add(heading);

        JLabel l1 = new JLabel("Property ID");
        l1.setBounds(80,100,100,30);
        l1.setForeground(Color.WHITE);

        txtId = new JTextField();
        txtId.setBounds(220,100,220,35);

        JLabel l2 = new JLabel("Property Name");
        l2.setBounds(80,160,120,30);
        l2.setForeground(Color.WHITE);

        txtName = new JTextField();
        txtName.setBounds(220,160,220,35);

        JLabel l3 = new JLabel("Type");
        l3.setBounds(80,220,120,30);
        l3.setForeground(Color.WHITE);

        txtType = new JTextField();
        txtType.setBounds(220,220,220,35);

        JLabel l4 = new JLabel("Rent");
        l4.setBounds(80,280,120,30);
        l4.setForeground(Color.WHITE);

        txtRent = new JTextField();
        txtRent.setBounds(220,280,220,35);

        JLabel l5 = new JLabel("Address");
        l5.setBounds(80,340,120,30);
        l5.setForeground(Color.WHITE);

        txtAddress = new JTextField();
        txtAddress.setBounds(220,340,220,35);

        JButton btnUpdate = new JButton("Update");

        btnUpdate.setBounds(220,430,150,45);
        btnUpdate.setBackground(Theme.BUTTON);
        btnUpdate.setForeground(Color.WHITE);

        panel.add(l1);
        panel.add(txtId);

        panel.add(l2);
        panel.add(txtName);

        panel.add(l3);
        panel.add(txtType);

        panel.add(l4);
        panel.add(txtRent);

        panel.add(l5);
        panel.add(txtAddress);

        panel.add(btnUpdate);

        add(panel);

        btnUpdate.addActionListener(e -> {

            JOptionPane.showMessageDialog(
                    this,
                    "Property Updated Successfully");

        });
    }
}