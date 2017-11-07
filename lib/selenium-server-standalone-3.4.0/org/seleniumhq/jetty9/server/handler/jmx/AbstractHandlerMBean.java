package org.seleniumhq.jetty9.server.handler.jmx;

import java.io.File;
import java.io.IOException;
import org.seleniumhq.jetty9.jmx.ObjectMBean;
import org.seleniumhq.jetty9.server.Server;
import org.seleniumhq.jetty9.server.handler.AbstractHandler;
import org.seleniumhq.jetty9.server.handler.AbstractHandlerContainer;
import org.seleniumhq.jetty9.server.handler.ContextHandler;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.resource.Resource;

















public class AbstractHandlerMBean
  extends ObjectMBean
{
  private static final Logger LOG = Log.getLogger(AbstractHandlerMBean.class);
  

  public AbstractHandlerMBean(Object managedObject)
  {
    super(managedObject);
  }
  


  public String getObjectContextBasis()
  {
    if (_managed != null)
    {
      String basis = null;
      if ((_managed instanceof ContextHandler))
      {
        ContextHandler handler = (ContextHandler)_managed;
        String context = getContextName(handler);
        if (context == null)
          context = handler.getDisplayName();
        if (context != null) {
          return context;
        }
      } else if ((_managed instanceof AbstractHandler))
      {
        AbstractHandler handler = (AbstractHandler)_managed;
        Server server = handler.getServer();
        if (server != null)
        {

          ContextHandler context = (ContextHandler)AbstractHandlerContainer.findContainerOf(server, ContextHandler.class, handler);
          

          if (context != null)
            basis = getContextName(context);
        }
      }
      if (basis != null)
        return basis;
    }
    return super.getObjectContextBasis();
  }
  

  protected String getContextName(ContextHandler context)
  {
    String name = null;
    
    if ((context.getContextPath() != null) && (context.getContextPath().length() > 0))
    {
      int idx = context.getContextPath().lastIndexOf('/');
      name = idx < 0 ? context.getContextPath() : context.getContextPath().substring(++idx);
      if ((name == null) || (name.length() == 0)) {
        name = "ROOT";
      }
    }
    if ((name == null) && (context.getBaseResource() != null))
    {
      try
      {
        if (context.getBaseResource().getFile() != null) {
          name = context.getBaseResource().getFile().getName();
        }
      }
      catch (IOException e) {
        LOG.ignore(e);
        name = context.getBaseResource().getName();
      }
    }
    
    if ((context.getVirtualHosts() != null) && (context.getVirtualHosts().length > 0)) {
      name = '"' + name + "@" + context.getVirtualHosts()[0] + '"';
    }
    return name;
  }
}
