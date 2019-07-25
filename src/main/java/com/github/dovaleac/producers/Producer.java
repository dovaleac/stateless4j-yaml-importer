package com.github.dovaleac.producers;

import com.github.dovaleac.domain.AllFiles;
import com.github.dovaleac.domain.Method;
import com.github.dovaleac.domain.ExecutionConfig;
import com.github.dovaleac.domain.StateConfiguration;
import com.github.dovaleac.exceptions.ValidationException;
import com.github.dovaleac.jackson.StateMachine;
import com.github.dovaleac.jackson.States;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

public interface Producer {
  String produceState(String packageName, States states) throws IOException;

  String produceTrigger(String packageName, StateMachine stateMachine) throws IOException;

  Stream<Method> gatherOnEntryMethods(StateMachine stateMachine) throws ValidationException;

  Stream<Method> gatherOnExitMethods(StateMachine stateMachine);

  String produceInterface(String packageName, StateMachine stateMachine)
      throws IOException, ValidationException;

  Stream<StateConfiguration> produceStateConfigurations(StateMachine stateMachine,
      Map<String, Method> onEntry, Map<String, Method> onExit);

  String produceStateMachine(String packageName, StateMachine stateMachine, String tab,
      String variableName) throws ValidationException;

  default AllFiles getAllFiles(StateMachine stateMachine, ExecutionConfig options)
      throws ValidationException, IOException {
    String producedState = produceState(options.getPackageName(), stateMachine.getStates());
    String producedStateMachine = produceStateMachine(options.getPackageName(), stateMachine,
        options.getTab(), options.getVariableName());
    String producedTrigger = produceTrigger(options.getPackageName(), stateMachine);
    String producedInterface = produceInterface(options.getPackageName(), stateMachine);

    return new AllFiles(
        producedState,
        producedStateMachine,
        producedInterface,
        producedTrigger);
  }

  AllFiles getFileNames(StateMachine stateMachine);
}
