package com.beust.jcommander.converters;

import java.util.Arrays;

public class CommaParameterSplitter implements IParameterSplitter {
  public CommaParameterSplitter() {}
  
  public java.util.List<String> split(String value) {
    return Arrays.asList(value.split(","));
  }
}
