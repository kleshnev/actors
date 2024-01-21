package org.example;

import scala.Option;

import java.util.ArrayList;
import java.util.List;

class Courier {
    double pricePerKm;
    Coordinates position;
    List<Order> orders = new ArrayList<>();

    public Courier(double pricePerKm, Coordinates position) {
        this.pricePerKm = pricePerKm;
        this.position = position;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public List<Order> getOrders() {
        return orders;
    }

    @Override
    public String toString() {
        return "Courier at position " + position + " with orders: " + orders;
    }
}