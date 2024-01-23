package org.example;

public class ActorInfo {
    private final String name;
    private final String orderName;

    private final int price;
    private final Coordinates coordinates;
    private final int distance;

    private final int maxWeight;
    private final int weightLeft;

    private boolean accept;

    public ActorInfo(String name, int price, Coordinates coordinates, int distance, int maxWeight , int weightLeft , boolean accept, String orderName) {
        this.name = name;
        this.price = price;
        this.coordinates = coordinates;
        this.distance = distance;
        this.maxWeight = maxWeight;
        this.weightLeft = weightLeft;
        this.accept = accept;
        this.orderName = orderName;
    }

    public String getOrderName() {
        return orderName;
    }

    public boolean isAccept() {
        return accept;
    }

    public int getWeight(){
        return maxWeight;
    }public int weightLeft(){
        return weightLeft;
    }
    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public int getDistance() {
        return distance;
    }
}


