package com.phone.generated;

import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.triggers.TriggerWithParameters1;

public abstract class AbstractPhone {

  protected final StateMachine<State, Trigger> stateMachine;

  public AbstractPhone(State initialState) {
    this.stateMachine = new StateMachine<>(initialState, new PhoneStateMachine().getConfig(this));
  }

  public abstract void notifyCallIsAccepted(AbstractPhone caller);

  public abstract void startRinging(AbstractPhone caller);

  public abstract void notifyReceiverIsBusy(AbstractPhone caller);

  public abstract void notifyCallIsRejected(AbstractPhone caller);

  public abstract void callReceiver(AbstractPhone receiver);

  public abstract void shouldICatchIt(AbstractPhone caller);

  public abstract void log(Trigger trigger, State state);

  public void fireReceiveCall(AbstractPhone caller) {
    this.stateMachine.fire(new TriggerWithParameters1<>(
      Trigger.ReceiveCall, AbstractPhone.class),
      caller);
  }

  public void fireCallEntered(AbstractPhone caller) {
    this.stateMachine.fire(new TriggerWithParameters1<>(
      Trigger.CallEntered, AbstractPhone.class),
      caller);
  }

  public void fireRejectCall(AbstractPhone caller) {
    this.stateMachine.fire(new TriggerWithParameters1<>(
      Trigger.RejectCall, AbstractPhone.class),
      caller);
  }

  public void fireAcceptCall(AbstractPhone caller) {
    this.stateMachine.fire(new TriggerWithParameters1<>(
      Trigger.AcceptCall, AbstractPhone.class),
      caller);
  }

  public void fireFinishCall() {
    this.stateMachine.fire(Trigger.FinishCall);
  }

  public void fireCall(AbstractPhone receiver) {
    this.stateMachine.fire(new TriggerWithParameters1<>(
      Trigger.Call, AbstractPhone.class),
      receiver);
  }

  public void fireCallAccepted() {
    this.stateMachine.fire(Trigger.CallAccepted);
  }

  public void fireCallRejected() {
    this.stateMachine.fire(Trigger.CallRejected);
  }

  public void fireReceiverIsBusy() {
    this.stateMachine.fire(Trigger.ReceiverIsBusy);
  }


}