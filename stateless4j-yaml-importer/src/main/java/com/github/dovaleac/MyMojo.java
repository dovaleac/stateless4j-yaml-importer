package com.github.dovaleac;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.github.dovaleac.diagrams.PlantUmlDiagramService;
import com.github.dovaleac.domain.ExecutionConfig;
import com.github.dovaleac.exceptions.ValidationException;
import com.github.dovaleac.io.IoService;
import com.github.dovaleac.io.IoServiceImpl;
import com.github.dovaleac.jackson.JacksonService;
import com.github.dovaleac.jackson.State;
import com.github.dovaleac.jackson.StateMachine;
import com.github.dovaleac.substitution.Substitutions;
import net.sourceforge.plantuml.SourceStringReader;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Goal which touches a timestamp file.
 *
 * @goal touch
 * @phase process-sources
 */
@Mojo(name = "generate", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class MyMojo extends AbstractMojo {

  @Parameter(property = "com.dovaleac.stateless4j.yamlimporter.package", required = true)
  private String packageName;

  @Parameter(property = "com.dovaleac.stateless4j.yamlimporter.yamlfile", required = true)
  private File yamlFileLocation;

  @Parameter(property = "com.dovaleac.stateless4j.yamlimporter.destination", required = true)
  private String destinationFolder;

  @Parameter(property = "com.dovaleac.stateless4j.yamlimporter.diagram.generate", required =
      false, defaultValue = "false")
  private boolean generateDiagram;

  @Parameter(property = "com.dovaleac.stateless4j.yamlimporter.diagram.destination", required =
      false, defaultValue = "src/main/resources/diagrams")
  private String diagramDestination;

  @Parameter(property = "com.dovaleac.stateless4j.yamlimporter.diagram.fileName", required =
      false, defaultValue = "states.png")
  private String diagramFileName;

  @Parameter(property = "com.dovaleac.stateless4j.yamlimporter.diagram.initialState", required =
      false)
  private String initialState;

  @Parameter(property = "com.dovaleac.stateless4j.yamlimporter.spacesForTab", required = false,
      defaultValue = "2")
  private int spacesForTab;

  @Parameter(property = "com.dovaleac.stateless4j.yamlimporter.useTab", required = false,
      defaultValue = "false")
  private boolean useTab;

  @Parameter(property = "com.dovaleac.stateless4j.yamlimporter.variableName", required = false,
      defaultValue = "config")
  private String variableName;

  public void execute() throws MojoExecutionException {
    try {
      StateMachine stateMachine = JacksonService.parseYamlFile(yamlFileLocation);

      ExecutionConfig options = generateConfig();

      Substitutions.init(stateMachine, options);

      IoService ioService = new IoServiceImpl();
      ioService.createFiles(stateMachine, Paths.get(destinationFolder));

      if (generateDiagram) {
        if (diagramDestination == null) {
          diagramDestination = destinationFolder;
        }

        if (initialState == null) {
          initialState = stateMachine.getStates().getElements()
                  .stream()
                  .filter(State::isInitial)
                  .findAny()
                  .orElseThrow(() -> new ValidationException("No initial state provided"))
                  .getName();
        }

        String diagramContent = new PlantUmlDiagramService()
            .generatePlantUmlDiagramTxt(stateMachine, initialState, true);

        Path diagramDestinationAsPath = Paths.get(diagramDestination);
        Files.createDirectories(diagramDestinationAsPath);
        File pngFile = diagramDestinationAsPath.resolve(diagramFileName).toFile();
        OutputStream png = new FileOutputStream(pngFile);

        SourceStringReader reader = new SourceStringReader(diagramContent);
        reader.outputImage(png);
      }

    } catch (IOException | ValidationException ex) {
      ex.printStackTrace();
      throw new MojoExecutionException(ex.getMessage());
    }
  }

  ExecutionConfig generateConfig() {
    ExecutionConfig config = new ExecutionConfig(packageName);

    if (useTab) {
      config = config.withTab("\t");
    } else {
      StringBuilder stringBuilder = new StringBuilder();
      for (int i = 0; i < spacesForTab; i++) {
        stringBuilder.append(" ");
      }
      config = config.withTab(stringBuilder.toString());
    }

    config.withVariableName(variableName);
    return config;
  }
}
