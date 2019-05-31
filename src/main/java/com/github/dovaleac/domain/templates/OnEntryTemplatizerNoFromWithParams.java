package com.github.dovaleac.domain.templates;

import com.github.dovaleac.jackson.Param;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OnEntryTemplatizerNoFromWithParams extends OnEntryTemplatizer {
  public OnEntryTemplatizerNoFromWithParams(String tab) {
    super(tab);
  }

  @Override
  public String apply(OnEntryCalculationParams params) {
    String qualifiedTransition = "Transition<" + params.getTriggerClassName() + ", "
        + params.getStateClassName() + ">";

    List<Param> paramList = params.getParams().collect(Collectors.toList());

    return severalTabs(3) + ".onEntry(new Action"
        + params.getNumParams()
        + "<"
        + qualifiedTransition
        + ", "
        + "Object[]>() {"
        + "\n"
        + severalTabs(4)
        + ">, Object[]>() {"
        + qualifiedTransition
        + " trans, Object[] args) {"
        + "\n"
        + severalTabs(5)
        + params.getDelegateVariableName()
        + "."
        + params.getMethodName()
        + "("
        + IntStream.range(0, params.getNumParams())
        .boxed()
        .map(integer -> "(" + paramList.get(integer) + ") args[" + integer + "]")
        .collect(Collectors.joining(", "))
        + ";"
        + "\n"
        + severalTabs(4)
        + "}})";

  }
}
