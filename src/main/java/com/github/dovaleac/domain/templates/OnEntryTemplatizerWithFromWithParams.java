package com.github.dovaleac.domain.templates;

import com.github.dovaleac.jackson.Param;
import com.github.dovaleac.jackson.parsed.ParsedClass;
import com.github.dovaleac.jackson.parsed.ParsedParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OnEntryTemplatizerWithFromWithParams extends OnEntryTemplatizer {
  private static final String TEMPLATE =
      "${tab3}.onEntryFrom(\n"
          + "${tab4}new TriggerWithParameters${numParams}<>("
          + "${triggerClassName}.${from}, ${paramClasses}),\n"
          + "${tab4}${paramQualifiedParams} -> {\n"
          + "${forcedTypes}"
          + "${tab4}${delegateVariableName}.${methodName}${paramUnqualifiedParams};\n"
          + "${tab3}}, ${paramClasses})";

  public OnEntryTemplatizerWithFromWithParams(String tab) {
    super(tab, TEMPLATE);
  }

  @Override
  protected Map<String, Object> extraVariables(OnEntryCalculationParams params) {

    List<Param> paramList = params.getParams().collect(Collectors.toList());

    String paramClasses =
        paramList.stream()
            .map(Param::parse)
            .map(ParsedParam::getClassName)
            .map(ParsedClass::getClassName)
            .map(className -> className + ".class")
            .collect(Collectors.joining(", "));

    Map<ParsedParam, String> variablesForParams = createVariablesForParams(paramList);
    String forcedTypes = calculateParameterizedParams(variablesForParams);
    Collection<String> parameterizedVariables = variablesForParams.values();

    String paramQualifiedParams =
        "("
            + paramList.stream()
                .map(Param::parse)
                .map(
                    parsedParam ->
                        parsedParam.getClassName().getClassName()
                            + " "
                            + parsedParam.getVariableName())
                .collect(Collectors.joining(", "))
            + ")";

    String paramUnqualifiedParams =
        "("
            + paramList.stream()
                .map(
                    param -> {
                      String variableName = param.getVariableName();
                      if (parameterizedVariables.contains(variableName)) {
                        return variableName + "1";
                      } else {
                        return variableName;
                      }
                    })
                .collect(Collectors.joining(", "))
            + ")";

    return Map.of(
        "from", params.getFrom(),
        "paramClasses", paramClasses,
        "forcedTypes", forcedTypes,
        "paramQualifiedParams", paramQualifiedParams,
        "paramUnqualifiedParams", paramUnqualifiedParams);
  }

  private String calculateParameterizedParams(Map<ParsedParam, String> paramVariables) {
    return paramVariables.entrySet().stream()
        .map(
            entry -> {
              String classNameWithParameter =
                  entry.getKey().getClassName().getClassNameWithParameter();
              return String.format(
                  tabMap.get("tab4") + "%s %s1 = (%s) %s;\n",
                  classNameWithParameter,
                  entry.getValue(),
                  classNameWithParameter,
                  entry.getValue());
            })
        .collect(Collectors.joining());
  }

  private Map<ParsedParam, String> createVariablesForParams(List<Param> paramList) {
    return paramList.stream()
        .map(Param::parse)
        .filter(parsedParam -> parsedParam.getClassName().isParameterized())
        .collect(Collectors.toMap(o -> o, ParsedParam::getVariableName));
  }
}
