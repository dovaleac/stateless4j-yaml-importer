package com.github.dovaleac.diagrams;

import com.github.dovaleac.jackson.JacksonService;
import com.github.dovaleac.jackson.StateMachine;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class PlantUmlDiagramServiceTest {


  private StateMachine stateMachine;

  @Before
  public void setUp() throws Exception {
    stateMachine = JacksonService.parseYamlFile(new File(
        "src/test/resources/repeatedOnEntryWithoutFrom.yaml"));
  }

  @Test
  public void diagram() {
    String actual = new PlantUmlDiagramService()
        .generatePlantUmlDiagramTxt(stateMachine, "State0", true);

    String expected = "@startuml\n" +
        "[*] --> State0\n" +
        "State0 --> State1 : TO_1(Integer i)\n" +
        "State2 --> State1 : TO_1(Integer i)\n" +
        "State0 --> State2 : TO_2(Integer i)\n" +
        "State1 --> State2 : TO_2(Integer i)\n" +
        "State2 --> State2 : TO_2(Integer i)\n" +
        "State1 --> State1 : TO_1(Integer i)\n" +
        "State1: ON ENTRY METHODS\n" +
        "State1: onEntry()\n" +
        "State2: ON ENTRY METHODS\n" +
        "State2: onEntry()\n" +
        "State1: ON EXIT METHODS\n" +
        "State1: onExit()\n" +
        "State2: ON EXIT METHODS\n" +
        "State2: onExit()\n" +
        "@enduml";

    assertEquals(expected, actual);
  }
}