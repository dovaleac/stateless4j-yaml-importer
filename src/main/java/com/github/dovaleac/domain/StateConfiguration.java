package com.github.dovaleac.domain;

import com.github.dovaleac.domain.templates.OnEntryCalculationParams;
import com.github.dovaleac.domain.templates.OnEntryTemplateConfig;
import com.github.dovaleac.domain.templates.OnEntryTemplateSelection;
import com.github.dovaleac.jackson.Param;
import com.github.dovaleac.jackson.parsed.ParsedClass;
import com.github.dovaleac.jackson.parsed.ParsedParam;

import java.util.*;
import java.util.stream.Collectors;

public class StateConfiguration {
  private final String state;
  private final String superState;
  private final List<Method> onEntry;
  private final List<Method> onExit;
  private final Map<String, String> transitions;
  private final Set<String> stateless4jImportedClasses = new HashSet<>();

  public StateConfiguration(
      String state,
      String superState,
      List<Method> onEntry,
      List<Method> onExit,
      Map<String, String> transitions) {
    this.state = state;
    this.superState = superState;
    this.onEntry = onEntry;
    this.onExit = onExit;
    this.transitions = transitions;
  }

  public Set<String> getStateless4jImportedClasses() {
    return stateless4jImportedClasses;
  }

  public String produceConfigurationText(
      String tab,
      String variableName,
      String triggerClassName,
      String stateClassName,
      String delegateVariableName) {

    final String superStateConfiguration = produceSuperState(superState, tab, stateClassName);
    String onExit = produceOnExit(tab, delegateVariableName);
    String onEntry = produceOnEntry(tab, delegateVariableName, triggerClassName, stateClassName);
    String permits = producePermits(tab, triggerClassName, stateClassName);
    if (!permits.isEmpty()) {
      permits = "\n" + permits;
    }
    if (!onExit.isEmpty()) {
      permits += "\n";
    }
    if (!onEntry.isEmpty()) {
      onExit += "\n";
    }
    return variableName
        + ".configure("
        + stateClassName
        + "."
        + state
        + ")"
        + superStateConfiguration
        + permits
        + onExit
        + onEntry
        + ";";
  }

  private String produceSuperState(String superState, String tab, String stateClassName) {
    if (superState == null) {
      return "";
    }

    return '\n' + tab + tab + tab + ".substateOf(" + stateClassName + "." + superState + ")";
  }

  String produceOnEntry(
      String tab, String delegateVariableName, String triggerClassName, String stateClassName) {
    return onEntry.stream()
        .map(
            method -> {
              int numParams = (int) method.getParams().count();
              String from = method.getFrom();
              String methodName = method.getName();

              OnEntryCalculationParams onEntryCalculationParams =
                  new OnEntryCalculationParams(
                      delegateVariableName,
                      triggerClassName,
                      stateClassName,
                      numParams,
                      from,
                      methodName,
                      method.getParams());

              boolean hasFrom = from != null;
              boolean hasParams = numParams > 0;

              Stateless4jImportedClass.getImportedClass(hasFrom, numParams)
                  .ifPresent(stateless4jImportedClasses::add);

              method.getParams()
                  .map(Param::parse)
                  .map(ParsedParam::getClassName)
                  .filter(ParsedClass::isQualified)
                  .map(ParsedClass::getWholeName)
                  .forEachOrdered(stateless4jImportedClasses::add);

              return OnEntryTemplateSelection.getTemplatizer(hasFrom, hasParams)
                  .apply(new OnEntryTemplateConfig(tab))
                  .apply(onEntryCalculationParams);
            })
        .collect(Collectors.joining("\n"));
  }

  String produceOnExit(String tab, String delegateVariableName) {
    return onExit.stream()
        .map(Method::getName)
        .map(s -> tab + tab + tab + ".onExit(() -> " + delegateVariableName + "." + s + "())")
        .collect(Collectors.joining("\n"));
  }

  String producePermits(String tab, String triggerClassName, String stateClassName) {
    return transitions.entrySet().stream()
        .map(
            entry ->
                tab
                    + tab
                    + tab
                    + ".permit("
                    + triggerClassName
                    + "."
                    + entry.getKey()
                    + ", "
                    + stateClassName
                    + "."
                    + entry.getValue()
                    + ")")
        .collect(Collectors.joining("\n"));
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
        + "state='"
        + state
        + '\''
        + ", onEntry="
        + onEntry
        + ", onExit="
        + onExit
        + ", transitions="
        + transitions
        + '}';
  }
}
