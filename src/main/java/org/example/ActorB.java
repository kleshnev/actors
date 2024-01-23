package org.example;

import akka.actor.UntypedAbstractActor;

import java.util.ArrayList;
import java.util.List;

public class ActorB extends UntypedAbstractActor {

    private int price;
    private String name;
    private Coordinates coordinates;

    private int maxWeight;

    private int weightLeft;

    private List<AddOrderMessage> orderSequence = new ArrayList<>();

    public ActorB(Coordinates coordinates, int maxWeight) {
        this.coordinates = coordinates;
        this.maxWeight = maxWeight;
        this.weightLeft = maxWeight;
    }

    @Override
    public void preStart() {
        this.price = new java.util.Random().nextInt(100);
        this.name = getSelf().path().name();
        System.out.println("Cour " + name + " price " + price + " maxWeight " + maxWeight +" coords "+ coordinates);
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
                int weightWithNewOrder =  weightLeft-request.getWeight();
                if (weightWithNewOrder>0) {
                    getSender().tell(new ActorInfo(name, price * totalDistance, coordinates, totalDistance, maxWeight, weightWithNewOrder, true, request.getOrderName()), getSelf());
                }else {
                    getSender().tell(new ActorInfo(name, price * totalDistance, coordinates, totalDistance, maxWeight, weightWithNewOrder, false, request.getOrderName()), getSelf());
                }
            } else {
                unhandled(message);
            }
        } else if (message instanceof AddOrderMessage) {
            AddOrderMessage addOrderMessage = (AddOrderMessage) message;
            String actorAName = addOrderMessage.getActorAName();
            weightLeft-=addOrderMessage.getWeight();
            // Add the order to the list
            orderSequence.add(addOrderMessage);

            System.out.println(name + ": Order " + actorAName + " added to the list of " + name);
        }
    }


    private int calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x2 - x1) + Math.abs(y2 - y1);
    }
}
