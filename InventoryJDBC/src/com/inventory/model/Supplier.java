package com.inventory.model;

public class Supplier {
    private int id;
    private String name;
    private String contact;

    // Constructor
    public Supplier(String name, String contact) {
        this.name = name;
        this.contact = contact;
    }

    public Supplier() {}

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    @Override
    public String toString() {
        return id + " | " + name + " | " + contact;
    }
}
