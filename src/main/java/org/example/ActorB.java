package org.example;

import akka.actor.UntypedAbstractActor;

import java.util.Random;

class ActorB extends UntypedAbstractActor {

    private int price;
    private String name;

    @Override
    public void preStart() {
        this.price = new Random().nextInt(100);
        this.name = "ActorB" + getSelf().path().name();
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof String) {
            String command = (String) message;
            if (command.equals("GetInfo")) {
                getSender().tell(new ActorInfo(name, price), getSelf());
            } else {
                unhandled(message);
            }
        }
    }
}