package com.github.dovaleac.jackson;

import java.util.List;
import java.util.Objects;

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
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    States states = (States) obj;

    if (!Objects.equals(className, states.className)) {
      return false;
    }
    return Objects.equals(elements, states.elements);
  }

  @Override
  public int hashCode() {
    int result = className != null ? className.hashCode() : 0;
    result = 31 * result + (elements != null ? elements.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "States{"
        + "className='" + className + '\''
        + ", elements=" + elements
        + '}';
  }
}
