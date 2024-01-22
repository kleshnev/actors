package org.example;

import akka.actor.UntypedAbstractActor;

public class ActorB extends UntypedAbstractActor {

    private int price;
    private String name;
    private Coordinates coordinates;

    public ActorB(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public void preStart() {
        this.price = new java.util.Random().nextInt(100);
        this.name = getSelf().path().name();
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof ActorBRequest) {
            ActorBRequest request = (ActorBRequest) message;
            if (request.getCommand().equals("GetInfo")) {
                Coordinates courierCoordinates = coordinates;
                Coordinates orderFromCoordinates = request.getOrderFromCoordinates();
                Coordinates orderToCoordinates = request.getOrderToCoordinates();

                int distanceFromTo = calculateDistance(
                        courierCoordinates.getX(), courierCoordinates.getY(),
                        orderFromCoordinates.getX(), orderFromCoordinates.getY());

                int distanceToDestination = calculateDistance(
                        orderFromCoordinates.getX(), orderFromCoordinates.getY(),
                        orderToCoordinates.getX(), orderToCoordinates.getY());

                int totalDistance = distanceFromTo + distanceToDestination;

                System.out.println(name + ": Total Distance for  "+ request.getOrderName() + " is: " + totalDistance);

                getSender().tell(new ActorInfo(name, price*totalDistance, coordinates, totalDistance), getSelf());
            } else {
                unhandled(message);
            }
        } else {
            unhandled(message);
        }
    }


    private int calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x2 - x1) + Math.abs(y2 - y1);
    }
}
