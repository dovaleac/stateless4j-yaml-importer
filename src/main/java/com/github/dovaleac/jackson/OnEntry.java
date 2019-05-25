package com.github.dovaleac.jackson;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class OnEntry {
  private String name;
  @JsonProperty(required = false)
  private String from;

  public OnEntry() {
  }

  public OnEntry(String name, String from) {
    this.name = name;
    this.from = from;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    OnEntry onEntry = (OnEntry) obj;

    if (!Objects.equals(name, onEntry.name)) {
      return false;
    }
    return Objects.equals(from, onEntry.from);
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (from != null ? from.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "OnEntry{"
        + "name='" + name + '\''
        + ", from='" + from + '\''
        + '}';
  }
}
