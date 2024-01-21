package org.example;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

class OrderActor extends AbstractActor {
    private List<ActorRef> couriers;
    private Random random = new Random();

    public OrderActor(List<ActorRef> couriers) {
        this.couriers = couriers;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, message -> {
                    if (message.equals("start")) {
                        askCouriers();
                    } else if (message.equals("printCouriers")) {
                        printCouriers();
                    }
                })
                .match(Order.class, order -> {
                    ActorRef randomCourier = couriers.get(random.nextInt(couriers.size()));
                    randomCourier.tell(order, getSelf());
                })
                .matchEquals("ack", ack -> {
                    // Acknowledgment from courier
                    System.out.println("Order assigned to courier: " + getSender().path());
                })
                .build();
    }

    private void askCouriers() {
        for (ActorRef courier : couriers) {
            courier.tell("hello", getSelf());
        }
    }

    private void printCouriers() {
        for (ActorRef courier : couriers) {
            courier.tell("getOrders", getSelf());
        }
    }

    public static Props props(List<ActorRef> couriers) {
        return Props.create(OrderActor.class, () -> new OrderActor(couriers));
    }
}


