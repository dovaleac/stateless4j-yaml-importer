package com.github.dovaleac;

import com.github.dovaleac.io.ResourceService;
import com.github.dovaleac.jackson.StateMachine;

import java.nio.file.Path;
import java.util.function.Function;

public enum FileToGenerate {
  TRIGGER(StateMachine::getTriggerClassName, ResourceService.get().getTriggerTemplate()),
  STATE(stateMachine -> stateMachine.getStates().getClassName(), ResourceService.get().getStateTemplate()),
  MACHINE(StateMachine::getClassName, ResourceService.get().getStateMachineTemplate()),
  DELEGATE(StateMachine::getDelegateInterfaceName, ResourceService.get().getDelegateTemplate());

  private final Function<StateMachine, String> fileNameGetter;
  private final String template;

  FileToGenerate(
      Function<StateMachine, String> fileNameGetter, String template) {
    this.fileNameGetter = fileNameGetter;
    this.template = template;
  }

  public Path getFileDestination(StateMachine stateMachine, Path destinationFolder) {
    return destinationFolder.resolve(fileNameGetter.apply(stateMachine));
  }

  public String getTemplate() {
    return template;
  }
}
