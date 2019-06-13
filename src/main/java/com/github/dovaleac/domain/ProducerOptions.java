package com.github.dovaleac.domain;

public class ProducerOptions {

  private final String packageName;
  private String tab = "  ";
  private String variableName = "config";

  public ProducerOptions(String packageName) {
    this.packageName = packageName;
  }

  public String getPackageName() {
    return packageName;
  }

  public String getTab() {
    return tab;
  }

  public ProducerOptions withTab(String tab) {
    this.tab = tab;
    return this;
  }

  public String getVariableName() {
    return variableName;
  }

  public ProducerOptions withVariableName(String variableName) {
    this.variableName = variableName;
    return this;
  }
}
