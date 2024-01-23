package org.example;

public class ActorBRequest {
    private final String command;
    private  Coordinates courierCoordinates;
    private final Coordinates orderFromCoordinates;
    private final Coordinates orderToCoordinates;
    private final String orderName;
    private final int weight;

    public ActorBRequest(String command, Coordinates orderFromCoordinates, Coordinates orderToCoordinates, String orderName, int weight) {
        this.command = command;
        this.orderFromCoordinates = orderFromCoordinates;
        this.orderToCoordinates = orderToCoordinates;
        this.orderName = orderName;
        this.weight =weight;
    }

    public int getWeight(){
        return weight;
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
