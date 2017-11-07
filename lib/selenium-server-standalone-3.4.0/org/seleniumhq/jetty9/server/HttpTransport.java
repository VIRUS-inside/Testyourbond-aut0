package org.seleniumhq.jetty9.server;

import java.nio.ByteBuffer;
import org.seleniumhq.jetty9.http.MetaData.Request;
import org.seleniumhq.jetty9.http.MetaData.Response;
import org.seleniumhq.jetty9.util.Callback;

public abstract interface HttpTransport
{
  public abstract void send(MetaData.Response paramResponse, boolean paramBoolean1, ByteBuffer paramByteBuffer, boolean paramBoolean2, Callback paramCallback);
  
  public abstract boolean isPushSupported();
  
  public abstract void push(MetaData.Request paramRequest);
  
  public abstract void onCompleted();
  
  public abstract void abort(Throwable paramThrowable);
  
  public abstract boolean isOptimizedForDirectBuffers();
}
