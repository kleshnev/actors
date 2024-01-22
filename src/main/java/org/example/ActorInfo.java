package org.example;

public class ActorInfo {
    private final String name;
    private final int price;
    private final Coordinates coordinates;
    private final int distance;

    public ActorInfo(String name, int price, Coordinates coordinates, int distance) {
        this.name = name;
        this.price = price;
        this.coordinates = coordinates;
        this.distance = distance;
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


