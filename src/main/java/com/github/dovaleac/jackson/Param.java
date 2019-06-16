package com.github.dovaleac.jackson;

import com.github.dovaleac.jackson.parsed.ParsedClass;
import com.github.dovaleac.jackson.parsed.ParsedParam;

import java.util.Objects;

public class Param {
  private String className;
  private String variableName;

  public Param() {
  }

  public Param(String className, String variableName) {
    this.className = className;
    this.variableName = variableName;
  }

  public ParsedParam parse() {
    return new ParsedParam(ParsedClass.parse(className), variableName);
  }

  public String getParamDefinition() {
    return String.format("%s %s", className, variableName);
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
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    Param param = (Param) obj;

    if (!Objects.equals(className, param.className)) {
      return false;
    }
    return Objects.equals(variableName, param.variableName);
  }

  @Override
  public int hashCode() {
    int result = className != null ? className.hashCode() : 0;
    result = 31 * result + (variableName != null ? variableName.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Param{"
        + "className='" + className + '\''
        + ", variableName='" + variableName + '\''
        + '}';
  }
}
