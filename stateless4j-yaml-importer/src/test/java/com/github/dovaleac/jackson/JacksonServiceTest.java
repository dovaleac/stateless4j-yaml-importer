package com.github.dovaleac.jackson;

import com.github.dovaleac.jackson.*;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class JacksonServiceTest {

  private StateMachine expected;

  @Before
  public void setUp() throws Exception {
    expected =
        new StateMachine(
            "ClassName",
            "Trigger",
            "Delegate",
            "delegate",
            "GeneratedStateMachine", new States(
                "StateClassName",
                Arrays.asList(
                    new State(
                        "State1", Arrays.asList(new OnEntry("entry1", null)), Arrays.asList(
                            "exit1"), List.of("JUMP"), null, true),
                    new State(
                        "State2", List.of(), Arrays.asList("exit31", "exit32"), List.of(), null, false),
                    new State(
                        "State3",
                        Arrays.asList(new OnEntry("entry21", "FALL"), new OnEntry("entry22",
                                "WALK")),
                        List.of(), List.of(), null, false),
                    new State(
                        "State4", List.of(), Arrays.asList("exit31", "exit1"), List.of(), null, false),
                    new State(
                        "State1son", List.of(), List.of(), List.of(), "State1", false))),
            Arrays.asList(
                new Transition("State1", "State2", "FLY"),
                new Transition("State2", "State4", "JUMP"),
                new Transition("State3", "State4", "JUMP"),
                new Transition(
                    "State1", "State3", "FALL"),
                new Transition(
                    "State2",
                    "State3",
                    "WALK")),
            Arrays.asList(
                new TriggerWithParameters("FALL", Arrays.asList(new Param("Integer", "height"))),
                new TriggerWithParameters("WALK", Arrays.asList(new Param("String", "param1"), new Param("Integer", "param2")))
            ), List.of("T", "T2"), List.of("T"), new EventLog("logTriggerName"));
  }

  @Test
  public void parseYamlFile() throws Exception {
    StateMachine stateMachine =
        JacksonService.parseYamlFile(new File("src/test/resources" + "/stateMachine.yaml"));
    assertEquals(expected, stateMachine);
  }
}
