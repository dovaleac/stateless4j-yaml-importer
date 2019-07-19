package com.github.dovaleac.jackson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StateMachine {
  private String className;
  private String triggerClassName;
  private String delegateInterfaceName;
  private String delegateVariableName;
  private States states;
  private List<Transition> transitions;
  private List<TriggerWithParameters> triggersWithParameters;
  private List<String> stateMachineParameters = new ArrayList<>();
  private List<String> delegateParameters = new ArrayList<>();
  private EventLog eventLog;

  public StateMachine() {
  }

  public StateMachine(String className, String triggerClassName, String delegateInterfaceName,
      String delegateVariableName, States states,
      List<Transition> transitions,
      List<TriggerWithParameters> triggersWithParameters,
      List<String> stateMachineParameters, List<String> delegateParameters,
      EventLog eventLog) {
    this.className = className;
    this.triggerClassName = triggerClassName;
    this.delegateInterfaceName = delegateInterfaceName;
    this.delegateVariableName = delegateVariableName;
    this.states = states;
    this.transitions = transitions;
    this.triggersWithParameters = triggersWithParameters;
    this.stateMachineParameters = stateMachineParameters;
    this.delegateParameters = delegateParameters;
    this.eventLog = eventLog;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getTriggerClassName() {
    return triggerClassName;
  }

  public void setTriggerClassName(String triggerClassName) {
    this.triggerClassName = triggerClassName;
  }

  public String getDelegateInterfaceName() {
    return delegateInterfaceName;
  }

  public void setDelegateInterfaceName(String delegateInterfaceName) {
    this.delegateInterfaceName = delegateInterfaceName;
  }

  public String getDelegateVariableName() {
    return delegateVariableName;
  }

  public void setDelegateVariableName(String delegateVariableName) {
    this.delegateVariableName = delegateVariableName;
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

  public List<TriggerWithParameters> getTriggersWithParameters() {
    return triggersWithParameters;
  }

  public void setTriggersWithParameters(
      List<TriggerWithParameters> triggersWithParameters) {
    this.triggersWithParameters = triggersWithParameters;
  }

  public List<String> getStateMachineParameters() {
    return stateMachineParameters;
  }

  public void setStateMachineParameters(List<String> stateMachineParameters) {
    this.stateMachineParameters = stateMachineParameters;
  }

  public List<String> getDelegateParameters() {
    return delegateParameters;
  }

  public void setDelegateParameters(List<String> delegateParameters) {
    this.delegateParameters = delegateParameters;
  }

  public EventLog getEventLog() {
    return eventLog;
  }

  public void setEventLog(EventLog eventLog) {
    this.eventLog = eventLog;
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
    if (!Objects.equals(triggerClassName, that.triggerClassName)) {
      return false;
    }
    if (!Objects.equals(delegateInterfaceName, that.delegateInterfaceName)) {
      return false;
    }
    if (!Objects.equals(delegateVariableName, that.delegateVariableName)) {
      return false;
    }
    if (!Objects.equals(states, that.states)) {
      return false;
    }
    if (!Objects.equals(transitions, that.transitions)) {
      return false;
    }
    if (!Objects.equals(triggersWithParameters, that.triggersWithParameters)) {
      return false;
    }
    if (!Objects.equals(stateMachineParameters, that.stateMachineParameters)) {
      return false;
    }
    if (!Objects.equals(delegateParameters, that.delegateParameters)) {
      return false;
    }
    return Objects.equals(eventLog, that.eventLog);
  }

  @Override
  public int hashCode() {
    int result = className != null ? className.hashCode() : 0;
    result = 31 * result + (triggerClassName != null ? triggerClassName.hashCode() : 0);
    result = 31 * result + (delegateInterfaceName != null ? delegateInterfaceName.hashCode() : 0);
    result = 31 * result + (delegateVariableName != null ? delegateVariableName.hashCode() : 0);
    result = 31 * result + (states != null ? states.hashCode() : 0);
    result = 31 * result + (transitions != null ? transitions.hashCode() : 0);
    result = 31 * result + (triggersWithParameters != null ? triggersWithParameters.hashCode() : 0);
    result = 31 * result + (stateMachineParameters != null ? stateMachineParameters.hashCode() : 0);
    result = 31 * result + (delegateParameters != null ? delegateParameters.hashCode() : 0);
    result = 31 * result + (eventLog != null ? eventLog.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "StateMachine{"
        + "className='" + className + '\''
        + ", triggerClassName='" + triggerClassName + '\''
        + ", delegateInterfaceName='" + delegateInterfaceName + '\''
        + ", delegateVariableName='" + delegateVariableName + '\''
        + ", states=" + states
        + ", transitions=" + transitions
        + ", triggersWithParameters=" + triggersWithParameters
        + ", stateMachineParameters=" + stateMachineParameters
        + ", delegateParameters=" + delegateParameters
        + ", eventLog=" + eventLog
        + '}';
  }
}
