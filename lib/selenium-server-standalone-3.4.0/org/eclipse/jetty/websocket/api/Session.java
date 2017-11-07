package org.eclipse.jetty.websocket.api;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;

public abstract interface Session
  extends Closeable
{
  public abstract void close();
  
  public abstract void close(CloseStatus paramCloseStatus);
  
  public abstract void close(int paramInt, String paramString);
  
  public abstract void disconnect()
    throws IOException;
  
  public abstract long getIdleTimeout();
  
  public abstract InetSocketAddress getLocalAddress();
  
  public abstract WebSocketPolicy getPolicy();
  
  public abstract String getProtocolVersion();
  
  public abstract RemoteEndpoint getRemote();
  
  public abstract InetSocketAddress getRemoteAddress();
  
  public abstract UpgradeRequest getUpgradeRequest();
  
  public abstract UpgradeResponse getUpgradeResponse();
  
  public abstract boolean isOpen();
  
  public abstract boolean isSecure();
  
  public abstract void setIdleTimeout(long paramLong);
  
  public abstract SuspendToken suspend();
}
