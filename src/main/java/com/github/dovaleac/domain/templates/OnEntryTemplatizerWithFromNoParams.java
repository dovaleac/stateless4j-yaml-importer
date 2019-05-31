package com.github.dovaleac.domain.templates;

import java.util.Map;

public class OnEntryTemplatizerWithFromNoParams extends OnEntryTemplatizer {
  private static final String TEMPLATE =
      "${tab3}.onEntryFrom(${stateClassName}.${from}, "
          + "() -> ${delegateVariableName}.${methodName}())";

  public OnEntryTemplatizerWithFromNoParams(String tab) {
    super(tab, TEMPLATE);
  }

  @Override
  protected Map<String, Object> extraVariables(OnEntryCalculationParams params) {
    return Map.of("from", params.getFrom());
  }
}
