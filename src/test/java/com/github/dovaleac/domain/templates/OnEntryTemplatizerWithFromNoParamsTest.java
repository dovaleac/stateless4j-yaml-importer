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
        "TRIGGER",
        "method",
        Stream.empty()
    );

    String actual = onEntryTemplatizerNoFromNoParams.apply(params);
    String expected = "      .onEntryFrom(Trigger.TRIGGER, () -> delegate.method())";

    assertEquals(expected, actual);

  }
}