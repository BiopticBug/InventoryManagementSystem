package com.inventory.dao;

import com.inventory.db.DBConnection;
import com.inventory.model.Supplier;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {

    public void addSupplier(Supplier supplier) {
        String sql = "INSERT INTO supplier (name, contact) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, supplier.getName());
            stmt.setString(2, supplier.getContact());
            stmt.executeUpdate();
            System.out.println("✅ Supplier added successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM supplier";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Supplier s = new Supplier();
                s.setId(rs.getInt("supplier_id"));
                s.setName(rs.getString("name"));
                s.setContact(rs.getString("contact"));
                suppliers.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return suppliers;
    }

    public void deleteSupplier(int supplierId) {
        String sql = "DELETE FROM supplier WHERE supplier_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, supplierId);
            stmt.executeUpdate();
            System.out.println("✅ Supplier deleted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
