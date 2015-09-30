package spendingManager.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.FiniteDuration;
import spendingManager.akka.AkkaFactory;
import spendingManager.akka.ReadWorker;
import spendingManager.akka.WriteWorker;
import spendingManager.domain.Spending;
import akka.actor.ActorRef;
import akka.pattern.Patterns;
import akka.util.Timeout;

@RestController
public class SpendingController {

    private final AtomicLong counter = new AtomicLong();
    private ActorRef writeWorker = AkkaFactory.getActorSystem()
                .actorOf(WriteWorker.createWorker(), "writeWorker");

    private ActorRef readWorker = AkkaFactory.getActorSystem()
                .actorOf(ReadWorker.createWorker(), "readWorker");

    @RequestMapping("api/expenses/write")
    public Map<String, String> recordExpense(
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
        Map<String, String> response = new HashMap<String, String>();
        response.put("id", Long.toString(count));
        response.put("message", "Entry has been received and is being saved");
        return response;
    }

    @RequestMapping("api/expenses/read")
    public Spending recordExpense(
        @RequestParam(value="id") long id
        ) throws Exception {

        System.out.println("read route called");

        //blocking call uses ask
        FiniteDuration duration = FiniteDuration.apply(10, "seconds");
        
        Future<Object> answer = Patterns
        		.ask(readWorker, new Long(id), 
        				Timeout.durationToTimeout(duration));
        Object result = Await.result(answer, duration);
        //this would usually be a push request because
        // this function would have to wait for a notification from "readWorker"
        // saying that it has finished writing the value.
        return (Spending) result;
    }
}