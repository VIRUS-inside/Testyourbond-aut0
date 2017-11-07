package javax.servlet;

import java.util.Set;

public abstract interface ServletContainerInitializer
{
  public abstract void onStartup(Set<Class<?>> paramSet, ServletContext paramServletContext)
    throws ServletException;
}
