package spendingManager.akka;

import akka.actor.ActorSystem;
import akka.actor.ActorRef;

/**
 *
 */
public class AkkaFactory {

    private static ActorSystem system = null;

    public static ActorSystem getActorSystem() {

        if(system == null) {
            system = ActorSystem.create("CalcSystem");
        }

        return system;

	}
}