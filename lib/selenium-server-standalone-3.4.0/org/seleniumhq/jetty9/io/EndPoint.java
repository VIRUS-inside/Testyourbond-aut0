package org.seleniumhq.jetty9.io;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ReadPendingException;
import java.nio.channels.WritePendingException;
import org.seleniumhq.jetty9.util.Callback;

public abstract interface EndPoint
  extends Closeable
{
  public abstract InetSocketAddress getLocalAddress();
  
  public abstract InetSocketAddress getRemoteAddress();
  
  public abstract boolean isOpen();
  
  public abstract long getCreatedTimeStamp();
  
  public abstract void shutdownOutput();
  
  public abstract boolean isOutputShutdown();
  
  public abstract boolean isInputShutdown();
  
  public abstract void close();
  
  public abstract int fill(ByteBuffer paramByteBuffer)
    throws IOException;
  
  public abstract boolean flush(ByteBuffer... paramVarArgs)
    throws IOException;
  
  public abstract Object getTransport();
  
  public abstract long getIdleTimeout();
  
  public abstract void setIdleTimeout(long paramLong);
  
  public abstract void fillInterested(Callback paramCallback)
    throws ReadPendingException;
  
  public abstract boolean tryFillInterested(Callback paramCallback);
  
  public abstract boolean isFillInterested();
  
  public abstract void write(Callback paramCallback, ByteBuffer... paramVarArgs)
    throws WritePendingException;
  
  public abstract Connection getConnection();
  
  public abstract void setConnection(Connection paramConnection);
  
  public abstract void onOpen();
  
  public abstract void onClose();
  
  public abstract boolean isOptimizedForDirectBuffers();
  
  public abstract void upgrade(Connection paramConnection);
}
