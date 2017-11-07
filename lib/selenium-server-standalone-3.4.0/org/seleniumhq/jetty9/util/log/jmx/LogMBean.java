package org.seleniumhq.jetty9.util.log.jmx;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.seleniumhq.jetty9.jmx.ObjectMBean;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.annotation.ManagedOperation;
import org.seleniumhq.jetty9.util.annotation.Name;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;




















@ManagedObject("Jetty Logging")
public class LogMBean
  extends ObjectMBean
{
  public LogMBean(Object managedObject)
  {
    super(managedObject);
  }
  
  @ManagedAttribute("list of instantiated loggers")
  public List<String> getLoggers()
  {
    List<String> keySet = new ArrayList(Log.getLoggers().keySet());
    return keySet;
  }
  
  @ManagedOperation("true if debug enabled for the given logger")
  public boolean isDebugEnabled(@Name("logger") String logger)
  {
    return Log.getLogger(logger).isDebugEnabled();
  }
  
  @ManagedOperation("Set debug enabled for given logger")
  public void setDebugEnabled(@Name("logger") String logger, @Name("enabled") Boolean enabled)
  {
    Log.getLogger(logger).setDebugEnabled(enabled.booleanValue());
  }
}
