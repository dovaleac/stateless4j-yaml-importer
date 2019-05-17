package com.github.dovaleac.jackson;

import java.util.List;

public class States {
  private String className;
  private List<State> elements;

  public States() {
  }

  public States(String className, List<State> elements) {
    this.className = className;
    this.elements = elements;
  }

  public String getClassName() {
    return className;
  }

  public void setClassName(String className) {
    this.className = className;
  }

  public List<State> getElements() {
    return elements;
  }

  public void setElements(List<State> elements) {
    this.elements = elements;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    States states = (States) o;

    if (className != null ? !className.equals(states.className) : states.className != null)
      return false;
    return elements != null ? elements.equals(states.elements) : states.elements == null;
  }

  @Override
  public int hashCode() {
    int result = className != null ? className.hashCode() : 0;
    result = 31 * result + (elements != null ? elements.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "States{" +
        "className='" + className + '\'' +
        ", elements=" + elements +
        '}';
  }
}
