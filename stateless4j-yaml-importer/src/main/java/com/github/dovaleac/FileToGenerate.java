package com.github.dovaleac;

import com.github.dovaleac.io.ResourceService;
import com.github.dovaleac.jackson.StateMachine;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

/*
 * This should be an enum, but the mojo plugins don't allow Enums with fields
 * */
public abstract class FileToGenerate {

  public static final TriggerFileToGenerate TRIGGER = new TriggerFileToGenerate();
  public static final StateFileToGenerate STATE = new StateFileToGenerate();
  public static final MachineFileToGenerate MACHINE = new MachineFileToGenerate();
  public static final DelegateFileToGenerate DELEGATE = new DelegateFileToGenerate();
  private final Function<StateMachine, String> fileNameGetter;
  private final String template;

  private FileToGenerate(Function<StateMachine, String> fileNameGetter, String template) {
    this.fileNameGetter = fileNameGetter;
    this.template = template;
  }

  public static Stream<FileToGenerate> values() {
    return List.of(TRIGGER, STATE, MACHINE, DELEGATE).stream();
  }

  public Path getFileDestination(StateMachine stateMachine, Path destinationFolder) {
    return destinationFolder.resolve(fileNameGetter.apply(stateMachine) + ".java");
  }

  public String getTemplate() {
    return template;
  }

  public static class TriggerFileToGenerate extends FileToGenerate {
    private TriggerFileToGenerate() {
      super(StateMachine::getTriggerClassName, ResourceService.get().getTriggerTemplate());
    }
  }

  public static class StateFileToGenerate extends FileToGenerate {
    private StateFileToGenerate() {
      super(stateMachine -> stateMachine.getStates().getClassName(),
          ResourceService.get().getStateTemplate()
      );
    }
  }

  public static class MachineFileToGenerate extends FileToGenerate {
    private MachineFileToGenerate() {
      super(StateMachine::getClassName, ResourceService.get().getStateMachineTemplate());
    }
  }

  public static class DelegateFileToGenerate extends FileToGenerate {
    private DelegateFileToGenerate() {
      super(StateMachine::getDelegateInterfaceName, ResourceService.get().getDelegateTemplate());
    }
  }
}
