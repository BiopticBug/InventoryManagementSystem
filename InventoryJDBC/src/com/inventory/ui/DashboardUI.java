package com.inventory.ui;

import com.formdev.flatlaf.FlatLightLaf;
import com.inventory.dao.ProductDAO;
import com.inventory.model.Product;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardUI extends JFrame {

    private ProductDAO productDAO;

    public DashboardUI() {
        FlatLightLaf.setup();
        productDAO = new ProductDAO();

        setTitle("📊 Inventory Dashboard");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 10));

        JLabel title = new JLabel("Inventory Analytics Overview", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(30, 60, 100));
        add(title, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 10, 10));

        // Chart and metrics
        mainPanel.add(createCategoryChartPanel());
        mainPanel.add(createStatsPanel());

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createCategoryChartPanel() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        List<Product> products = productDAO.getAllProducts();
        Map<String, Integer> categoryTotals = new HashMap<>();

        for (Product p : products) {
            categoryTotals.put(p.getCategory(), categoryTotals.getOrDefault(p.getCategory(), 0) + p.getQuantity());
        }

        for (Map.Entry<String, Integer> entry : categoryTotals.entrySet()) {
            dataset.addValue(entry.getValue(), "Stock", entry.getKey());
        }

        JFreeChart barChart = ChartFactory.createBarChart(
                "Category-wise Stock Levels",
                "Category",
                "Quantity",
                dataset
        );

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new Dimension(400, 400));

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(chartPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createStatsPanel() {
        JPanel statsPanel = new JPanel();
        statsPanel.setLayout(new BoxLayout(statsPanel, BoxLayout.Y_AXIS));
        statsPanel.setBorder(BorderFactory.createTitledBorder("📦 Inventory Summary"));
        statsPanel.setBackground(new Color(245, 250, 255));

        List<Product> products = productDAO.getAllProducts();
        int totalProducts = products.size();
        int totalQty = products.stream().mapToInt(Product::getQuantity).sum();
        long lowStockCount = products.stream().filter(p -> p.getQuantity() <= p.getReorderLevel()).count();

        JLabel totalLabel = new JLabel("Total Products: " + totalProducts);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        totalLabel.setForeground(new Color(0, 102, 204));

        JLabel stockLabel = new JLabel("Total Stock Quantity: " + totalQty);
        stockLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        stockLabel.setForeground(new Color(0, 150, 100));

        JLabel lowStockLabel = new JLabel("Low Stock Alerts: " + lowStockCount);
        lowStockLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lowStockLabel.setForeground(lowStockCount > 0 ? Color.RED : new Color(100, 150, 100));

        statsPanel.add(Box.createVerticalStrut(30));
        statsPanel.add(totalLabel);
        statsPanel.add(Box.createVerticalStrut(20));
        statsPanel.add(stockLabel);
        statsPanel.add(Box.createVerticalStrut(20));
        statsPanel.add(lowStockLabel);
        statsPanel.add(Box.createVerticalStrut(50));

        if (lowStockCount > 0) {
            JButton reorderBtn = new JButton("📧 Generate Reorder Report");
            reorderBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
            reorderBtn.setBackground(new Color(255, 193, 7));
            reorderBtn.setForeground(Color.WHITE);
            reorderBtn.addActionListener(e -> showReorderProducts());
            statsPanel.add(reorderBtn);
        }

        return statsPanel;
    }

    private void showReorderProducts() {
        List<Product> products = productDAO.getAllProducts();
        StringBuilder sb = new StringBuilder("Products below reorder level:\n\n");

        for (Product p : products) {
            if (p.getQuantity() <= p.getReorderLevel()) {
                sb.append(String.format("ID: %d | %s | Qty: %d | Reorder: %d\n",
                        p.getId(), p.getName(), p.getQuantity(), p.getReorderLevel()));
            }
        }

        JOptionPane.showMessageDialog(this, sb.toString(), "⚠️ Reorder Report", JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardUI().setVisible(true));
    }
}
