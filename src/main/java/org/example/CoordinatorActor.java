package org.example;


import akka.actor.UntypedAbstractActor;

import java.util.ArrayList;
import java.util.List;

class CoordinatorActor extends UntypedAbstractActor {

    private List<ActorInfo> allResponses = new ArrayList<>();

    @Override
    public void onReceive(Object message) {
        if (message instanceof ActorInfo) {
            ActorInfo response = (ActorInfo) message;
            allResponses.add(response);

            // Check if responses from all ActorA instances have been received
            if (allResponses.size() == 3 /* total number of ActorA instances */) {
                // Process all responses here
                for (ActorInfo actorInfo : allResponses) {
                    System.out.println("Coordinator received: " +
                            "ActorB name: " + actorInfo.getName() +
                            ", Price: " + actorInfo.getPrice());
                }
                // Clear the list for the next round of responses
                allResponses.clear();
            }
        } else {
            unhandled(message);
        }
    }
}