package com.github.dovaleac.jackson;

import java.util.List;
import java.util.Objects;

public class TriggerWithParameters {
  private String trigger;
  private List<Param> params;

  public TriggerWithParameters() {
  }

  public TriggerWithParameters(String trigger, List<Param> params) {
    this.trigger = trigger;
    this.params = params;
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

    TriggerWithParameters that = (TriggerWithParameters) obj;

    if (!Objects.equals(trigger, that.trigger)) {
      return false;
    }
    return Objects.equals(params, that.params);
  }

  @Override
  public int hashCode() {
    int result = trigger != null ? trigger.hashCode() : 0;
    result = 31 * result + (params != null ? params.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "TriggerWithParameters{"
        + "trigger='" + trigger + '\''
        + ", params=" + params
        + '}';
  }
}
