package com.github.dovaleac.gatherers.trigger;

import com.github.dovaleac.jackson.StateMachine;
import com.github.dovaleac.jackson.Transition;

import java.util.stream.Collectors;

public class TriggerGathererImpl implements TriggerGatherer {

  private static volatile TriggerGathererImpl mInstance;

  private TriggerGathererImpl() {
  }

  static TriggerGathererImpl getInstance() {
    if (mInstance == null) {
      synchronized (TriggerGathererImpl.class) {
        if (mInstance == null) {
          mInstance = new TriggerGathererImpl();
        }
      }
    }
    return mInstance;
  }

  @Override
  public String getJoinedTriggers(StateMachine stateMachine) {
    return stateMachine.getTransitions().stream()
        .map(Transition::getTrigger)
        .distinct()
        .sorted()
        .collect(Collectors.joining(",\n  "));
  }
}
