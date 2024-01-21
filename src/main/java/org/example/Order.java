package org.example;

class Order {
    double price;
    Coordinates from;
    Coordinates to;

    public Order(double price, Coordinates from, Coordinates to) {
        this.price = price;
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "Order{price=" + price + ", from=" + from + ", to=" + to + '}';
    }
}
