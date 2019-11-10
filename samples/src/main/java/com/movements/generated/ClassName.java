package com.movements.generated;

import com.github.oxo42.stateless4j.StateMachineConfig;
import com.github.oxo42.stateless4j.triggers.TriggerWithParameters1;
import com.github.oxo42.stateless4j.triggers.TriggerWithParameters2;
import com.movements.nongenerated.ParameterizedClass;

public class ClassName<T, T2> {

  public StateMachineConfig<StateClassName, Trigger> getConfig(Delegate<T> delegate) {
    StateMachineConfig<StateClassName, Trigger> config =
        new StateMachineConfig<>();

    config.configure(StateClassName.State1)
      .permit(Trigger.FLY, StateClassName.State2)
      .permit(Trigger.FALL, StateClassName.State3)
      .onExit(() -> delegate.exit1())
      .onEntry(() -> delegate.entry1());

    config.configure(StateClassName.State2)
      .permit(Trigger.WALK, StateClassName.State3)
      .permit(Trigger.JUMP, StateClassName.State4)
      .onExit(() -> delegate.exit31())
      .onExit(() -> delegate.exit32());

    config.configure(StateClassName.State3)
      .permit(Trigger.JUMP, StateClassName.State4)
      .onEntryFrom(
        new TriggerWithParameters1<>(Trigger.FALL, Integer.class),
        (Integer height) -> {
        delegate.entry21(height);
      }, Integer.class)
      .onEntryFrom(
        new TriggerWithParameters2<>(Trigger.WALK, ParameterizedClass.class, Integer.class),
        (ParameterizedClass parameterizedClass, Integer integer) -> {
        ParameterizedClass<String, Double> parameterizedClass1 = (ParameterizedClass<String, Double>) parameterizedClass;
        delegate.entry22(parameterizedClass1, integer);
      }, ParameterizedClass.class, Integer.class);

    config.configure(StateClassName.State4)
      .onExit(() -> delegate.exit31())
      .onExit(() -> delegate.exit1());

    config.configure(StateClassName.State1son)
      .substateOf(StateClassName.State1);

    return config;
  }
}