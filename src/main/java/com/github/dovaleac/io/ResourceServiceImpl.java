package com.github.dovaleac.io;

public class ResourceServiceImpl implements ResourceService {

  private static volatile ResourceServiceImpl mInstance;

  private ResourceServiceImpl() {}

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
    return "package ${packageName};\n"
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
        + "public class ${className}${stateMachineParameters} {\n"
        + "\n"
        + "  public StateMachineConfig<${stateClassName}, ${triggerClassName}> getConfig"
        + "(${delegateInterfaceNameForStateMachine} ${delegateVariableName}) {\n"
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
    return "package ${packageName};\n"
        + "\n"
        + "public enum ${triggerClassName} {\n"
        + "  ${triggers}\n"
        + "}";
  }

  @Override
  public String getDelegateTemplate() {
    return "package ${packageName};\n"
        + "\n"
        + "import com.github.oxo42.stateless4j.StateMachine;\n"
        + "${delegateImports}\n"
        + "\n"
        + "public abstract class ${delegateInterfaceName}${delegateParameters} {\n"
        + "\n"
        + "  protected final StateMachine<${stateClassName}, ${triggerClassName}> stateMachine;\n"
        + "\n"
        + "  public ${delegateInterfaceName}(${stateClassName} initialState) {\n"
        + "    this.stateMachine = new StateMachine<>(initialState, new ${className}().getConfig"
        + "(this));\n"
        + "  }\n\n"
        + "  ${delegateMethods}\n\n"
        + "${fireMethods}\n"
        + "}";
  }
}
