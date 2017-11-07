package org.eclipse.jetty.websocket.api;

public abstract interface WebSocketConnectionListener
{
  public abstract void onWebSocketClose(int paramInt, String paramString);
  
  public abstract void onWebSocketConnect(Session paramSession);
  
  public abstract void onWebSocketError(Throwable paramThrowable);
}
