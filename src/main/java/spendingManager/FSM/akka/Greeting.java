package spendingManager.FSM.akka;

import java.util.HashMap;
import java.util.Map;

import scala.collection.mutable.ArraySeq;
import spendingManager.FSM.ImmutableGoodbye;
import spendingManager.FSM.ImmutableHello;
import spendingManager.FSM.akka.base.GreetingBase;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Trying to map this to FSM implementation FSM : State(S) x Event(E) -> Actions
 * (A), State(S') If we are in state S and the event E occurs, we should perform
 * the actions A and make a transition to the state S'.
 *
 * “We would argue that behavior is role-based” Excerpt From: Duncan K. DeVore.
 * “Reactive Application Development MEAP V03.” iBooks.
 *
 * In our example below: We have "hello" and "goodbye" <b>States</b> We also
 * have "ImmutableHello" and "ImmutableGoodbye" <b>Events</b>
 * 
 * Initially: S = hello It receives E = ImmutableHello Which produces A =
 * HELLOA, S' = goodbye In short State(hello) x Event(ImmutableHello) ->
 * Actions(HELLOA), State(goodbye)
 * 
 * 
 * Note: as far as CQRS terms, we are now treating commands as events
 * 
 * @author mars
 *
 */
public class Greeting extends GreetingBase {

	private final LoggingAdapter log = Logging.getLogger(getContext().system(),
			this);

	@Override
	public void onReceive(Object message) {

		if (getState() == State.HELLO) {

			if (message instanceof ImmutableHello) {

				StateData stateData = (StateData) getStateData(getState());

				// -- action performed HELLOA
				stateData.countMessages += 1;
				log.info("[hello - count = {}] Received Hello: {}",
						stateData.countMessages,
						((ImmutableHello) message).name);
				ImmutableHello ih = new ImmutableHello("Greetings Hello");

				getSender().tell(ih, getSelf());
				// -- end of action

				// changing the state to greet now
				setState(State.GOODBYE);

			} else {
				System.err.printf("[hello - unhandled] - message: %s \n",
						message.toString());
				unhandled(message);
			}

		} else if (getState() == State.GOODBYE) {

			if (message instanceof ImmutableGoodbye) {

				// -- action performed GOODBYEA
				StateData stateData = (StateData) getStateData(getState());
				stateData.countMessages += 10;
				log.info("[goodbye - count = {}] Received Goodbye: {}",
						stateData.countMessages,
						((ImmutableGoodbye) message).name);
				ImmutableGoodbye ig = new ImmutableGoodbye("Greetings goodbye");

				getSender().tell(ig, getSelf());
				// -- end of action

				// changing the state to greet now
				setState(State.HELLO);

			} else {
				System.err.printf("[goodbye - unhandled] - message: %s \n",
						message.toString());
				unhandled(message);
			}

		} else if (!isInitialized()) {

			Map<State, Object> stateHolder = new HashMap<State, Object>();
			stateHolder.put(State.HELLO, new StateData());
			stateHolder.put(State.GOODBYE, new StateData());
			init(stateHolder);
		} else {
			log.warning("received unknown message {} in state {}", message,
					getState());
			unhandled(message);
		}
	}

	@Override
	public void transition(State old, State next) {
		
		// here you perform any required operation before moving to the next state
		// nothing done in here for now
	}

	
    public static Props createWorker() {
        return Props.create(Greeting.class, new ArraySeq<Object>(0));
    }
	
	
	/**
	 * this class will be in charge of encapsulating the mutable variables for
	 * each state that this greeting actor can be in
	 * 
	 * @author mars
	 *
	 */
	class StateData {
		public int countMessages = 0;
	}
}
