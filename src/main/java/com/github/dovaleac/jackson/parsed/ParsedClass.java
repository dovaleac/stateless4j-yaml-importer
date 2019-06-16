package com.github.dovaleac.jackson.parsed;

import java.util.Objects;

public class ParsedClass {
  private final String wholeName;
  private final String className;
  private final String classNameWithParameter;
  private final boolean isParameterized;

  public ParsedClass(
      String wholeName, String className, String classNameWithParameter, boolean isParameterized) {
    this.wholeName = wholeName;
    this.className = className;
    this.classNameWithParameter = classNameWithParameter;
    this.isParameterized = isParameterized;
  }

  public static ParsedClass parse(String receivedClassName) {
    int lastDot = receivedClassName.lastIndexOf(".");
    String classNameWithPossibleParameter =
        lastDot > 0 ? receivedClassName.substring(lastDot + 1) : receivedClassName;
    int gt = classNameWithPossibleParameter.indexOf("<");
    boolean isParameterized = gt > 0;
    String className =
        isParameterized
            ? classNameWithPossibleParameter.substring(0, gt)
            : classNameWithPossibleParameter;

    String wholeClassName =
        isParameterized
            ? receivedClassName.substring(0, receivedClassName.indexOf("<"))
            : receivedClassName;

    return new ParsedClass(
        wholeClassName, className, classNameWithPossibleParameter, isParameterized);
  }

  public String getWholeName() {
    return wholeName;
  }

  public String getClassName() {
    return className;
  }

  public String getClassNameWithParameter() {
    return classNameWithParameter;
  }

  public boolean isParameterized() {
    return isParameterized;
  }

  public boolean isQualified() {
    return !Objects.equals(className, wholeName);
  }
}
