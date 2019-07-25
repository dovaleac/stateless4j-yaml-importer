package com.github.dovaleac.gatherers.stateMachine.templates;

public class OnEntryTemplatizerNoFromNoParams extends OnEntryTemplatizer {
  private static final String TEMPLATE =
      "${tab3}.onEntry(() -> ${delegateVariableName}.${methodName}())";

  public OnEntryTemplatizerNoFromNoParams(String tab) {
    super(tab, TEMPLATE);
  }

}
