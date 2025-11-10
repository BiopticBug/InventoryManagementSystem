package com.inventory;

import com.inventory.dao.ProductDAO;
import com.inventory.dao.SupplierDAO;
import com.inventory.model.Product;
import com.inventory.model.Supplier;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        SupplierDAO supplierDAO = new SupplierDAO();
        ProductDAO productDAO = new ProductDAO();

        // Add supplier
        Supplier supplier = new Supplier("Tech Solutions", "tech@example.com");
        supplierDAO.addSupplier(supplier);

        // View suppliers
        List<Supplier> suppliers = supplierDAO.getAllSuppliers();
        for (Supplier s : suppliers) {
            System.out.println(s);
        }

        // Add a product linked to a supplier (assuming supplier_id = 1)
        Product p = new Product("Monitor", "SKU202", 15, 5, 1);
        productDAO.addProduct(p);

        // Show all products
        List<Product> products = productDAO.getAllProducts();
        for (Product prod : products) {
            System.out.println(prod.getId() + " | " + prod.getName() + " | Supplier: " + prod.getSupplierId());
        }
    }
}
