package com.github.dovaleac.transformers;

import com.github.dovaleac.jackson.State;
import com.github.dovaleac.jackson.StateMachine;
import com.github.dovaleac.jackson.Transition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AbstractCodeTransformer implements CodeTransformer {

  private final StateMachine stateMachine;

  public AbstractCodeTransformer(StateMachine stateMachine) {
    this.stateMachine = stateMachine;
  }

  public String getClassName() {
    return stateMachine.getClassName();
  }

  public String getStateClassName() {
    return stateMachine.getStates().getClassName();
  }

  public String getStateNames() {
    List<State> elements = stateMachine.getStates().getElements();

    if (elements.isEmpty()) {
      return "";
    }

    StringBuilder sb = new StringBuilder(elements.get(0).getName());

    for (int i = 1; i < elements.size(); i++) {
      sb.append(", ").append(elements.get(i).getName());
    }

    return sb.toString();
  }

  public String getTriggerClassName() {
    return null;
  }

  public String getTriggerNames() {
    List<String> elements = new ArrayList<String>();
    List<Transition> transitions = stateMachine.getTransitions();
    for (Transition transition : transitions) {
      if (!elements.contains(transition.getTrigger())) {
        elements.add(transition.getTrigger());
      }
    }

    if (elements.isEmpty()) {
      return "";
    }

    StringBuilder sb = new StringBuilder(elements.get(0));

    for (int i = 1; i < elements.size(); i++) {
      sb.append(", ").append(elements.get(i));
    }

    return sb.toString();
  }

  public String getDelegateInterfaceName() {
    return stateMachine.getDelegateInterfaceName();
  }

  public String getMethod(String method, Map<String, String> variables) {
    StringBuilder sb = new StringBuilder("  void ");
    sb.append(method);
    sb.append("(");

    if (variables.isEmpty()) {
      return sb.append(");").toString();
    }

    ArrayList<Map.Entry<String, String>> entries =
        new ArrayList<Map.Entry<String, String>>(variables.entrySet());

    sb.append(param(entries.get(0)));

    for (Map.Entry<String, String> entry : entries.subList(1, entries.size() - 1)) {
      sb.append(", ").append(param(entry));
    }

    return sb.toString();
  }

  private String param(Map.Entry<String, String> entry) {
    return entry.getKey() + " " + entry.getValue();
  }
}
