package org.openqa.grid.web.servlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import org.openqa.grid.internal.Registry;














public abstract class RegistryBasedServlet
  extends HttpServlet
{
  private Registry registry;
  
  public RegistryBasedServlet(Registry registry)
  {
    this.registry = registry;
  }
  
  protected Registry getRegistry() {
    if (registry == null) {
      registry = ((Registry)getServletContext().getAttribute(Registry.KEY));
    }
    
    return registry;
  }
}
