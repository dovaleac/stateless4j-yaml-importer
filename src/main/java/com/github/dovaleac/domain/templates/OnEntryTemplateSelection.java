package com.github.dovaleac.domain.templates;

import java.util.function.Function;
import java.util.stream.Stream;

public enum OnEntryTemplateSelection {
  NO_FROM_NO_PARAMS(false, false,
      config -> new OnEntryTemplatizerNoFromNoParams(config.getTab())),
  NO_FROM_WITH_PARAMS(false, true,
      config -> new OnEntryTemplatizerNoFromWithParams(config.getTab())),
  WITH_FROM_NO_PARAMS(true, false,
      config -> new OnEntryTemplatizerWithFromNoParams(config.getTab())),
  WITH_FROM_WITH_PARAMS(true, true,
      config -> new OnEntryTemplatizerWithFromWithParams(config.getTab()));


  private final boolean hasFrom;
  private final boolean hasParams;
  private final Function<OnEntryTemplateConfig, OnEntryTemplatizer> templatizer;

  OnEntryTemplateSelection(boolean hasFrom, boolean hasParams,
      Function<OnEntryTemplateConfig, OnEntryTemplatizer> templatizer) {
    this.hasFrom = hasFrom;
    this.hasParams = hasParams;
    this.templatizer = templatizer;
  }

  public static OnEntryTemplateSelection getByCase(boolean hasFrom, boolean hasParams) {
    return Stream.of(values())
        .filter(candidate -> candidate.hasFrom == hasFrom && candidate.hasParams == hasParams)
        .findAny()
        .orElse(null);
  }

  public Function<OnEntryTemplateConfig, OnEntryTemplatizer> getTemplatizer() {
    return templatizer;
  }
}
