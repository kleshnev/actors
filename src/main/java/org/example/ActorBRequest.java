package org.example;

public class ActorBRequest {
    private final String command;
    private  Coordinates courierCoordinates;
    private final Coordinates orderFromCoordinates;
    private final Coordinates orderToCoordinates;

    private final String orderName;

    public ActorBRequest(String command, Coordinates orderFromCoordinates, Coordinates orderToCoordinates, String orderName) {
        this.command = command;
        //this.courierCoordinates = courierCoordinates;
        this.orderFromCoordinates = orderFromCoordinates;
        this.orderToCoordinates = orderToCoordinates;
        this.orderName = orderName;
    }

    public String getCommand() {
        return command;
    }
    public String getOrderName() {
        return orderName;
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
