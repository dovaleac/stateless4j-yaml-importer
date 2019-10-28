package com.github.dovaleac.gatherers.statemachine.templates;

import com.github.dovaleac.substitution.VariableSubstitutionService;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class OnEntryTemplatizer implements Function<OnEntryCalculationParams, String> {
  protected final String template;
  protected final Map<String, String> tabMap;

  public OnEntryTemplatizer(String tab, String template) {
    this.template = template;
    this.tabMap = IntStream.range(3, 6).boxed().collect(Collectors.toMap(i -> "tab" + i,
        i -> severalTabs(i, tab)));
  }

  protected String severalTabs(int numTabs, String tab) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < numTabs; i++) {
      stringBuilder.append(tab);
    }
    return stringBuilder.toString();
  }

  protected Map<String, Object> extraVariables(OnEntryCalculationParams params) {
    return Map.of();
  }

  @Override
  public String apply(OnEntryCalculationParams onEntryCalculationParams) {

    Map<String, Object> substitutions = onEntryCalculationParams.asMutableMap();

    substitutions.putAll(tabMap);
    substitutions.putAll(extraVariables(onEntryCalculationParams));

    return VariableSubstitutionService.get().replaceAll(template, substitutions);
  }
}
