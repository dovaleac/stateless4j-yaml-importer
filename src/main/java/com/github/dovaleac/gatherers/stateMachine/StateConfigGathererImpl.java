package com.github.dovaleac.gatherers.statemachine;

import com.github.dovaleac.domain.Method;
import com.github.dovaleac.domain.StateConfiguration;
import com.github.dovaleac.jackson.EventLog;
import com.github.dovaleac.jackson.OnEntry;
import com.github.dovaleac.jackson.StateMachine;
import com.github.dovaleac.jackson.Transition;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StateConfigGathererImpl implements StateConfigGatherer {

  private static volatile StateConfigGathererImpl mInstance;

  private StateConfigGathererImpl() {}

  public static StateConfigGathererImpl getInstance() {
    if (mInstance == null) {
      synchronized (StateConfigGathererImpl.class) {
        if (mInstance == null) {
          mInstance = new StateConfigGathererImpl();
        }
      }
    }
    return mInstance;
  }

  @Override
  public Map<String, Object> calculateStateMachineImportsAndConfigStates(
      StateMachine stateMachine,
      Map<String, Method> onEntryMethods,
      Map<String, Method> onExitMethods,
      String tab,
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

    if (stateMachine.getEventLog() != null) {
      stateless4jImportedClasses.add("java.util.stream.Stream");
    }
    String imports =
        Stream.concat(
                stateless4jImportedClasses.stream()
                    .map(importedClass -> "import " + importedClass + ";"),
                Stream.of("import com.github.oxo42.stateless4j.StateMachineConfig;"))
            .sorted()
            .collect(Collectors.joining("\n"));

    Map<String, Object> substitutions = new HashMap<>();
    substitutions.put("imports", imports);
    substitutions.put("configStates", configStates);
    return substitutions;
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
}
