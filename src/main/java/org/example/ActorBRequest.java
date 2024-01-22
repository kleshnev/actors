package org.example;

public class ActorBRequest {
    private final String command;
    private final Coordinates courierCoordinates;
    private final Coordinates orderFromCoordinates;
    private final Coordinates orderToCoordinates;

    public ActorBRequest(String command, Coordinates courierCoordinates, Coordinates orderFromCoordinates, Coordinates orderToCoordinates) {
        this.command = command;
        this.courierCoordinates = courierCoordinates;
        this.orderFromCoordinates = orderFromCoordinates;
        this.orderToCoordinates = orderToCoordinates;
    }

    public String getCommand() {
        return command;
    }

    public Coordinates getCourierCoordinates() {
        return courierCoordinates;
    }

    public Coordinates getOrderFromCoordinates() {
        return orderFromCoordinates;
    }

    public Coordinates getOrderToCoordinates() {
        return orderToCoordinates;
    }
}
