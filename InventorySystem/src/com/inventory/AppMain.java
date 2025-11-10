package com.inventory;

import com.inventory.dao.ProductDAO;
import com.inventory.model.Product;

import java.util.List;

public class AppMain {
    public static void main(String[] args) {
        try {
            ProductDAO dao = new ProductDAO();
            List<Product> products = dao.getAll();
            System.out.println("Products count: " + products.size());
            for (Product p : products) {
                System.out.println(p.getId() + " - " + p.getName() + " - " + p.getQuantity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
