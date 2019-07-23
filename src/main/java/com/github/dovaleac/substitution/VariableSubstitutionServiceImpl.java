package com.github.dovaleac.substitution;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VariableSubstitutionServiceImpl implements VariableSubstitutionService {
  @Override
  public String replaceAll(String text, Substitutions substitutions) {
    Map<String, Object> variables = substitutions.getSubstitutions();
    return Pattern.compile(VARIABLE_SUBSTITUTION_REGEX)
        .matcher(text)
        .replaceAll(
            matchResult -> {
              String replacement = matchResult.group(1);
              String variable = matchResult.group(2);

              Object found = variables.get(variable);
              return found == null ? quoteForReplacement(replacement) : found.toString();
            });
  }

  private String quoteForReplacement(String original) {
    return original.replaceAll("\\$", Matcher.quoteReplacement("\\$"));
  }
}
