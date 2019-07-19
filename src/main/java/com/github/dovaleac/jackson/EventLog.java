package com.github.dovaleac.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class EventLog {
  @JsonProperty(required = true)
  private String method;

  public EventLog(String method) {
    this.method = method;
  }

  public EventLog() {
  }

  public String getMethod() {
    return method;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    EventLog eventLog = (EventLog) obj;

    return Objects.equals(method, eventLog.method);
  }

  @Override
  public int hashCode() {
    return method != null ? method.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "EventLog{"
        + "method='" + method + '\''
        + '}';
  }
}
