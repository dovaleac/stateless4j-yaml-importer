package com.github.dovaleac.gatherers.delegate;

import com.github.dovaleac.domain.Method;
import com.github.dovaleac.jackson.StateMachine;

import java.util.Map;

public interface DelegateGatherer {
  String getDelegateInterfaceNameForStateMachine(StateMachine stateMachine);

  String getDelegateMethods(
      StateMachine stateMachine,
      Map<String, Method> onEntryMethods,
      Map<String, Method> onExitMethods);

  static DelegateGatherer getInstance() {
    return DelegateGathererImpl.getInstance();
  }
}
