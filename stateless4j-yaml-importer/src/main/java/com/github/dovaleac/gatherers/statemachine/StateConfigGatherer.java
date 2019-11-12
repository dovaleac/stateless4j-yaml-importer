package com.github.dovaleac.gatherers.statemachine;

import com.github.dovaleac.domain.Method;
import com.github.dovaleac.jackson.StateMachine;

import java.util.Map;

public interface StateConfigGatherer {

  static StateConfigGatherer getInstance() {
    return StateConfigGathererImpl.getInstance();
  }

  Map<String, Object> calculateStateMachineImportsAndConfigStates(
      StateMachine stateMachine,
      Map<String, Method> onEntryMethods,
      Map<String, Method> onExitMethods,
      String tab,
      String variableName);
}
