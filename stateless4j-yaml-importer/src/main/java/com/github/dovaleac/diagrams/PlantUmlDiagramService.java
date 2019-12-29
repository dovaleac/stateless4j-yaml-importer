package com.github.dovaleac.diagrams;

import com.github.dovaleac.jackson.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlantUmlDiagramService implements DiagramService {
  @Override
  public String generatePlantUmlDiagramTxt(
      StateMachine stateMachine, String initialState, boolean printTriggers) {
    StringBuilder stringBuilder = new StringBuilder("@startuml\n[*] --> " + initialState + "\n");

    Map<String, String> triggersParamsMap =
        getTriggersParamsMap(stateMachine.getAllTriggersAsTriggersWithParameters());

    stateMachine.getTransitions().stream()
        .map(
            transition ->
                formatTransition(
                    transition,
                    printTriggers,
                    triggersParamsMap))
        .forEach(stringBuilder::append);

    stateMachine.getStates().getElements().stream()
        .flatMap(
            state -> {
              String stateName = state.getName();
              List<OnEntry> onEntryList = state.getOnEntry();
              if (!onEntryList.isEmpty()) {
                stringBuilder.append(stateName).append(": ").append("ON ENTRY METHODS\n");
                return onEntryList.stream()
                    .map(
                        onEntry -> {
                          if (onEntry.getFrom() == null) {
                            return stateName + ": " + onEntry.getName() + "()";
                          } else {
                            return stateName
                                + ": "
                                + onEntry.getFrom()
                                + " -> "
                                + onEntry.getName()
                                + triggersParamsMap.get(onEntry.getFrom());
                          }
                        });
              } else {
                return Stream.of();
              }
            })
        .map(s -> s + '\n')
        .forEach(stringBuilder::append);

    stateMachine.getStates().getElements().stream()
        .flatMap(
            state -> {
              String stateName = state.getName();
              List<String> onExitList = state.getOnExit();
              if (!onExitList.isEmpty()) {
                stringBuilder.append(stateName).append(": ").append("ON EXIT METHODS\n");
                return onExitList.stream().map(onExit -> stateName + ": " + onExit + "()");
              } else {
                return Stream.of();
              }
            })
        .map(s -> s + '\n')
        .forEach(stringBuilder::append);

    return stringBuilder.append("@enduml").toString();
  }

  Map<String, String> getTriggersParamsMap(Stream<TriggerWithParameters> triggersWithParameters) {
    return triggersWithParameters
        .collect(Collectors.toMap(TriggerWithParameters::getTrigger,
            trigger ->
                String.format(
                    "(%s)",
                    trigger.getParams().stream()
                        .map(Param::getParamDefinition)
                        .collect(Collectors.joining(", ")))));

  }

  String formatTransition(
      Transition transition,
      boolean printTriggers,
      Map<String, String> triggersParamsMap) {
    return printTriggers
        ? String.format(
            "%s --> %s : %s%s\n",
            transition.getFrom(),
            transition.getTo(),
            transition.getTrigger(),
        triggersParamsMap.get(transition.getTrigger()))
        : String.format("%s --> %s\n", transition.getFrom(), transition.getTo());
  }

  private String getTriggerRepresentation(Transition transition,
      Stream<TriggerWithParameters> triggersWithParameters) {
    return triggersWithParameters
        .filter(trigger -> Objects.equals(trigger.getTrigger(), transition.getTrigger()))
        .findAny()
        .map(
            trigger ->
                String.format(
                    "%s(%s)",
                    trigger.getTrigger(),
                    trigger.getParams().stream()
                        .map(Param::getParamDefinition)
                        .collect(Collectors.joining(", "))))
        .orElse(transition.getTrigger());
  }
}
