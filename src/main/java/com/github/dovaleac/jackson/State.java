package com.github.dovaleac.jackson;

import java.util.List;
import java.util.Objects;

public class State {
  private String name;
  private List<OnEntry> onEntry;
  private List<String> onExit;
  private String superState;

  public State() {
  }

  public State(String name, List<OnEntry> onEntry, List<String> onExit, String superState) {
    this.name = name;
    this.onEntry = onEntry;
    this.onExit = onExit;
    this.superState = superState;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<OnEntry> getOnEntry() {
    return onEntry;
  }

  public void setOnEntry(List<OnEntry> onEntry) {
    this.onEntry = onEntry;
  }

  public List<String> getOnExit() {
    return onExit;
  }

  public void setOnExit(List<String> onExit) {
    this.onExit = onExit;
  }

  public String getSuperState() {
    return superState;
  }

  public void setSuperState(String superState) {
    this.superState = superState;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    State state = (State) obj;

    if (!Objects.equals(name, state.name)) {
      return false;
    }
    if (!Objects.equals(onEntry, state.onEntry)) {
      return false;
    }
    if (!Objects.equals(onExit, state.onExit)) {
      return false;
    }
    return Objects.equals(superState, state.superState);
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (onEntry != null ? onEntry.hashCode() : 0);
    result = 31 * result + (onExit != null ? onExit.hashCode() : 0);
    result = 31 * result + (superState != null ? superState.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "State{"
        + "name='" + name + '\''
        + ", onEntry=" + onEntry
        + ", onExit=" + onExit
        + ", superState='" + superState + '\''
        + '}';
  }
}
