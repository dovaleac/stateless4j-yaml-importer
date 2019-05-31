package com.github.dovaleac.domain.templates;

public class OnEntryTemplatizerNoFromNoParams extends OnEntryTemplatizer {
  public OnEntryTemplatizerNoFromNoParams(String tab) {
    super(tab);
  }

  @Override
  public String apply(OnEntryCalculationParams params) {
    return severalTabs(3) + ".onEntry(() -> " + params.getDelegateVariableName() + "."
        + params.getMethodName() + "())";
  }
}
