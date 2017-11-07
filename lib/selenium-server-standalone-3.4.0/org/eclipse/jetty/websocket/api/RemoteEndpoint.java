package org.eclipse.jetty.websocket.api;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.Future;

public abstract interface RemoteEndpoint
{
  public abstract void sendBytes(ByteBuffer paramByteBuffer)
    throws IOException;
  
  public abstract Future<Void> sendBytesByFuture(ByteBuffer paramByteBuffer);
  
  public abstract void sendBytes(ByteBuffer paramByteBuffer, WriteCallback paramWriteCallback);
  
  public abstract void sendPartialBytes(ByteBuffer paramByteBuffer, boolean paramBoolean)
    throws IOException;
  
  public abstract void sendPartialString(String paramString, boolean paramBoolean)
    throws IOException;
  
  public abstract void sendPing(ByteBuffer paramByteBuffer)
    throws IOException;
  
  public abstract void sendPong(ByteBuffer paramByteBuffer)
    throws IOException;
  
  public abstract void sendString(String paramString)
    throws IOException;
  
  public abstract Future<Void> sendStringByFuture(String paramString);
  
  public abstract void sendString(String paramString, WriteCallback paramWriteCallback);
  
  public abstract BatchMode getBatchMode();
  
  public abstract void setBatchMode(BatchMode paramBatchMode);
  
  public abstract InetSocketAddress getInetSocketAddress();
  
  public abstract void flush()
    throws IOException;
}
