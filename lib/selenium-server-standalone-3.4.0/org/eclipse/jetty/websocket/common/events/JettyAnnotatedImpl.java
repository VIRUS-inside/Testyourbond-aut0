package org.eclipse.jetty.websocket.common.events;

import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;



















public class JettyAnnotatedImpl
  implements EventDriverImpl
{
  private ConcurrentHashMap<Class<?>, JettyAnnotatedMetadata> cache = new ConcurrentHashMap();
  
  public JettyAnnotatedImpl() {}
  
  public EventDriver create(Object websocket, WebSocketPolicy policy) {
    Class<?> websocketClass = websocket.getClass();
    synchronized (this)
    {
      JettyAnnotatedMetadata metadata = (JettyAnnotatedMetadata)cache.get(websocketClass);
      if (metadata == null)
      {
        JettyAnnotatedScanner scanner = new JettyAnnotatedScanner();
        metadata = scanner.scan(websocketClass);
        cache.put(websocketClass, metadata);
      }
      return new JettyAnnotatedEventDriver(policy, websocket, metadata);
    }
  }
  

  public String describeRule()
  {
    return "class is annotated with @" + WebSocket.class.getName();
  }
  

  public boolean supports(Object websocket)
  {
    WebSocket anno = (WebSocket)websocket.getClass().getAnnotation(WebSocket.class);
    return anno != null;
  }
  

  public String toString()
  {
    return String.format("%s [cache.count=%d]", new Object[] { getClass().getSimpleName(), Integer.valueOf(cache.size()) });
  }
}
