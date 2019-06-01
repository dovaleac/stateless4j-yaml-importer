package com.github.dovaleac.domain;

import java.util.Optional;

public class Stateless4jImportedClass {

  public static final String TRIGGER_WITH_PARAMETERS = "com.github.oxo42.stateless4j.triggers.TriggerWithParameters";
  public static final String ACTION = "com.github.oxo42.stateless4j.delegates.Action";

  public static Optional<String> getImportedClass(boolean hasFrom, int numParams) {
    if (numParams == 0) {
      return Optional.empty();
    }
    if (hasFrom) {
      return Optional.of(TRIGGER_WITH_PARAMETERS + numParams);
    } else {
      return Optional.of(ACTION + numParams);
    }
  }
}
