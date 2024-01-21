package org.example;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.routing.RoundRobinPool;

import java.util.ArrayList;
import java.util.List;

import static akka.pattern.Patterns.ask;

public class LogisticsSystem {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("LogisticsSystem");

        List<ActorRef> couriers = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Coordinates courierPosition = new Coordinates(i * 10, i * 10);
            ActorRef courier = system.actorOf(CourierActor.props(courierPosition).withRouter(new RoundRobinPool(5)), "courier" + i);
            couriers.add(courier);
        }

        ActorRef orderActor = system.actorOf(OrderActor.props(couriers), "orderActor");
        orderActor.tell("start", ActorRef.noSender());

        // Simulating more orders
        orderActor.tell(new Order(20.0, new Coordinates(5, 5), new Coordinates(15, 15)), ActorRef.noSender());
        orderActor.tell(new Order(15.0, new Coordinates(10, 10), new Coordinates(20, 20)), ActorRef.noSender());

        // Print couriers and their orders
        orderActor.tell("printCouriers", ActorRef.noSender());

        // Shutting down the system after a delay to allow actors to complete their tasks
        system.terminate();
    }
}
