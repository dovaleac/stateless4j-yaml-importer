package com.github.dovaleac.domain.templates;

import com.github.dovaleac.jackson.Param;

import java.util.List;
import java.util.stream.Collectors;

public class OnEntryTemplatizerWithFromNoParams extends OnEntryTemplatizer {
  public OnEntryTemplatizerWithFromNoParams(String tab) {
    super(tab);
  }

  @Override
  public String apply(OnEntryCalculationParams params) {
    return severalTabs(3)
        + ".onEntryFrom("
        + params.getFrom()
        + ", "
        + params.getDelegateVariableName()
        + "::"
        + params.getMethodName()
        + ")";

  }
}
