package com.github.dovaleac.domain;

import com.github.dovaleac.jackson.Param;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class Method {
  private final String name;
  private final List<Param> params;

  public Method(String name, List<Param> params) {
    this.name = name;
    this.params = params;
  }

  public Method(String name, Param... params) {
    this.name = name;
    this.params = Arrays.asList(params);
  }

  public Method(String name) {
    this.name = name;
    this.params = new ArrayList<>(0);
  }

  public String getName() {
    return name;
  }

  public Stream<Param> getParams() {
    return params.stream();
  }

  public Method withParams(List<Param> params) {
    return new Method(name, params);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    Method method = (Method) obj;

    if (!Objects.equals(name, method.name)) {
      return false;
    }
    return Objects.equals(params, method.params);
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (params != null ? params.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "Method{"
        + "name='" + name + '\''
        + ", params=" + params
        + '}';
  }
}
