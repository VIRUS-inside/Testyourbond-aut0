package javax.servlet;

import java.util.Enumeration;

public abstract interface ServletConfig
{
  public abstract String getServletName();
  
  public abstract ServletContext getServletContext();
  
  public abstract String getInitParameter(String paramString);
  
  public abstract Enumeration<String> getInitParameterNames();
}
