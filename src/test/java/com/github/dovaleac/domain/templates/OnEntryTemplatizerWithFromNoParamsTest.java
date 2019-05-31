package com.github.dovaleac.domain.templates;

import org.junit.Test;

import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class OnEntryTemplatizerWithFromNoParamsTest {

  @Test
  public void test() throws Exception {
    OnEntryTemplatizerWithFromNoParams onEntryTemplatizerNoFromNoParams =
        new OnEntryTemplatizerWithFromNoParams("  ");

    OnEntryCalculationParams params = new OnEntryCalculationParams(
        "delegate",
        "Trigger",
        "State",
        0,
        "PREVIOUS_STATE",
        "method",
        Stream.empty()
    );

    String actual = onEntryTemplatizerNoFromNoParams.apply(params);
    String expected = "      .onEntryFrom(State.PREVIOUS_STATE, () -> delegate.method())";

    assertEquals(expected, actual);

  }
}