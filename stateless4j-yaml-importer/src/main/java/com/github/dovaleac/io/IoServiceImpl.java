package com.github.dovaleac.io;

import com.github.dovaleac.FileToGenerate;
import com.github.dovaleac.jackson.StateMachine;
import com.github.dovaleac.substitution.VariableSubstitutionService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class IoServiceImpl implements IoService {

  @Override
  public void createFiles(StateMachine stateMachine, Path folder) throws IOException {
    try {
      FileToGenerate.values()
          .forEach(
              fileToGenerate -> {
                try {
                  Path file = fileToGenerate.getFileDestination(stateMachine, folder);
                  String content =
                      VariableSubstitutionService.get().replaceAll(fileToGenerate.getTemplate());
                  createOrUpdateFile(file, content);
                } catch (IOException ex) {
                  throw new RuntimeException(ex);
                }
              });
    } catch (RuntimeException ex) {
      if (ex.getCause() instanceof IOException) {
        throw ((IOException) ex.getCause());
      } else {
        throw ex;
      }
    }
  }

  void createOrUpdateFile(Path folder, String fileName, String content) throws IOException {
    Files.write(
        folder.resolve(fileName),
        content.getBytes(),
        StandardOpenOption.TRUNCATE_EXISTING,
        StandardOpenOption.CREATE);
  }

  @Override
  public void createOrUpdateFile(Path file, String content) throws IOException {
    Files.write(
        file, content.getBytes());
  }
}
