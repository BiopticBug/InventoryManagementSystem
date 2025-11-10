package com.inventory.dao;

import com.inventory.model.Product;
import com.inventory.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public List<Product> getAll() throws SQLException {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT id, sku, name, price, quantity FROM products";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Product p = new Product(
                  rs.getInt("id"),
                  rs.getString("sku"),
                  rs.getString("name"),
                  rs.getDouble("price"),
                  rs.getInt("quantity")
                );
                list.add(p);
            }
        }
        return list;
    }
    // add create, update, delete methods
}
