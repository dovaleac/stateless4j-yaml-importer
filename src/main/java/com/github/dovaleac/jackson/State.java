package com.github.dovaleac.jackson;

import java.util.List;

public class State {
  private String name;
  private List<OnEntry> onEntry;
  private List<String> onExit;

  public State() {
  }

  public State(String name, List<OnEntry> onEntry, List<String> onExit) {
    this.name = name;
    this.onEntry = onEntry;
    this.onExit = onExit;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    State state = (State) o;

    if (name != null ? !name.equals(state.name) : state.name != null) return false;
    if (onEntry != null ? !onEntry.equals(state.onEntry) : state.onEntry != null) return false;
    if (onExit != null ? !onExit.equals(state.onExit) : state.onExit != null) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (onEntry != null ? onEntry.hashCode() : 0);
    result = 31 * result + (onExit != null ? onExit.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "State{" +
        "name='" + name + '\'' +
        ", onEntry=" + onEntry +
        ", onExit=" + onExit +
        '}';
  }
}
