package com.github.dovaleac.domain.templates;

import com.github.dovaleac.jackson.Param;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OnEntryTemplatizerWithFromWithParams extends OnEntryTemplatizer {
  private static final String TEMPLATE =
      "${tab3}.onEntryFrom(\n"
          + "${tab4}new TriggerWithParameters${numParams}<>("
          + "${triggerClassName}.${from}, ${paramClasses}),\n"
          + "${tab4}${paramQualifiedParams} -> {\n"
          + "${tab4}${delegateVariableName}.${methodName}${paramUnqualifiedParams};\n"
          + "${tab3}})";

  public OnEntryTemplatizerWithFromWithParams(String tab) {
    super(tab, TEMPLATE);
  }

  @Override
  protected Map<String, Object> extraVariables(OnEntryCalculationParams params) {

    List<Param> paramList = params.getParams().collect(Collectors.toList());

    String paramClasses =
        paramList.stream()
            .map(Param::getClassName)
            .map(className -> className + ".class")
            .collect(Collectors.joining(", "));

    String paramQualifiedParams =
        "("
            + paramList.stream().map(Param::getParamDefinition).collect(Collectors.joining(", "))
            + ")";

    String paramUnqualifiedParams =
        "("
            + paramList.stream().map(Param::getVariableName).collect(Collectors.joining(", "))
            + ")";

    return Map.of(
        "from", params.getFrom(),
        "paramClasses", paramClasses,
        "paramQualifiedParams", paramQualifiedParams,
        "paramUnqualifiedParams", paramUnqualifiedParams);
  }
}
