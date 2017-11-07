package org.eclipse.jetty.websocket.common.scopes;

import org.eclipse.jetty.websocket.common.WebSocketSession;

public abstract interface WebSocketSessionScope
{
  public abstract WebSocketSession getWebSocketSession();
  
  public abstract WebSocketContainerScope getContainerScope();
}
