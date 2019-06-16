package com.github.dovaleac.domain.templates;

import com.github.dovaleac.jackson.Param;
import org.junit.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

  @Test
  public void testParameterized1() throws Exception {
    OnEntryTemplatizerWithFromWithParams onEntryTemplatizerNoFromNoParams =
        new OnEntryTemplatizerWithFromWithParams("  ");

    OnEntryCalculationParams params = new OnEntryCalculationParams(
        "delegate",
        "Trigger",
        "State",
        2,
        "PREVIOUS_STATE",
        "method",
        Stream.of(new Param("List<Integer>", "ints"),
            new Param("String", "stringValue"))
    );

    String actual = onEntryTemplatizerNoFromNoParams.apply(params);
    String expected =
        "      .onEntryFrom(\n"
            + "        new TriggerWithParameters2<>(Trigger.PREVIOUS_STATE, List.class, String.class),\n"
            + "        (List ints, String stringValue) -> {\n"
            + "        List<Integer> ints1 = (List<Integer>) ints;\n"
            + "        delegate.method(ints1, stringValue);\n"
            + "      })";

    assertEquals(expected, actual);
  }

  @Test
  public void testParameterized2() throws Exception {
    OnEntryTemplatizerWithFromWithParams onEntryTemplatizerNoFromNoParams =
        new OnEntryTemplatizerWithFromWithParams("  ");

    OnEntryCalculationParams params = new OnEntryCalculationParams(
        "delegate",
        "Trigger",
        "State",
        2,
        "PREVIOUS_STATE",
        "method",
        Stream.of(new Param("List<Integer>", "ints"),
            new Param("Stream<String>", "strings"))
    );

    String actual = onEntryTemplatizerNoFromNoParams.apply(params);
    String expected1 =
        "      .onEntryFrom(\n"
            + "        new TriggerWithParameters2<>(Trigger.PREVIOUS_STATE, List.class, Stream.class),\n"
            + "        (List ints, Stream strings) -> {\n"
            + "        List<Integer> ints1 = (List<Integer>) ints;\n"
            + "        Stream<String> strings1 = (Stream<String>) strings;\n"
            + "        delegate.method(ints1, strings1);\n"
            + "      })";

    String expected2 =
        "      .onEntryFrom(\n"
            + "        new TriggerWithParameters2<>(Trigger.PREVIOUS_STATE, List.class, Stream.class),\n"
            + "        (List ints, Stream strings) -> {\n"
            + "        Stream<String> strings1 = (Stream<String>) strings;\n"
            + "        List<Integer> ints1 = (List<Integer>) ints;\n"
            + "        delegate.method(ints1, strings1);\n"
            + "      })";

    assertTrue(List.of(expected1, expected2).contains(actual));
  }
}