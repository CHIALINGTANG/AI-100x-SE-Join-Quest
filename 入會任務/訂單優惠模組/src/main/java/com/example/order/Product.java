package com.example.order;

public class Product {
    private String name;
    private double unitPrice;
    private String category;
    
    public Product(String name, double unitPrice, String category) {
        this.name = name;
        this.unitPrice = unitPrice;
        this.category = category;
    }
    
    // Getters
    public String getName() { return name; }
    public double getUnitPrice() { return unitPrice; }
    public String getCategory() { return category; }
}