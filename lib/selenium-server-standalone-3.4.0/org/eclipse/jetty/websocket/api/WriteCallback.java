package org.eclipse.jetty.websocket.api;

public abstract interface WriteCallback
{
  public abstract void writeFailed(Throwable paramThrowable);
  
  public abstract void writeSuccess();
}
