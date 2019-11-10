package com.movements.generated;

import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.triggers.TriggerWithParameters1;
import com.github.oxo42.stateless4j.triggers.TriggerWithParameters2;

public abstract class Delegate<T> {

  protected final StateMachine<StateClassName, Trigger> stateMachine;

  public Delegate(StateClassName initialState) {
    this.stateMachine = new StateMachine<>(initialState, new ClassName().getConfig(this));
  }

  public abstract void entry22(com.movements.nongenerated.ParameterizedClass<String, Double> parameterizedClass, Integer integer);

  public abstract void entry1();

  public abstract void exit31();

  public abstract void exit32();

  public abstract void exit1();

  public abstract void entry21(Integer height);

  public void fireFly() {
    this.stateMachine.fire(Trigger.FLY);
  }

  public void fireJump() {
    this.stateMachine.fire(Trigger.JUMP);
  }

  public void fireFall(Integer height) {
    this.stateMachine.fire(new TriggerWithParameters1<>(
      Trigger.FALL, Integer.class),
      height);
  }

  public void fireWalk(com.movements.nongenerated.ParameterizedClass<String, Double> parameterizedClass, Integer integer) {
    this.stateMachine.fire(new TriggerWithParameters2<>(
      Trigger.WALK, com.movements.nongenerated.ParameterizedClass.class, Integer.class),
      parameterizedClass, integer);
  }


}