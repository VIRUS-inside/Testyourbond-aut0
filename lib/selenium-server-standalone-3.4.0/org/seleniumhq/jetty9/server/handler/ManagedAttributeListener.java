package org.seleniumhq.jetty9.server.handler;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;






















public class ManagedAttributeListener
  implements ServletContextListener, ServletContextAttributeListener
{
  private static final Logger LOG = Log.getLogger(ManagedAttributeListener.class);
  
  final Set<String> _managedAttributes = new HashSet();
  final ContextHandler _context;
  
  public ManagedAttributeListener(ContextHandler context, String... managedAttributes)
  {
    _context = context;
    
    for (String attr : managedAttributes) {
      _managedAttributes.add(attr);
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("managedAttributes {}", new Object[] { _managedAttributes });
    }
  }
  
  public void attributeReplaced(ServletContextAttributeEvent event)
  {
    if (_managedAttributes.contains(event.getName())) {
      updateBean(event.getName(), event.getValue(), event.getServletContext().getAttribute(event.getName()));
    }
  }
  
  public void attributeRemoved(ServletContextAttributeEvent event)
  {
    if (_managedAttributes.contains(event.getName())) {
      updateBean(event.getName(), event.getValue(), null);
    }
  }
  
  public void attributeAdded(ServletContextAttributeEvent event)
  {
    if (_managedAttributes.contains(event.getName())) {
      updateBean(event.getName(), null, event.getValue());
    }
  }
  

  public void contextInitialized(ServletContextEvent event)
  {
    Enumeration<String> e = event.getServletContext().getAttributeNames();
    while (e.hasMoreElements())
    {
      String name = (String)e.nextElement();
      if (_managedAttributes.contains(name)) {
        updateBean(name, null, event.getServletContext().getAttribute(name));
      }
    }
  }
  
  public void contextDestroyed(ServletContextEvent event)
  {
    Enumeration<String> e = _context.getServletContext().getAttributeNames();
    while (e.hasMoreElements())
    {
      String name = (String)e.nextElement();
      if (_managedAttributes.contains(name)) {
        updateBean(name, event.getServletContext().getAttribute(name), null);
      }
    }
  }
  
  protected void updateBean(String name, Object oldBean, Object newBean) {
    LOG.info("update {} {}->{} on {}", new Object[] { name, oldBean, newBean, _context });
    if (LOG.isDebugEnabled())
      LOG.debug("update {} {}->{} on {}", new Object[] { name, oldBean, newBean, _context });
    _context.updateBean(oldBean, newBean, false);
  }
}
