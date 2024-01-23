package org.example;


import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.UntypedAbstractActor;
import akka.dispatch.Futures;
import akka.dispatch.OnComplete;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

class CoordinatorActor extends UntypedAbstractActor {

    private List<ActorInfo> allResponses = new ArrayList<>();

    @Override
    public void onReceive(Object message) {
        if (message instanceof ActorInfo) {
            ActorInfo response = (ActorInfo) message;
            allResponses.add(response);

            // Check if responses from all ActorA instances have been received
            if (allResponses.size() == 6 /* total number of ActorA instances */) {
                // Process all responses here
                for (ActorInfo actorInfo : allResponses) {
                    System.out.println("Coordinator received: " +
                            "Courier name: " + actorInfo.getName() +
                            ", Price: " + actorInfo.getPrice());
                }
                List<Future<Object>> futures = new ArrayList<>();
                PrintRequest request = new PrintRequest("getOrderSequence");
                for (int i = 0; i <allResponses.size() ; i++) {
                    Future<Object> future = Patterns.ask(
                            getContext().actorSelection("/user/Courier" + i),
                            request,
                            new Timeout(Duration.create(5, TimeUnit.SECONDS))
                    );
                    futures.add(future);

                }
                Future<Iterable<Object>> aggregate = Futures.sequence(futures, getContext().system().dispatcher());
                aggregate.onComplete(new OnComplete<Iterable<Object>>() {
                    @Override
                    public void onComplete(Throwable failure, Iterable<Object> success) {
                        if (failure == null) {
                            // Process the aggregated results
                            for (Object result : success) {
                                if (result instanceof ActorResult) {
                                    ActorResult actorResult = (ActorResult) result;
                                    System.out.println("Courier " + actorResult.getName() + " Order Sequence: " + actorResult.getOrderSequence());
                                }
                            }
                        } else {
                            System.err.println("Failed to aggregate results: " + failure.getMessage());
                        }
                    }
                }, getContext().system().dispatcher());

                allResponses.clear();
            }
        } else {
            unhandled(message);
        }
    }


}