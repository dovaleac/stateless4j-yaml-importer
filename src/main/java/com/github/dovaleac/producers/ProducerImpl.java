package com.github.dovaleac.producers;

import com.github.dovaleac.domain.AllFiles;
import com.github.dovaleac.domain.Method;
import com.github.dovaleac.domain.StateConfiguration;
import com.github.dovaleac.exceptions.ValidationException;
import com.github.dovaleac.io.ResourceService;
import com.github.dovaleac.jackson.*;
import com.github.dovaleac.substitution.VariableSubstitutionService;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProducerImpl implements Producer {

  public static final String STATE_PATH = ResourceService.get().getStateTemplate();
  public static final String TRIGGER_PATH = ResourceService.get().getTriggerTemplate();
  public static final String INTERFACE_PATH = ResourceService.get().getDelegateTemplate();
  public static final String STATE_MACHINE_PATH = ResourceService.get().getStateMachineTemplate();

  @Override
  public String produceState(String packageName, States states) throws IOException {

    String stateClassName = states.getClassName();
    String joinedStates =
        states.getElements().stream()
            .map(State::getName)
            .sorted()
            .collect(Collectors.joining(", "));

    Map<String, String> substitutions =
        Map.of("package", packageName, "stateClassName", stateClassName, "states", joinedStates);

    return VariableSubstitutionService.get().replaceAll(STATE_PATH, substitutions);
  }

  @Override
  public String produceTrigger(String packageName, StateMachine stateMachine) throws IOException {

    String triggerClassName = stateMachine.getTriggerClassName();
    String joinedTriggers =
        stateMachine.getTransitions().stream()
            .map(Transition::getTrigger)
            .distinct()
            .sorted()
            .collect(Collectors.joining(", "));

    Map<String, String> substitutions =
        Map.of(
            "package",
            packageName,
            "triggerClassName",
            triggerClassName,
            "triggers",
            joinedTriggers);

    return VariableSubstitutionService.get().replaceAll(TRIGGER_PATH, substitutions);
  }

  @Override
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

  @Override
  public Stream<Method> gatherOnExitMethods(StateMachine stateMachine) {
    return stateMachine.getStates().getElements().stream()
        .map(State::getOnExit)
        .filter(Objects::nonNull)
        .flatMap(Collection::stream)
        .distinct()
        .map(Method::new);
  }

  @Override
  public String produceInterface(String packageName, StateMachine stateMachine)
      throws IOException, ValidationException {
    List<String> delegateParameters = stateMachine.getDelegateParameters();
    String parameters = getClassParameters(delegateParameters);
    Stream<Method> onEntryMethods = gatherOnEntryMethods(stateMachine);
    Stream<Method> onExitMethods = gatherOnExitMethods(stateMachine);

    Stream<String> methodStream = Stream.concat(onEntryMethods, onExitMethods)
        .distinct()
        .map(Method::generateMethodDefinition);

    EventLog eventLog = stateMachine.getEventLog();
    if (eventLog != null) {
      methodStream = Stream.concat(methodStream, Stream.of(
          String.format("abstract void %s(%s trigger, %s state);", eventLog.getMethod(),
              stateMachine.getTriggerClassName(), stateMachine.getStates().getClassName())
      ));
    }
    String methods = methodStream
        .map(method -> "public " + method)
            .collect(Collectors.joining("\n\n  "));

    Map<String, String> substitutions =
        Map.of(
            "package",
            packageName,
            "parameters",
            parameters,
            "delegateInterfaceName",
            stateMachine.getDelegateInterfaceName(),
            "methods",
            methods);

    return VariableSubstitutionService.get().replaceAll(INTERFACE_PATH, substitutions);
  }

  @Override
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

  @Override
  public String produceStateMachine(
      String packageName, StateMachine stateMachine, String tab, String variableName)
      throws ValidationException {
    List<String> stateMachineParameters = stateMachine.getStateMachineParameters();
    String parameters = getClassParameters(stateMachineParameters);

    Map<String, Method> onEntryMethods =
        gatherOnEntryMethods(stateMachine).collect(Collectors.toMap(Method::getName, m -> m));

    Map<String, Method> onExitMethods =
        gatherOnExitMethods(stateMachine).collect(Collectors.toMap(Method::getName, m -> m));

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

    Map<String, Object> substitutions =
        Map.of(
            "configStates",
            configStates,
            "className",
            stateMachine.getClassName(),
            "parameters",
            parameters,
            "delegateClassName",
            getDelegateInterfaceNameForStateMachine(stateMachine),
            "delegateVariable",
            stateMachine.getDelegateVariableName(),
            "packageName",
            packageName,
            "variableName",
            variableName,
            "stateClassName",
            stateMachine.getStates().getClassName(),
            "triggerClassName",
            stateMachine.getTriggerClassName(),
            "imports",
            imports);

    return VariableSubstitutionService.get().replaceAll(STATE_MACHINE_PATH, substitutions);
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

  String getClassParameters(List<String> stateMachineParameters) {
    return stateMachineParameters.isEmpty()
        ? ""
        : "<" + String.join(", ", stateMachineParameters) + ">";
  }

  @Override
  public AllFiles getFileNames(StateMachine stateMachine) {
    return new AllFiles(
        stateMachine.getStates().getClassName() + ".java",
        stateMachine.getClassName() + ".java",
        stateMachine.getDelegateInterfaceName() + ".java",
        stateMachine.getTriggerClassName() + ".java");
  }
}
