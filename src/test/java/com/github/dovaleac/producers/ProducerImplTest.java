package com.github.dovaleac.producers;

import com.github.dovaleac.domain.Method;
import com.github.dovaleac.jackson.JacksonService;
import com.github.dovaleac.jackson.Param;
import com.github.dovaleac.jackson.StateMachine;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ProducerImplTest {

  private StateMachine stateMachine;
  private final String packageName = "com.github.dovaleac";

  @Before
  public void setUp() throws Exception {
    stateMachine = JacksonService.parseYamlFile(new File("src/test/resources/stateMachine.yaml"));
  }

  @Test
  public void state() throws Exception {

    String actual = new ProducerImpl().produceState(packageName, stateMachine.getStates());

    String expected =
        "package com.github.dovaleac;\n"
            + "\n"
            + "public enum StateClassName {\n"
            + "  State1, State2, State3, State4\n"
            + "}";

    assertEquals(expected, actual);
  }

  @Test
  public void trigger() throws Exception {

    String actual = new ProducerImpl().produceTrigger(packageName, stateMachine);

    String expected = "package com.github.dovaleac;\n"
            + "\n"
            + "public enum Trigger {\n"
            + "  FALL, FLY, JUMP, WALK\n"
            + "}";

    assertEquals(expected, actual);
  }

  @Test
  public void onEntryMethods() throws Exception {

    List<Method> actual = new ProducerImpl().gatherOnEntryMethods(stateMachine)
        .peek(System.out::println)
        .collect(Collectors.toList());

    List<Method> expected = List.of(
        new Method("entry1"),
        new Method("entry21", Arrays.asList(new Param("Integer", "height"))),
        new Method("entry22", Arrays.asList(new Param("String", "param1"), new Param("Integer",
            "param2")))
    );

    assertTrue(actual.containsAll(expected));
    assertTrue(expected.containsAll(actual));
    assertEquals(expected.size(), actual.size());
  }

  @Test
  public void onExitMethods() throws Exception {

    List<Method> actual = new ProducerImpl().gatherOnExitMethods(stateMachine)
        .peek(System.out::println)
        .collect(Collectors.toList());

    List<Method> expected = List.of(
        new Method("exit1"),
        new Method("exit31"),
        new Method("exit32")
    );

    assertTrue(actual.containsAll(expected));
    assertTrue(expected.containsAll(actual));
    assertEquals(expected.size(), actual.size());
  }
}
