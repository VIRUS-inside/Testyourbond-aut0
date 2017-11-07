package com.beust.jcommander.converters;

import com.beust.jcommander.IStringConverter;
import java.nio.file.Path;
import java.nio.file.Paths;





















public class PathConverter
  implements IStringConverter<Path>
{
  public PathConverter() {}
  
  public Path convert(String value)
  {
    return Paths.get(value, new String[0]);
  }
}
