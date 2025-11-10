package com.inventory.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import com.inventory.utils.DBConnection;

public class LoginForm extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;

    public LoginForm() {
        setTitle("Inventory System - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JLabel userLabel = new JLabel("Username:");
        usernameField = new JTextField();

        JLabel passLabel = new JLabel("Password:");
        passwordField = new JPasswordField();

        loginButton = new JButton("Login");
        statusLabel = new JLabel("", SwingConstants.CENTER);
        statusLabel.setForeground(Color.RED);

        loginButton.addActionListener(e -> attemptLogin());

        panel.add(userLabel);
        panel.add(usernameField);
        panel.add(passLabel);
        panel.add(passwordField);
        panel.add(new JLabel());
        panel.add(loginButton);
        panel.add(statusLabel);

        add(panel);
    }

    private void attemptLogin() {
    String username = usernameField.getText();
    String password = String.valueOf(passwordField.getPassword());

    if (username.isEmpty() || password.isEmpty()) {
        statusLabel.setText("Please enter both fields");
        return;
    }

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement("SELECT password_hash, role FROM users WHERE username=?")) {

        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String storedHash = rs.getString("password_hash");
            if (password.equals(storedHash)) {
                JOptionPane.showMessageDialog(this, "Welcome " + username + "!");
                dispose();
                new Dashboard().setVisible(true);
            } else {
                statusLabel.setText("Invalid credentials");
            }

        } else {
            statusLabel.setText("User not found");
        }

    } catch (SQLException ex) {
        statusLabel.setText("DB Error: " + ex.getMessage());
        ex.printStackTrace();
    }
}


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}
