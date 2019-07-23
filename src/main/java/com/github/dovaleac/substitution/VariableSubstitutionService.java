package com.github.dovaleac.substitution;

import java.util.Map;

public interface VariableSubstitutionService {

  String VARIABLE_SUBSTITUTION_REGEX = "(\\$\\{(\\w+)\\})";

  String replaceAll(String text, Substitutions substitutions);

  static VariableSubstitutionService get() {
    return new VariableSubstitutionServiceImpl();
  }
}
