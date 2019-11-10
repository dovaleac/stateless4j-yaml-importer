package com.phone.nongenerated;

import com.events.EventLog;
import com.phone.generated.AbstractPhone;
import com.phone.generated.State;
import com.phone.generated.Trigger;

import java.util.function.Consumer;

public class Phone extends AbstractPhone {

  private final boolean isPropaganda;
  private final String name;

  public Phone(State initialState, boolean isPropaganda, String name) {
    super(initialState);
    this.isPropaganda = isPropaganda;
    this.name = name;
  }

  @Override
  public void log(Trigger trigger, State state) {
    System.out.print(name + " says: ");
    EventLog.addEvent(trigger.name(), state.name(), name);
  }

  @Override
  public void notifyCallIsRejected(AbstractPhone caller) {
    System.out.println("I'm going to reject call by " + caller);
    applyConsumerToPhone(caller, AbstractPhone::fireCallRejected);
  }

  @Override
  public void notifyReceiverIsBusy(AbstractPhone caller) {
    System.out.println("I'm busy, sorry " + caller);
    applyConsumerToPhone(caller, AbstractPhone::fireReceiverIsBusy);
  }

  @Override
  public void startRinging(AbstractPhone caller) {
    System.out.println("I'm ringing, as I'm getting called by " + caller);
    fireCallEntered(caller);
  }

  @Override
  public void shouldICatchIt(AbstractPhone caller) {
    System.out.println("I'm deciding whether I should catch call by " + caller);
    applyConsumerToPhone(caller, phone -> {
      if (phone.isPropaganda) {
        fireRejectCall(caller);
      } else {
        fireAcceptCall(caller);
      }
    });
  }

  @Override
  public void callReceiver(AbstractPhone receiver) {
    System.out.println("I'm calling " + receiver);
    applyConsumerToPhone(receiver, phone -> phone.fireReceiveCall(this));
  }

  @Override
  public void notifyCallIsAccepted(AbstractPhone caller) {
    System.out.println("I'm accepting call by " + caller);
    applyConsumerToPhone(caller, AbstractPhone::fireCallAccepted);
  }

  private void applyConsumerToPhone(AbstractPhone otherPhone, Consumer<Phone> consumer) {
    if (otherPhone != null) {
      consumer.accept((Phone) otherPhone);
    }
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name;
  }
}
