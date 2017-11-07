package org.seleniumhq.jetty9.io.ssl;

import java.util.EventListener;
import java.util.EventObject;
import javax.net.ssl.SSLEngine;










































public abstract interface SslHandshakeListener
  extends EventListener
{
  public void handshakeSucceeded(Event event) {}
  
  public void handshakeFailed(Event event, Throwable failure) {}
  
  public static class Event
    extends EventObject
  {
    public Event(Object source)
    {
      super();
    }
    



    public SSLEngine getSSLEngine()
    {
      return (SSLEngine)getSource();
    }
  }
}
