package org.example;

public class ActorInfo {
    private final String name;
    private final int price;
    private final Coordinates coordinates;

    public ActorInfo(String name, int price, Coordinates coordinates) {
        this.name = name;
        this.price = price;
        this.coordinates = coordinates;
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
}
