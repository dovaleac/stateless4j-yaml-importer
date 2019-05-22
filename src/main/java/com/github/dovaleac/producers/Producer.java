package com.github.dovaleac.producers;

import com.github.dovaleac.jackson.States;

import java.io.IOException;

public interface Producer {
  String produceState(String packageName, States states) throws IOException;
}
