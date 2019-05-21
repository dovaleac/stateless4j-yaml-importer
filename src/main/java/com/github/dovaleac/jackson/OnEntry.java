package com.github.dovaleac.jackson;

import java.util.Objects;

public class OnEntry {
  private String name;

  public OnEntry() {
  }

  public OnEntry(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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

    return Objects.equals(name, onEntry.name);
  }

  @Override
  public int hashCode() {
    return name != null ? name.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "OnEntry{"
        + "name='" + name + '\''
        + '}';
  }
}
