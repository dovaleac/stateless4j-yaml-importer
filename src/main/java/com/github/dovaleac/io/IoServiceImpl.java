package com.github.dovaleac.io;

import com.github.dovaleac.domain.AllFiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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


    Files.write(folder.resolve(fileNames.getState()),
        allFiles.getState().getBytes());

    Files.write(folder.resolve(fileNames.getStateMachine()),
        allFiles.getStateMachine().getBytes());

    Files.write(folder.resolve(fileNames.getDelegate()),
        allFiles.getDelegate().getBytes());

    Files.write(folder.resolve(fileNames.getTrigger()),
        allFiles.getTrigger().getBytes());

  }
}
