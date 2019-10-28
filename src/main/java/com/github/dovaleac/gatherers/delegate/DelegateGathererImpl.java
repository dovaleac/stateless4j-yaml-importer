package com.github.dovaleac.gatherers.delegate;

import com.github.dovaleac.domain.Method;
import com.github.dovaleac.jackson.EventLog;
import com.github.dovaleac.jackson.StateMachine;
import com.github.dovaleac.jackson.TriggerWithParameters;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DelegateGathererImpl implements DelegateGatherer {

  private static volatile DelegateGathererImpl mInstance;

  private DelegateGathererImpl() {}

  static DelegateGathererImpl getInstance() {
    if (mInstance == null) {
      synchronized (DelegateGathererImpl.class) {
        if (mInstance == null) {
          mInstance = new DelegateGathererImpl();
        }
      }
    }
    return mInstance;
  }

  @Override
  public String getDelegateInterfaceNameForStateMachine(StateMachine stateMachine) {
    String delegateInterfaceName = stateMachine.getDelegateInterfaceName();
    List<String> delegateParameters = stateMachine.getDelegateParameters();
    if (delegateParameters.isEmpty()) {
      return delegateInterfaceName;
    } else {
      List<String> stateMachineParameters = stateMachine.getStateMachineParameters();
      return delegateInterfaceName
          + '<'
          + delegateParameters.stream()
              .map(parameter -> stateMachineParameters.contains(parameter) ? parameter : "?")
              .collect(Collectors.joining(", "))
          + '>';
    }
  }

  @Override
  public String getDelegateMethods(
      StateMachine stateMachine,
      Map<String, Method> onEntryMethods,
      Map<String, Method> onExitMethods) {
    EventLog eventLog = stateMachine.getEventLog();

    HashSet<Object> methodSet = new HashSet<>(onEntryMethods.values());
    methodSet.addAll(onExitMethods.values());
    Stream<String> stringStream =
        methodSet.stream().map((Object obj) -> ((Method) obj).generateMethodDefinition());
    if (eventLog != null) {
      stringStream =
          Stream.concat(
              stringStream,
              Stream.of(
                  String.format(
                      "void %s(%s trigger, %s " + "state);",
                      eventLog.getMethod(),
                      stateMachine.getTriggerClassName(),
                      stateMachine.getStates().getClassName())));
    }
    stringStream = stringStream.map(s -> "public abstract " + s);

    return stringStream.collect(Collectors.joining("\n\n  "));
  }

  @Override
  public String getDelegateImports(StateMachine stateMachine) {
    return stateMachine.getTriggersWithParameters().stream()
        .map(TriggerWithParameters::getParams)
        .map(List::size)
        .distinct()
        .sorted()
        .map(
            size ->
                "import com.github.oxo42.stateless4j.triggers.TriggerWithParameters" + size + ";")
        .collect(Collectors.joining("\n"));
  }

  @Override
  public String getFireMethods(StateMachine stateMachine, String fire, String tab) {
    return stateMachine
        .getAllTriggersAsTriggersWithParameters()
        .map(
            triggerWithParameters ->
                triggerWithParameters.asFireMethod(fire, tab, stateMachine.getTriggerClassName()))
        .collect(Collectors.joining());
  }
}
