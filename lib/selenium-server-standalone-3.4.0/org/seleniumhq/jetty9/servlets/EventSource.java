package org.seleniumhq.jetty9.servlets;

import java.io.IOException;

public abstract interface EventSource
{
  public abstract void onOpen(Emitter paramEmitter)
    throws IOException;
  
  public abstract void onClose();
  
  public static abstract interface Emitter
  {
    public abstract void event(String paramString1, String paramString2)
      throws IOException;
    
    public abstract void data(String paramString)
      throws IOException;
    
    public abstract void comment(String paramString)
      throws IOException;
    
    public abstract void close();
  }
}
