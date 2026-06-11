package a38;

import java.awt.*;
import javax.swing.*;

public class AdminDashboard extends JFrame {

    public AdminDashboard() {

        setTitle("Admin Dashboard");
        setSize(1100,650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(null);
        mainPanel.setBackground(Theme.BACKGROUND);

        JPanel sidebar = new JPanel(null);
        sidebar.setBounds(0,0,220,650);
        sidebar.setBackground(Theme.SIDEBAR);

        JLabel title = new JLabel("ADMIN PANEL");
        title.setBounds(20,30,180,40);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI",Font.BOLD,24));

        JButton userBtn = createButton("Manage Users");
        userBtn.setBounds(20,120,170,40);

        JButton propertyBtn = createButton("Properties");
        propertyBtn.setBounds(20,190,170,40);

        JButton reportBtn = createButton("Reports");
        reportBtn.setBounds(20,260,170,40);

        JButton logoutBtn = createButton("Logout");
        logoutBtn.setBounds(20,500,170,40);

        sidebar.add(title);
        sidebar.add(userBtn);
        sidebar.add(propertyBtn);
        sidebar.add(reportBtn);
        sidebar.add(logoutBtn);

        JLabel heading = new JLabel("Welcome Admin");
        heading.setBounds(350,50,300,40);
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("Segoe UI",Font.BOLD,30));

        mainPanel.add(sidebar);
        mainPanel.add(heading);

        add(mainPanel);

        userBtn.addActionListener(e ->
                new ManageUsers().setVisible(true));

        propertyBtn.addActionListener(e ->
                new ManageProperties().setVisible(true));

        reportBtn.addActionListener(e ->
                new ReportsPage().setVisible(true));

        logoutBtn.addActionListener(e -> {
            new LoginPage().setVisible(true);
            dispose();
        });

    }

    private JButton createButton(String text){

        JButton btn = new JButton(text);
        btn.setBackground(Theme.BUTTON);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);

        return btn;
    }
}