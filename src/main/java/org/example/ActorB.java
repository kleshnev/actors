package org.example;

import akka.actor.UntypedAbstractActor;

public class ActorB extends UntypedAbstractActor {

    private int price;
    private String name;
    private Coordinates coordinates;

    public ActorB(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public void preStart() {
        this.price =  new java.util.Random().nextInt(100);
        this.name = "ActorB" + getSelf().path().name();
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof String) {
            String command = (String) message;
            if (command.equals("GetInfo")) {
                getSender().tell(new ActorInfo(name, price, coordinates), getSelf());
            } else {
                unhandled(message);
            }
        }
    }
}
