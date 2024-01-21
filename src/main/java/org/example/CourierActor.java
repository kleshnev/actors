package org.example;

import akka.actor.AbstractActor;
import akka.actor.Props;

import java.util.ArrayList;
import java.util.List;

class CourierActor extends AbstractActor {
    private Coordinates position;
    private List<Order> orders = new ArrayList<>();

    public CourierActor(Coordinates position) {
        this.position = position;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, message -> {
                    if (message.equals("hello")) {
                        System.out.println("Courier at position " + position + " says: Hello!");
                    } else if (message.equals("getOrders")) {
                        getSender().tell(new ArrayList<>(orders), getSelf());
                    }
                })
                .match(Order.class, order -> {
                    addOrder(order);
                    // Send acknowledgment to OrderActor directly
                    getContext().getParent().tell("ack", getSelf());
                })
                .build();
    }

    private void addOrder(Order order) {
        orders.add(order);
        // Simulating order processing
        System.out.println("Courier at position " + position + " received order: " + order);
    }

    public static Props props(Coordinates position) {
        return Props.create(CourierActor.class, () -> new CourierActor(position));
    }
}


