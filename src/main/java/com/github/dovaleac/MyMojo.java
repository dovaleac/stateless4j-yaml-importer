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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Goal which touches a timestamp file.
 *
 * @goal touch
 * @phase process-sources
 */
public class MyMojo extends AbstractMojo {

  /**
   * Location of the file.
   *
   * @parameter expression="${project.build.directory}"
   * @required
   */
  private File outputDirectory;

  public void execute() throws MojoExecutionException {
    File file = outputDirectory;

    if (!file.exists()) {
      file.mkdirs();
    }

    File touch = new File(file, "touch.txt");

    FileWriter fileWriter = null;
    try {
      fileWriter = new FileWriter(touch);

      fileWriter.write("touch.txt");
    } catch (IOException ex) {
      throw new MojoExecutionException("Error creating file " + touch, ex);
    } finally {
      if (fileWriter != null) {
        try {
          fileWriter.close();
        } catch (IOException ex) {
          // ignore
        }
      }
    }
  }
}