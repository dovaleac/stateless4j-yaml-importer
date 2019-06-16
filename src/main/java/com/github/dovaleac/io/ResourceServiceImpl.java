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
        + "  private final ${delegateClassName} ${delegateVariable};\n"
        + "\n"
        + "  public ${className}(${delegateClassName} ${delegateVariable}) {\n"
        + "    this.${delegateVariable} = ${delegateVariable};\n"
        + "  }\n"
        + "\n"
        + "  public StateMachineConfig<${stateClassName}, ${triggerClassName}> getConfig() {\n"
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
        + "public interface ${delegateInterfaceName}${parameters} {\n"
        + "  ${methods}\n"
        + "}";
  }
}
