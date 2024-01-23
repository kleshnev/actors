package org.example;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;

import java.util.concurrent.CompletionStage;

public class AkkaExample {


    public static void main(String[] args) {
        // Create the ActorSystem
        ActorSystem system = ActorSystem.create("AkkaExampleSystem");
        int courCount = 3;

        // Create the CoordinatorActor
        ActorRef coordinator = system.actorOf(Props.create(CoordinatorActor.class), "coordinator");

        // Create multiple instances of ActorB with different coordinates
        for (int i = 1; i <= courCount; i++) {
            Coordinates courierCoordinates = new Coordinates(i * 5, 0);
            system.actorOf(Props.create(ActorB.class, courierCoordinates, i*60), "Courier" + i);
        }
        // Create and start multiple instances of ActorA with different coordinates
        for (int i = 1; i <= 6; i++) {
            Coordinates fromCoordinates = new Coordinates(i * 10, 0);
            Coordinates toCoordinates = new Coordinates((i + 1) * 10, 0);
            ActorRef actorA = system.actorOf(Props.create(ActorA.class, coordinator, "Order" + i, fromCoordinates, toCoordinates, i*30 , i*50 ,courCount ), "Order" + i);

            // Send a start message to each ActorA instance
            actorA.tell("Start", ActorRef.noSender());
        }
    }
}