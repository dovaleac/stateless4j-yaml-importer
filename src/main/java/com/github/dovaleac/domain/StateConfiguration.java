package com.github.dovaleac.domain;

import com.github.dovaleac.jackson.Param;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class StateConfiguration {
  private final String state;
  private final List<Method> onEntry;
  private final List<Method> onExit;
  private final Map<String, String> transitions;

  public StateConfiguration(
      String state, List<Method> onEntry, List<Method> onExit, Map<String, String> transitions) {
    this.state = state;
    this.onEntry = onEntry;
    this.onExit = onExit;
    this.transitions = transitions;
  }

  public String produceConfigurationText(
      String tab,
      String variableName,
      String triggerClassName,
      String stateClassName,
      String delegateVariableName) {

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
        + permits
        + onExit
        + onEntry
        + ";";
  }

  String produceOnEntry(
      String tab, String delegateVariableName, String triggerClassName, String stateClassName) {
    return onEntry.stream()
        .map(
            method -> {
              int numParams = (int) method.getParams().count();
              String from = method.getFrom();
              String methodName = method.getName();

              if (from == null) {
                if (numParams == 0) {
                  return onEntryNoParamsNoFrom(tab, delegateVariableName, methodName);
                } else {
                  return onEntryWithParamsNoFrom(
                      tab,
                      delegateVariableName,
                      methodName,
                      method.getParams(),
                      numParams,
                      triggerClassName,
                      stateClassName);
                }
              } else {
                if (numParams == 0) {
                  return onEntryNoParamsWithFrom(tab, delegateVariableName, methodName, from);
                } else {
                  return onEntryWithParamsWithFrom(
                      tab,
                      delegateVariableName,
                      methodName,
                      method.getParams(),
                      numParams,
                      triggerClassName,
                      from);
                }
              }
            })
        .collect(Collectors.joining("\n"));
  }

  private String onEntryWithParamsWithFrom(
      String tab,
      String delegateVariableName,
      String methodName,
      Stream<Param> params,
      int paramSize,
      String triggerClassName,
      String from) {

    List<Param> paramList = params.collect(Collectors.toList());

    String paramClasses =
        paramList.stream()
            .map(Param::getClassName)
            .map(className -> className + ".class")
            .collect(Collectors.joining(", "));

    String paramQualifiedParams =
        "("
            + paramList.stream().map(Param::getParamDefinition).collect(Collectors.joining(", "))
            + ")";

    String paramUnqualifiedParams =
        "("
            + paramList.stream().map(Param::getVariableName).collect(Collectors.joining(", "))
            + ")";

    return tab
        + tab
        + tab
        + ".onEntryFrom(\n"
        + tab
        + tab
        + tab
        + tab
        + "new TriggerWithParameters"
        + paramSize
        + "<>(\n"
        + tab
        + tab
        + tab
        + tab
        + tab
        + triggerClassName
        + "."
        + from
        + ", "
        + paramClasses
        + "),\n"
        + tab
        + tab
        + tab
        + tab
        + paramQualifiedParams
        + " -> {\n"
        + tab
        + tab
        + tab
        + tab
        + delegateVariableName
        + "."
        + methodName
        + paramUnqualifiedParams
        + ";\n"
        + tab
        + tab
        + tab
        + "})";
  }

  private String onEntryNoParamsWithFrom(
      String tab, String delegateVariableName, String methodName, String from) {
    return tab
        + tab
        + tab
        + ".onEntryFrom("
        + from
        + ", "
        + delegateVariableName
        + "::"
        + methodName
        + ")";
  }

  private String onEntryWithParamsNoFrom(
      String tab,
      String delegateVariableName,
      String methodName,
      Stream<Param> params,
      int paramSize,
      String triggerClassName,
      String stateClassName) {

    String qualifiedTransition = "Transition<" + triggerClassName + ", " + stateClassName + ">";

    List<Param> paramList = params.collect(Collectors.toList());

    return tab
        + tab
        + tab
        + ".onEntry(new Action"
        + paramSize
        + "<"
        + qualifiedTransition
        + ", "
        + "Object[]>() {"
        + "\n"
        + tab
        + tab
        + tab
        + tab
        + ">, Object[]>() {"
        + qualifiedTransition
        + " trans, Object[] args) {"
        + "\n"
        + tab
        + tab
        + tab
        + tab
        + tab
        + delegateVariableName
        + "."
        + methodName
        + "("
        + IntStream.range(0, paramSize)
            .boxed()
            .map(integer -> "(" + paramList.get(integer) + ") args[" + integer + "]")
            .collect(Collectors.joining(", "))
        + ";"
        + "\n"
        + tab
        + tab
        + tab
        + tab
        + "}})";
  }

  private String onEntryNoParamsNoFrom(String tab, String delegateVariableName, String methodName) {
    return tab + tab + tab + ".onEntry(() -> " + delegateVariableName + "." + methodName + "())";
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
