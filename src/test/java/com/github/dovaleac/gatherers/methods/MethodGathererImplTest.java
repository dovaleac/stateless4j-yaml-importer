package com.github.dovaleac.gatherers.methods;

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

public class MethodGathererImplTest {


  private StateMachine stateMachine;

  @Before
  public void setUp() throws Exception {
    stateMachine = JacksonService.parseYamlFile(new File("src/test/resources/stateMachine.yaml"));
  }

  @Test
  public void onEntryMethods() throws Exception {

    List<Method> actual =
        MethodGatherer.getInstance()
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
        MethodGatherer.getInstance()
            .gatherOnExitMethods(stateMachine)
            .peek(System.out::println)
            .collect(Collectors.toList());

    List<Method> expected =
        List.of(new Method("exit1"), new Method("exit31"), new Method("exit32"));

    assertTrue(actual.containsAll(expected));
    assertTrue(expected.containsAll(actual));
    assertEquals(expected.size(), actual.size());
  }
}