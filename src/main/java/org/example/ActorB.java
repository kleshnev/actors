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

                List<AddOrderMessage> tempOrderSequence = new ArrayList<>(orderSequence);
                AddOrderMessage tempOrderMessage = new AddOrderMessage(request.getOrderName(), orderFromCoordinates ,orderToCoordinates , request.getWeight());
                int totalDistance = optimizeOrderSequenceAndTellDistance(tempOrderSequence,tempOrderMessage);

//                int distanceFromTo = calculateDistance(
//                        courierCoordinates,
//                        orderFromCoordinates);
//
//                int distanceToDestination = calculateDistance(
//                        orderFromCoordinates,
//                        orderToCoordinates);
//
//                int totalDistance = distanceFromTo + distanceToDestination;

                System.out.println(name + ": Total Distance for  "+ request.getOrderName() + " is: " + totalDistance);
                int weightWithNewOrder =  weightLeft-request.getWeight();
                if (weightWithNewOrder>0) {
                    getSender().tell(new ActorInfo(name, price*totalDistance , coordinates, totalDistance, maxWeight, weightWithNewOrder, true, request.getOrderName()), getSelf());
                }else {
                    getSender().tell(new ActorInfo(name, price*totalDistance , coordinates, totalDistance, maxWeight, weightWithNewOrder, false, request.getOrderName()), getSelf());
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

    private int optimizeOrderSequenceAndTellDistance(List<AddOrderMessage> tmpOrderSequence, AddOrderMessage tempOrderMessage) {
        tmpOrderSequence.add(tempOrderMessage);
        if (tmpOrderSequence.size() > 1) {
            List<Coordinates> optimizedPath = new ArrayList<>(tmpOrderSequence.size() + 1);

            // Add the starting point (Courier's current coordinates)
            optimizedPath.add(coordinates);

            // Add the order coordinates to the optimized path
            for (AddOrderMessage order : tmpOrderSequence) {
                optimizedPath.add(order.getToCoordinates());
            }

            // Sort the optimized path based on the shortest distance
            optimizedPath.sort((c1, c2) -> calculateDistance(coordinates, c1) - calculateDistance(coordinates, c2));

            // Find the position of the new order in the optimized path
            int newPosition = optimizedPath.indexOf(tempOrderMessage.getToCoordinates());

            // Calculate the total distance only for the new order
            int totalDistance;
            if (newPosition > 0) {
                totalDistance = calculateDistance(optimizedPath.get(newPosition - 1), tempOrderMessage.getToCoordinates());
            } else {
                // If newPosition is 0, it means the new order is at the starting position
                totalDistance = calculateDistance(coordinates, tempOrderMessage.getToCoordinates());
            }

            // Calculate the total price for the optimized path
            return totalDistance;

            /*System.out.println(name + ": Optimized path: " + optimizedPath +
                    ", Total Distance: " + totalDistance +
                    ", Total Price: " + totalPrice);*/
        }
        return calculateDistance(coordinates, tempOrderMessage.getToCoordinates());
    }


    private int calculateDistance(Coordinates c1, Coordinates c2) {
        return Math.abs(c2.getX() - c1.getX()) + Math.abs(c2.getY() - c1.getY());
    }
}
