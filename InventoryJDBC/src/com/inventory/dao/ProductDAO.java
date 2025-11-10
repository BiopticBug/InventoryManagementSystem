package com.inventory.dao;

import com.inventory.db.DBConnection;
import com.inventory.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // ➕ Add new product
    public void addProduct(Product product) {
        String sql = "INSERT INTO product (name, sku, quantity, price, category, reorder_level, supplier_id, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.getName());
            stmt.setString(2, product.getSku());
            stmt.setInt(3, product.getQuantity());
            stmt.setDouble(4, product.getPrice());
            stmt.setString(5, product.getCategory());
            stmt.setInt(6, product.getReorderLevel());
            stmt.setInt(7, product.getSupplierId());
            stmt.setString(8, product.getStatus());

            stmt.executeUpdate();
            System.out.println("✅ Product added successfully!");
        } catch (SQLException e) {
            System.out.println("❌ SQL Error in addProduct(): " + e.getMessage());
        }
    }

    // 🧾 Get all products
    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM product ORDER BY product_id DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("product_id"));
                p.setName(rs.getString("name"));
                p.setSku(rs.getString("sku"));
                p.setQuantity(rs.getInt("quantity"));
                p.setPrice(rs.getDouble("price"));
                p.setCategory(rs.getString("category"));
                p.setReorderLevel(rs.getInt("reorder_level"));
                p.setSupplierId(rs.getInt("supplier_id"));
                p.setStatus(rs.getString("status"));
                p.setDateAdded(rs.getString("date_added"));
                products.add(p);
            }
        } catch (SQLException e) {
            System.out.println("❌ SQL Error in getAllProducts(): " + e.getMessage());
        }
        return products;
    }

    // ✏️ Update quantity
    public void updateQuantity(int productId, int newQuantity) {
        String sql = "UPDATE product SET quantity = ? WHERE product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, newQuantity);
            stmt.setInt(2, productId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🗑️ Delete product
    public void deleteProduct(int productId) {
        String sql = "DELETE FROM product WHERE product_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 🔍 Search by keyword
    public List<Product> searchProducts(String keyword) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM product WHERE name LIKE ? OR sku LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            stmt.setString(2, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("product_id"));
                p.setName(rs.getString("name"));
                p.setSku(rs.getString("sku"));
                p.setQuantity(rs.getInt("quantity"));
                p.setPrice(rs.getDouble("price"));
                p.setCategory(rs.getString("category"));
                p.setReorderLevel(rs.getInt("reorder_level"));
                p.setSupplierId(rs.getInt("supplier_id"));
                p.setStatus(rs.getString("status"));
                p.setDateAdded(rs.getString("date_added"));
                products.add(p);
            }
        } catch (SQLException e) {
            System.out.println("❌ SQL Error in searchProducts(): " + e.getMessage());
        }
        return products;
    }
}
