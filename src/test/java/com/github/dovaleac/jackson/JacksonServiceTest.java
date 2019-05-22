package com.github.dovaleac.jackson;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
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
            new States(
                "StateClassName",
                Arrays.asList(
                    new State(
                        "State1", Arrays.asList(new OnEntry("entry1")), Arrays.asList("exit1")),
                    new State(
                        "State2",
                        Arrays.asList(new OnEntry("entry21"), new OnEntry("entry22")),
                        null),
                    new State(
                        "State3", null, Arrays.asList("exit31", "exit32")),
                    new State(
                        "State4", null, Arrays.asList("exit31", "exit1")))),
            Arrays.asList(
                new Transition("State1", "State2", "FLY", null),
                new Transition("State2", "State4", "JUMP", null),
                new Transition("State3", "State4", "JUMP", null),
                new Transition(
                    "State1", "State3", "FALL", Arrays.asList(new Param("Integer", "height"))),
                new Transition(
                    "State2",
                    "State3",
                    "WALK",
                    Arrays.asList(new Param("String", "param1"), new Param("Integer", "param2")))));
  }

  @Test
  public void parseYamlFile() throws Exception {
    StateMachine stateMachine =
        JacksonService.parseYamlFile(new File("src/test/resources" + "/stateMachine.yaml"));
    assertEquals(expected, stateMachine);
  }
}
