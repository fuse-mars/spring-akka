package spendingManager.FSM.akka.base;

import java.util.Map;

import akka.actor.UntypedActor;
/**
 * This is a modified version from 
 * <link href="http://doc.akka.io/docs/akka/snapshot/java/fsm.html">Akka website</link>
 * @author mars
 *
 */
public abstract class GreetingBase extends UntypedActor {

	/*
	 * This is the mutable state of this state machine.
	 */
	protected enum State {HELLO, GOODBYE};
	
	//--mars
	protected Map<State, Object> stateHolder = null;
	
	private State state = null; //default is null, should be State.HELLO

	/*
	 * Then come all the mutator methods:
	 */
	protected void init(Map<State, Object> stateHolder) {
		if(!isInitialized()){
			// each state has values for all instance varibles of the actor
			// these values are unique to a state
			// a previous state can send its values to the current one
			this.stateHolder = stateHolder;
			this.state = State.HELLO;
		}
	}

	protected void setState(State s) {
		if (state != s) {
			transition(state, s);
			state = s;
		}
	}

	/*
	 * Here are the interrogation methods:
	 */
	protected boolean isInitialized() {
		return stateHolder != null;
	}

	protected State getState() {
		return state;
	}
	
	protected Map<State, Object> getStateHolder() {
		if (stateHolder == null)
			throw new IllegalStateException("getTarget(): not yet initialized");
		return stateHolder;
	}
	
	protected Object getStateData(State state) {
		return stateHolder.get(state);
	}

	/*
	 * And finally the callbacks (only one in this example: react to state
	 * change)
	 */
	abstract protected void transition(State old, State next);

}