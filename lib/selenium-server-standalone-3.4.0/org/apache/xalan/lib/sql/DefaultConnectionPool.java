package org.apache.xalan.lib.sql;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import org.apache.xalan.res.XSLMessages;































public class DefaultConnectionPool
  implements ConnectionPool
{
  private Driver m_Driver = null;
  


  private static final boolean DEBUG = false;
  


  private String m_driver = new String("");
  

  private String m_url = new String("");
  







  private int m_PoolMinSize = 1;
  






  private Properties m_ConnectionProtocol = new Properties();
  



  private Vector m_pool = new Vector();
  



  private boolean m_IsActive = false;
  




  public DefaultConnectionPool() {}
  



  public boolean isEnabled()
  {
    return m_IsActive;
  }
  





  public void setDriver(String d)
  {
    m_driver = d;
  }
  





  public void setURL(String url)
  {
    m_url = url;
  }
  







  public void freeUnused()
  {
    Iterator i = m_pool.iterator();
    while (i.hasNext())
    {
      PooledConnection pcon = (PooledConnection)i.next();
      


      if (!pcon.inUse())
      {





        pcon.close();
        i.remove();
      }
    }
  }
  





  public boolean hasActiveConnections()
  {
    return m_pool.size() > 0;
  }
  






  public void setPassword(String p)
  {
    m_ConnectionProtocol.put("password", p);
  }
  





  public void setUser(String u)
  {
    m_ConnectionProtocol.put("user", u);
  }
  







  public void setProtocol(Properties p)
  {
    Enumeration e = p.keys();
    while (e.hasMoreElements())
    {
      String key = (String)e.nextElement();
      m_ConnectionProtocol.put(key, p.getProperty(key));
    }
  }
  








  public void setMinConnections(int n)
  {
    m_PoolMinSize = n;
  }
  











  public boolean testConnection()
  {
    try
    {
      Connection conn = getConnection();
      










      if (conn == null) { return false;
      }
      releaseConnection(conn);
      





      return true;
    }
    catch (Exception e) {}
    






    return false;
  }
  









  public synchronized Connection getConnection()
    throws IllegalArgumentException, SQLException
  {
    PooledConnection pcon = null;
    




    if (m_pool.size() < m_PoolMinSize) { initializePool();
    }
    
    for (int x = 0; x < m_pool.size(); x++)
    {

      pcon = (PooledConnection)m_pool.elementAt(x);
      

      if (!pcon.inUse())
      {

        pcon.setInUse(true);
        

        return pcon.getConnection();
      }
    }
    




    Connection con = createConnection();
    


    pcon = new PooledConnection(con);
    

    pcon.setInUse(true);
    

    m_pool.addElement(pcon);
    

    return pcon.getConnection();
  }
  






  public synchronized void releaseConnection(Connection con)
    throws SQLException
  {
    for (int x = 0; x < m_pool.size(); x++)
    {

      PooledConnection pcon = (PooledConnection)m_pool.elementAt(x);
      


      if (pcon.getConnection() == con)
      {





        if (!isEnabled())
        {
          con.close();
          m_pool.removeElementAt(x); break;
        }
        








        pcon.setInUse(false);
        

        break;
      }
    }
  }
  







  public synchronized void releaseConnectionOnError(Connection con)
    throws SQLException
  {
    for (int x = 0; x < m_pool.size(); x++)
    {

      PooledConnection pcon = (PooledConnection)m_pool.elementAt(x);
      


      if (pcon.getConnection() == con)
      {





        con.close();
        m_pool.removeElementAt(x);
        



        break;
      }
    }
  }
  




  private Connection createConnection()
    throws SQLException
  {
    Connection con = null;
    


    con = m_Driver.connect(m_url, m_ConnectionProtocol);
    
    return con;
  }
  







  public synchronized void initializePool()
    throws IllegalArgumentException, SQLException
  {
    if (m_driver == null)
    {
      throw new IllegalArgumentException(XSLMessages.createMessage("ER_NO_DRIVER_NAME_SPECIFIED", null));
    }
    

    if (m_url == null)
    {
      throw new IllegalArgumentException(XSLMessages.createMessage("ER_NO_URL_SPECIFIED", null));
    }
    

    if (m_PoolMinSize < 1)
    {
      throw new IllegalArgumentException(XSLMessages.createMessage("ER_POOLSIZE_LESS_THAN_ONE", null));
    }
    






    try
    {
      m_Driver = ((Driver)ObjectFactory.newInstance(m_driver, ObjectFactory.findClassLoader(), true));
      




      DriverManager.registerDriver(m_Driver);
    }
    catch (ObjectFactory.ConfigurationError e)
    {
      throw new IllegalArgumentException(XSLMessages.createMessage("ER_INVALID_DRIVER_NAME", null));

    }
    catch (Exception e)
    {
      throw new IllegalArgumentException(XSLMessages.createMessage("ER_INVALID_DRIVER_NAME", null));
    }
    


    if (!m_IsActive) {
      return;
    }
    
    do
    {
      Connection con = createConnection();
      
      if (con != null)
      {



        PooledConnection pcon = new PooledConnection(con);
        

        addConnection(pcon);

      }
      
    }
    while (m_pool.size() < m_PoolMinSize);
  }
  






  private void addConnection(PooledConnection value)
  {
    m_pool.addElement(value);
  }
  











  protected void finalize()
    throws Throwable
  {
    for (int x = 0; x < m_pool.size(); x++)
    {






      PooledConnection pcon = (PooledConnection)m_pool.elementAt(x);
      


      if (!pcon.inUse()) { pcon.close();
      }
      else
      {



        try
        {



          Thread.sleep(30000L);
          pcon.close();
        }
        catch (InterruptedException ie) {}
      }
    }
    








    super.finalize();
  }
  













  public void setPoolEnabled(boolean flag)
  {
    m_IsActive = flag;
    if (!flag) {
      freeUnused();
    }
  }
}
