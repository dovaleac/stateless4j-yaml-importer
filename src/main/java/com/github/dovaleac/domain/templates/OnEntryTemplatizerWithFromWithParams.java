package com.github.dovaleac.domain.templates;

import com.github.dovaleac.jackson.Param;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OnEntryTemplatizerWithFromWithParams extends OnEntryTemplatizer {
  public OnEntryTemplatizerWithFromWithParams(String tab) {
    super(tab);
  }

  @Override
  public String apply(OnEntryCalculationParams params) {

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

    return severalTabs(3)
        + ".onEntryFrom(\n"
        + severalTabs(4)
        + "new TriggerWithParameters"
        + params.getNumParams()
        + "<>(\n"
        + severalTabs(5)
        + params.getTriggerClassName()
        + "."
        + params.getFrom()
        + ", "
        + paramClasses
        + "),\n"
        + severalTabs(4)
        + paramQualifiedParams
        + " -> {\n"
        + severalTabs(4)
        + params.getDelegateVariableName()
        + "."
        + params.getMethodName()
        + paramUnqualifiedParams
        + ";\n"
        + severalTabs(3)
        + "})";

  }
}
