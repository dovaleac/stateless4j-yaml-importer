package com.github.dovaleac.gatherers.state;

import com.github.dovaleac.jackson.State;
import com.github.dovaleac.jackson.StateMachine;

import java.util.stream.Collectors;

public class StateGathererImpl implements StateGatherer {

  private static volatile StateGathererImpl mInstance;

  private StateGathererImpl() {
  }

  public static StateGathererImpl getInstance() {
    if (mInstance == null) {
      synchronized (StateGathererImpl.class) {
        if (mInstance == null) {
          mInstance = new StateGathererImpl();
        }
      }
    }
    return mInstance;
  }

  @Override
  public String getJoinedStates(StateMachine stateMachine) {
    return stateMachine.getStates().getElements().stream()
        .map(State::getName)
        .sorted()
        .collect(Collectors.joining(", "));
  }
}
