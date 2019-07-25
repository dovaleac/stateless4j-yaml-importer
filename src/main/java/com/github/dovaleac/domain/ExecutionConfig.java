package com.github.dovaleac.domain;

public class ExecutionConfig {

  private final String packageName;
  private String tab = "  ";
  private String variableName = "config";

  public ExecutionConfig(String packageName) {
    this.packageName = packageName;
  }

  public String getPackageName() {
    return packageName;
  }

  public String getTab() {
    return tab;
  }

  public ExecutionConfig withTab(String tab) {
    this.tab = tab;
    return this;
  }

  public String getVariableName() {
    return variableName;
  }

  public ExecutionConfig withVariableName(String variableName) {
    this.variableName = variableName;
    return this;
  }
}
