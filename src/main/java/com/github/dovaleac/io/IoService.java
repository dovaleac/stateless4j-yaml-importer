package com.github.dovaleac.io;

import com.github.dovaleac.jackson.StateMachine;

import java.io.IOException;
import java.nio.file.Path;

public interface IoService {

  void createFiles(StateMachine stateMachine, Path folder) throws IOException;

  void createOrUpdateFile(Path file, String content) throws IOException;
}
