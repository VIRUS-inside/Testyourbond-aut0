package org.apache.xalan.lib.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public abstract interface ConnectionPool
{
  public abstract boolean isEnabled();
  
  public abstract void setDriver(String paramString);
  
  public abstract void setURL(String paramString);
  
  public abstract void freeUnused();
  
  public abstract boolean hasActiveConnections();
  
  public abstract void setPassword(String paramString);
  
  public abstract void setUser(String paramString);
  
  public abstract void setMinConnections(int paramInt);
  
  public abstract boolean testConnection();
  
  public abstract Connection getConnection()
    throws SQLException;
  
  public abstract void releaseConnection(Connection paramConnection)
    throws SQLException;
  
  public abstract void releaseConnectionOnError(Connection paramConnection)
    throws SQLException;
  
  public abstract void setPoolEnabled(boolean paramBoolean);
  
  public abstract void setProtocol(Properties paramProperties);
}
