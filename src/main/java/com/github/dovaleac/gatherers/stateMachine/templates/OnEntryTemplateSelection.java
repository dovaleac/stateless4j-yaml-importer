package com.github.dovaleac.gatherers.stateMachine.templates;

import java.util.function.Function;

public class OnEntryTemplateSelection {

  public static Function<OnEntryTemplateConfig, OnEntryTemplatizer> getTemplatizer(
      boolean hasFrom, boolean hasParams) {
    if (hasFrom) {
      if (hasParams) {
        return config -> new OnEntryTemplatizerWithFromWithParams(config.getTab());
      } else {
        return config -> new OnEntryTemplatizerWithFromNoParams(config.getTab());
      }
    } else {
      if (hasParams) {
        return config -> new OnEntryTemplatizerNoFromWithParams(config.getTab());
      } else {
        return config -> new OnEntryTemplatizerNoFromNoParams(config.getTab());
      }
    }
  }
}
