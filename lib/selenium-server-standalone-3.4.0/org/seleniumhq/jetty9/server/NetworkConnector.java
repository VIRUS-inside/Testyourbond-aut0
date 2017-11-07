package org.seleniumhq.jetty9.server;

import java.io.Closeable;
import java.io.IOException;

public abstract interface NetworkConnector
  extends Connector, Closeable
{
  public abstract void open()
    throws IOException;
  
  public abstract void close();
  
  public abstract boolean isOpen();
  
  public abstract String getHost();
  
  public abstract int getPort();
  
  public abstract int getLocalPort();
}
