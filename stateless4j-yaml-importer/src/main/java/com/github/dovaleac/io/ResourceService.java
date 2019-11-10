package com.github.dovaleac.io;

public interface ResourceService {

  String getStateTemplate();

  String getStateMachineTemplate();

  String getTriggerTemplate();

  String getDelegateTemplate();

  static ResourceService get() {
    return ResourceServiceImpl.getInstance();
  }
}
