package com.movements.nongenerated;

public class ParameterizedClass<FT, ST> {
  private final FT ft;
  private final ST st;

  public ParameterizedClass(FT ft, ST st) {
    this.ft = ft;
    this.st = st;
  }

  @Override
  public String toString() {
    return ft + " " + st;
  }
}
