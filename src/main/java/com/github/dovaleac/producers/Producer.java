package com.github.dovaleac.producers;

import com.github.dovaleac.domain.Method;
import com.github.dovaleac.exceptions.ValidationException;
import com.github.dovaleac.jackson.StateMachine;
import com.github.dovaleac.jackson.States;

import java.io.IOException;
import java.util.stream.Stream;

public interface Producer {
  String produceState(String packageName, States states) throws IOException;

  String produceTrigger(String packageName, StateMachine stateMachine) throws IOException;

  Stream<Method> gatherOnEntryMethods(StateMachine stateMachine) throws ValidationException;

  Stream<Method> gatherOnExitMethods(StateMachine stateMachine);
}
