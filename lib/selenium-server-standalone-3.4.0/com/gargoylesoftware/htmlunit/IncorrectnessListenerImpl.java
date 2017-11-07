package com.gargoylesoftware.htmlunit;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;






















public class IncorrectnessListenerImpl
  implements IncorrectnessListener, Serializable
{
  private static final Log LOG = LogFactory.getLog(IncorrectnessListenerImpl.class);
  

  public IncorrectnessListenerImpl() {}
  
  public void notify(String message, Object origin)
  {
    LOG.warn(message);
  }
}
