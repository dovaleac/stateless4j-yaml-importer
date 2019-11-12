package com.github.dovaleac.diagrams;

import com.github.dovaleac.jackson.StateMachine;

public interface DiagramService {
  String generatePlantUmlDiagramTxt(StateMachine stateMachine, String initialState,
      boolean printLabels);
}
