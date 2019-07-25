package com.github.dovaleac.io;

import com.github.dovaleac.domain.AllFiles;
import com.github.dovaleac.jackson.StateMachine;

import java.io.IOException;
import java.nio.file.Path;

public interface IoService {

  void createFiles(AllFiles allFiles, AllFiles fileNames, Path folder) throws IOException;
  void createFiles(StateMachine stateMachine, Path folder) throws IOException;
}
