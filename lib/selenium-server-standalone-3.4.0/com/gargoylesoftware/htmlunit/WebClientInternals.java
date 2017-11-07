package com.gargoylesoftware.htmlunit;

import com.gargoylesoftware.htmlunit.javascript.host.WebSocket;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
























public class WebClientInternals
  implements Serializable
{
  private Collection<Listener> listeners_;
  
  public WebClientInternals() {}
  
  public void addListener(Listener listener)
  {
    synchronized (this) {
      if (listeners_ == null) {
        listeners_ = Collections.synchronizedList(new ArrayList());
      }
    }
    listeners_.add(listener);
  }
  




  public void removeListener(Listener listener)
  {
    listeners_.remove(listener);
  }
  






  public void created(WebSocket webSocket)
  {
    if (listeners_ != null) {
      synchronized (listeners_) {
        for (Listener l : listeners_) {
          l.webSocketCreated(webSocket);
        }
      }
    }
  }
  
  public static abstract interface Listener
  {
    public abstract void webSocketCreated(WebSocket paramWebSocket);
  }
}
