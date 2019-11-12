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

  String getDelegateImports(StateMachine stateMachine);

  String getFireMethods(StateMachine stateMachine, String fire, String tab);

  static DelegateGatherer getInstance() {
    return DelegateGathererImpl.getInstance();
  }
}
