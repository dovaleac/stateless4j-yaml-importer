package com.github.dovaleac.jackson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class State {
  private String name;
  private List<OnEntry> onEntry = new ArrayList<>();
  private List<String> onExit = new ArrayList<>();
  private List<String> ignore = new ArrayList<>();
  private String superState;
  private boolean isInitial = false;

  public State() {
  }

  public State(String name, List<OnEntry> onEntry, List<String> onExit,
      List<String> ignore, String superState, boolean isInitial) {
    this.name = name;
    this.onEntry = onEntry;
    this.onExit = onExit;
    this.ignore = ignore;
    this.superState = superState;
    this.isInitial = isInitial;
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

  public List<String> getIgnore() {
    return ignore;
  }

  public void setIgnore(List<String> ignore) {
    this.ignore = ignore;
  }

  public String getSuperState() {
    return superState;
  }

  public void setSuperState(String superState) {
    this.superState = superState;
  }

  public boolean isInitial() {
    return isInitial;
  }

  public void setInitial(boolean initial) {
    isInitial = initial;
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

    if (isInitial != state.isInitial) {
      return false;
    }
    if (!Objects.equals(name, state.name)) {
      return false;
    }
    if (!Objects.equals(onEntry, state.onEntry)) {
      return false;
    }
    if (!Objects.equals(onExit, state.onExit)) {
      return false;
    }
    if (!Objects.equals(ignore, state.ignore)) {
      return false;
    }
    return Objects.equals(superState, state.superState);
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (onEntry != null ? onEntry.hashCode() : 0);
    result = 31 * result + (onExit != null ? onExit.hashCode() : 0);
    result = 31 * result + (ignore != null ? ignore.hashCode() : 0);
    result = 31 * result + (superState != null ? superState.hashCode() : 0);
    result = 31 * result + (isInitial ? 1 : 0);
    return result;
  }

  @Override
  public String toString() {
    return "State{"
        + "name='" + name + '\''
        + ", onEntry=" + onEntry
        + ", onExit=" + onExit
        + ", ignore=" + ignore
        + ", superState='" + superState + '\''
        + ", isInitial=" + isInitial
        + '}';
  }
}
