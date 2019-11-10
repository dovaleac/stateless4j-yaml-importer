package com.phone.nongenerated;

import com.events.EventLog;
import com.phone.generated.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class PhoneTest {
  public static final String MICHAEL = "michael";
  public static final String ANNA = "anna";
  public static final String JANE = "jane";
  public static final String PROPAGANDA = "propaganda";
  Phone michael;
  Phone anna;
  Phone jane;
  Phone propaganda;

  @BeforeEach
  void setUp() {
    michael = new Phone(State.IDLE, false, MICHAEL);
    anna = new Phone(State.IDLE, false, ANNA);
    jane = new Phone(State.SPEAKING, false, JANE);
    propaganda = new Phone(State.IDLE, true, PROPAGANDA);

    EventLog.restart();
  }

  @Test
  void testNormalCall() {
    List<PhoneEvent> expected = List.of(
        new PhoneEvent(MICHAEL, "Call", "CALLING"),
        new PhoneEvent(ANNA, "ReceiveCall", "CALL_ENTERING"),
        new PhoneEvent(ANNA, "CallEntered", "RINGING"),
        new PhoneEvent(ANNA, "AcceptCall", "SPEAKING"),
        new PhoneEvent(MICHAEL, "CallAccepted", "SPEAKING")
    );
    michael.fireCall(anna);
    assertIterableEquals(expected, EventLog.getPhoneEvents());
  }

  @Test
  void testRejectCall() {
    List<PhoneEvent> expected = List.of(
        new PhoneEvent(PROPAGANDA, "Call", "CALLING"),
        new PhoneEvent(ANNA, "ReceiveCall", "CALL_ENTERING"),
        new PhoneEvent(ANNA, "CallEntered", "RINGING"),
        new PhoneEvent(ANNA, "RejectCall", "IDLE"),
        new PhoneEvent(PROPAGANDA, "CallRejected", "IDLE")
    );
    propaganda.fireCall(anna);
    assertIterableEquals(expected, EventLog.getPhoneEvents());
  }

  @Test
  void testBusyCall() {
    List<PhoneEvent> expected = List.of(
        new PhoneEvent(MICHAEL, "Call", "CALLING"),
        new PhoneEvent(JANE, "ReceiveCall", "SPEAKING"),
        new PhoneEvent(MICHAEL, "ReceiverIsBusy", "IDLE")
    );
    michael.fireCall(jane);
    assertIterableEquals(expected, EventLog.getPhoneEvents());
  }

}