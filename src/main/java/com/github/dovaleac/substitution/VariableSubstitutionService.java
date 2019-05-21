package com.github.dovaleac.substitution;

import com.github.dovaleac.substitution.impl.VariableSubstitutionServiceImpl;

import java.util.Map;

public interface VariableSubstitutionService {

  String VARIABLE_SUBSTITUTION_REGEX = "(\\$\\{(\\w+)\\})";

  String replaceAll(String text, Map<String, ?> variables);

  static VariableSubstitutionService get() {
    return new VariableSubstitutionServiceImpl();
  }
}
