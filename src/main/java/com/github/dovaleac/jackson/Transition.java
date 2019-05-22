package com.github.dovaleac.jackson;

import java.util.Objects;

public class Transition {
  private String from;
  private String to;
  private String trigger;

  public Transition() {
  }

  public Transition(String from, String to, String trigger) {
    this.from = from;
    this.to = to;
    this.trigger = trigger;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public String getTrigger() {
    return trigger;
  }

  public void setTrigger(String trigger) {
    this.trigger = trigger;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    Transition that = (Transition) obj;

    if (!Objects.equals(from, that.from)) {
      return false;
    }
    if (!Objects.equals(to, that.to)) {
      return false;
    }
    return Objects.equals(trigger, that.trigger);
  }

  @Override
  public int hashCode() {
    int result = from != null ? from.hashCode() : 0;
    result = 31 * result + (to != null ? to.hashCode() : 0);
    result = 31 * result + (trigger != null ? trigger.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Transition{"
        + "from='" + from + '\''
        + ", to='" + to + '\''
        + ", trigger='" + trigger + '\''
        + '}';
  }
}
