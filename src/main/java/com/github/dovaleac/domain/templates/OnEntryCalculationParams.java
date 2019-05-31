package com.github.dovaleac.domain.templates;

import com.github.dovaleac.jackson.Param;

import java.util.stream.Stream;

public class OnEntryCalculationParams {
  private final String delegateVariableName;
  private final String triggerClassName;
  private final String stateClassName;
  private final int numParams;
  private final String from;
  private final String methodName;
  private final Stream<Param> params;

  public OnEntryCalculationParams(String delegateVariableName,
      String triggerClassName, String stateClassName, int numParams, String from,
      String methodName, Stream<Param> params) {
    this.delegateVariableName = delegateVariableName;
    this.triggerClassName = triggerClassName;
    this.stateClassName = stateClassName;
    this.numParams = numParams;
    this.from = from;
    this.methodName = methodName;
    this.params = params;
  }

  public String getDelegateVariableName() {
    return delegateVariableName;
  }

  public String getTriggerClassName() {
    return triggerClassName;
  }

  public String getStateClassName() {
    return stateClassName;
  }

  public int getNumParams() {
    return numParams;
  }

  public String getFrom() {
    return from;
  }

  public String getMethodName() {
    return methodName;
  }

  public Stream<Param> getParams() {
    return params;
  }
}
