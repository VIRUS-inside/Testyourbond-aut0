package org.eclipse.jetty.websocket.common;

public enum ConnectionState
{
  CONNECTING,  CONNECTED,  OPEN,  CLOSING,  CLOSED;
  
  private ConnectionState() {}
}
