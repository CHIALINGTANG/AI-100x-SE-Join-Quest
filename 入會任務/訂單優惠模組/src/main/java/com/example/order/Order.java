package com.example.order;

import java.util.List;
import java.util.ArrayList;

public class Order {
    private double totalAmount;
    private double originalAmount;
    private double discount;
    private List<OrderItem> items;
    
    public Order() {
        this.items = new ArrayList<>();
    }
    
    // Getters and setters
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    
    public double getOriginalAmount() { return originalAmount; }
    public void setOriginalAmount(double originalAmount) { this.originalAmount = originalAmount; }
    
    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }
    
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}