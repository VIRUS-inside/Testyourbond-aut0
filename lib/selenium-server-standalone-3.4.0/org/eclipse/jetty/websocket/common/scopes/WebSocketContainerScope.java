package org.eclipse.jetty.websocket.common.scopes;

import java.util.concurrent.Executor;
import org.eclipse.jetty.io.ByteBufferPool;
import org.eclipse.jetty.util.DecoratedObjectFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.common.WebSocketSession;

public abstract interface WebSocketContainerScope
{
  public abstract ByteBufferPool getBufferPool();
  
  public abstract Executor getExecutor();
  
  public abstract DecoratedObjectFactory getObjectFactory();
  
  public abstract WebSocketPolicy getPolicy();
  
  public abstract SslContextFactory getSslContextFactory();
  
  public abstract boolean isRunning();
  
  public abstract void onSessionOpened(WebSocketSession paramWebSocketSession);
  
  public abstract void onSessionClosed(WebSocketSession paramWebSocketSession);
}
