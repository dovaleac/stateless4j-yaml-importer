package com.github.dovaleac.gatherers.state;

import com.github.dovaleac.jackson.StateMachine;

public interface StateGatherer {
  String getJoinedStates(StateMachine stateMachine);

  static StateGatherer getInstance() {
    return StateGathererImpl.getInstance();
  }
}
