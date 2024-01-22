package org.example;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class AkkaExample {

    public static void main(String[] args) {
        // Create the ActorSystem
        ActorSystem system = ActorSystem.create("AkkaExampleSystem");

        // Create the CoordinatorActor
        ActorRef coordinator = system.actorOf(Props.create(CoordinatorActor.class), "coordinator");

        // Create and start multiple instances of ActorA
        for (int i = 1; i <= 3; i++) {
            ActorRef actorA = system.actorOf(Props.create(ActorA.class, coordinator, "ActorA" + i), "actorA" + i);

            // Send a start message to each ActorA instance
            actorA.tell("Start", ActorRef.noSender());
        }

        // Create multiple instances of ActorB
        for (int i = 1; i <= 10; i++) {
            system.actorOf(Props.create(ActorB.class), "actorB" + i);
        }
    }
}