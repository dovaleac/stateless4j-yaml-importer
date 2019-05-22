package com.github.dovaleac.producers;

import com.github.dovaleac.jackson.JacksonService;
import com.github.dovaleac.jackson.StateMachine;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class ProducerImplTest {

  @Test
  public void state() throws Exception {
    StateMachine stateMachine = JacksonService.parseYamlFile(new File("src/test/resources/stateMachine.yaml"));

    String packageName = "com.github.dovaleac";

    String actual = new ProducerImpl().produceState(packageName, stateMachine.getStates());

    String expected = "package com.github.dovaleac;\n" +
        "\n" +
        "public enum StateClassName {\n" +
        "  State1, State2, State3, State4\n" +
        "}";

    assertEquals(expected, actual);
  }

  @Test
  public void trigger() throws Exception {
    StateMachine stateMachine = JacksonService.parseYamlFile(new File("src/test/resources/stateMachine.yaml"));

    String packageName = "com.github.dovaleac";

    String actual = new ProducerImpl().produceTrigger(packageName, stateMachine);

    String expected = "package com.github.dovaleac;\n"
            + "\n"
            + "public enum Trigger {\n"
            + "  FALL, FLY, JUMP, WALK\n"
            + "}";

    assertEquals(expected, actual);
  }
}