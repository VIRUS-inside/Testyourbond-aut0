package javax.servlet;

import java.io.IOException;

public abstract interface Servlet
{
  public abstract void init(ServletConfig paramServletConfig)
    throws ServletException;
  
  public abstract ServletConfig getServletConfig();
  
  public abstract void service(ServletRequest paramServletRequest, ServletResponse paramServletResponse)
    throws ServletException, IOException;
  
  public abstract String getServletInfo();
  
  public abstract void destroy();
}
