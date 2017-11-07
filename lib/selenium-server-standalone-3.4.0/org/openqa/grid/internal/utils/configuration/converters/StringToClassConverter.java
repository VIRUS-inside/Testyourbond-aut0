package org.openqa.grid.internal.utils.configuration.converters;

import com.beust.jcommander.IStringConverter;
import org.openqa.grid.common.exception.GridConfigurationException;
import org.openqa.grid.internal.listeners.Prioritizer;
import org.openqa.grid.internal.utils.CapabilityMatcher;














public abstract class StringToClassConverter<E>
{
  public StringToClassConverter() {}
  
  public E convert(String capabilityMatcherClass)
  {
    try
    {
      return Class.forName(capabilityMatcherClass).newInstance();
    }
    catch (Throwable e) {
      throw new GridConfigurationException("Error creating class with " + capabilityMatcherClass + " : " + e.getMessage(), e);
    }
  }
  
  public static class PrioritizerStringConverter
    extends StringToClassConverter<Prioritizer>
    implements IStringConverter<Prioritizer>
  {
    public PrioritizerStringConverter() {}
  }
  
  public static class CapabilityMatcherStringConverter
    extends StringToClassConverter<CapabilityMatcher>
    implements IStringConverter<CapabilityMatcher>
  {
    public CapabilityMatcherStringConverter() {}
  }
}
