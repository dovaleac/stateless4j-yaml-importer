package com.github.dovaleac.gatherers.statemachine.templates;

import org.junit.Test;

import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class OnEntryTemplatizerNoFromNoParamsTest {

  @Test
  public void test() throws Exception {
    OnEntryTemplatizerNoFromNoParams onEntryTemplatizerNoFromNoParams =
        new OnEntryTemplatizerNoFromNoParams("  ");

    OnEntryCalculationParams params = new OnEntryCalculationParams(
        "delegate",
        "Trigger",
        "State",
        0,
        null,
        "method",
        Stream.empty()
    );

    String actual = onEntryTemplatizerNoFromNoParams.apply(params);
    String expected = "      .onEntry(() -> delegate.method())";

    assertEquals(expected, actual);

  }
}