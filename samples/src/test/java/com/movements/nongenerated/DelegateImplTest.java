package com.movements.nongenerated;

import com.movements.generated.StateClassName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DelegateImplTest {

  @Test
  void test() {
    DelegateImpl delegate = new DelegateImpl(StateClassName.State1);

    delegate.fireFly();
    delegate.fireWalk(new ParameterizedClass("asd", 3.5), 0);

    delegate.fireJump();

    assertEquals(StateClassName.State4, delegate.getCurrentState());
    String expectedHistory = "exit1\n" +
        "exit31\n" +
        "exit32\n" +
        "entry22 asd 3.5 0\n";
    assertEquals(expectedHistory, delegate.currentHistory());
  }
}