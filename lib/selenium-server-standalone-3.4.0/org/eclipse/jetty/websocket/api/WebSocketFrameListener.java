package org.eclipse.jetty.websocket.api;

import org.eclipse.jetty.websocket.api.extensions.Frame;

public abstract interface WebSocketFrameListener
  extends WebSocketConnectionListener
{
  public abstract void onWebSocketFrame(Frame paramFrame);
}
