package org.seleniumhq.jetty9.server.handler.jmx;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import org.seleniumhq.jetty9.server.handler.ContextHandler;
import org.seleniumhq.jetty9.util.Attributes;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.annotation.ManagedOperation;
import org.seleniumhq.jetty9.util.annotation.Name;



















@ManagedObject("ContextHandler mbean wrapper")
public class ContextHandlerMBean
  extends AbstractHandlerMBean
{
  public ContextHandlerMBean(Object managedObject)
  {
    super(managedObject);
  }
  
  @ManagedAttribute("Map of context attributes")
  public Map<String, Object> getContextAttributes()
  {
    Map<String, Object> map = new HashMap();
    Attributes attrs = ((ContextHandler)_managed).getAttributes();
    Enumeration<String> en = attrs.getAttributeNames();
    while (en.hasMoreElements())
    {
      String name = (String)en.nextElement();
      Object value = attrs.getAttribute(name);
      map.put(name, value);
    }
    return map;
  }
  
  @ManagedOperation(value="Set context attribute", impact="ACTION")
  public void setContextAttribute(@Name(value="name", description="attribute name") String name, @Name(value="value", description="attribute value") Object value)
  {
    Attributes attrs = ((ContextHandler)_managed).getAttributes();
    attrs.setAttribute(name, value);
  }
  
  @ManagedOperation(value="Set context attribute", impact="ACTION")
  public void setContextAttribute(@Name(value="name", description="attribute name") String name, @Name(value="value", description="attribute value") String value)
  {
    Attributes attrs = ((ContextHandler)_managed).getAttributes();
    attrs.setAttribute(name, value);
  }
  
  @ManagedOperation(value="Remove context attribute", impact="ACTION")
  public void removeContextAttribute(@Name(value="name", description="attribute name") String name)
  {
    Attributes attrs = ((ContextHandler)_managed).getAttributes();
    attrs.removeAttribute(name);
  }
}
