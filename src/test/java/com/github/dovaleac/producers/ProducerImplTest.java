package com.github.dovaleac.producers;

import com.github.dovaleac.domain.Method;
import com.github.dovaleac.domain.StateConfiguration;
import com.github.dovaleac.jackson.JacksonService;
import com.github.dovaleac.jackson.Param;
import com.github.dovaleac.jackson.StateMachine;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

    String expected =
        "package com.github.dovaleac;\n"
            + "\n"
            + "public enum Trigger {\n"
            + "  FALL, FLY, JUMP, WALK\n"
            + "}";

    assertEquals(expected, actual);
  }

  @Test
  public void onEntryMethods() throws Exception {

    List<Method> actual =
        new ProducerImpl()
            .gatherOnEntryMethods(stateMachine)
            .peek(System.out::println)
            .collect(Collectors.toList());

    List<Method> expected =
        List.of(
            new Method("entry1"),
            new Method("entry21", "FALL", Arrays.asList(new Param("Integer", "height"))),
            new Method(
                "entry22",
                "WALK",
                Arrays.asList(new Param("String", "param1"), new Param("Integer", "param2"))));

    assertTrue(actual.containsAll(expected));
    assertTrue(expected.containsAll(actual));
    assertEquals(expected.size(), actual.size());
  }

  @Test
  public void onExitMethods() throws Exception {

    List<Method> actual =
        new ProducerImpl()
            .gatherOnExitMethods(stateMachine)
            .peek(System.out::println)
            .collect(Collectors.toList());

    List<Method> expected =
        List.of(new Method("exit1"), new Method("exit31"), new Method("exit32"));

    assertTrue(actual.containsAll(expected));
    assertTrue(expected.containsAll(actual));
    assertEquals(expected.size(), actual.size());
  }

  @Test
  public void delegate() throws Exception {

    String actual = new ProducerImpl().produceInterface(packageName, stateMachine);

    String expected =
        "package com.github.dovaleac;\n"
            + "\n"
            + "public interface Delegate {\n"
            + "  void entry1();\n"
            + "\n"
            + "  void entry21(Integer height);\n"
            + "\n"
            + "  void entry22(String param1, Integer param2);\n"
            + "\n"
            + "  void exit1();\n"
            + "\n"
            + "  void exit31();\n"
            + "\n"
            + "  void exit32();\n"
            + "}";

    assertEquals(expected, actual);
  }

  @Test
  public void getStateConfigurations() throws Exception {
    ProducerImpl producer = new ProducerImpl();

    Map<String, Method> onEntryMethods =
        producer
            .gatherOnEntryMethods(stateMachine)
            .collect(Collectors.toMap(Method::getName, m -> m));

    Map<String, Method> onExitMethods =
        producer
            .gatherOnExitMethods(stateMachine)
            .collect(Collectors.toMap(Method::getName, m -> m));

    List<StateConfiguration> actual =
        producer
            .produceStateConfigurations(stateMachine, onEntryMethods, onExitMethods)
            .peek(System.out::println)
            .collect(Collectors.toList());

    List<StateConfiguration> expected =
        List.of(
            new StateConfiguration(
                "State1",
                List.of(new Method("entry1")),
                List.of(new Method("exit1")),
                Map.of("FLY", "State2", "FALL", "State3")),
            new StateConfiguration(
                "State2",
                List.of(),
                List.of(new Method("exit31"), new Method("exit32")),
                Map.of("WALK", "State3", "JUMP", "State4")),
            new StateConfiguration(
                "State3",
                List.of(
                    new Method("entry21", "FALL", new Param("Integer", "height")),
                    new Method(
                        "entry22",
                        "WALK",
                        new Param("String", "param1"),
                        new Param("Integer", "param2"))),
                List.of(),
                Map.of("JUMP", "State4")),
            new StateConfiguration(
                "State4", List.of(), List.of(new Method("exit31"), new Method("exit1")), Map.of()));

    expected.forEach(System.out::println);

    assertTrue(actual.containsAll(expected));
    assertTrue(expected.containsAll(actual));
    assertEquals(expected.size(), actual.size());
  }

  @Test
  public void produceStateRepresentation() throws Exception {
    String actual = new ProducerImpl().produceStateMachine(packageName, stateMachine, "  ",
        "config");

    System.out.println(actual);
  }
}
