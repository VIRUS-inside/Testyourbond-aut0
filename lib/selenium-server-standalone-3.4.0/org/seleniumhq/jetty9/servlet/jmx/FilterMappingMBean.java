package org.seleniumhq.jetty9.servlet.jmx;

import org.seleniumhq.jetty9.jmx.ObjectMBean;
import org.seleniumhq.jetty9.servlet.FilterMapping;



















public class FilterMappingMBean
  extends ObjectMBean
{
  public FilterMappingMBean(Object managedObject)
  {
    super(managedObject);
  }
  
  public String getObjectNameBasis()
  {
    if ((_managed != null) && ((_managed instanceof FilterMapping)))
    {
      FilterMapping mapping = (FilterMapping)_managed;
      String name = mapping.getFilterName();
      if (name != null) {
        return name;
      }
    }
    return super.getObjectNameBasis();
  }
}
