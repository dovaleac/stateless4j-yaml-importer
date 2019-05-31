package com.github.dovaleac.domain.templates;

import com.github.dovaleac.jackson.Param;
import org.junit.Test;

import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class OnEntryTemplatizerWithFromWithParamsTest {

  @Test
  public void test1() throws Exception {
    OnEntryTemplatizerWithFromWithParams onEntryTemplatizerNoFromNoParams =
        new OnEntryTemplatizerWithFromWithParams("  ");

    OnEntryCalculationParams params = new OnEntryCalculationParams(
        "delegate",
        "Trigger",
        "State",
        1,
        "PREVIOUS_STATE",
        "method",
        Stream.of(new Param("Integer", "intValue"))
    );

    String actual = onEntryTemplatizerNoFromNoParams.apply(params);
    String expected =
        "      .onEntryFrom(\n"
            + "        new TriggerWithParameters1<>(Trigger.PREVIOUS_STATE, Integer.class),\n"
            + "        (Integer intValue) -> {\n"
            + "        delegate.method(intValue);\n"
            + "      })";

    assertEquals(expected, actual);
  }

  @Test
  public void test2() throws Exception {
    OnEntryTemplatizerWithFromWithParams onEntryTemplatizerNoFromNoParams =
        new OnEntryTemplatizerWithFromWithParams("  ");

    OnEntryCalculationParams params = new OnEntryCalculationParams(
        "delegate",
        "Trigger",
        "State",
        2,
        "PREVIOUS_STATE",
        "method",
        Stream.of(new Param("Integer", "intValue"),
            new Param("String", "stringValue"))
    );

    String actual = onEntryTemplatizerNoFromNoParams.apply(params);
    String expected =
        "      .onEntryFrom(\n"
            + "        new TriggerWithParameters2<>(Trigger.PREVIOUS_STATE, Integer.class, " +
            "String.class),\n"
            + "        (Integer intValue, String stringValue) -> {\n"
            + "        delegate.method(intValue, stringValue);\n"
            + "      })";

    assertEquals(expected, actual);
  }
}