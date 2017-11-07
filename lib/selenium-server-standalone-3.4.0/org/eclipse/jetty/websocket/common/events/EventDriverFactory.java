package org.eclipse.jetty.websocket.common.events;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.websocket.api.InvalidWebSocketException;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.common.scopes.WebSocketContainerScope;
























public class EventDriverFactory
{
  private static final Logger LOG = Log.getLogger(EventDriverFactory.class);
  private final WebSocketContainerScope containerScope;
  private final List<EventDriverImpl> implementations;
  
  public EventDriverFactory(WebSocketContainerScope containerScope)
  {
    this.containerScope = containerScope;
    implementations = new ArrayList();
    
    addImplementation(new JettyListenerImpl());
    addImplementation(new JettyAnnotatedImpl());
  }
  
  public void addImplementation(EventDriverImpl impl)
  {
    if (implementations.contains(impl))
    {
      LOG.warn("Ignoring attempt to add duplicate EventDriverImpl: " + impl, new Object[0]);
      return;
    }
    
    implementations.add(impl);
  }
  
  public void clearImplementations()
  {
    implementations.clear();
  }
  
  protected String getClassName(Object websocket)
  {
    return websocket.getClass().getName();
  }
  
  public List<EventDriverImpl> getImplementations()
  {
    return implementations;
  }
  
  public boolean removeImplementation(EventDriverImpl impl)
  {
    return implementations.remove(impl);
  }
  

  public String toString()
  {
    StringBuilder msg = new StringBuilder();
    msg.append(getClass().getSimpleName());
    msg.append("[implementations=[");
    boolean delim = false;
    for (EventDriverImpl impl : implementations)
    {
      if (delim)
      {
        msg.append(',');
      }
      msg.append(impl.toString());
      delim = true;
    }
    msg.append("]");
    return msg.toString();
  }
  







  public EventDriver wrap(Object websocket)
  {
    if (websocket == null)
    {
      throw new InvalidWebSocketException("null websocket object");
    }
    
    for (EventDriverImpl impl : implementations)
    {
      if (impl.supports(websocket))
      {
        try
        {
          return impl.create(websocket, containerScope.getPolicy().clonePolicy());
        }
        catch (Throwable e)
        {
          throw new InvalidWebSocketException("Unable to create websocket", e);
        }
      }
    }
    

    StringBuilder err = new StringBuilder();
    err.append(getClassName(websocket));
    err.append(" is not a valid WebSocket object.");
    err.append("  Object must obey one of the following rules: ");
    
    int len = implementations.size();
    for (int i = 0; i < len; i++)
    {
      EventDriverImpl impl = (EventDriverImpl)implementations.get(i);
      if (i > 0)
      {
        err.append(" or ");
      }
      err.append("\n(").append(i + 1).append(") ");
      err.append(impl.describeRule());
    }
    
    throw new InvalidWebSocketException(err.toString());
  }
}
