package com.github.dovaleac.util;

import com.github.dovaleac.util.StringUtils;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StringUtilsTest {

  @Test
  public void toCapitalCamelCase() {
    assertEquals("NormalState", StringUtils.toCapitalCamelCase("NORMAL_STATE"));
    assertEquals("NormalState", StringUtils.toCapitalCamelCase("NormalState"));
  }
}