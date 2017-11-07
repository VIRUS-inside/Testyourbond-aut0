package org.seleniumhq.jetty9.server.session;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
































public class DatabaseAdaptor
{
  static final Logger LOG = Log.getLogger("org.seleniumhq.jetty9.server.session");
  
  String _dbName;
  
  boolean _isLower;
  
  boolean _isUpper;
  
  protected String _blobType;
  
  protected String _longType;
  
  private String _driverClassName;
  
  private String _connectionUrl;
  
  private Driver _driver;
  
  private DataSource _datasource;
  private String _jndiName;
  
  public DatabaseAdaptor() {}
  
  public void adaptTo(DatabaseMetaData dbMeta)
    throws SQLException
  {
    _dbName = dbMeta.getDatabaseProductName().toLowerCase(Locale.ENGLISH);
    if (LOG.isDebugEnabled())
      LOG.debug("Using database {}", new Object[] { _dbName });
    _isLower = dbMeta.storesLowerCaseIdentifiers();
    _isUpper = dbMeta.storesUpperCaseIdentifiers();
  }
  

  public void setBlobType(String blobType)
  {
    _blobType = blobType;
  }
  
  public String getBlobType()
  {
    if (_blobType != null) {
      return _blobType;
    }
    if (_dbName.startsWith("postgres")) {
      return "bytea";
    }
    return "blob";
  }
  

  public void setLongType(String longType)
  {
    _longType = longType;
  }
  

  public String getLongType()
  {
    if (_longType != null) {
      return _longType;
    }
    if (_dbName == null) {
      throw new IllegalStateException("DbAdaptor missing metadata");
    }
    if (_dbName.startsWith("oracle")) {
      return "number(20)";
    }
    return "bigint";
  }
  








  public String convertIdentifier(String identifier)
  {
    if (identifier == null) {
      return null;
    }
    if (_dbName == null) {
      throw new IllegalStateException("DbAdaptor missing metadata");
    }
    if (_isLower)
      return identifier.toLowerCase(Locale.ENGLISH);
    if (_isUpper) {
      return identifier.toUpperCase(Locale.ENGLISH);
    }
    return identifier;
  }
  

  public String getDBName()
  {
    return _dbName;
  }
  

  public InputStream getBlobInputStream(ResultSet result, String columnName)
    throws SQLException
  {
    if (_dbName == null) {
      throw new IllegalStateException("DbAdaptor missing metadata");
    }
    if (_dbName.startsWith("postgres"))
    {
      byte[] bytes = result.getBytes(columnName);
      return new ByteArrayInputStream(bytes);
    }
    
    Blob blob = result.getBlob(columnName);
    return blob.getBinaryStream();
  }
  

  public boolean isEmptyStringNull()
  {
    if (_dbName == null) {
      throw new IllegalStateException("DbAdaptor missing metadata");
    }
    return _dbName.startsWith("oracle");
  }
  




  public boolean isRowIdReserved()
  {
    if (_dbName == null) {
      throw new IllegalStateException("DbAdaptor missing metadata");
    }
    return (_dbName != null) && (_dbName.startsWith("oracle"));
  }
  






  public void setDriverInfo(String driverClassName, String connectionUrl)
  {
    _driverClassName = driverClassName;
    _connectionUrl = connectionUrl;
  }
  






  public void setDriverInfo(Driver driverClass, String connectionUrl)
  {
    _driver = driverClass;
    _connectionUrl = connectionUrl;
  }
  

  public void setDatasource(DataSource ds)
  {
    _datasource = ds;
  }
  
  public void setDatasourceName(String jndi)
  {
    _jndiName = jndi;
  }
  

  public String getDatasourceName()
  {
    return _jndiName;
  }
  

  public DataSource getDatasource()
  {
    return _datasource;
  }
  

  public String getDriverClassName()
  {
    return _driverClassName;
  }
  

  public Driver getDriver()
  {
    return _driver;
  }
  
  public String getConnectionUrl()
  {
    return _connectionUrl;
  }
  


  public void initialize()
    throws Exception
  {
    if (_datasource != null) {
      return;
    }
    if (_jndiName != null)
    {
      InitialContext ic = new InitialContext();
      _datasource = ((DataSource)ic.lookup(_jndiName));
    }
    else if ((_driver != null) && (_connectionUrl != null))
    {
      DriverManager.registerDriver(_driver);
    }
    else if ((_driverClassName != null) && (_connectionUrl != null))
    {
      Class.forName(_driverClassName);
    }
    else
    {
      try
      {
        InitialContext ic = new InitialContext();
        _datasource = ((DataSource)ic.lookup("jdbc/sessions"));
      }
      catch (NamingException e)
      {
        throw new IllegalStateException("No database configured for sessions");
      }
    }
  }
  







  protected Connection getConnection()
    throws SQLException
  {
    if (_datasource != null) {
      return _datasource.getConnection();
    }
    return DriverManager.getConnection(_connectionUrl);
  }
  





  public String toString()
  {
    return String.format("%s[jndi=%s,driver=%s]", new Object[] { super.toString(), _jndiName, _driverClassName });
  }
}
