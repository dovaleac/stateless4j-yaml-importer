package com.events;

import com.phone.nongenerated.PhoneEvent;

import java.util.ArrayList;
import java.util.List;

public class EventLog {
  private static final List<PhoneEvent> PHONE_EVENTS = new ArrayList<>();

  public static void addEvent(String trigger, String state, String producer) {
    PhoneEvent phoneEvent = new PhoneEvent(producer, trigger, state);
    //phoneEvent.print();
    PHONE_EVENTS.add(phoneEvent);
  }

  public static List<PhoneEvent> getPhoneEvents() {
    return PHONE_EVENTS;
  }

  public static void restart() {
    PHONE_EVENTS.clear();
  }
}
