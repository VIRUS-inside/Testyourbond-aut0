package org.seleniumhq.jetty9.servlet.jmx;

import org.seleniumhq.jetty9.jmx.ObjectMBean;
import org.seleniumhq.jetty9.servlet.Holder;


















public class HolderMBean
  extends ObjectMBean
{
  public HolderMBean(Object managedObject)
  {
    super(managedObject);
  }
  

  public String getObjectNameBasis()
  {
    if ((_managed != null) && ((_managed instanceof Holder)))
    {
      Holder holder = (Holder)_managed;
      String name = holder.getName();
      if (name != null)
        return name;
    }
    return super.getObjectNameBasis();
  }
}
