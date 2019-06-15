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

  public static final String STATE_PATH =
      ResourceService.get().getStateTemplate();
  public static final String TRIGGER_PATH =
      ResourceService.get().getTriggerTemplate();
  public static final String INTERFACE_PATH =
      ResourceService.get().getDelegateTemplate();
  public static final String STATE_MACHINE_PATH =
      ResourceService.get().getStateMachineTemplate();

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
                          .orElseThrow(
                              () ->
                                  uncheckValidationException(
                                      "Method not found onEntry: %s", onEntry.getName()));

                  return new Method(onEntry.getName(),
                      onEntry.getFrom(), triggerWithParameters.getParams());
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
    Stream<Method> onEntryMethods = gatherOnEntryMethods(stateMachine);
    Stream<Method> onExitMethods = gatherOnExitMethods(stateMachine);

    String methods =
        Stream.concat(onEntryMethods, onExitMethods)
            .distinct()
            .map(Method::generateMethodDefinition)
            .collect(Collectors.joining("\n\n  "));

    Map<String, String> substitutions =
        Map.of(
            "package",
            packageName,
            "delegateInterfaceName",
            stateMachine.getDelegateInterfaceName(),
            "methods",
            methods);

    return VariableSubstitutionService.get().replaceAll(INTERFACE_PATH, substitutions);
  }

  private RuntimeException uncheckValidationException(String message, String name) {
    return new RuntimeException(
        new ValidationException(String.format("Method not found onEntry: ", name)));
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

              return new StateConfiguration(stateName, onEntryMethods, onExitMethods, transitions);
            });
  }

  @Override
  public String produceStateMachine(String packageName, StateMachine stateMachine, String tab,
      String variableName)
      throws ValidationException, IOException {
    Map<String, Method> onEntryMethods = gatherOnEntryMethods(stateMachine)
            .collect(Collectors.toMap(Method::getName, m -> m));

    Map<String, Method> onExitMethods = gatherOnExitMethods(stateMachine)
            .collect(Collectors.toMap(Method::getName, m -> m));

    Set<String> stateless4jImportedClasses = new HashSet<>();

    String configStates = produceStateConfigurations(stateMachine, onEntryMethods, onExitMethods)
        .map(stateConfiguration -> {
          String configurationText = tab + tab + stateConfiguration.produceConfigurationText(
              tab,
              variableName,
              stateMachine.getTriggerClassName(),
              stateMachine.getStates().getClassName(),
              stateMachine.getDelegateVariableName()
          );
          stateless4jImportedClasses.addAll(stateConfiguration.getStateless4jImportedClasses());
          return configurationText;
        })
        .collect(Collectors.joining("\n\n"));

    String imports = Stream.concat(stateless4jImportedClasses.stream()
        .map(importedClass -> "import " + importedClass + ";"),
        Stream.of("import com.github.oxo42.stateless4j.StateMachineConfig;"))
        .sorted()
        .collect(Collectors.joining("\n"));

    Map<String, Object> substitutions =
        Map.of(
            "configStates", configStates,
            "className", stateMachine.getClassName(),
            "delegateClassName", stateMachine.getDelegateInterfaceName(),
            "delegateVariable", stateMachine.getDelegateVariableName(),
            "packageName", packageName,
            "variableName", variableName,
            "stateClassName", stateMachine.getStates().getClassName(),
            "triggerClassName", stateMachine.getTriggerClassName(),
            "imports", imports);

    return VariableSubstitutionService.get().replaceAll(STATE_MACHINE_PATH, substitutions);

  }

  @Override
  public AllFiles getFileNames(StateMachine stateMachine) {
    return new AllFiles(
        stateMachine.getStates().getClassName() + ".java",
        stateMachine.getClassName() + ".java",
        stateMachine.getDelegateInterfaceName() + ".java",
        stateMachine.getTriggerClassName() + ".java"
    );
  }
}
