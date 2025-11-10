package com.inventory.ui;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;
import com.inventory.utils.DBConnection;

public class ProductForm extends JDialog {

    private JTextField skuField, nameField, categoryField, priceField, qtyField, stockField;
    private boolean isEdit = false;
    private int productId;

    public ProductForm(JFrame parent, String title, Vector<Object> existingData) {
        super(parent, title, true);
        setSize(400, 400);
        setLayout(new GridLayout(8, 2, 10, 10));
        setLocationRelativeTo(parent);

        add(new JLabel("SKU:"));
        skuField = new JTextField();
        add(skuField);

        add(new JLabel("Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Category:"));
        categoryField = new JTextField();
        add(categoryField);

        add(new JLabel("Price:"));
        priceField = new JTextField();
        add(priceField);

        add(new JLabel("Quantity:"));
        qtyField = new JTextField();
        add(qtyField);

        add(new JLabel("Stock:"));
        stockField = new JTextField();
        add(stockField);

        JButton saveButton = new JButton("💾 Save");
        JButton cancelButton = new JButton("❌ Cancel");
        add(saveButton);
        add(cancelButton);

        // Fill form if editing
        if (existingData != null) {
            isEdit = true;
            productId = (int) existingData.get(0);
            skuField.setText((String) existingData.get(1));
            nameField.setText((String) existingData.get(2));
            categoryField.setText((String) existingData.get(3));
            priceField.setText(String.valueOf(existingData.get(4)));
            qtyField.setText(String.valueOf(existingData.get(5)));
            stockField.setText(String.valueOf(existingData.get(6)));
        }

        saveButton.addActionListener(e -> saveProduct());
        cancelButton.addActionListener(e -> dispose());
    }

    private void saveProduct() {
        try (Connection conn = DBConnection.getConnection()) {
            if (isEdit) {
                PreparedStatement ps = conn.prepareStatement(
                        "UPDATE products SET sku=?, name=?, category=?, price=?, quantity=?, stock=? WHERE id=?");
                ps.setString(1, skuField.getText());
                ps.setString(2, nameField.getText());
                ps.setString(3, categoryField.getText());
                ps.setDouble(4, Double.parseDouble(priceField.getText()));
                ps.setInt(5, Integer.parseInt(qtyField.getText()));
                ps.setInt(6, Integer.parseInt(stockField.getText()));
                ps.setInt(7, productId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "✅ Product updated successfully!");
            } else {
                PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO products (sku, name, category, price, quantity, stock) VALUES (?, ?, ?, ?, ?, ?)");
                ps.setString(1, skuField.getText());
                ps.setString(2, nameField.getText());
                ps.setString(3, categoryField.getText());
                ps.setDouble(4, Double.parseDouble(priceField.getText()));
                ps.setInt(5, Integer.parseInt(qtyField.getText()));
                ps.setInt(6, Integer.parseInt(stockField.getText()));
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "✅ Product added successfully!");
            }
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error saving product: " + e.getMessage());
        }
    }
}
