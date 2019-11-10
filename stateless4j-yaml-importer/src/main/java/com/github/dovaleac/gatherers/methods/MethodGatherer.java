package com.github.dovaleac.gatherers.methods;

import com.github.dovaleac.domain.Method;
import com.github.dovaleac.exceptions.ValidationException;
import com.github.dovaleac.jackson.StateMachine;

import java.util.stream.Stream;

public interface MethodGatherer {

  Stream<Method> gatherOnEntryMethods(StateMachine stateMachine) throws ValidationException;

  Stream<Method> gatherOnExitMethods(StateMachine stateMachine);

  static MethodGatherer getInstance() {
    return MethodGathererImpl.getInstance();
  }
}
