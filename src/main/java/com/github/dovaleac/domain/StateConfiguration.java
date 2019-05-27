package com.github.dovaleac.domain;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StateConfiguration {
  private final String state;
  private final List<Method> onEntry;
  private final List<Method> onExit;
  private final Map<String, String> transitions;

  public StateConfiguration(String state, List<Method> onEntry,
      List<Method> onExit, Map<String, String> transitions) {
    this.state = state;
    this.onEntry = onEntry;
    this.onExit = onExit;
    this.transitions = transitions;
  }

  public String getState() {
    return state;
  }

  public List<Method> getOnEntry() {
    return onEntry;
  }

  public List<Method> getOnExit() {
    return onExit;
  }

  public Map<String, String> getTransitions() {
    return transitions;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    StateConfiguration that = (StateConfiguration) obj;

    if (!Objects.equals(state, that.state)) {
      return false;
    }
    if (!Objects.equals(onEntry, that.onEntry)) {
      return false;
    }
    if (!Objects.equals(onExit, that.onExit)) {
      return false;
    }
    return Objects.equals(transitions, that.transitions);
  }

  @Override
  public int hashCode() {
    int result = state != null ? state.hashCode() : 0;
    result = 31 * result + (onEntry != null ? onEntry.hashCode() : 0);
    result = 31 * result + (onExit != null ? onExit.hashCode() : 0);
    result = 31 * result + (transitions != null ? transitions.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "StateConfiguration{"
        + "state='" + state + '\''
        + ", onEntry=" + onEntry
        + ", onExit=" + onExit
        + ", transitions=" + transitions
        + '}';
  }
}
