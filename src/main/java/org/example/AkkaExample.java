package org.example;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class AkkaExample {

    public static void main(String[] args) {
        // Create the ActorSystem
        ActorSystem system = ActorSystem.create("AkkaExampleSystem");

        // Create the CoordinatorActor
        ActorRef coordinator = system.actorOf(Props.create(CoordinatorActor.class), "coordinator");

        // Create and start multiple instances of ActorA with different coordinates
        for (int i = 1; i <= 2; i++) {
            Coordinates fromCoordinates = new Coordinates(i * 10, 0);
            Coordinates toCoordinates = new Coordinates((i + 1) * 10, 0);
            ActorRef actorA = system.actorOf(Props.create(ActorA.class, coordinator, "Order" + i, fromCoordinates, toCoordinates), "Order" + i);

            // Send a start message to each ActorA instance
            actorA.tell("Start", ActorRef.noSender());
        }

        // Create multiple instances of ActorB with different coordinates
        for (int i = 1; i <= 3; i++) {
            Coordinates courierCoordinates = new Coordinates(i * 5, 0);
            system.actorOf(Props.create(ActorB.class, courierCoordinates), "Courier" + i);
        }
    }
}