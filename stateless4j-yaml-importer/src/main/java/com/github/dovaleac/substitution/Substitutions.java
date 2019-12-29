package com.github.dovaleac.substitution;

import com.github.dovaleac.domain.ExecutionConfig;
import com.github.dovaleac.domain.Method;
import com.github.dovaleac.exceptions.ValidationException;
import com.github.dovaleac.gatherers.delegate.DelegateGatherer;
import com.github.dovaleac.gatherers.methods.MethodGatherer;
import com.github.dovaleac.gatherers.state.StateGatherer;
import com.github.dovaleac.gatherers.statemachine.StateConfigGatherer;
import com.github.dovaleac.gatherers.trigger.TriggerGatherer;
import com.github.dovaleac.jackson.StateMachine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Substitutions {

  private static volatile Substitutions mInstance;

  private Map<String, Object> substitutions = new HashMap<>();
  private Map<String, Method> onEntryMethods;
  private Map<String, Method> onExitMethods;

  private final MethodGatherer methodGatherer = MethodGatherer.getInstance();
  private final StateConfigGatherer stateConfigGatherer = StateConfigGatherer.getInstance();
  private final DelegateGatherer delegateGatherer = DelegateGatherer.getInstance();
  private final TriggerGatherer triggerGatherer = TriggerGatherer.getInstance();
  private final StateGatherer stateGatherer = StateGatherer.getInstance();

  private Substitutions() {}

  public static void init(StateMachine stateMachine, ExecutionConfig executionConfig)
      throws ValidationException {
    getInstance().initPrivate(stateMachine, executionConfig);
  }

  private static Substitutions getInstance() {
    if (mInstance == null) {
      synchronized (Substitutions.class) {
        if (mInstance == null) {
          mInstance = new Substitutions();
        }
      }
    }
    return mInstance;
  }

  static Map<String, Object> getSubstitutions() {
    return getInstance().substitutions;
  }

  private void initPrivate(StateMachine stateMachine, ExecutionConfig executionConfig)
      throws ValidationException {
    onEntryMethods = methodGatherer
        .gatherOnEntryMethods(stateMachine)
        .distinct()
        .collect(Collectors.toMap(Method::getName, m -> m, (m1, m2) -> m1));

    onExitMethods = methodGatherer
        .gatherOnExitMethods(stateMachine)
        .distinct()
        .collect(Collectors.toMap(Method::getName, m -> m));

    String tab = executionConfig.getTab();
    String variableName = executionConfig.getVariableName();

    substitutions.put("tab", tab);
    substitutions.put("packageName", executionConfig.getPackageName());
    substitutions.put("variableName", variableName);

    substitutions.put("className", stateMachine.getClassName());
    substitutions.put("stateClassName", stateMachine.getStates().getClassName());
    substitutions.put("states", stateGatherer.getJoinedStates(stateMachine));
    substitutions.put("delegateInterfaceName", stateMachine.getDelegateInterfaceName());
    substitutions.put(
        "delegateInterfaceNameForStateMachine",
        delegateGatherer.getDelegateInterfaceNameForStateMachine(stateMachine));
    substitutions.put("delegateVariableName", stateMachine.getDelegateVariableName());
    substitutions.put("triggerClassName", stateMachine.getTriggerClassName());
    substitutions.put("triggers", triggerGatherer.getJoinedTriggers(stateMachine));
    substitutions.put(
        "delegateParameters", getClassParameters(stateMachine.getDelegateParameters()));
    substitutions.put(
        "stateMachineParameters", getClassParameters(stateMachine.getStateMachineParameters()));
    substitutions.put(
        "delegateMethods",
        delegateGatherer.getDelegateMethods(stateMachine, onEntryMethods, onExitMethods));
    substitutions.put("delegateImports", delegateGatherer.getDelegateImports(stateMachine));
    substitutions.put("fireMethods", delegateGatherer.getFireMethods(stateMachine, "fire",
        executionConfig.getTab()));

    substitutions.putAll(
        stateConfigGatherer.calculateStateMachineImportsAndConfigStates(
            stateMachine, onEntryMethods, onExitMethods, tab, variableName));
  }

  String getClassParameters(List<String> stateMachineParameters) {
    return stateMachineParameters.isEmpty()
        ? ""
        : "<" + String.join(", ", stateMachineParameters) + ">";
  }

  public static Map<String, Method> getOnEntryMethods() {
    return getInstance().onEntryMethods;
  }

  public static Map<String, Method> getOnExitMethods() {
    return getInstance().onExitMethods;
  }
}
