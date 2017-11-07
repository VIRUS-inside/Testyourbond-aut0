package org.eclipse.jetty.websocket.common.events;

import org.eclipse.jetty.websocket.api.WebSocketConnectionListener;
import org.eclipse.jetty.websocket.api.WebSocketListener;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;

















public class JettyListenerImpl
  implements EventDriverImpl
{
  public JettyListenerImpl() {}
  
  public EventDriver create(Object websocket, WebSocketPolicy policy)
  {
    WebSocketConnectionListener listener = (WebSocketConnectionListener)websocket;
    return new JettyListenerEventDriver(policy, listener);
  }
  

  public String describeRule()
  {
    return "class implements " + WebSocketListener.class.getName();
  }
  

  public boolean supports(Object websocket)
  {
    return websocket instanceof WebSocketConnectionListener;
  }
}
