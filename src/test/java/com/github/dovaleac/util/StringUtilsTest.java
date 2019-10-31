package com.github.dovaleac.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class StringUtilsTest {

  @Test
  public void toCapitalCamelCase() {
    assertEquals("NormalState", StringUtils.toCapitalCamelCase("NORMAL_STATE"));
    assertEquals("NormalState", StringUtils.toCapitalCamelCase("NormalState"));
  }
}