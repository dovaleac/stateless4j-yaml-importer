package com.github.dovaleac.domain;

public class AllFiles {
  private final String state;
  private final String stateMachine;
  private final String delegate;
  private final String trigger;

  public AllFiles(String state, String stateMachine, String delegate, String trigger) {
    this.state = state;
    this.stateMachine = stateMachine;
    this.delegate = delegate;
    this.trigger = trigger;
  }

  public String getState() {
    return state;
  }

  public String getStateMachine() {
    return stateMachine;
  }

  public String getDelegate() {
    return delegate;
  }

  public String getTrigger() {
    return trigger;
  }
}
