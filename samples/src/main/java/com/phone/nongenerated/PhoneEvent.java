package com.phone.nongenerated;

import com.events.Event;

import java.util.Objects;

public class PhoneEvent implements Event {
  private final String producer;
  private final String trigger;
  private final String state;

  public PhoneEvent(String producer, String trigger, String state) {
    this.producer = producer;
    this.trigger = trigger;
    this.state = state;
  }

  @Override
  public void print() {
    System.out.println(toString());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    PhoneEvent phoneEvent = (PhoneEvent) obj;

    if (!Objects.equals(producer, phoneEvent.producer)) {
      return false;
    }
    if (!Objects.equals(trigger, phoneEvent.trigger)) {
      return false;
    }
    return Objects.equals(state, phoneEvent.state);
  }

  @Override
  public int hashCode() {
    int result = producer != null ? producer.hashCode() : 0;
    result = 31 * result + (trigger != null ? trigger.hashCode() : 0);
    result = 31 * result + (state != null ? state.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "PhoneEvent{"
        + "producer='" + producer + '\''
        + ", trigger='" + trigger + '\''
        + ", state='" + state + '\''
        + '}';
  }
}
