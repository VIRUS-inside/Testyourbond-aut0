package javax.servlet;

import java.util.Collection;
import java.util.EnumSet;

public abstract interface FilterRegistration
  extends Registration
{
  public abstract void addMappingForServletNames(EnumSet<DispatcherType> paramEnumSet, boolean paramBoolean, String... paramVarArgs);
  
  public abstract Collection<String> getServletNameMappings();
  
  public abstract void addMappingForUrlPatterns(EnumSet<DispatcherType> paramEnumSet, boolean paramBoolean, String... paramVarArgs);
  
  public abstract Collection<String> getUrlPatternMappings();
  
  public static abstract interface Dynamic
    extends FilterRegistration, Registration.Dynamic
  {}
}
