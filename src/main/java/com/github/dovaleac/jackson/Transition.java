package com.github.dovaleac.jackson;

import java.util.List;

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
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Transition that = (Transition) o;

    if (from != null ? !from.equals(that.from) : that.from != null) return false;
    if (to != null ? !to.equals(that.to) : that.to != null) return false;
    if (trigger != null ? !trigger.equals(that.trigger) : that.trigger != null) return false;
    return params != null ? params.equals(that.params) : that.params == null;
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
    return "Transition{" +
        "from='" + from + '\'' +
        ", to='" + to + '\'' +
        ", trigger='" + trigger + '\'' +
        ", params=" + params +
        '}';
  }
}
