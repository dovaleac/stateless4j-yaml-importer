package com.github.dovaleac.io;

import com.github.dovaleac.domain.AllFiles;

import java.io.IOException;
import java.nio.file.Path;

public interface IoService {

  void createFiles(AllFiles allFiles, AllFiles fileNames, Path folder) throws IOException;
}
