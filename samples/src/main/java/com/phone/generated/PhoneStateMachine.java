package com.phone.generated;

import com.github.oxo42.stateless4j.StateMachineConfig;
import com.github.oxo42.stateless4j.triggers.TriggerWithParameters1;
import java.util.stream.Stream;

public class PhoneStateMachine {

  public StateMachineConfig<State, Trigger> getConfig(AbstractPhone phone) {
    StateMachineConfig<State, Trigger> config =
        new StateMachineConfig<>();


  Stream.of(State.values())
    .forEach(
      state ->
        Stream.of(Trigger.values())
          .forEach(
            trigger ->
              config
                .configure(state)
                .onEntryFrom(
                  trigger,
                  () -> phone.log(trigger, state))));

    config.configure(State.IDLE)
      .permit(Trigger.ReceiveCall, State.CALL_ENTERING)
      .permit(Trigger.Call, State.CALLING)
      .onEntryFrom(
        new TriggerWithParameters1<>(Trigger.RejectCall, AbstractPhone.class),
        (AbstractPhone caller) -> {
        phone.notifyCallIsRejected(caller);
      }, AbstractPhone.class);

    config.configure(State.CALL_ENTERING)
      .permit(Trigger.CallEntered, State.RINGING)
      .onEntryFrom(
        new TriggerWithParameters1<>(Trigger.ReceiveCall, AbstractPhone.class),
        (AbstractPhone caller) -> {
        phone.startRinging(caller);
      }, AbstractPhone.class);

    config.configure(State.RINGING)
      .permitReentry(Trigger.ReceiveCall)
      .permit(Trigger.AcceptCall, State.SPEAKING)
      .permit(Trigger.RejectCall, State.IDLE)
      .onEntryFrom(
        new TriggerWithParameters1<>(Trigger.CallEntered, AbstractPhone.class),
        (AbstractPhone caller) -> {
        phone.shouldICatchIt(caller);
      }, AbstractPhone.class)
      .onEntryFrom(
        new TriggerWithParameters1<>(Trigger.ReceiveCall, AbstractPhone.class),
        (AbstractPhone caller) -> {
        phone.notifyReceiverIsBusy(caller);
      }, AbstractPhone.class);

    config.configure(State.CALLING)
      .permit(Trigger.ReceiverIsBusy, State.IDLE)
      .permit(Trigger.CallRejected, State.IDLE)
      .permit(Trigger.CallAccepted, State.SPEAKING)
      .onEntryFrom(
        new TriggerWithParameters1<>(Trigger.Call, AbstractPhone.class),
        (AbstractPhone receiver) -> {
        phone.callReceiver(receiver);
      }, AbstractPhone.class);

    config.configure(State.SPEAKING)
      .permitReentry(Trigger.ReceiveCall)
      .permit(Trigger.FinishCall, State.IDLE)
      .onEntryFrom(
        new TriggerWithParameters1<>(Trigger.AcceptCall, AbstractPhone.class),
        (AbstractPhone caller) -> {
        phone.notifyCallIsAccepted(caller);
      }, AbstractPhone.class)
      .onEntryFrom(
        new TriggerWithParameters1<>(Trigger.ReceiveCall, AbstractPhone.class),
        (AbstractPhone caller) -> {
        phone.notifyReceiverIsBusy(caller);
      }, AbstractPhone.class);

    return config;
  }
}