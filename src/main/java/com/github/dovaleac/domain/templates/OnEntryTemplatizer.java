package com.github.dovaleac.domain.templates;

import java.util.function.Function;

public abstract class OnEntryTemplatizer implements Function<OnEntryCalculationParams, String> {
  protected final String tab;

  public OnEntryTemplatizer(String tab) {
    this.tab = tab;
  }

  protected String severalTabs(int numTabs) {
    StringBuilder stringBuilder = new StringBuilder();
    for (int i = 0; i < numTabs; i++) {
      stringBuilder.append(tab);
    }
    return stringBuilder.toString();
  }
}
