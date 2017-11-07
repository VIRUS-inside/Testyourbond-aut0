package org.eclipse.jetty.websocket.common.io.http;

import java.nio.ByteBuffer;

public abstract interface HttpResponseHeaderParseListener
{
  public abstract void addHeader(String paramString1, String paramString2);
  
  public abstract void setRemainingBuffer(ByteBuffer paramByteBuffer);
  
  public abstract void setStatusCode(int paramInt);
  
  public abstract void setStatusReason(String paramString);
}
