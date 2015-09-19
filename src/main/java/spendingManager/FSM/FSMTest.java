package spendingManager.FSM;

import spendingManager.FSM.akka.Greeting;
import spendingManager.FSM.akka.ImmutableGoodbye;
import spendingManager.FSM.akka.ImmutableHello;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class FSMTest {

	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create("FSMSystem");
		ActorRef greetingActor = system.actorOf(Greeting.createWorker(), "Greeting");
		
		// this command will put greetingActor in the "hello" state
		greetingActor.tell(
				 	new Object(),
                	ActorRef.noSender()
            );
		
		 
		 greetingActor.tell(
				 new ImmutableHello("Hi there success-1"),
                	ActorRef.noSender()
            );
		 // At this point greetingActor will respond to goodbye commands
		 greetingActor.tell(
				 new ImmutableHello("Hi there error-1"),
                	ActorRef.noSender()
            );
		 greetingActor.tell(
				 new ImmutableHello("Hi there error-2"),
                	ActorRef.noSender()
            );
		 greetingActor.tell(
				 new ImmutableHello("Hi there error-3"),
                	ActorRef.noSender()
            );

		 
		 greetingActor.tell(
				 new ImmutableGoodbye("good bye success-1"),
                	ActorRef.noSender()
            );
		// At this point greetingActor will respond to hello commands
		 
		 //here we test the messageCounter variables
		 greetingActor.tell(
				 new ImmutableHello("Hi there success-2"),
                	ActorRef.noSender()
            );
		 greetingActor.tell(
				 new ImmutableGoodbye("good bye success-2"),
                	ActorRef.noSender()
            );
		 greetingActor.tell(
				 new ImmutableHello("Hi there success-3"),
                	ActorRef.noSender()
            );
//		 system.shutdown();
	}

}
