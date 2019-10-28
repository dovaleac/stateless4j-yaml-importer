package com.github.dovaleac.gatherers.statemachine.templates;

import com.github.dovaleac.jackson.Param;
import org.junit.Test;

import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class OnEntryTemplatizerNoFromWithParamsTest {

  @Test
  public void test1() throws Exception {
    OnEntryTemplatizerNoFromWithParams onEntryTemplatizerNoFromNoParams =
        new OnEntryTemplatizerNoFromWithParams("  ");

    OnEntryCalculationParams params = new OnEntryCalculationParams(
        "delegate",
        "Trigger",
        "State",
        1,
        null,
        "method",
        Stream.of(new Param("Integer", "intValue"))
    );

    String actual = onEntryTemplatizerNoFromNoParams.apply(params);
    String expected =
        "      .onEntry(new Action1<Transition<Trigger, State>, Object[]>() {\n"
            + "        public void doIt(Transition<Trigger, State> trans, Object[] args) {\n"
            + "          delegate.method((Integer) args[0]);\n"
            + "        }})";

    assertEquals(expected, actual);
  }

  @Test
  public void test2() throws Exception {
    OnEntryTemplatizerNoFromWithParams onEntryTemplatizerNoFromNoParams =
        new OnEntryTemplatizerNoFromWithParams("  ");

    OnEntryCalculationParams params = new OnEntryCalculationParams(
        "delegate",
        "Trigger",
        "State",
        2,
        null,
        "method",
        Stream.of(new Param("Integer", "intValue"),
            new Param("String", "stringValue"))
    );

    String actual = onEntryTemplatizerNoFromNoParams.apply(params);
    String expected =
        "      .onEntry(new Action2<Transition<Trigger, State>, Object[]>() {\n"
            + "        public void doIt(Transition<Trigger, State> trans, Object[] args) {\n"
            + "          delegate.method((Integer) args[0], (String) args[1]);\n"
            + "        }})";

    assertEquals(expected, actual);
  }
}