package com.github.dovaleac.gatherers.trigger;

import com.github.dovaleac.jackson.StateMachine;

public interface TriggerGatherer {
  String getJoinedTriggers(StateMachine stateMachine);

  static TriggerGatherer getInstance() {
    return TriggerGathererImpl.getInstance();
  }
}
