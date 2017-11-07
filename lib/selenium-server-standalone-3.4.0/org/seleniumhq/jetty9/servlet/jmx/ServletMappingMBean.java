package org.seleniumhq.jetty9.servlet.jmx;

import org.seleniumhq.jetty9.jmx.ObjectMBean;
import org.seleniumhq.jetty9.servlet.ServletMapping;



















public class ServletMappingMBean
  extends ObjectMBean
{
  public ServletMappingMBean(Object managedObject)
  {
    super(managedObject);
  }
  
  public String getObjectNameBasis()
  {
    if ((_managed != null) && ((_managed instanceof ServletMapping)))
    {
      ServletMapping mapping = (ServletMapping)_managed;
      String name = mapping.getServletName();
      if (name != null) {
        return name;
      }
    }
    return super.getObjectNameBasis();
  }
}
