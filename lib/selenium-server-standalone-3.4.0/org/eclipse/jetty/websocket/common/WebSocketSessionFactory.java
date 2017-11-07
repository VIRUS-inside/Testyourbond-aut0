package org.eclipse.jetty.websocket.common;

import java.net.URI;
import org.eclipse.jetty.websocket.common.events.EventDriver;
import org.eclipse.jetty.websocket.common.events.JettyAnnotatedEventDriver;
import org.eclipse.jetty.websocket.common.events.JettyListenerEventDriver;
import org.eclipse.jetty.websocket.common.scopes.WebSocketContainerScope;






















public class WebSocketSessionFactory
  implements SessionFactory
{
  private final WebSocketContainerScope containerScope;
  
  public WebSocketSessionFactory(WebSocketContainerScope containerScope)
  {
    this.containerScope = containerScope;
  }
  

  public boolean supports(EventDriver websocket)
  {
    return ((websocket instanceof JettyAnnotatedEventDriver)) || ((websocket instanceof JettyListenerEventDriver));
  }
  

  public WebSocketSession createSession(URI requestURI, EventDriver websocket, LogicalConnection connection)
  {
    return new WebSocketSession(containerScope, requestURI, websocket, connection);
  }
}
