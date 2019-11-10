package com.github.dovaleac.gatherers.statemachine.templates;

import com.github.dovaleac.jackson.Param;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OnEntryTemplatizerNoFromWithParams extends OnEntryTemplatizer {
  private static final String TEMPLATE =
      "${tab3}.onEntry(new Action${numParams}<${qualifiedTransition}, Object[]>() {\n"
      + "${tab4}public void doIt(${qualifiedTransition} trans, Object[] args) {\n"
      + "${tab5}${delegateVariableName}.${methodName}(${paramList});\n"
      + "${tab4}}})";

  public OnEntryTemplatizerNoFromWithParams(String tab) {
    super(tab,TEMPLATE);
  }

  @Override
  protected Map<String, Object> extraVariables(OnEntryCalculationParams params) {
    List<Param> paramList = params.getParams().collect(Collectors.toList());
    return Map.of(
        "qualifiedTransition", "Transition<" + params.getTriggerClassName() + ", "
            + params.getStateClassName() + ">",
        "paramList", IntStream.range(0, params.getNumParams())
            .boxed()
            .map(integer -> "(" + paramList.get(integer).getClassName()
                + ") args[" + integer + "]")
            .collect(Collectors.joining(", "))
    );
  }
}
