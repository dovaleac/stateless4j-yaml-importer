package com.github.dovaleac.producers;

import com.github.dovaleac.jackson.State;
import com.github.dovaleac.jackson.StateMachine;
import com.github.dovaleac.jackson.States;
import com.github.dovaleac.jackson.Transition;
import com.github.dovaleac.substitution.VariableSubstitutionService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProducerImpl implements Producer {

  public static final Path STATE_PATH = Paths.get("src", "main", "resources", "templates", "State" +
      ".txt");
  public static final Path TRIGGER_PATH = Paths.get("src", "main", "resources", "templates",
      "Trigger.txt");

  @Override
  public String produceState(String packageName, States states) throws IOException {

    String original = Files.lines(STATE_PATH).collect(Collectors.joining("\n"));

    String stateClassName = states.getClassName();
    String joinedStates = states.getElements().stream()
        .map(State::getName)
        .sorted()
        .collect(Collectors.joining(", "));

    Map<String, String> substitutions = Map.of("package", packageName,
        "stateClassName", stateClassName,
        "states", joinedStates);

    return VariableSubstitutionService.get().replaceAll(original, substitutions);
  }

  @Override
  public String produceTrigger(String packageName,
      StateMachine stateMachine) throws IOException {

    String original = Files.lines(TRIGGER_PATH).collect(Collectors.joining("\n"));

    String triggerClassName = stateMachine.getTriggerClassName();
    String joinedTriggers = stateMachine.getTransitions().stream()
        .map(Transition::getTrigger)
        .distinct()
        .sorted()
        .collect(Collectors.joining(", "));

    Map<String, String> substitutions = Map.of("package", packageName,
        "triggerClassName", triggerClassName,
        "triggers", joinedTriggers);

    return VariableSubstitutionService.get().replaceAll(original, substitutions);
  }
}
