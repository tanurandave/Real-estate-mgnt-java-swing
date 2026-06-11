package a38;

import java.awt.*;
import javax.swing.*;

public class DeleteProperty extends JFrame {

    JTextField txtId;

    public DeleteProperty() {

        setTitle("Delete Property");
        setSize(500,300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(null);
        panel.setBackground(Theme.BACKGROUND);

        JLabel heading = new JLabel("DELETE PROPERTY");

        heading.setBounds(120,30,250,40);
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("Segoe UI",Font.BOLD,26));

        panel.add(heading);

        JLabel lblId = new JLabel("Property ID");
        lblId.setBounds(70,110,100,30);
        lblId.setForeground(Color.WHITE);

        txtId = new JTextField();
        txtId.setBounds(180,110,180,35);

        JButton btnDelete = new JButton("Delete");

        btnDelete.setBounds(170,180,140,40);
        btnDelete.setBackground(Color.RED);
        btnDelete.setForeground(Color.WHITE);

        panel.add(lblId);
        panel.add(txtId);
        panel.add(btnDelete);

        add(panel);

        btnDelete.addActionListener(e -> {

            JOptionPane.showMessageDialog(
                    this,
                    "Property Deleted Successfully");

        });
    }
}