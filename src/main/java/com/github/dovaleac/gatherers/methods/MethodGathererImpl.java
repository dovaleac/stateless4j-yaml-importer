package com.github.dovaleac.gatherers.methods;

import com.github.dovaleac.domain.Method;
import com.github.dovaleac.exceptions.ValidationException;
import com.github.dovaleac.jackson.State;
import com.github.dovaleac.jackson.StateMachine;
import com.github.dovaleac.jackson.TriggerWithParameters;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Stream;

public class MethodGathererImpl implements MethodGatherer {

  private static volatile MethodGathererImpl mInstance;

  private MethodGathererImpl() {
  }

  static MethodGathererImpl getInstance() {
    if (mInstance == null) {
      synchronized (MethodGathererImpl.class) {
        if (mInstance == null) {
          mInstance = new MethodGathererImpl();
        }
      }
    }
    return mInstance;
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
}
