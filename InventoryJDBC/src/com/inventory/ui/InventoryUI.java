package com.inventory.ui;

import com.formdev.flatlaf.FlatLightLaf;
import com.inventory.dao.ProductDAO;
import com.inventory.model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class InventoryUI extends JFrame {

    private ProductDAO productDAO;
    private JTable table;
    private DefaultTableModel model;

    private JTextField nameField, skuField, qtyField, reorderField, supplierField, priceField, categoryField, statusField;

    public InventoryUI() {
        // Modern theme
        FlatLightLaf.setup();

        productDAO = new ProductDAO();

        setTitle("💼 Smart Inventory Manager");
        setSize(1100, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 10));

        // Header
        JLabel header = new JLabel("Inventory Management Dashboard", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 26));
        header.setForeground(new Color(20, 80, 120));
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        add(header, BorderLayout.NORTH);

        // Left Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(10, 2, 10, 8));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add / Edit Product"));
        inputPanel.setBackground(new Color(245, 250, 255));

        inputPanel.add(new JLabel("Product Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("SKU:"));
        skuField = new JTextField();
        inputPanel.add(skuField);

        inputPanel.add(new JLabel("Quantity:"));
        qtyField = new JTextField();
        inputPanel.add(qtyField);

        inputPanel.add(new JLabel("Price:"));
        priceField = new JTextField();
        inputPanel.add(priceField);

        inputPanel.add(new JLabel("Category:"));
        categoryField = new JTextField();
        inputPanel.add(categoryField);

        inputPanel.add(new JLabel("Reorder Level:"));
        reorderField = new JTextField();
        inputPanel.add(reorderField);

        inputPanel.add(new JLabel("Status:"));
        statusField = new JTextField("Available");
        inputPanel.add(statusField);

        inputPanel.add(new JLabel("Supplier ID:"));
        supplierField = new JTextField();
        inputPanel.add(supplierField);

        JButton addBtn = styledButton("➕ Add Product", new Color(40, 167, 69));
        JButton refreshBtn = styledButton("🔄 Refresh", new Color(0, 123, 255));

        inputPanel.add(addBtn);
        inputPanel.add(refreshBtn);

        add(inputPanel, BorderLayout.WEST);

        // Center Table
        model = new DefaultTableModel(new String[]{
                "ID", "Name", "SKU", "Qty", "Price", "Category", "Reorder", "Status", "Supplier", "Date Added"
        }, 0);
        table = new JTable(model);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setFont(new Font("Segoe UI", Font.BOLD, 15));
        tableHeader.setBackground(new Color(200, 230, 250));
        tableHeader.setForeground(Color.DARK_GRAY);

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Bottom buttons
        JButton deleteBtn = styledButton("🗑️ Delete Selected", new Color(220, 53, 69));
        JButton updateQtyBtn = styledButton("✏️ Update Quantity", new Color(255, 193, 7));
        JButton dashboardBtn = styledButton("📊 View Dashboard", new Color(0, 123, 255));

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(240, 245, 250));
        bottomPanel.add(deleteBtn);
        bottomPanel.add(updateQtyBtn);
        bottomPanel.add(dashboardBtn);
        dashboardBtn.addActionListener(e -> new DashboardUI().setVisible(true));


        // Load data
        refreshTable();

        // Button listeners
        addBtn.addActionListener(e -> addProduct());
        refreshBtn.addActionListener(e -> refreshTable());
        deleteBtn.addActionListener(e -> deleteSelected());
        updateQtyBtn.addActionListener(e -> updateQuantity());
    }

    private JButton styledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void addProduct() {
        try {
            String name = nameField.getText();
            String sku = skuField.getText();
            int qty = Integer.parseInt(qtyField.getText());
            double price = Double.parseDouble(priceField.getText());
            String category = categoryField.getText();
            int reorder = Integer.parseInt(reorderField.getText());
            String status = statusField.getText();
            int supplierId = Integer.parseInt(supplierField.getText());

            Product p = new Product(name, sku, qty, reorder, supplierId, price, category, status);
            productDAO.addProduct(p);
            JOptionPane.showMessageDialog(this, "✅ Product added successfully!");
            clearForm();
            refreshTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error adding product: " + e.getMessage());
        }
    }

    private void clearForm() {
        nameField.setText("");
        skuField.setText("");
        qtyField.setText("");
        priceField.setText("");
        categoryField.setText("");
        reorderField.setText("");
        statusField.setText("Available");
        supplierField.setText("");
    }

    private void refreshTable() {
        model.setRowCount(0);
        List<Product> products = productDAO.getAllProducts();
        for (Product p : products) {
            model.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    p.getSku(),
                    p.getQuantity(),
                    String.format("%.2f", p.getPrice()),
                    p.getCategory(),
                    p.getReorderLevel(),
                    p.getStatus(),
                    p.getSupplierId(),
                    p.getDateAdded()
            });
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to delete.");
            return;
        }
        int id = (int) model.getValueAt(row, 0);
        productDAO.deleteProduct(id);
        JOptionPane.showMessageDialog(this, "🗑️ Product deleted!");
        refreshTable();
    }

    private void updateQuantity() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a product first!");
            return;
        }
        int id = (int) model.getValueAt(row, 0);
        String newQty = JOptionPane.showInputDialog(this, "Enter new quantity:");
        try {
            int qty = Integer.parseInt(newQty);
            productDAO.updateQuantity(id, qty);
            JOptionPane.showMessageDialog(this, "✏️ Quantity updated!");
            refreshTable();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number entered.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new InventoryUI().setVisible(true);
        });
    }
}
