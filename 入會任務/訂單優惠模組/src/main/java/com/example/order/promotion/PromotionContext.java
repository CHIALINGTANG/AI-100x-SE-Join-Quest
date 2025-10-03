package com.example.order.promotion;

import com.example.order.OrderItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PromotionContext {

    private final List<OrderItem> originalItems;
    private final List<OrderItem> finalItems;
    private final double originalAmount;
    private double discount;

    public PromotionContext(List<OrderItem> items) {
        this.originalItems = cloneItems(items);
        this.finalItems = cloneItems(items);
        this.originalAmount = calculateOriginalAmount(items);
    }

    public List<OrderItem> getOriginalItems() {
        return Collections.unmodifiableList(originalItems);
    }

    public List<OrderItem> getFinalItems() {
        return finalItems;
    }

    public double getOriginalAmount() {
        return originalAmount;
    }

    public double getDiscount() {
        return discount;
    }

    public double getCurrentSubtotal() {
        return originalAmount - discount;
    }

    public void addDiscount(double amount) {
        if (amount <= 0) {
            return;
        }
        discount += amount;
    }

    public List<OrderItem> snapshotFinalItems() {
        return cloneItems(finalItems);
    }

    private List<OrderItem> cloneItems(List<OrderItem> items) {
        List<OrderItem> copies = new ArrayList<>();
        if (items == null) {
            return copies;
        }
        for (OrderItem item : items) {
            copies.add(new OrderItem(item.getProduct(), item.getQuantity()));
        }
        return copies;
    }

    private double calculateOriginalAmount(List<OrderItem> items) {
        double total = 0;
        if (items == null) {
            return total;
        }
        for (OrderItem item : items) {
            total += item.getProduct().getUnitPrice() * item.getQuantity();
        }
        return total;
    }
}