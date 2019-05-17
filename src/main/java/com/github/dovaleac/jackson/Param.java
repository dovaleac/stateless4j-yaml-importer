package com.github.dovaleac.jackson;

public class Param {
  private String className;
  private String variableName;

  public Param() {
  }

  public Param(String className, String variableName) {
    this.className = className;
    this.variableName = variableName;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public String getVariableName() {
    return variableName;
  }

  public void setVariableName(String variableName) {
    this.variableName = variableName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Param param = (Param) o;

    if (className != null ? !className.equals(param.className) : param.className != null)
      return false;
    return variableName != null ? variableName.equals(param.variableName) : param.variableName == null;
  }

  @Override
  public int hashCode() {
    int result = className != null ? className.hashCode() : 0;
    result = 31 * result + (variableName != null ? variableName.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Param{" +
        "className='" + className + '\'' +
        ", variableName='" + variableName + '\'' +
        '}';
  }
}
