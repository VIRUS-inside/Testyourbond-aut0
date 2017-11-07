package org.eclipse.jetty.websocket.client.masks;

import org.eclipse.jetty.websocket.common.WebSocketFrame;

public abstract interface Masker
{
  public abstract void setMask(WebSocketFrame paramWebSocketFrame);
}
