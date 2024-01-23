package org.example;

// Add a new message class for sending the order to ActorB

public class AddOrderMessage {
    private final String actorAName;
    private final String orderName;

    public AddOrderMessage(String actorAName, String orderName) {
        this.actorAName = actorAName;
        this.orderName = orderName;
    }

    public String getActorAName() {
        return actorAName;
    }

    public String getOrderName() {
        return orderName;
    }
}

