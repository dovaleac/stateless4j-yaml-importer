package com.github.dovaleac.jackson;

public class OnEntry {
  private String name;

  public OnEntry() {
  }

  public OnEntry(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    OnEntry onEntry = (OnEntry) o;

    return name != null ? name.equals(onEntry.name) : onEntry.name == null;
  }

  @Override
  public int hashCode() {
    return name != null ? name.hashCode() : 0;
  }

  @Override
  public String toString() {
    return "OnEntry{" +
        "name='" + name + '\'' +
        '}';
  }
}
