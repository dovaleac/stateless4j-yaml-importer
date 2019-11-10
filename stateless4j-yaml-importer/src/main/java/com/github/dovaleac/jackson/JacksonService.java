package com.github.dovaleac.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;

public class JacksonService {

  public static StateMachine parseYamlFile(File yamlFile) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
    return objectMapper.readValue(yamlFile, StateMachine.class);
  }
}
