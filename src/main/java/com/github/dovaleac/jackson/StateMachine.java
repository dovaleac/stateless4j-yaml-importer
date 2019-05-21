package com.github.dovaleac.jackson;

import java.util.List;
import java.util.Objects;

public class StateMachine {
  private String className;
  private String delegateInterfaceName;
  private States states;
  private List<Transition> transitions;

  public StateMachine() {
  }

  public StateMachine(String className, String delegateInterfaceName,
      States states,
      List<Transition> transitions) {
    this.className = className;
    this.delegateInterfaceName = delegateInterfaceName;
    this.states = states;
    this.transitions = transitions;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getDelegateInterfaceName() {
    return delegateInterfaceName;
  }

  public void setDelegateInterfaceName(String delegateInterfaceName) {
    this.delegateInterfaceName = delegateInterfaceName;
  }

  public States getStates() {
    return states;
  }

  public void setStates(States states) {
    this.states = states;
  }

  public List<Transition> getTransitions() {
    return transitions;
  }

  public void setTransitions(List<Transition> transitions) {
    this.transitions = transitions;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    StateMachine that = (StateMachine) obj;

    if (!Objects.equals(className, that.className)) {
      return false;
    }
    if (!Objects.equals(delegateInterfaceName, that.delegateInterfaceName)) {
      return false;
    }
    if (!Objects.equals(states, that.states)) {
      return false;
    }
    return Objects.equals(transitions, that.transitions);
  }

  @Override
  public int hashCode() {
    int result = className != null ? className.hashCode() : 0;
    result = 31 * result + (delegateInterfaceName != null ? delegateInterfaceName.hashCode() : 0);
    result = 31 * result + (states != null ? states.hashCode() : 0);
    result = 31 * result + (transitions != null ? transitions.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "StateMachine{"
        + "className='" + className + '\''
        + ", delegateInterfaceName='" + delegateInterfaceName + '\''
        + ", states=" + states
        + ", transitions=" + transitions
        + '}';
  }
}
