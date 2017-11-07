package javax.servlet;

import java.util.Enumeration;

public abstract interface FilterConfig
{
  public abstract String getFilterName();
  
  public abstract ServletContext getServletContext();
  
  public abstract String getInitParameter(String paramString);
  
  public abstract Enumeration<String> getInitParameterNames();
}
