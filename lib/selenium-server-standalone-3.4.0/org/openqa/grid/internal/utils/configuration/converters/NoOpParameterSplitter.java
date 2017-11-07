package org.openqa.grid.internal.utils.configuration.converters;

import com.beust.jcommander.converters.IParameterSplitter;
import java.util.Arrays;
import java.util.List;















public class NoOpParameterSplitter
  implements IParameterSplitter
{
  public NoOpParameterSplitter() {}
  
  public List<String> split(String value)
  {
    return Arrays.asList(new String[] { value });
  }
}
