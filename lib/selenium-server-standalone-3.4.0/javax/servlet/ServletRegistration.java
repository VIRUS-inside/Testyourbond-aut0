package javax.servlet;

import java.util.Collection;
import java.util.Set;

public abstract interface ServletRegistration
  extends Registration
{
  public abstract Set<String> addMapping(String... paramVarArgs);
  
  public abstract Collection<String> getMappings();
  
  public abstract String getRunAsRole();
  
  public static abstract interface Dynamic
    extends ServletRegistration, Registration.Dynamic
  {
    public abstract void setLoadOnStartup(int paramInt);
    
    public abstract Set<String> setServletSecurity(ServletSecurityElement paramServletSecurityElement);
    
    public abstract void setMultipartConfig(MultipartConfigElement paramMultipartConfigElement);
    
    public abstract void setRunAsRole(String paramString);
  }
}
