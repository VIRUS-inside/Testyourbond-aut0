package org.apache.http.impl.conn;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpConnection;








































@Deprecated
public class IdleConnectionHandler
{
  private final Log log = LogFactory.getLog(getClass());
  

  private final Map<HttpConnection, TimeValues> connectionToTimes;
  

  public IdleConnectionHandler()
  {
    connectionToTimes = new HashMap();
  }
  








  public void add(HttpConnection connection, long validDuration, TimeUnit unit)
  {
    long timeAdded = System.currentTimeMillis();
    
    if (log.isDebugEnabled()) {
      log.debug("Adding connection at: " + timeAdded);
    }
    
    connectionToTimes.put(connection, new TimeValues(timeAdded, validDuration, unit));
  }
  







  public boolean remove(HttpConnection connection)
  {
    TimeValues times = (TimeValues)connectionToTimes.remove(connection);
    if (times == null) {
      log.warn("Removing a connection that never existed!");
      return true;
    }
    return System.currentTimeMillis() <= timeExpires;
  }
  



  public void removeAll()
  {
    connectionToTimes.clear();
  }
  






  public void closeIdleConnections(long idleTime)
  {
    long idleTimeout = System.currentTimeMillis() - idleTime;
    
    if (log.isDebugEnabled()) {
      log.debug("Checking for connections, idle timeout: " + idleTimeout);
    }
    
    for (Map.Entry<HttpConnection, TimeValues> entry : connectionToTimes.entrySet()) {
      HttpConnection conn = (HttpConnection)entry.getKey();
      TimeValues times = (TimeValues)entry.getValue();
      long connectionTime = timeAdded;
      if (connectionTime <= idleTimeout) {
        if (log.isDebugEnabled()) {
          log.debug("Closing idle connection, connection time: " + connectionTime);
        }
        try {
          conn.close();
        } catch (IOException ex) {
          log.debug("I/O error closing connection", ex);
        }
      }
    }
  }
  
  public void closeExpiredConnections()
  {
    long now = System.currentTimeMillis();
    if (log.isDebugEnabled()) {
      log.debug("Checking for expired connections, now: " + now);
    }
    
    for (Map.Entry<HttpConnection, TimeValues> entry : connectionToTimes.entrySet()) {
      HttpConnection conn = (HttpConnection)entry.getKey();
      TimeValues times = (TimeValues)entry.getValue();
      if (timeExpires <= now) {
        if (log.isDebugEnabled()) {
          log.debug("Closing connection, expired @: " + timeExpires);
        }
        try {
          conn.close();
        } catch (IOException ex) {
          log.debug("I/O error closing connection", ex);
        }
      }
    }
  }
  

  private static class TimeValues
  {
    private final long timeAdded;
    
    private final long timeExpires;
    

    TimeValues(long now, long validDuration, TimeUnit validUnit)
    {
      timeAdded = now;
      if (validDuration > 0L) {
        timeExpires = (now + validUnit.toMillis(validDuration));
      } else {
        timeExpires = Long.MAX_VALUE;
      }
    }
  }
}
