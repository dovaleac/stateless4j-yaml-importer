package com.github.dovaleac.io;

import com.github.dovaleac.FileToGenerate;
import com.github.dovaleac.domain.AllFiles;
import com.github.dovaleac.jackson.StateMachine;
import com.github.dovaleac.substitution.Substitutions;
import com.github.dovaleac.substitution.VariableSubstitutionService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.stream.Stream;

public class IoServiceImpl implements IoService {
  @Override
  public void createFiles(AllFiles allFiles, AllFiles fileNames, Path folder) throws IOException {

    if ( Files.exists(folder) && !Files.isDirectory(folder)) {
      throw new IOException("Folder " + folder + " is not a directory");
    }

    if ( Files.exists(folder) && !Files.isWritable(folder)) {
      throw new IOException("Folder " + folder + " is not writable");
    }

    if ( Files.exists(folder) && !Files.isReadable(folder)) {
      throw new IOException("Folder " + folder + " is not readable");
    }

    if (!Files.exists(folder)) {
      Files.createDirectories(folder);
    }

    createOrUpdateFile(folder, fileNames.getState(), allFiles.getState());

    createOrUpdateFile(folder, fileNames.getStateMachine(), allFiles.getStateMachine());

    createOrUpdateFile(folder, fileNames.getDelegate(), allFiles.getDelegate());

    createOrUpdateFile(folder, fileNames.getTrigger(), allFiles.getTrigger());

  }

  @Override
  public void createFiles(StateMachine stateMachine, Path folder) throws IOException {
    try {
      Stream.of(FileToGenerate.values())
          .forEach(fileToGenerate -> {
            try {
              Path file = fileToGenerate.getFileDestination(stateMachine, folder);
              String content = VariableSubstitutionService.get()
                  .replaceAll(fileToGenerate.getTemplate(), Substitutions.getInstance());
              createOrUpdateFile(file, content);
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          });
    } catch (RuntimeException e) {
      if (e.getCause() instanceof IOException) {
        throw ((IOException) e.getCause());
      } else {
        throw e;
      }
    }
  }

  void createOrUpdateFile(Path folder, String fileName, String content) throws IOException {
    Files.write(folder.resolve(fileName),
        content.getBytes(),
        StandardOpenOption.TRUNCATE_EXISTING,
        StandardOpenOption.CREATE
    );
  }

  void createOrUpdateFile(Path file, String content) throws IOException {
    Files.write(file,
        content.getBytes(),
        StandardOpenOption.TRUNCATE_EXISTING,
        StandardOpenOption.CREATE
    );
  }
}
