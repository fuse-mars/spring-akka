package spendingManager.akka;

import akka.actor.Props;
import akka.actor.UntypedActor;
import scala.collection.mutable.ArraySeq;

import spendingManager.domain.Spending;

/**
 * In charge of factorial calculation 
 * The result is to the parent (Master object)
 */
public class ReadWorker extends UntypedActor {

    private static Spending spending = null;
    public static Spending getValue() {
        return spending;
    }

    @Override
    public void onReceive(Object message) {
        if (message instanceof Long) {
        
            //1. accomplish the task in hand   
            
            //in this example, we read the requested value 
            // saves it into a static variable
            ReadWorker.spending = Database.read((Long) message);
            
            //2. After the task is "completely" done,
            //   send notification to kafka
            System.out.println("[ ReadWorker ] done reading from db");
            // getSender().tell(new Result(bigInt), getSelf());
        
        } else
            unhandled(message);
    }

    public static Props createWorker() {
        return Props.create(ReadWorker.class, new ArraySeq<Object>(0));
    }
}