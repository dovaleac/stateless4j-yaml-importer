package com.github.dovaleac.jackson.parsed;

import java.util.Objects;

public class ParsedParam {
  private final ParsedClass className;
  private final String variableName;

  public ParsedParam(ParsedClass className, String variableName) {
    this.className = className;
    this.variableName = variableName;
  }

  public ParsedClass getClassName() {
    return className;
  }

  public String getVariableName() {
    return variableName;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    ParsedParam that = (ParsedParam) obj;

    if (!Objects.equals(className, that.className)) {
      return false;
    }
    return Objects.equals(variableName, that.variableName);
  }

  @Override
  public int hashCode() {
    int result = className != null ? className.hashCode() : 0;
    result = 31 * result + (variableName != null ? variableName.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "ParsedParam{"
        + "className=" + className
        + ", variableName='" + variableName + '\''
        + '}';
  }
}
