package com.github.dovaleac.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ResourceServiceImpl implements ResourceService {

  private static volatile ResourceServiceImpl mInstance;

  private ResourceServiceImpl() {
  }

  static ResourceServiceImpl getInstance() {
    if (mInstance == null) {
      synchronized (ResourceServiceImpl.class) {
        if (mInstance == null) {
          mInstance = new ResourceServiceImpl();
        }
      }
    }
    return mInstance;
  }

  @Override
  public String getStateTemplate() {
    return "package ${package};\n"
        + "\n"
        + "public enum ${stateClassName} {\n"
        + "  ${states}\n"
        + "}";
  }

  @Override
  public String getStateMachineTemplate() {
    return "package ${packageName};\n"
        + "\n"
        + "${imports}\n"
        + "\n"
        + "public class ${className}${parameters} {\n"
        + "\n"
        + "  public static StateMachineConfig<${stateClassName}, ${triggerClassName}> getConfig" +
        "(${delegateClassName} ${delegateVariable}) {\n"
        + "    StateMachineConfig<${stateClassName}, ${triggerClassName}> ${variableName} =\n"
        + "        new StateMachineConfig<>();\n"
        + "\n"
        + "${configStates}\n"
        + "\n"
        + "    return ${variableName};\n"
        + "  }\n"
        + "}";
  }

  @Override
  public String getTriggerTemplate() {
    return "package ${package};\n"
        + "\n"
        + "public enum ${triggerClassName} {\n"
        + "  ${triggers}\n"
        + "}";
  }

  @Override
  public String getDelegateTemplate() {
    return "package ${package};\n"
        + "\n"
        + "public abstract class ${delegateInterfaceName}${parameters} {\n"
        + "\n"
        + "  protected final StateMachine<${stateClassName}, ${triggerClassName}> stateMachine;\n"
        + "\n"
        + "  public ${delegateInterfaceName}(${stateClassName} initialState) {\n"
        + "    this.stateMachine = new StateMachine<>(initialState, ${className}.getConfig(this));\n"
        + "  }"
        + "  ${methods}\n"
        + "}";
  }
}
