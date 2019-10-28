package com.github.dovaleac.util;

import org.apache.commons.lang3.text.WordUtils;

public class StringUtils {

  public static String toCapitalCamelCase(String otherCase) {
    return WordUtils.capitalizeFully(otherCase.replaceAll("_", " ")).replaceAll(" ", "");
  }
}
