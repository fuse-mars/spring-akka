package spendingManager.FSM.akka;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.Procedure;

/**
 * Trying to map this to FSM implementation
 * FSM : State(S) x Event(E) -> Actions (A), State(S')
 *   If we are in state S and the event E occurs, we should perform the actions A and make a transition to the state S'.
 *
 * “We would argue that behavior is role-based”
 * Excerpt From: Duncan K. DeVore. “Reactive Application Development MEAP V03.” iBooks. 
 *
 * In our example below:
 * We have "hello" and "goodbye" <b>States</b>
 * We also have "ImmutableHello" and "ImmutableGoodbye" <b>Events</b>
 * 
 * Initially: S = hello
 * It receives E = ImmutableHello
 * Which produces A = HELLOA, S' = goodbye
 * In short
 * State(hello) x Event(ImmutableHello) -> Actions(HELLOA), State(goodbye)
 * 
 * 
 * Note: as far as CQRS terms, we are now treating commands as events
 * @author mars
 *
 */
public class Greeting extends UntypedActor {

	LoggingAdapter log = Logging.getLogger(getContext().system(), this);
	
	//hello state (formally called role)
	Procedure<Object> hello = new Procedure<Object>() {

		@Override
		public void apply(Object message) {

			if(message instanceof ImmutableHello) {
				
				//-- action performed HELLOA
				log.info("Received Hello: {}", message);
				ImmutableHello ih = new ImmutableHello("Greetings Hello");
				
				getSender().tell(ih, getSelf());
				//-- end of action
				
				// changing the state to greet now
				getContext().become(goodbye);
				
			}
			
		}
		
	}; 
	
	Procedure<Object> goodbye = new Procedure<Object>() {
		
		@Override
		public void apply(Object message) {

			if(message instanceof ImmutableGoodbye) {
				
				//-- action performed GOODBYEA
				log.info("Received Goodbye: {}", message);
				ImmutableGoodbye ig = new ImmutableGoodbye("Greetings goodbye");
				
				getSender().tell(ig, getSelf());
				//-- end of action
				
				// changing the state to greet now
				getContext().become(hello);
				
			} else {
				unhandled(message);
			}
			
		}
	};
	
	/*
	 * “The GreetingActor will start in a role where it only accepts Hello messages.”
	 * Excerpt From: Duncan K. DeVore. “Reactive Application Development MEAP V03.” iBooks. 
	 * @see akka.actor.UntypedActor#onReceive(java.lang.Object)
	 */
	@Override
	public void onReceive(Object arg0) throws Exception {
		getContext().become(hello);
		
	}

}
