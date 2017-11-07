package org.eclipse.jetty.websocket.api;

public abstract interface WebSocketListener
  extends WebSocketConnectionListener
{
  public abstract void onWebSocketBinary(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  public abstract void onWebSocketText(String paramString);
}
