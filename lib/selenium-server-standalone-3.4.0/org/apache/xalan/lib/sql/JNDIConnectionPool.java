package org.apache.xalan.lib.sql;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import javax.naming.InitialContext;
import javax.naming.NamingException;




































public class JNDIConnectionPool
  implements ConnectionPool
{
  protected Object jdbcSource = null;
  






  private Method getConnectionWithArgs = null;
  private Method getConnection = null;
  




  protected String jndiPath = null;
  



  protected String user = null;
  



  protected String pwd = null;
  




  public JNDIConnectionPool() {}
  




  public JNDIConnectionPool(String jndiDatasourcePath)
  {
    jndiPath = jndiDatasourcePath.trim();
  }
  




  public void setJndiPath(String jndiPath)
  {
    this.jndiPath = jndiPath;
  }
  




  public String getJndiPath()
  {
    return jndiPath;
  }
  






  public boolean isEnabled()
  {
    return true;
  }
  






  public void setDriver(String d)
  {
    throw new Error("This method is not supported. All connection information is handled by the JDBC datasource provider");
  }
  








  public void setURL(String url)
  {
    throw new Error("This method is not supported. All connection information is handled by the JDBC datasource provider");
  }
  







  public void freeUnused() {}
  







  public boolean hasActiveConnections()
  {
    return false;
  }
  







  public void setPassword(String p)
  {
    if (p != null) p = p.trim();
    if ((p != null) && (p.length() == 0)) { p = null;
    }
    pwd = p;
  }
  







  public void setUser(String u)
  {
    if (u != null) u = u.trim();
    if ((u != null) && (u.length() == 0)) { u = null;
    }
    user = u;
  }
  






  public Connection getConnection()
    throws SQLException
  {
    if (jdbcSource == null)
    {
      try
      {
        findDatasource();
      }
      catch (NamingException ne)
      {
        throw new SQLException("Could not create jndi context for " + jndiPath + " - " + ne.getLocalizedMessage());
      }
    }
    


    try
    {
      if ((user != null) || (pwd != null))
      {
        Object[] arglist = { user, pwd };
        return (Connection)getConnectionWithArgs.invoke(jdbcSource, arglist);
      }
      

      Object[] arglist = new Object[0];
      return (Connection)getConnection.invoke(jdbcSource, arglist);

    }
    catch (Exception e)
    {
      throw new SQLException("Could not create jndi connection for " + jndiPath + " - " + e.getLocalizedMessage());
    }
  }
  






  protected void findDatasource()
    throws NamingException
  {
    try
    {
      InitialContext context = new InitialContext();
      jdbcSource = context.lookup(jndiPath);
      
      Class[] withArgs = { String.class, String.class };
      getConnectionWithArgs = jdbcSource.getClass().getDeclaredMethod("getConnection", withArgs);
      

      Class[] noArgs = new Class[0];
      getConnection = jdbcSource.getClass().getDeclaredMethod("getConnection", noArgs);

    }
    catch (NamingException e)
    {

      throw e;

    }
    catch (NoSuchMethodException e)
    {

      throw new NamingException("Unable to resolve JNDI DataSource - " + e);
    }
  }
  
  public void releaseConnection(Connection con) throws SQLException
  {
    con.close();
  }
  
  public void releaseConnectionOnError(Connection con) throws SQLException
  {
    con.close();
  }
  








  public void setPoolEnabled(boolean flag)
  {
    if (!flag) { jdbcSource = null;
    }
  }
  







  public void setProtocol(Properties p) {}
  






  public void setMinConnections(int n) {}
  






  public boolean testConnection()
  {
    if (jdbcSource == null)
    {
      try
      {
        findDatasource();
      }
      catch (NamingException ne)
      {
        return false;
      }
    }
    
    return true;
  }
}
