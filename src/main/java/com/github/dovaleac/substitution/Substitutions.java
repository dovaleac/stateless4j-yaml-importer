package com.github.dovaleac.substitution;

import com.github.dovaleac.domain.Method;
import com.github.dovaleac.domain.ProducerOptions;
import com.github.dovaleac.domain.StateConfiguration;
import com.github.dovaleac.exceptions.ValidationException;
import com.github.dovaleac.jackson.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Substitutions {

  private static volatile Substitutions mInstance;
  private Map<String, Object> substitutions = new HashMap<>();

  private Substitutions() {}

  public static Substitutions getInstance() {
    if (mInstance == null) {
      synchronized (Substitutions.class) {
        if (mInstance == null) {
          mInstance = new Substitutions();
        }
      }
    }
    return mInstance;
  }

  Map<String, Object> getSubstitutions() {
    return substitutions;
  }

  public void init(StateMachine stateMachine, ProducerOptions producerOptions)
      throws ValidationException {
    Map<String, Method> onEntryMethods =
        gatherOnEntryMethods(stateMachine).collect(Collectors.toMap(Method::getName, m -> m));
    Map<String, Method> onExitMethods =
        gatherOnExitMethods(stateMachine).collect(Collectors.toMap(Method::getName, m -> m));

    String tab = producerOptions.getTab();
    String variableName = producerOptions.getVariableName();

    substitutions.put("tab", tab);
    substitutions.put("packageName", producerOptions.getPackageName());
    substitutions.put("variableName", variableName);

    substitutions.put("className", stateMachine.getClassName());
    substitutions.put("stateClassName", stateMachine.getStates().getClassName());
    substitutions.put("states", getJoinedStates(stateMachine));
    substitutions.put("delegateInterfaceName", stateMachine.getDelegateInterfaceName());
    substitutions.put("delegateInterfaceNameForStateMachine",
        getDelegateInterfaceNameForStateMachine(stateMachine));
    substitutions.put("delegateVariableName", stateMachine.getDelegateVariableName());
    substitutions.put("triggerName", stateMachine.getTriggerClassName());
    substitutions.put("triggers", getJoinedTriggers(stateMachine));
    substitutions.put(
        "delegateParameters", getClassParameters(stateMachine.getDelegateParameters()));
    substitutions.put(
        "stateMachineParameters", getClassParameters(stateMachine.getStateMachineParameters()));
    substitutions.put(
        "delegateMethods", getDelegateMethods(stateMachine, onEntryMethods, onExitMethods));

    calculateStateMachineImportsAndConfigStates(stateMachine, onEntryMethods, onExitMethods, tab, variableName);
  }

  void calculateStateMachineImportsAndConfigStates(StateMachine stateMachine,
      Map<String, Method> onEntryMethods, Map<String, Method> onExitMethods, String tab,
      String variableName) {
    Set<String> stateless4jImportedClasses = new HashSet<>();

    String configStates =
        logTriggerEvents(stateMachine, tab, variableName)
            + produceStateConfigurations(stateMachine, onEntryMethods, onExitMethods)
            .map(
                stateConfiguration -> {
                  String configurationText =
                      tab
                          + tab
                          + stateConfiguration.produceConfigurationText(
                          tab,
                          variableName,
                          stateMachine.getTriggerClassName(),
                          stateMachine.getStates().getClassName(),
                          stateMachine.getDelegateVariableName());
                  stateless4jImportedClasses.addAll(
                      stateConfiguration.getStateless4jImportedClasses());
                  return configurationText;
                })
            .collect(Collectors.joining("\n\n"));

    Set<String> importedClasses = stateless4jImportedClasses;
    if (stateMachine.getEventLog() != null) {
      importedClasses.add("java.util.stream.Stream");
    }
    String imports =
        Stream.concat(
            importedClasses.stream()
                .map(importedClass -> "import " + importedClass + ";"),
            Stream.of("import com.github.oxo42.stateless4j.StateMachineConfig;"))
            .sorted()
            .collect(Collectors.joining("\n"));

    substitutions.put("imports", imports);
    substitutions.put("configStates", configStates);
  }

  public Stream<StateConfiguration> produceStateConfigurations(
      StateMachine stateMachine, Map<String, Method> onEntry, Map<String, Method> onExit) {
    return stateMachine.getStates().getElements().stream()
        .map(
            state -> {
              String stateName = state.getName();

              List<OnEntry> stateOnEntry = state.getOnEntry();
              List<Method> onEntryMethods =
                  stateOnEntry == null
                  ? new ArrayList<>(0)
                  : stateOnEntry.stream()
                      .map(OnEntry::getName)
                      .map(onEntry::get)
                      .collect(Collectors.toList());

              List<String> stateOnExit = state.getOnExit();
              List<Method> onExitMethods =
                  stateOnExit == null
                  ? new ArrayList<>(0)
                  : stateOnExit.stream().map(onExit::get).collect(Collectors.toList());

              Map<String, String> transitions =
                  stateMachine.getTransitions().stream()
                      .filter(transition -> Objects.equals(transition.getFrom(), stateName))
                      .collect(Collectors.toMap(Transition::getTrigger, Transition::getTo));

              return new StateConfiguration(
                  stateName,
                  state.getSuperState(),
                  onEntryMethods,
                  onExitMethods,
                  state.getIgnore(),
                  transitions);
            });
  }


  String logTriggerEvents(StateMachine stateMachine, String tab, String variableName) {
    EventLog eventLog = stateMachine.getEventLog();
    if (eventLog == null) {
      return "";
    }

    return "\n"
        + tab
        + "Stream.of("
        + stateMachine.getStates().getClassName()
        + ".values())\n"
        + tab
        + tab
        + ".forEach(\n"
        + tab
        + tab
        + tab
        + "state ->\n"
        + tab
        + tab
        + tab
        + tab
        + "Stream.of("
        + stateMachine.getTriggerClassName()
        + ".values())\n"
        + tab
        + tab
        + tab
        + tab
        + tab
        + ".forEach(\n"
        + tab
        + tab
        + tab
        + tab
        + tab
        + tab
        + "trigger ->\n"
        + tab
        + tab
        + tab
        + tab
        + tab
        + tab
        + tab
        + variableName
        + "\n"
        + tab
        + tab
        + tab
        + tab
        + tab
        + tab
        + tab
        + tab
        + ".configure(state)\n"
        + tab
        + tab
        + tab
        + tab
        + tab
        + tab
        + tab
        + tab
        + ".onEntryFrom(\n"
        + tab
        + tab
        + tab
        + tab
        + tab
        + tab
        + tab
        + tab
        + tab
        + "trigger,\n"
        + tab
        + tab
        + tab
        + tab
        + tab
        + tab
        + tab
        + tab
        + tab
        + "() -> "
        + stateMachine.getDelegateVariableName()
        + "."
        + eventLog.getMethod()
        + "(trigger, state))));\n\n";
  }

  String getDelegateInterfaceNameForStateMachine(StateMachine stateMachine) {
    String delegateInterfaceName = stateMachine.getDelegateInterfaceName();
    List<String> delegateParameters = stateMachine.getDelegateParameters();
    if (delegateParameters.isEmpty()) {
      return delegateInterfaceName;
    } else {
      List<String> stateMachineParameters = stateMachine.getStateMachineParameters();
      return delegateInterfaceName
          + '<'
          + delegateParameters.stream()
          .map(parameter -> stateMachineParameters.contains(parameter) ? parameter : "?")
          .collect(Collectors.joining(", "))
          + '>';
    }
  }

  String getJoinedTriggers(StateMachine stateMachine) {
    return stateMachine.getTransitions().stream()
        .map(Transition::getTrigger)
        .distinct()
        .sorted()
        .collect(Collectors.joining(", "));
  }

  String getJoinedStates(StateMachine stateMachine) {
    return stateMachine.getStates().getElements().stream()
        .map(State::getName)
        .sorted()
        .collect(Collectors.joining(", "));
  }

  String getDelegateMethods(
      StateMachine stateMachine,
      Map<String, Method> onEntryMethods,
      Map<String, Method> onExitMethods) {
    EventLog eventLog = stateMachine.getEventLog();

    HashSet<Object> methodSet = new HashSet<>(onEntryMethods.values());
    methodSet.addAll(onExitMethods.values());
    Stream<String> stringStream =
        methodSet.stream().map((Object t) -> ((Method) t).generateMethodDefinition());
    if (eventLog != null) {
      stringStream =
          Stream.concat(
              stringStream,
              Stream.of(
                  String.format(
                      "void %s(%s trigger, %s " + "state);",
                      eventLog.getMethod(),
                      stateMachine.getTriggerClassName(),
                      stateMachine.getStates().getClassName())));
    }
    stringStream = stringStream.map(s -> "public abstract " + s);

    return stringStream.collect(Collectors.joining("\n\n  "));
  }

  String getClassParameters(List<String> stateMachineParameters) {
    return stateMachineParameters.isEmpty()
        ? ""
        : "<" + String.join(", ", stateMachineParameters) + ">";
  }

  public Stream<Method> gatherOnEntryMethods(StateMachine stateMachine) throws ValidationException {
    try {
      return stateMachine.getStates().getElements().stream()
          .map(State::getOnEntry)
          .filter(Objects::nonNull)
          .flatMap(Collection::stream)
          .distinct()
          .map(
              onEntry -> {
                if (onEntry.getFrom() == null) {
                  return new Method(onEntry.getName());
                } else {
                  TriggerWithParameters triggerWithParameters =
                      stateMachine.getTriggersWithParameters().stream()
                          .filter(
                              trigger -> Objects.equals(trigger.getTrigger(), onEntry.getFrom()))
                          .findAny()
                          .orElse(null);

                  if (triggerWithParameters == null) {
                    return new Method(onEntry.getName(), onEntry.getFrom());
                  } else {
                    return new Method(
                        onEntry.getName(), onEntry.getFrom(), triggerWithParameters.getParams());
                  }
                }
              });
    } catch (RuntimeException ex) {
      throw (ValidationException) ex.getCause();
    }
  }

  public Stream<Method> gatherOnExitMethods(StateMachine stateMachine) {
    return stateMachine.getStates().getElements().stream()
        .map(State::getOnExit)
        .filter(Objects::nonNull)
        .flatMap(Collection::stream)
        .distinct()
        .map(Method::new);
  }
}
