package a38;

import java.awt.*;   
import javax.swing.*;

public class LandingPage extends JFrame {

    JButton loginBtn, registerBtn;

    public LandingPage() {

        setTitle("Real Estate & Rental Management");
        setSize(1000,600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new ImagePanel("src\\\\a38\\\\images\\\\bg.png");
        panel.setLayout(null);

        JLabel title = new JLabel("REAL ESTATE & RENTAL MANAGEMENT");
        title.setBounds(180,100,700,50);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI",Font.BOLD,30));

        loginBtn = createButton("LOGIN");
        loginBtn.setBounds(300,320,150,50);

        registerBtn = createButton("REGISTER");
        registerBtn.setBounds(550,320,150,50);

        panel.add(title);
        panel.add(loginBtn);
        panel.add(registerBtn);

        add(panel);

        loginBtn.addActionListener(e->{
            new LoginPage().setVisible(true);
            dispose();
        });

        registerBtn.addActionListener(e->{
            new RegisterPage().setVisible(true);
            dispose();
        });
    }

    private JButton createButton(String text){

        JButton btn = new JButton(text);

        btn.setBackground(Theme.BUTTON);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI",Font.BOLD,16));
        btn.setFocusPainted(false);

        return btn;
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new LandingPage().setVisible(true);
        });

    }
}