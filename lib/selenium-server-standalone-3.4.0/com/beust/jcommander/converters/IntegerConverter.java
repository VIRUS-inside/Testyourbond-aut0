package com.beust.jcommander.converters;

import com.beust.jcommander.ParameterException;






















public class IntegerConverter
  extends BaseConverter<Integer>
{
  public IntegerConverter(String optionName)
  {
    super(optionName);
  }
  
  public Integer convert(String value) {
    try {
      return Integer.valueOf(Integer.parseInt(value));
    } catch (NumberFormatException ex) {
      throw new ParameterException(getErrorString(value, "an integer"));
    }
  }
}
