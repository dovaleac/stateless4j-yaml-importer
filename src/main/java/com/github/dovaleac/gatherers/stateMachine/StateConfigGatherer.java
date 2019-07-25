package com.github.dovaleac.gatherers.stateMachine;

import com.github.dovaleac.domain.Method;
import com.github.dovaleac.jackson.StateMachine;

import java.util.Map;

public interface StateConfigGatherer {

  Map<String, Object> calculateStateMachineImportsAndConfigStates(
      StateMachine stateMachine,
      Map<String, Method> onEntryMethods,
      Map<String, Method> onExitMethods,
      String tab,
      String variableName);

  static StateConfigGatherer getInstance() {
    return StateConfigGathererImpl.getInstance();
  }
}
