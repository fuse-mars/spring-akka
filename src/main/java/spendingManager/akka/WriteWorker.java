package spendingManager.akka;

import akka.actor.Props;
import akka.actor.UntypedActor;
import scala.collection.mutable.ArraySeq;

import spendingManager.domain.Spending;

/**
 * In charge of factorial calculation 
 * The result is to the parent (Master object)
 */
public class WriteWorker extends UntypedActor {

    @Override
    public void onReceive(Object message) {
        if (message instanceof Spending) {
        
            //1. accomplish the task in hand   
            
            //in this example, we save food and amount to db (list)
            Database.write( (Spending) message );
            
            //2. After the task is "completely" done,
            //   send notification to kafka
            System.out.println("[ WriteWorker ] done writing to db");
            // getSender().tell(new Result(bigInt), getSelf());
        
        } else
            unhandled(message);
    }

    public static Props createWorker() {
        return Props.create(WriteWorker.class, new ArraySeq<Object>(0));
    }
}