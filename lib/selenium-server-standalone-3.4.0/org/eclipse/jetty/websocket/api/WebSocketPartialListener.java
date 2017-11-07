package org.eclipse.jetty.websocket.api;

import java.nio.ByteBuffer;

public abstract interface WebSocketPartialListener
  extends WebSocketConnectionListener
{
  public abstract void onWebSocketPartialBinary(ByteBuffer paramByteBuffer, boolean paramBoolean);
  
  public abstract void onWebSocketPartialText(String paramString, boolean paramBoolean);
}
