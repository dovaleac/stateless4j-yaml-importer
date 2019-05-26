package com.github.dovaleac.producers;

import com.github.dovaleac.domain.Method;
import com.github.dovaleac.exceptions.ValidationException;
import com.github.dovaleac.jackson.*;
import com.github.dovaleac.substitution.VariableSubstitutionService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProducerImpl implements Producer {

  public static final Path STATE_PATH =
      Paths.get("src", "main", "resources", "templates", "State.txt");
  public static final Path TRIGGER_PATH =
      Paths.get("src", "main", "resources", "templates", "Trigger.txt");
  public static final Path INTERFACE_PATH =
      Paths.get("src", "main", "resources", "templates", "Interface.txt");

  @Override
  public String produceState(String packageName, States states) throws IOException {

    String original = Files.lines(STATE_PATH).collect(Collectors.joining("\n"));

    String stateClassName = states.getClassName();
    String joinedStates =
        states.getElements().stream()
            .map(State::getName)
            .sorted()
            .collect(Collectors.joining(", "));

    Map<String, String> substitutions =
        Map.of("package", packageName, "stateClassName", stateClassName, "states", joinedStates);

    return VariableSubstitutionService.get().replaceAll(original, substitutions);
  }

  @Override
  public String produceTrigger(String packageName, StateMachine stateMachine) throws IOException {

    String original = Files.lines(TRIGGER_PATH).collect(Collectors.joining("\n"));

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

    return VariableSubstitutionService.get().replaceAll(original, substitutions);
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

                  return new Method(onEntry.getName(), triggerWithParameters.getParams());
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

    String methods = Stream.concat(onEntryMethods, onExitMethods)
        .distinct()
        .map(Method::generateMethodDefinition)
        .collect(Collectors.joining("\n\n  "));

    String original = Files.lines(INTERFACE_PATH).collect(Collectors.joining("\n"));

    Map<String, String> substitutions =
        Map.of("package", packageName,
            "delegateInterfaceName", stateMachine.getDelegateInterfaceName(),
            "methods", methods);

    return VariableSubstitutionService.get().replaceAll(original, substitutions);
  }

  private RuntimeException uncheckValidationException(String message, String name) {
    return new RuntimeException(
        new ValidationException(String.format("Method not found onEntry: ", name)));
  }
}
