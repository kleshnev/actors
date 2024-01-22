package org.example;

import akka.actor.ActorRef;
import akka.actor.UntypedAbstractActor;
import akka.dispatch.Futures;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

class ActorA extends UntypedAbstractActor {

    private final ActorRef coordinator;
    private final String name;
    private final Coordinates coordinates;

    public ActorA(ActorRef coordinator, String name, Coordinates coordinates) {
        this.coordinator = coordinator;
        this.name = name;
        this.coordinates = coordinates;
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof String) {
            String command = (String) message;
            System.out.println(name + " received command: " + command);

            // Generate one random number for all ActorB instances
            int randomNumber = new Random().nextInt(100);

            // Create a list to hold Future responses from ActorB instances
            List<Future<Object>> futures = new ArrayList<>();

            // Ask all ActorB instances for their prices and names
            for (int i = 1; i <= 10; i++) {
                Future<Object> future = Patterns.ask(getContext().actorSelection("/user/Courier" + i), "GetInfo", new Timeout(Duration.create(5, TimeUnit.SECONDS)));
                futures.add(future);
            }

            // Combine multiple futures into a single future representing the completion of all of them
            Future<Iterable<Object>> aggregate = Futures.sequence(futures, getContext().system().dispatcher());

            // Process the responses asynchronously
            aggregate.onComplete(response -> {
                if (response.isSuccess()) {
                    Iterable<Object> responses = response.get();

                    // Filter ActorB instances whose prices are in range of ActorA number
                    List<ActorInfo> actorInfosInRange = new ArrayList<>();
                    for (Object resp : responses) {
                        if (resp instanceof ActorInfo) {
                            ActorInfo actorInfo = (ActorInfo) resp;
                            System.out.println(name + ": Courier name: " + actorInfo.getName() + " Coordinates: (" + actorInfo.getCoordinates().getX() +
                                            ", " + actorInfo.getCoordinates().getY() + ")" +
                                    ", Courier price: " + actorInfo.getPrice() +
                                    ", Order number: " + randomNumber + "/ Order coords: " + coordinates.getX()+":"+coordinates.getY());

                            if (actorInfo.getPrice() >= randomNumber) {
                                actorInfosInRange.add(actorInfo);
                            }
                        }
                    }

                    // Choose the ActorB with the minimum price within the range
                    if (!actorInfosInRange.isEmpty()) {
                        ActorInfo cheapestActor = actorInfosInRange.stream()
                                .min(Comparator.comparingInt(ActorInfo::getPrice))
                                .orElse(null);

                        if (cheapestActor != null) {
                            System.out.println(name + ": Cheapest Courier: " + cheapestActor.getName() +
                                    ", Price: " + cheapestActor.getPrice() +
                                    ", Coordinates: (" + cheapestActor.getCoordinates().getX() +
                                    ", " + cheapestActor.getCoordinates().getY() + ")");
                            coordinator.tell(cheapestActor, getSelf());  // Send the chosen ActorB to the Coordinator
                        }
                    } else {
                        System.out.println(name + ": No Courier within the specified price range.");
                    }

                    System.out.println(name + ": All Courier responses received by " + name);
                } else {
                    System.out.println(name + ": Failed to get Courier responses.");
                }
                return null;
            }, getContext().system().dispatcher());
        } else {
            unhandled(message);
        }
    }
}