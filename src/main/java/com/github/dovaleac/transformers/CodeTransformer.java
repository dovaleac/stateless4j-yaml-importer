package com.github.dovaleac.transformers;

import java.util.Map;

public interface CodeTransformer {

  String getClassName();

  String getStateClassName();

  String getStateNames();

  String getTriggerClassName();

  String getTriggerNames();

  String getDelegateInterfaceName();

  String getMethod(String method, Map<String, String> variables);



}
