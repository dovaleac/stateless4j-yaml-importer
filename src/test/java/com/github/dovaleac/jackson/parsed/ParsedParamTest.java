package com.github.dovaleac.jackson.parsed;

import org.junit.Test;

import static org.junit.Assert.*;

public class ParsedParamTest {

  @Test
  public void basicClass() {
    String className = "String";

    ParsedClass parsedClass = ParsedClass.parse(className);

    assertEquals("String", parsedClass.getClassName());
    assertEquals("String", parsedClass.getClassNameWithParameter());
    assertEquals("String", parsedClass.getWholeName());
    assertFalse(parsedClass.isParameterized());
  }

  @Test
  public void qualifiedClass() {
    String className = "io.github.String";

    ParsedClass parsedClass = ParsedClass.parse(className);

    assertEquals("String", parsedClass.getClassName());
    assertEquals("String", parsedClass.getClassNameWithParameter());
    assertEquals("io.github.String", parsedClass.getWholeName());
    assertFalse(parsedClass.isParameterized());
  }

  @Test
  public void parameterizedClass() {
    String className = "Flowable<T>";

    ParsedClass parsedClass = ParsedClass.parse(className);

    assertEquals("Flowable", parsedClass.getClassName());
    assertEquals("Flowable<T>", parsedClass.getClassNameWithParameter());
    assertEquals("Flowable", parsedClass.getWholeName());
    assertTrue(parsedClass.isParameterized());
  }

  @Test
  public void qualifiedAndParameterizedClass() {
    String className = "io.github.Flowable<T>";

    ParsedClass parsedClass = ParsedClass.parse(className);

    assertEquals("Flowable", parsedClass.getClassName());
    assertEquals("Flowable<T>", parsedClass.getClassNameWithParameter());
    assertEquals("io.github.Flowable", parsedClass.getWholeName());
    assertTrue(parsedClass.isParameterized());
  }

}