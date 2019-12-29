package com.github.dovaleac.jackson;

import com.github.dovaleac.jackson.parsed.ParsedClass;
import com.github.dovaleac.jackson.parsed.ParsedParam;
import com.github.dovaleac.substitution.VariableSubstitutionService;
import com.github.dovaleac.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class TriggerWithParameters {
  private String trigger;
  private List<Param> params;

  public TriggerWithParameters() {}

  public TriggerWithParameters(String trigger) {
    this(trigger, List.of());
  }

  public TriggerWithParameters(String trigger, List<Param> params) {
    this.trigger = trigger;
    this.params = params;
  }

  public String getTrigger() {
    return trigger;
  }

  public void setTrigger(String trigger) {
    this.trigger = trigger;
  }

  public List<Param> getParams() {
    return params;
  }

  public void setParams(List<Param> params) {
    this.params = params;
  }

  public boolean hasSameParamsWithoutNames(TriggerWithParameters other) {
    if (params.size() != other.params.size()) {
      return false;
    }

    for (int i = 0; i < params.size(); i++) {
      if (!Objects.equals(params.get(i).getClassName(), other.params.get(i).getClassName())) {
        return false;
      }
    }

    return true;
  }

  public String asFireMethod(String fire, String tab, String triggerClassName) {
    String template =
        "${tab}public void ${fire}${trigger}(${paramsWithClasses}) {\n"
            + "${tab}${tab}this.stateMachine.fire(${triggerToFire});\n"
            + "${tab}}\n\n";

    String triggerToFire =
        params.isEmpty() ? triggerClassName + "." + trigger : getTriggerCall(triggerClassName, tab);

    Map<String, Object> substitutions =
        Map.of(
            "fire",
            fire,
            "tab",
            tab,
            "trigger",
            StringUtils.toCapitalCamelCase(trigger),
            "paramsWithClasses",
            params.stream().map(Param::getParamDefinition).collect(Collectors.joining(", ")),
            "triggerToFire",
            triggerToFire);

    return VariableSubstitutionService.get().replaceAll(template, substitutions);
  }

  private String getTriggerCall(String triggerClassName, String tab) {
    String template =
        "new TriggerWithParameters${size}<>(\n"
            + "${tab}${tab}${tab}${triggerClassName}.${trigger}, ${paramClasses}),\n"
            + "${tab}${tab}${tab}${paramVars}";
    Map<String, Object> substitutions =
        Map.of(
            "size",
            params.size(),
            "triggerClassName",
            triggerClassName,
            "trigger",
            trigger,
            "paramClasses",
            params.stream()
                .map(Param::parse)
                .map(ParsedParam::getClassName)
                .map(ParsedClass::getWholeName)
                .map(paramClass -> paramClass + ".class")
                .collect(Collectors.joining(", ")),
            "paramVars",
            params.stream().map(Param::getVariableName).collect(Collectors.joining(", ")),
            "tab",
            tab);

    return VariableSubstitutionService.get().replaceAll(template, substitutions);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }

    TriggerWithParameters that = (TriggerWithParameters) obj;

    if (!Objects.equals(trigger, that.trigger)) {
      return false;
    }
    return Objects.equals(params, that.params);
  }

  @Override
  public int hashCode() {
    int result = trigger != null ? trigger.hashCode() : 0;
    result = 31 * result + (params != null ? params.hashCode() : 0);
    return result;
  }

  @Override
  public String toString() {
    return "TriggerWithParameters{" + "trigger='" + trigger + '\'' + ", params=" + params + '}';
  }
}
