package org.eclipse.jetty.websocket.api.extensions;

public abstract interface IncomingFrames
{
  public abstract void incomingError(Throwable paramThrowable);
  
  public abstract void incomingFrame(Frame paramFrame);
}
