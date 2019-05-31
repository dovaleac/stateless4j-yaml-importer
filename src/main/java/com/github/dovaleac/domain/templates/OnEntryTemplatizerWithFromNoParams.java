package com.github.dovaleac.domain.templates;

import com.github.dovaleac.jackson.Param;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OnEntryTemplatizerWithFromNoParams extends OnEntryTemplatizer {
  private static final String TEMPLATE =
      "${tab3}.onEntryFrom(${from}, () -> ${delegateVariableName}.${methodName}())";
  public OnEntryTemplatizerWithFromNoParams(String tab) {
    super(tab, TEMPLATE);
  }

  @Override
  protected Map<String, Object> extraVariables(OnEntryCalculationParams params) {
    return Map.of("from", params.getFrom());
  }
}
