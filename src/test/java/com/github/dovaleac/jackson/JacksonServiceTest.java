package com.github.dovaleac.jackson;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;

import static org.junit.Assert.*;

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
            new States(
                "StateClassName",
                Arrays.asList(
                    new State(
                        "State1", Arrays.asList(new OnEntry("entry1", null)), Arrays.asList(
                            "exit1"), null),
                    new State(
                        "State2", null, Arrays.asList("exit31", "exit32"), null),
                    new State(
                        "State3",
                        Arrays.asList(new OnEntry("entry21", "FALL"), new OnEntry("entry22",
                                "WALK")),
                        null, null),
                    new State(
                        "State4", null, Arrays.asList("exit31", "exit1"), null),
                    new State(
                        "State1son", null, null, "State1"))),
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
            ));
  }

  @Test
  public void parseYamlFile() throws Exception {
    StateMachine stateMachine =
        JacksonService.parseYamlFile(new File("src/test/resources" + "/stateMachine.yaml"));
    assertEquals(expected, stateMachine);
  }
}
