package com.github.dovaleac.gatherers.statemachine;

import com.github.dovaleac.domain.Method;
import com.github.dovaleac.domain.StateConfiguration;
import com.github.dovaleac.gatherers.methods.MethodGatherer;
import com.github.dovaleac.jackson.JacksonService;
import com.github.dovaleac.jackson.Param;
import com.github.dovaleac.jackson.StateMachine;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class StateConfigGathererImplTest {

  private StateMachine stateMachine;
  private final String packageName = "com.github.dovaleac";

  @Before
  public void setUp() throws Exception {
    stateMachine = JacksonService.parseYamlFile(new File("src/test/resources/stateMachine.yaml"));
  }

  @Test
  public void getStateConfigurations() throws Exception {
    Map<String, Method> onEntryMethods =
        MethodGatherer.getInstance()
            .gatherOnEntryMethods(stateMachine)
            .collect(Collectors.toMap(Method::getName, m -> m));

    Map<String, Method> onExitMethods =
        MethodGatherer.getInstance()
            .gatherOnExitMethods(stateMachine)
            .collect(Collectors.toMap(Method::getName, m -> m));

    List<StateConfiguration> actual =
        StateConfigGathererImpl.getInstance()
            .produceStateConfigurations(stateMachine, onEntryMethods, onExitMethods)
            .collect(Collectors.toList());

    List<StateConfiguration> expected =
        List.of(
            new StateConfiguration(
                "State1",
                null, List.of(new Method("entry1")),
                List.of(new Method("exit1")),
                List.of("JUMP"), Map.of("FLY", "State2", "FALL", "State3")),
            new StateConfiguration(
                "State2",
                null, List.of(),
                List.of(new Method("exit31"), new Method("exit32")),
                List.of(), Map.of("WALK", "State3", "JUMP", "State4")),
            new StateConfiguration(
                "State3",
                null, List.of(
                new Method("entry21", "FALL", new Param("Integer", "height")),
                new Method(
                    "entry22",
                    "WALK",
                    new Param("String", "param1"),
                    new Param("Integer", "param2"))),
                List.of(),
                List.of(), Map.of("JUMP", "State4")),
            new StateConfiguration(
                "State4", null, List.of(), List.of(new Method("exit31"), new Method(
                "exit1")), List.of(), Map.of()),
            new StateConfiguration(
                "State1son", "State1", List.of(), List.of(), List.of(), Map.of())
        );

    assertTrue(actual.containsAll(expected));
    assertTrue(expected.containsAll(actual));
    assertEquals(expected.size(), actual.size());
  }

}