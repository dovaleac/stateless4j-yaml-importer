package com.github.dovaleac.jackson;

import java.util.List;
import java.util.Objects;

public class Transition {
  private String from;
  private String to;
  private String trigger;
  private List<Param> params;

  public Transition() {
  }

  public Transition(String from, String to, String trigger, List<Param> params) {
    this.from = from;
    this.to = to;
    this.trigger = trigger;
    this.params = params;
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

  public List<Param> getParams() {
    return params;
  }

  public void setParams(List<Param> params) {
    this.params = params;
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
    if (!Objects.equals(trigger, that.trigger)) {
      return false;
    }
    return Objects.equals(params, that.params);
  }

  @Override
  public int hashCode() {
    int result = from != null ? from.hashCode() : 0;
    result = 31 * result + (to != null ? to.hashCode() : 0);
    result = 31 * result + (trigger != null ? trigger.hashCode() : 0);
    result = 31 * result + (params != null ? params.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Transition{"
        + "from='" + from + '\''
        + ", to='" + to + '\''
        + ", trigger='" + trigger + '\''
        + ", params=" + params
        + '}';
  }
}
