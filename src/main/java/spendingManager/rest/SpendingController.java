package spendingManager.rest;

import java.util.concurrent.atomic.AtomicLong;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import akka.actor.ActorRef;

import spendingManager.domain.Spending;

import spendingManager.akka.AkkaFactory;
import spendingManager.akka.WriteWorker;
import spendingManager.akka.ReadWorker;

@RestController
public class SpendingController {

    private final AtomicLong counter = new AtomicLong();
    private ActorRef writeWorker = AkkaFactory.getActorSystem()
                .actorOf(WriteWorker.createWorker(), "writeWorker");

    private ActorRef readWorker = AkkaFactory.getActorSystem()
                .actorOf(ReadWorker.createWorker(), "readWorker");

    @RequestMapping("api/expenses/write")
    public Map recordExpense(
        @RequestParam(value="name", defaultValue="Burrito from Chipotle") String name,
        @RequestParam(value="amount") double amount
        ) {

        System.out.println("write route called");


        long count = counter.incrementAndGet();
        // send a command to save this information
        writeWorker.tell(
            new Spending(count, name, amount),
            ActorRef.noSender()
        );

        //return a message showing the user what is being done
        Map<String, String> response = new HashMap();
        response.put("id", Long.toString(count));
        response.put("message", "Entry has been received and is being saved");
        return response;
    }

    @RequestMapping("api/expenses/read")
    public Spending recordExpense(
        @RequestParam(value="id") long id
        ) {

        System.out.println("read route called");

        // send a command to read the requested value
        readWorker.tell(new Long(id), ActorRef.noSender());

        //this would usually be a push request because
        // this function would have to wait for a notification from "readWorker"
        // saying that it has finished writing the value.
        return ReadWorker.getValue();
    }
}