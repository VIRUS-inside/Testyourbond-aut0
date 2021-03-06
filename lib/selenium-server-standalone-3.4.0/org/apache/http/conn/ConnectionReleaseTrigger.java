package org.apache.http.conn;

import java.io.IOException;

public abstract interface ConnectionReleaseTrigger
{
  public abstract void releaseConnection()
    throws IOException;
  
  public abstract void abortConnection()
    throws IOException;
}
