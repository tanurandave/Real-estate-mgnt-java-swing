package a38;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import javax.swing.*;

public class AddProperty extends JFrame {

    JTextField txtName, txtRent, txtAddress;

    JTextArea txtDescription;

    // =====================================================
    // PROPERTY TYPE DROPDOWN
    // =====================================================

    JComboBox<String> typeBox;

    JComboBox<String> statusBox;

    JButton uploadBtn, saveBtn, resetBtn;

    JLabel lblImage;

    String imagePath = "";

    public AddProperty() {

        setTitle("Add Property");
        setSize(850, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(null);

        panel.setBackground(Theme.BACKGROUND);

        // =====================================================
        // HEADING
        // =====================================================

        JLabel heading = new JLabel("ADD PROPERTY");

        heading.setBounds(260, 20, 350, 40);

        heading.setForeground(Color.WHITE);

        heading.setFont(
                new Font("Segoe UI", Font.BOLD, 30));

        panel.add(heading);

        // =====================================================
        // PROPERTY NAME
        // =====================================================

        JLabel l1 = new JLabel("Property Name");

        l1.setBounds(60, 100, 130, 30);

        l1.setForeground(Color.WHITE);

        l1.setFont(new Font("Segoe UI", Font.BOLD, 15));

        txtName = new JTextField();

        txtName.setBounds(220, 100, 250, 38);

        txtName.setFont(
                new Font("Segoe UI", Font.PLAIN, 15));

        // =====================================================
        // PROPERTY TYPE DROPDOWN
        // =====================================================

        JLabel l2 = new JLabel("Property Type");

        l2.setBounds(60, 170, 130, 30);

        l2.setForeground(Color.WHITE);

        l2.setFont(new Font("Segoe UI", Font.BOLD, 15));

        typeBox = new JComboBox<>(new String[]{

            "Select Property Type",

            "Commercial",

            "Residential",

            "Apartment",

            "Villa",

            "Office",

            "Shop",

            "Warehouse",

            "Farm House",

            "PG",

            "Hostel",

            "Bungalow",

            "Studio Apartment"

        });

        typeBox.setBounds(220, 170, 250, 38);

        typeBox.setFont(
                new Font("Segoe UI", Font.PLAIN, 15));

        // =====================================================
        // RENT
        // =====================================================

        JLabel l3 = new JLabel("Rent Amount");

        l3.setBounds(60, 240, 130, 30);

        l3.setForeground(Color.WHITE);

        l3.setFont(new Font("Segoe UI", Font.BOLD, 15));

        txtRent = new JTextField();

        txtRent.setBounds(220, 240, 250, 38);

        txtRent.setFont(
                new Font("Segoe UI", Font.PLAIN, 15));

        // =====================================================
        // ADDRESS
        // =====================================================

        JLabel l4 = new JLabel("Address");

        l4.setBounds(60, 310, 130, 30);

        l4.setForeground(Color.WHITE);

        l4.setFont(new Font("Segoe UI", Font.BOLD, 15));

        txtAddress = new JTextField();

        txtAddress.setBounds(220, 310, 250, 38);

        txtAddress.setFont(
                new Font("Segoe UI", Font.PLAIN, 15));

        // =====================================================
        // DESCRIPTION
        // =====================================================

        JLabel l5 = new JLabel("Description");

        l5.setBounds(60, 380, 130, 30);

        l5.setForeground(Color.WHITE);

        l5.setFont(new Font("Segoe UI", Font.BOLD, 15));

        txtDescription = new JTextArea();

        txtDescription.setFont(
                new Font("Segoe UI", Font.PLAIN, 15));

        JScrollPane sp = new JScrollPane(txtDescription);

        sp.setBounds(220, 380, 250, 90);

        // =====================================================
        // STATUS
        // =====================================================

        JLabel l6 = new JLabel("Status");

        l6.setBounds(60, 500, 130, 30);

        l6.setForeground(Color.WHITE);

        l6.setFont(new Font("Segoe UI", Font.BOLD, 15));

        statusBox = new JComboBox<>(new String[]{

            "Vacant",

            "Occupied"
        });

        statusBox.setBounds(220, 500, 250, 38);

        statusBox.setFont(
                new Font("Segoe UI", Font.PLAIN, 15));

        // =====================================================
        // IMAGE SECTION
        // =====================================================

        JLabel imgTitle = new JLabel("PROPERTY IMAGE");

        imgTitle.setBounds(570, 100, 200, 30);

        imgTitle.setForeground(Color.WHITE);

        imgTitle.setFont(
                new Font("Segoe UI", Font.BOLD, 18));

        lblImage = new JLabel();

        lblImage.setBounds(540, 150, 220, 220);

        lblImage.setBorder(
                BorderFactory.createLineBorder(
                        Color.WHITE,
                        2));

        lblImage.setHorizontalAlignment(JLabel.CENTER);

        lblImage.setBackground(Color.WHITE);

        lblImage.setOpaque(true);

        uploadBtn = new JButton("Upload Image");

        uploadBtn.setBounds(570, 400, 160, 45);

        uploadBtn.setBackground(Theme.BUTTON);

        uploadBtn.setForeground(Color.WHITE);

        uploadBtn.setFocusPainted(false);

        uploadBtn.setFont(
                new Font("Segoe UI", Font.BOLD, 15));

        // =====================================================
        // BUTTONS
        // =====================================================

        saveBtn = new JButton("Save");

        saveBtn.setBounds(220, 580, 120, 45);

        saveBtn.setBackground(Theme.BUTTON);

        saveBtn.setForeground(Color.WHITE);

        saveBtn.setFocusPainted(false);

        saveBtn.setFont(
                new Font("Segoe UI", Font.BOLD, 16));

        resetBtn = new JButton("Reset");

        resetBtn.setBounds(370, 580, 120, 45);

        resetBtn.setBackground(Color.DARK_GRAY);

        resetBtn.setForeground(Color.WHITE);

        resetBtn.setFocusPainted(false);

        resetBtn.setFont(
                new Font("Segoe UI", Font.BOLD, 16));

        // =====================================================
        // ADD COMPONENTS
        // =====================================================

        panel.add(l1);
        panel.add(txtName);

        panel.add(l2);
        panel.add(typeBox);

        panel.add(l3);
        panel.add(txtRent);

        panel.add(l4);
        panel.add(txtAddress);

        panel.add(l5);
        panel.add(sp);

        panel.add(l6);
        panel.add(statusBox);

        panel.add(imgTitle);

        panel.add(lblImage);

        panel.add(uploadBtn);

        panel.add(saveBtn);

        panel.add(resetBtn);

        add(panel);

        // =====================================================
        // BUTTON ACTIONS
        // =====================================================

        uploadBtn.addActionListener(e -> uploadImage());

        resetBtn.addActionListener(e -> clearFields());

        saveBtn.addActionListener(e -> saveProperty());
    }

    // =====================================================
    // UPLOAD IMAGE
    // =====================================================

    private void uploadImage() {

        JFileChooser chooser = new JFileChooser();

        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {

            File file = chooser.getSelectedFile();

            imagePath = file.getAbsolutePath();

            ImageIcon icon = new ImageIcon(imagePath);

            Image img = icon.getImage().getScaledInstance(
                    220,
                    220,
                    Image.SCALE_SMOOTH);

            lblImage.setIcon(new ImageIcon(img));
        }
    }

    // =====================================================
    // SAVE PROPERTY
    // =====================================================

    private void saveProperty() {

        try {

            // =====================================================
            // VALIDATION
            // =====================================================

            if (txtName.getText().trim().isEmpty()
                    || typeBox.getSelectedIndex() == 0
                    || txtRent.getText().trim().isEmpty()
                    || txtAddress.getText().trim().isEmpty()
                    || imagePath.isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Please Fill All Required Fields");

                return;
            }

            Connection con =
                    DBConnection.getConnection();

            String query =
                    "INSERT INTO properties "
                    + "(property_name, property_type, "
                    + "rent_amount, address, description, "
                    + "status, property_image) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement pst =
                    con.prepareStatement(query);

            pst.setString(1, txtName.getText());

            // =====================================================
            // PROPERTY TYPE FROM DROPDOWN
            // =====================================================

            pst.setString(2,
                    typeBox.getSelectedItem().toString());

            pst.setDouble(3,
                    Double.parseDouble(txtRent.getText()));

            pst.setString(4,
                    txtAddress.getText());

            pst.setString(5,
                    txtDescription.getText());

            pst.setString(6,
                    statusBox.getSelectedItem().toString());

            // =====================================================
            // IMAGE STORE
            // =====================================================

            FileInputStream fis =
                    new FileInputStream(imagePath);

            pst.setBinaryStream(
                    7,
                    fis,
                    fis.available());

            int rows = pst.executeUpdate();

            if (rows > 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Property Added Successfully");

                clearFields();

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Insert Failed");
            }

            con.close();

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(this,
                    "Rent Must Be Numeric");

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error");
        }
    }

    // =====================================================
    // CLEAR FIELDS
    // =====================================================

    private void clearFields() {

        txtName.setText("");

        typeBox.setSelectedIndex(0);

        txtRent.setText("");

        txtAddress.setText("");

        txtDescription.setText("");

        statusBox.setSelectedIndex(0);

        lblImage.setIcon(null);

        imagePath = "";
    }

    // =====================================================
    // MAIN METHOD
    // =====================================================

    public static void main(String[] args) {

        new AddProperty().setVisible(true);
    }
}