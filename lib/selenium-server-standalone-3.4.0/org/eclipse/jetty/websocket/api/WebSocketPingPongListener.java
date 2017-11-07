package org.eclipse.jetty.websocket.api;

import java.nio.ByteBuffer;

public abstract interface WebSocketPingPongListener
  extends WebSocketConnectionListener
{
  public abstract void onWebSocketPing(ByteBuffer paramByteBuffer);
  
  public abstract void onWebSocketPong(ByteBuffer paramByteBuffer);
}
