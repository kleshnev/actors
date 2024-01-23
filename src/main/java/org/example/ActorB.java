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
        System.out.println("Cour " + name + " price " + price + " maxWeight " + maxWeight + " coords " + coordinates);
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
                AddOrderMessage tempOrderMessage = new AddOrderMessage(request.getOrderName(), orderFromCoordinates, orderToCoordinates, request.getWeight());
                int totalDistance = optimizeOrderSequenceAndTellDistance(tempOrderSequence, tempOrderMessage);

//                int distanceFromTo = calculateDistance(
//                        courierCoordinates,
//                        orderFromCoordinates);
//
//                int distanceToDestination = calculateDistance(
//                        orderFromCoordinates,
//                        orderToCoordinates);
//
//                int totalDistance = distanceFromTo + distanceToDestination;

                System.out.println(name + ": Total Distance for  " + request.getOrderName() + " is: " + totalDistance);
                int weightWithNewOrder = weightLeft - request.getWeight();
                if (weightWithNewOrder > 0) {
                    getSender().tell(new ActorInfo(name, price * totalDistance, coordinates, totalDistance, maxWeight, weightWithNewOrder, true, request.getOrderName()), getSelf());
                } else {
                    getSender().tell(new ActorInfo(name, price * totalDistance, coordinates, totalDistance, maxWeight, weightWithNewOrder, false, request.getOrderName()), getSelf());
                }
            } else {
                unhandled(message);
            }
        } else if (message instanceof AddOrderMessage) {
            AddOrderMessage addOrderMessage = (AddOrderMessage) message;
            String actorAName = addOrderMessage.getActorAName();
            weightLeft -= addOrderMessage.getWeight();
            // Add the order to the list
            orderSequence.add(addOrderMessage);
            //optimizeOrderSequenceAndTellDistance(orderSequence, addOrderMessage);

            System.out.println(name + ": Order " + actorAName + " added to the list of " + name);
        } else if (message.equals("getOrderSequence")) {
            // Print orders for this courier
            getSender().tell(new ActorResult(orderSequence, name),getSelf());
        }
    }

    private int optimizeOrderSequenceAndTellDistance(List<AddOrderMessage> tmpOrderSequence, AddOrderMessage tempOrderMessage) {
        tmpOrderSequence.add(tempOrderMessage);

        if (tmpOrderSequence.size() > 1) {
            List<AddOrderMessage> bestOrderSequence = new ArrayList<>(tmpOrderSequence);
            int bestTotalDistance = calculateTotalDistance(bestOrderSequence);

            // Generate all permutations of the orders
            List<List<AddOrderMessage>> permutations = new ArrayList<>();
            generatePermutations(tmpOrderSequence, tmpOrderSequence.size(), permutations);

            // Find the permutation with the minimum total distance
            for (List<AddOrderMessage> permutation : permutations) {
                int totalDistance = calculateTotalDistance(permutation);
                if (totalDistance < bestTotalDistance) {
                    bestTotalDistance = totalDistance;
                    bestOrderSequence = new ArrayList<>(permutation);
                }
            }

            // Print the optimized path
            System.out.println(name + ": Optimized path: " + bestOrderSequence +
                    ", Total Distance: " + bestTotalDistance);

            return bestTotalDistance;
        }

        return calculateDistance(coordinates, tempOrderMessage.getToCoordinates());
    }

    private int calculateTotalDistance(List<AddOrderMessage> orderSequence) {
        int totalDistance = 0;

        for (int i = 0; i < orderSequence.size(); i++) {
            Coordinates fromCoords;
            Coordinates toCoords;

            if (i == 0) {
                // Starting position of the courier
                fromCoords = coordinates;
            } else {
                // TO point of the previous order
                fromCoords = orderSequence.get(i - 1).getToCoordinates();
            }

            // FROM point of the current order
            toCoords = orderSequence.get(i).getFromCoordinates();
            totalDistance += calculateDistance(fromCoords, toCoords);

            // TO point of the current order
            toCoords = orderSequence.get(i).getToCoordinates();
            totalDistance += calculateDistance(orderSequence.get(i).getFromCoordinates(), toCoords);
        }

        return totalDistance;
    }

    private void generatePermutations(List<AddOrderMessage> orders, int n, List<List<AddOrderMessage>> result) {
        if (n == 1) {
            result.add(new ArrayList<>(orders));
        } else {
            for (int i = 0; i < n - 1; i++) {
                generatePermutations(orders, n - 1, result);
                if (n % 2 == 0) {
                    swap(orders, i, n - 1);
                } else {
                    swap(orders, 0, n - 1);
                }
            }
            generatePermutations(orders, n - 1, result);
        }
    }

    private void swap(List<AddOrderMessage> orders, int i, int j) {
        AddOrderMessage temp = orders.get(i);
        orders.set(i, orders.get(j));
        orders.set(j, temp);
    }


    private int calculateDistance(Coordinates c1, Coordinates c2) {
        return Math.abs(c2.getX() - c1.getX()) + Math.abs(c2.getY() - c1.getY());
    }
}
