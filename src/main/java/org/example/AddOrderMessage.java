package org.example;

// Add a new message class for sending the order to ActorB

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddOrderMessage {
    private final String actorAName;
    //private final String orderName;
    private final Coordinates fromCoordinates;
    private final Coordinates toCoordinates;
    private final int weight;

    public AddOrderMessage(String actorAName,  Coordinates from , Coordinates to, int weight) {
        this.actorAName = actorAName;
        fromCoordinates = from;
        toCoordinates = to;
        this.weight = weight;
    }

}

