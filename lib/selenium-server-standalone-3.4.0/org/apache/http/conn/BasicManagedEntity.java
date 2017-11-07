package org.apache.http.conn;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.SocketException;
import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.util.Args;
import org.apache.http.util.EntityUtils;



















































@Deprecated
public class BasicManagedEntity
  extends HttpEntityWrapper
  implements ConnectionReleaseTrigger, EofSensorWatcher
{
  protected ManagedClientConnection managedConn;
  protected final boolean attemptReuse;
  
  public BasicManagedEntity(HttpEntity entity, ManagedClientConnection conn, boolean reuse)
  {
    super(entity);
    Args.notNull(conn, "Connection");
    managedConn = conn;
    attemptReuse = reuse;
  }
  
  public boolean isRepeatable()
  {
    return false;
  }
  
  public InputStream getContent() throws IOException
  {
    return new EofSensorInputStream(wrappedEntity.getContent(), this);
  }
  
  private void ensureConsumed() throws IOException {
    if (managedConn == null) {
      return;
    }
    try
    {
      if (attemptReuse)
      {
        EntityUtils.consume(wrappedEntity);
        managedConn.markReusable();
      } else {
        managedConn.unmarkReusable();
      }
    } finally {
      releaseManagedConnection();
    }
  }
  


  @Deprecated
  public void consumeContent()
    throws IOException
  {
    ensureConsumed();
  }
  
  public void writeTo(OutputStream outstream) throws IOException
  {
    super.writeTo(outstream);
    ensureConsumed();
  }
  
  public void releaseConnection() throws IOException
  {
    ensureConsumed();
  }
  
  public void abortConnection()
    throws IOException
  {
    if (managedConn != null) {
      try {
        managedConn.abortConnection();
      } finally {
        managedConn = null;
      }
    }
  }
  
  public boolean eofDetected(InputStream wrapped) throws IOException
  {
    try {
      if (managedConn != null) {
        if (attemptReuse)
        {

          wrapped.close();
          managedConn.markReusable();
        } else {
          managedConn.unmarkReusable();
        }
      }
    } finally {
      releaseManagedConnection();
    }
    return false;
  }
  
  public boolean streamClosed(InputStream wrapped) throws IOException
  {
    try {
      if (managedConn != null) {
        if (attemptReuse) {
          boolean valid = managedConn.isOpen();
          
          try
          {
            wrapped.close();
            managedConn.markReusable();
          } catch (SocketException ex) {
            if (valid) {
              throw ex;
            }
          }
        } else {
          managedConn.unmarkReusable();
        }
      }
    } finally {
      releaseManagedConnection();
    }
    return false;
  }
  
  public boolean streamAbort(InputStream wrapped) throws IOException
  {
    if (managedConn != null) {
      managedConn.abortConnection();
    }
    return false;
  }
  








  protected void releaseManagedConnection()
    throws IOException
  {
    if (managedConn != null) {
      try {
        managedConn.releaseConnection();
      } finally {
        managedConn = null;
      }
    }
  }
}
