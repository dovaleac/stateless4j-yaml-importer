package com.github.dovaleac.io;

import com.github.dovaleac.domain.AllFiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

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

  void createOrUpdateFile(Path folder, String fileName, String content) throws IOException {
    Files.write(folder.resolve(fileName),
        content.getBytes(),
        StandardOpenOption.TRUNCATE_EXISTING,
        StandardOpenOption.CREATE
    );
  }
}
