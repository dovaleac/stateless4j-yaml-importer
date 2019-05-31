package com.github.dovaleac.domain;

import com.github.dovaleac.jackson.JacksonService;
import com.github.dovaleac.jackson.StateMachine;
import com.github.dovaleac.producers.ProducerImpl;
import org.junit.Test;

import java.io.File;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class StateConfigurationTest {

  @Test
  public void produceConfigurationText() throws Exception{
    StateMachine stateMachine =
        JacksonService.parseYamlFile(new File("src/test/resources" + "/stateMachine.yaml"));

    ProducerImpl producer = new ProducerImpl();

    Map<String, Method> onEntryMethods =
        producer
            .gatherOnEntryMethods(stateMachine)
            .collect(Collectors.toMap(Method::getName, m -> m));

    Map<String, Method> onExitMethods =
        producer
            .gatherOnExitMethods(stateMachine)
            .collect(Collectors.toMap(Method::getName, m -> m));


    String configText = producer.produceStateConfigurations(stateMachine, onEntryMethods, onExitMethods)
        .map((StateConfiguration tab) -> tab.produceConfigurationText("  ",
            "stateMachine", stateMachine.getTriggerClassName(),
            stateMachine.getStates().getClassName(), stateMachine.getDelegateInterfaceName().toLowerCase()))
        .collect(Collectors.joining("\n\n"));

    System.out.println(configText);
  }
}