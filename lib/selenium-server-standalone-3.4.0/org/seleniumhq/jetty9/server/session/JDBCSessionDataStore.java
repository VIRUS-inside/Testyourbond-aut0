package org.seleniumhq.jetty9.server.session;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import org.seleniumhq.jetty9.util.ClassLoadingObjectInputStream;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;


























public class JDBCSessionDataStore
  extends AbstractSessionDataStore
{
  static final Logger LOG = Log.getLogger("org.seleniumhq.jetty9.server.session");
  
  protected boolean _initialized = false;
  
  private DatabaseAdaptor _dbAdaptor;
  
  private SessionTableSchema _sessionTableSchema;
  
  private boolean _schemaProvided;
  
  public JDBCSessionDataStore() {}
  

  public static class SessionTableSchema
  {
    public static final int MAX_INTERVAL_NOT_SET = -999;
    
    protected DatabaseAdaptor _dbAdaptor;
    protected String _schemaName = null;
    protected String _tableName = "JettySessions";
    protected String _idColumn = "sessionId";
    protected String _contextPathColumn = "contextPath";
    protected String _virtualHostColumn = "virtualHost";
    protected String _lastNodeColumn = "lastNode";
    protected String _accessTimeColumn = "accessTime";
    protected String _lastAccessTimeColumn = "lastAccessTime";
    protected String _createTimeColumn = "createTime";
    protected String _cookieTimeColumn = "cookieTime";
    protected String _lastSavedTimeColumn = "lastSavedTime";
    protected String _expiryTimeColumn = "expiryTime";
    protected String _maxIntervalColumn = "maxInterval";
    protected String _mapColumn = "map";
    
    public SessionTableSchema() {}
    
    protected void setDatabaseAdaptor(DatabaseAdaptor dbadaptor)
    {
      _dbAdaptor = dbadaptor;
    }
    
    public String getSchemaName() {
      return _schemaName;
    }
    
    public void setSchemaName(String schemaName) {
      checkNotNull(schemaName);
      _schemaName = schemaName;
    }
    
    public String getTableName()
    {
      return _tableName;
    }
    
    public void setTableName(String tableName) {
      checkNotNull(tableName);
      _tableName = tableName;
    }
    
    private String getSchemaTableName()
    {
      return (getSchemaName() != null ? getSchemaName() + "." : "") + getTableName();
    }
    
    public String getIdColumn()
    {
      return _idColumn;
    }
    
    public void setIdColumn(String idColumn) {
      checkNotNull(idColumn);
      _idColumn = idColumn;
    }
    
    public String getContextPathColumn() {
      return _contextPathColumn;
    }
    
    public void setContextPathColumn(String contextPathColumn) {
      checkNotNull(contextPathColumn);
      _contextPathColumn = contextPathColumn;
    }
    
    public String getVirtualHostColumn() {
      return _virtualHostColumn;
    }
    
    public void setVirtualHostColumn(String virtualHostColumn) {
      checkNotNull(virtualHostColumn);
      _virtualHostColumn = virtualHostColumn;
    }
    
    public String getLastNodeColumn() {
      return _lastNodeColumn;
    }
    
    public void setLastNodeColumn(String lastNodeColumn) {
      checkNotNull(lastNodeColumn);
      _lastNodeColumn = lastNodeColumn;
    }
    
    public String getAccessTimeColumn() {
      return _accessTimeColumn;
    }
    
    public void setAccessTimeColumn(String accessTimeColumn) {
      checkNotNull(accessTimeColumn);
      _accessTimeColumn = accessTimeColumn;
    }
    
    public String getLastAccessTimeColumn() {
      return _lastAccessTimeColumn;
    }
    
    public void setLastAccessTimeColumn(String lastAccessTimeColumn) {
      checkNotNull(lastAccessTimeColumn);
      _lastAccessTimeColumn = lastAccessTimeColumn;
    }
    
    public String getCreateTimeColumn() {
      return _createTimeColumn;
    }
    
    public void setCreateTimeColumn(String createTimeColumn) {
      checkNotNull(createTimeColumn);
      _createTimeColumn = createTimeColumn;
    }
    
    public String getCookieTimeColumn() {
      return _cookieTimeColumn;
    }
    
    public void setCookieTimeColumn(String cookieTimeColumn) {
      checkNotNull(cookieTimeColumn);
      _cookieTimeColumn = cookieTimeColumn;
    }
    
    public String getLastSavedTimeColumn() {
      return _lastSavedTimeColumn;
    }
    
    public void setLastSavedTimeColumn(String lastSavedTimeColumn) {
      checkNotNull(lastSavedTimeColumn);
      _lastSavedTimeColumn = lastSavedTimeColumn;
    }
    
    public String getExpiryTimeColumn() {
      return _expiryTimeColumn;
    }
    
    public void setExpiryTimeColumn(String expiryTimeColumn) {
      checkNotNull(expiryTimeColumn);
      _expiryTimeColumn = expiryTimeColumn;
    }
    
    public String getMaxIntervalColumn() {
      return _maxIntervalColumn;
    }
    
    public void setMaxIntervalColumn(String maxIntervalColumn) {
      checkNotNull(maxIntervalColumn);
      _maxIntervalColumn = maxIntervalColumn;
    }
    
    public String getMapColumn() {
      return _mapColumn;
    }
    
    public void setMapColumn(String mapColumn) {
      checkNotNull(mapColumn);
      _mapColumn = mapColumn;
    }
    
    public String getCreateStatementAsString()
    {
      if (_dbAdaptor == null) {
        throw new IllegalStateException("No DBAdaptor");
      }
      String blobType = _dbAdaptor.getBlobType();
      String longType = _dbAdaptor.getLongType();
      
      return "create table " + _tableName + " (" + _idColumn + " varchar(120), " + _contextPathColumn + " varchar(60), " + _virtualHostColumn + " varchar(60), " + _lastNodeColumn + " varchar(60), " + _accessTimeColumn + " " + longType + ", " + _lastAccessTimeColumn + " " + longType + ", " + _createTimeColumn + " " + longType + ", " + _cookieTimeColumn + " " + longType + ", " + _lastSavedTimeColumn + " " + longType + ", " + _expiryTimeColumn + " " + longType + ", " + _maxIntervalColumn + " " + longType + ", " + _mapColumn + " " + blobType + ", primary key(" + _idColumn + ", " + _contextPathColumn + "," + _virtualHostColumn + "))";
    }
    




    public String getCreateIndexOverExpiryStatementAsString(String indexName)
    {
      return "create index " + indexName + " on " + getSchemaTableName() + " (" + getExpiryTimeColumn() + ")";
    }
    
    public String getCreateIndexOverSessionStatementAsString(String indexName)
    {
      return "create index " + indexName + " on " + getSchemaTableName() + " (" + getIdColumn() + ", " + getContextPathColumn() + ")";
    }
    
    public String getAlterTableForMaxIntervalAsString()
    {
      if (_dbAdaptor == null)
        throw new IllegalStateException("No DBAdaptor");
      String longType = _dbAdaptor.getLongType();
      String stem = "alter table " + getSchemaTableName() + " add " + getMaxIntervalColumn() + " " + longType;
      if (_dbAdaptor.getDBName().contains("oracle")) {
        return stem + " default " + 64537 + " not null";
      }
      return stem + " not null default " + 64537;
    }
    
    private void checkNotNull(String s)
    {
      if (s == null)
        throw new IllegalArgumentException(s);
    }
    
    public String getInsertSessionStatementAsString() {
      return 
      

        "insert into " + getSchemaTableName() + " (" + getIdColumn() + ", " + getContextPathColumn() + ", " + getVirtualHostColumn() + ", " + getLastNodeColumn() + ", " + getAccessTimeColumn() + ", " + getLastAccessTimeColumn() + ", " + getCreateTimeColumn() + ", " + getCookieTimeColumn() + ", " + getLastSavedTimeColumn() + ", " + getExpiryTimeColumn() + ", " + getMaxIntervalColumn() + ", " + getMapColumn() + ")  values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    }
    




    public PreparedStatement getUpdateSessionStatement(Connection connection, String canonicalContextPath)
      throws SQLException
    {
      String s = "update " + getSchemaTableName() + " set " + getLastNodeColumn() + " = ?, " + getAccessTimeColumn() + " = ?, " + getLastAccessTimeColumn() + " = ?, " + getLastSavedTimeColumn() + " = ?, " + getExpiryTimeColumn() + " = ?, " + getMaxIntervalColumn() + " = ?, " + getMapColumn() + " = ? where ";
      
      if ((canonicalContextPath == null) || ("".equals(canonicalContextPath)))
      {
        if (_dbAdaptor.isEmptyStringNull())
        {


          s = s + getIdColumn() + " = ? and " + getContextPathColumn() + " is null and " + getVirtualHostColumn() + " = ?";
          return connection.prepareStatement(s);
        }
      }
      
      return connection.prepareStatement(s + getIdColumn() + " = ? and " + getContextPathColumn() + " = ? and " + 
        getVirtualHostColumn() + " = ?");
    }
    

    public PreparedStatement getExpiredSessionsStatement(Connection connection, String canonicalContextPath, String vhost, long expiry)
      throws SQLException
    {
      if (_dbAdaptor == null) {
        throw new IllegalStateException("No DB adaptor");
      }
      
      if ((canonicalContextPath == null) || ("".equals(canonicalContextPath)))
      {
        if (_dbAdaptor.isEmptyStringNull())
        {
          PreparedStatement statement = connection.prepareStatement("select " + getIdColumn() + ", " + getExpiryTimeColumn() + " from " + 
            getSchemaTableName() + " where " + 
            getContextPathColumn() + " is null and " + 
            getVirtualHostColumn() + " = ? and " + getExpiryTimeColumn() + " >0 and " + getExpiryTimeColumn() + " <= ?");
          statement.setString(1, vhost);
          statement.setLong(2, expiry);
          return statement;
        }
      }
      
      PreparedStatement statement = connection.prepareStatement("select " + getIdColumn() + ", " + getExpiryTimeColumn() + " from " + 
        getSchemaTableName() + " where " + getContextPathColumn() + " = ? and " + 
        getVirtualHostColumn() + " = ? and " + 
        getExpiryTimeColumn() + " >0 and " + getExpiryTimeColumn() + " <= ?");
      
      statement.setString(1, canonicalContextPath);
      statement.setString(2, vhost);
      statement.setLong(3, expiry);
      return statement;
    }
    

    public PreparedStatement getMyExpiredSessionsStatement(Connection connection, SessionContext sessionContext, long expiry)
      throws SQLException
    {
      if (_dbAdaptor == null) {
        throw new IllegalStateException("No DB adaptor");
      }
      if ((sessionContext.getCanonicalContextPath() == null) || ("".equals(sessionContext.getCanonicalContextPath())))
      {
        if (_dbAdaptor.isEmptyStringNull())
        {
          PreparedStatement statement = connection.prepareStatement("select " + getIdColumn() + ", " + getExpiryTimeColumn() + " from " + 
            getSchemaTableName() + " where " + 
            getLastNodeColumn() + " = ?  and " + 
            getContextPathColumn() + " is null and " + 
            getVirtualHostColumn() + " = ? and " + getExpiryTimeColumn() + " >0 and " + getExpiryTimeColumn() + " <= ?");
          statement.setString(1, sessionContext.getWorkerName());
          statement.setString(2, sessionContext.getVhost());
          statement.setLong(3, expiry);
          return statement;
        }
      }
      
      PreparedStatement statement = connection.prepareStatement("select " + getIdColumn() + ", " + getExpiryTimeColumn() + " from " + 
        getSchemaTableName() + " where " + 
        getLastNodeColumn() + " = ? and " + 
        getContextPathColumn() + " = ? and " + 
        getVirtualHostColumn() + " = ? and " + 
        getExpiryTimeColumn() + " >0 and " + getExpiryTimeColumn() + " <= ?");
      
      statement.setString(1, sessionContext.getWorkerName());
      statement.setString(2, sessionContext.getCanonicalContextPath());
      statement.setString(3, sessionContext.getVhost());
      statement.setLong(4, expiry);
      return statement;
    }
    

    public PreparedStatement getAllAncientExpiredSessionsStatement(Connection connection)
      throws SQLException
    {
      if (_dbAdaptor == null) {
        throw new IllegalStateException("No DB adaptor");
      }
      PreparedStatement statement = connection.prepareStatement("select " + getIdColumn() + ", " + getContextPathColumn() + ", " + getVirtualHostColumn() + " from " + 
        getSchemaTableName() + " where " + 
        getExpiryTimeColumn() + " >0 and " + getExpiryTimeColumn() + " <= ?");
      return statement;
    }
    

    public PreparedStatement getCheckSessionExistsStatement(Connection connection, String canonicalContextPath)
      throws SQLException
    {
      if (_dbAdaptor == null) {
        throw new IllegalStateException("No DB adaptor");
      }
      
      if ((canonicalContextPath == null) || ("".equals(canonicalContextPath)))
      {
        if (_dbAdaptor.isEmptyStringNull())
        {
          PreparedStatement statement = connection.prepareStatement("select " + getIdColumn() + ", " + getExpiryTimeColumn() + " from " + 
            getSchemaTableName() + " where " + 
            getIdColumn() + " = ? and " + 
            getContextPathColumn() + " is null and " + 
            getVirtualHostColumn() + " = ?");
          return statement;
        }
      }
      
      PreparedStatement statement = connection.prepareStatement("select " + getIdColumn() + ", " + getExpiryTimeColumn() + " from " + 
        getSchemaTableName() + " where " + 
        getIdColumn() + " = ? and " + 
        getContextPathColumn() + " = ? and " + 
        getVirtualHostColumn() + " = ?");
      return statement;
    }
    
    public void fillCheckSessionExistsStatement(PreparedStatement statement, String id, SessionContext contextId)
      throws SQLException
    {
      statement.clearParameters();
      ParameterMetaData metaData = statement.getParameterMetaData();
      if (metaData.getParameterCount() < 3)
      {
        statement.setString(1, id);
        statement.setString(2, contextId.getVhost());
      }
      else
      {
        statement.setString(1, id);
        statement.setString(2, contextId.getCanonicalContextPath());
        statement.setString(3, contextId.getVhost());
      }
    }
    

    public PreparedStatement getLoadStatement(Connection connection, String id, SessionContext contextId)
      throws SQLException
    {
      if (_dbAdaptor == null) {
        throw new IllegalStateException("No DB adaptor");
      }
      
      if ((contextId.getCanonicalContextPath() == null) || ("".equals(contextId.getCanonicalContextPath())))
      {
        if (_dbAdaptor.isEmptyStringNull())
        {
          PreparedStatement statement = connection.prepareStatement("select * from " + getSchemaTableName() + " where " + 
            getIdColumn() + " = ? and " + 
            getContextPathColumn() + " is null and " + 
            getVirtualHostColumn() + " = ?");
          statement.setString(1, id);
          statement.setString(2, contextId.getVhost());
          
          return statement;
        }
      }
      
      PreparedStatement statement = connection.prepareStatement("select * from " + getSchemaTableName() + " where " + 
        getIdColumn() + " = ? and " + getContextPathColumn() + " = ? and " + 
        getVirtualHostColumn() + " = ?");
      statement.setString(1, id);
      statement.setString(2, contextId.getCanonicalContextPath());
      statement.setString(3, contextId.getVhost());
      
      return statement;
    }
    


    public PreparedStatement getUpdateStatement(Connection connection, String id, SessionContext contextId)
      throws SQLException
    {
      if (_dbAdaptor == null) {
        throw new IllegalStateException("No DB adaptor");
      }
      


      String s = "update " + getSchemaTableName() + " set " + getLastNodeColumn() + " = ?, " + getAccessTimeColumn() + " = ?, " + getLastAccessTimeColumn() + " = ?, " + getLastSavedTimeColumn() + " = ?, " + getExpiryTimeColumn() + " = ?, " + getMaxIntervalColumn() + " = ?, " + getMapColumn() + " = ? where ";
      
      if ((contextId.getCanonicalContextPath() == null) || ("".equals(contextId.getCanonicalContextPath())))
      {
        if (_dbAdaptor.isEmptyStringNull())
        {
          PreparedStatement statement = connection.prepareStatement(s + getIdColumn() + " = ? and " + 
            getContextPathColumn() + " is null and " + 
            getVirtualHostColumn() + " = ?");
          statement.setString(1, id);
          statement.setString(2, contextId.getVhost());
          return statement;
        }
      }
      PreparedStatement statement = connection.prepareStatement(s + getIdColumn() + " = ? and " + getContextPathColumn() + " = ? and " + 
        getVirtualHostColumn() + " = ?");
      statement.setString(1, id);
      statement.setString(2, contextId.getCanonicalContextPath());
      statement.setString(3, contextId.getVhost());
      
      return statement;
    }
    



    public PreparedStatement getDeleteStatement(Connection connection, String id, SessionContext contextId)
      throws Exception
    {
      if (_dbAdaptor == null)
      {
        throw new IllegalStateException("No DB adaptor");
      }
      
      if ((contextId.getCanonicalContextPath() == null) || ("".equals(contextId.getCanonicalContextPath())))
      {
        if (_dbAdaptor.isEmptyStringNull())
        {
          PreparedStatement statement = connection.prepareStatement("delete from " + getSchemaTableName() + " where " + 
            getIdColumn() + " = ? and " + getContextPathColumn() + " = ? and " + 
            getVirtualHostColumn() + " = ?");
          statement.setString(1, id);
          statement.setString(2, contextId.getVhost());
          return statement;
        }
      }
      
      PreparedStatement statement = connection.prepareStatement("delete from " + getSchemaTableName() + " where " + 
        getIdColumn() + " = ? and " + getContextPathColumn() + " = ? and " + 
        getVirtualHostColumn() + " = ?");
      statement.setString(1, id);
      statement.setString(2, contextId.getCanonicalContextPath());
      statement.setString(3, contextId.getVhost());
      
      return statement;
    }
    






    public void prepareTables()
      throws SQLException
    {
      Connection connection = _dbAdaptor.getConnection();Throwable localThrowable12 = null;
      try { Statement statement = connection.createStatement();Throwable localThrowable13 = null;
        try
        {
          connection.setAutoCommit(true);
          DatabaseMetaData metaData = connection.getMetaData();
          _dbAdaptor.adaptTo(metaData);
          


          String tableName = _dbAdaptor.convertIdentifier(getTableName());
          String schemaName = _dbAdaptor.convertIdentifier(getSchemaName());
          ResultSet result = metaData.getTables(null, schemaName, tableName, null);Throwable localThrowable14 = null;
          try {
            if (!result.next())
            {

              statement.executeUpdate(getCreateStatementAsString());

            }
            else
            {
              ResultSet colResult = null;
              try
              {
                colResult = metaData.getColumns(null, schemaName, tableName, _dbAdaptor
                  .convertIdentifier(getMaxIntervalColumn()));
              }
              catch (SQLException s)
              {
                JDBCSessionDataStore.LOG.warn("Problem checking if " + getTableName() + " table contains " + 
                  getMaxIntervalColumn() + " column. Ensure table contains column definition: \"" + 
                  getMaxIntervalColumn() + " long not null default -999\"", new Object[0]);
                throw s;
              }
              try
              {
                if (!colResult.next())
                {
                  try
                  {

                    statement.executeUpdate(getAlterTableForMaxIntervalAsString());
                  }
                  catch (SQLException s)
                  {
                    JDBCSessionDataStore.LOG.warn("Problem adding " + getMaxIntervalColumn() + " column. Ensure table contains column definition: \"" + 
                      getMaxIntervalColumn() + " long not null default -999\"", new Object[0]);
                    
                    throw s;
                  }
                }
              }
              finally
              {
                colResult.close();
              }
            }
          }
          catch (Throwable localThrowable1)
          {
            localThrowable14 = localThrowable1;throw localThrowable1;
          }
          finally {}
          












































          String index1 = "idx_" + getTableName() + "_expiry";
          String index2 = "idx_" + getTableName() + "_session";
          
          boolean index1Exists = false;
          boolean index2Exists = false;
          ResultSet result = metaData.getIndexInfo(null, schemaName, tableName, false, true);Throwable localThrowable15 = null;
          try {
            while (result.next())
            {
              String idxName = result.getString("INDEX_NAME");
              if (index1.equalsIgnoreCase(idxName)) {
                index1Exists = true;
              } else if (index2.equalsIgnoreCase(idxName)) {
                index2Exists = true;
              }
            }
          }
          catch (Throwable localThrowable4)
          {
            localThrowable15 = localThrowable4;throw localThrowable4;
          }
          finally {}
          







          if (!index1Exists)
            statement.executeUpdate(getCreateIndexOverExpiryStatementAsString(index1));
          if (!index2Exists) {
            statement.executeUpdate(getCreateIndexOverSessionStatementAsString(index2));
          }
        }
        catch (Throwable localThrowable7)
        {
          localThrowable13 = localThrowable7;throw localThrowable7; } finally {} } catch (Throwable localThrowable10) { localThrowable12 = localThrowable10;throw localThrowable10;






































      }
      finally
      {






































        if (connection != null) { if (localThrowable12 != null) try { connection.close(); } catch (Throwable localThrowable11) { localThrowable12.addSuppressed(localThrowable11); } else { connection.close();
          }
        }
      }
    }
    
    public String toString()
    {
      return String.format("%s[%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s]", new Object[] { super.toString(), _schemaName, _tableName, _idColumn, _contextPathColumn, _virtualHostColumn, _cookieTimeColumn, _createTimeColumn, _expiryTimeColumn, _accessTimeColumn, _lastAccessTimeColumn, _lastNodeColumn, _lastSavedTimeColumn, _maxIntervalColumn });
    }
  }
  

















  protected void doStart()
    throws Exception
  {
    if (_dbAdaptor == null) {
      throw new IllegalStateException("No jdbc config");
    }
    initialize();
    super.doStart();
  }
  



  protected void doStop()
    throws Exception
  {
    super.doStop();
    _initialized = false;
    if (!_schemaProvided) {
      _sessionTableSchema = null;
    }
  }
  

  public void initialize()
    throws Exception
  {
    if (!_initialized)
    {
      _initialized = true;
      

      if (_sessionTableSchema == null)
      {
        _sessionTableSchema = new SessionTableSchema();
        addBean(_sessionTableSchema, true);
      }
      
      _dbAdaptor.initialize();
      _sessionTableSchema.setDatabaseAdaptor(_dbAdaptor);
      _sessionTableSchema.prepareTables();
    }
  }
  





  public SessionData load(final String id)
    throws Exception
  {
    final AtomicReference<SessionData> reference = new AtomicReference();
    final AtomicReference<Exception> exception = new AtomicReference();
    
    Runnable r = new Runnable()
    {
      public void run() {
        try {
          Connection connection = _dbAdaptor.getConnection();Throwable localThrowable15 = null;
          try { PreparedStatement statement = _sessionTableSchema.getLoadStatement(connection, id, _context);Throwable localThrowable16 = null;
            try { ResultSet result = statement.executeQuery();Throwable localThrowable17 = null;
              try {
                SessionData data = null;
                if (result.next())
                {
                  data = newSessionData(id, result
                    .getLong(_sessionTableSchema.getCreateTimeColumn()), result
                    .getLong(_sessionTableSchema.getAccessTimeColumn()), result
                    .getLong(_sessionTableSchema.getLastAccessTimeColumn()), result
                    .getLong(_sessionTableSchema.getMaxIntervalColumn()));
                  data.setCookieSet(result.getLong(_sessionTableSchema.getCookieTimeColumn()));
                  data.setLastNode(result.getString(_sessionTableSchema.getLastNodeColumn()));
                  data.setLastSaved(result.getLong(_sessionTableSchema.getLastSavedTimeColumn()));
                  data.setExpiry(result.getLong(_sessionTableSchema.getExpiryTimeColumn()));
                  data.setContextPath(result.getString(_sessionTableSchema.getContextPathColumn()));
                  data.setVhost(result.getString(_sessionTableSchema.getVirtualHostColumn()));
                  try {
                    InputStream is = _dbAdaptor.getBlobInputStream(result, _sessionTableSchema.getMapColumn());Throwable localThrowable18 = null;
                    try { ClassLoadingObjectInputStream ois = new ClassLoadingObjectInputStream(is);Throwable localThrowable19 = null;
                      try {
                        Object o = ois.readObject();
                        data.putAllAttributes((Map)o);
                      }
                      catch (Throwable localThrowable1)
                      {
                        localThrowable19 = localThrowable1;throw localThrowable1; } finally {} } catch (Throwable localThrowable4) { localThrowable18 = localThrowable4;throw localThrowable4;

                    }
                    finally
                    {
                      if (is != null) if (localThrowable18 != null) try { is.close(); } catch (Throwable localThrowable5) { localThrowable18.addSuppressed(localThrowable5); } else is.close();
                    }
                  } catch (Exception e) {
                    throw new UnreadableSessionDataException(id, _context, e);
                  }
                  
                  if (JDBCSessionDataStore.LOG.isDebugEnabled()) {
                    JDBCSessionDataStore.LOG.debug("LOADED session {}", new Object[] { data });
                  }
                }
                else if (JDBCSessionDataStore.LOG.isDebugEnabled()) {
                  JDBCSessionDataStore.LOG.debug("No session {}", new Object[] { id });
                }
                reference.set(data);
              }
              catch (Throwable localThrowable7)
              {
                localThrowable17 = localThrowable7;throw localThrowable7; } finally {} } catch (Throwable localThrowable10) { localThrowable16 = localThrowable10;throw localThrowable10; } finally {} } catch (Throwable localThrowable13) { localThrowable15 = localThrowable13;throw localThrowable13;

















          }
          finally
          {

















            if (connection != null) if (localThrowable15 != null) try { connection.close(); } catch (Throwable localThrowable14) { localThrowable15.addSuppressed(localThrowable14); } else connection.close();
          }
        } catch (Exception e) {
          exception.set(e);
        }
        
      }
      
    };
    _context.run(r);
    if (exception.get() != null) {
      throw ((Exception)exception.get());
    }
    return (SessionData)reference.get();
  }
  
  /* Error */
  public boolean delete(String id)
    throws Exception
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 32	org/seleniumhq/jetty9/server/session/JDBCSessionDataStore:_dbAdaptor	Lorg/seleniumhq/jetty9/server/session/DatabaseAdaptor;
    //   4: invokevirtual 109	org/seleniumhq/jetty9/server/session/DatabaseAdaptor:getConnection	()Ljava/sql/Connection;
    //   7: astore_2
    //   8: aconst_null
    //   9: astore_3
    //   10: aload_0
    //   11: getfield 51	org/seleniumhq/jetty9/server/session/JDBCSessionDataStore:_sessionTableSchema	Lorg/seleniumhq/jetty9/server/session/JDBCSessionDataStore$SessionTableSchema;
    //   14: aload_2
    //   15: aload_1
    //   16: aload_0
    //   17: getfield 78	org/seleniumhq/jetty9/server/session/JDBCSessionDataStore:_context	Lorg/seleniumhq/jetty9/server/session/SessionContext;
    //   20: invokevirtual 113	org/seleniumhq/jetty9/server/session/JDBCSessionDataStore$SessionTableSchema:getDeleteStatement	(Ljava/sql/Connection;Ljava/lang/String;Lorg/seleniumhq/jetty9/server/session/SessionContext;)Ljava/sql/PreparedStatement;
    //   23: astore 4
    //   25: aconst_null
    //   26: astore 5
    //   28: aload_2
    //   29: iconst_1
    //   30: invokeinterface 119 2 0
    //   35: aload 4
    //   37: invokeinterface 125 1 0
    //   42: istore 6
    //   44: getstatic 127	org/seleniumhq/jetty9/server/session/JDBCSessionDataStore:LOG	Lorg/seleniumhq/jetty9/util/log/Logger;
    //   47: invokeinterface 133 1 0
    //   52: ifeq +37 -> 89
    //   55: getstatic 127	org/seleniumhq/jetty9/server/session/JDBCSessionDataStore:LOG	Lorg/seleniumhq/jetty9/util/log/Logger;
    //   58: ldc -121
    //   60: iconst_2
    //   61: anewarray 137	java/lang/Object
    //   64: dup
    //   65: iconst_0
    //   66: aload_1
    //   67: aastore
    //   68: dup
    //   69: iconst_1
    //   70: iload 6
    //   72: ifle +7 -> 79
    //   75: iconst_1
    //   76: goto +4 -> 80
    //   79: iconst_0
    //   80: invokestatic 147	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
    //   83: aastore
    //   84: invokeinterface 151 3 0
    //   89: iload 6
    //   91: ifle +7 -> 98
    //   94: iconst_1
    //   95: goto +4 -> 99
    //   98: iconst_0
    //   99: istore 7
    //   101: aload 4
    //   103: ifnull +37 -> 140
    //   106: aload 5
    //   108: ifnull +25 -> 133
    //   111: aload 4
    //   113: invokeinterface 154 1 0
    //   118: goto +22 -> 140
    //   121: astore 8
    //   123: aload 5
    //   125: aload 8
    //   127: invokevirtual 158	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   130: goto +10 -> 140
    //   133: aload 4
    //   135: invokeinterface 154 1 0
    //   140: aload_2
    //   141: ifnull +33 -> 174
    //   144: aload_3
    //   145: ifnull +23 -> 168
    //   148: aload_2
    //   149: invokeinterface 159 1 0
    //   154: goto +20 -> 174
    //   157: astore 8
    //   159: aload_3
    //   160: aload 8
    //   162: invokevirtual 158	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   165: goto +9 -> 174
    //   168: aload_2
    //   169: invokeinterface 159 1 0
    //   174: iload 7
    //   176: ireturn
    //   177: astore 6
    //   179: aload 6
    //   181: astore 5
    //   183: aload 6
    //   185: athrow
    //   186: astore 9
    //   188: aload 4
    //   190: ifnull +37 -> 227
    //   193: aload 5
    //   195: ifnull +25 -> 220
    //   198: aload 4
    //   200: invokeinterface 154 1 0
    //   205: goto +22 -> 227
    //   208: astore 10
    //   210: aload 5
    //   212: aload 10
    //   214: invokevirtual 158	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   217: goto +10 -> 227
    //   220: aload 4
    //   222: invokeinterface 154 1 0
    //   227: aload 9
    //   229: athrow
    //   230: astore 4
    //   232: aload 4
    //   234: astore_3
    //   235: aload 4
    //   237: athrow
    //   238: astore 11
    //   240: aload_2
    //   241: ifnull +33 -> 274
    //   244: aload_3
    //   245: ifnull +23 -> 268
    //   248: aload_2
    //   249: invokeinterface 159 1 0
    //   254: goto +20 -> 274
    //   257: astore 12
    //   259: aload_3
    //   260: aload 12
    //   262: invokevirtual 158	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   265: goto +9 -> 274
    //   268: aload_2
    //   269: invokeinterface 159 1 0
    //   274: aload 11
    //   276: athrow
    // Line number table:
    //   Java source line #768	-> byte code offset #0
    //   Java source line #769	-> byte code offset #10
    //   Java source line #768	-> byte code offset #25
    //   Java source line #771	-> byte code offset #28
    //   Java source line #772	-> byte code offset #35
    //   Java source line #773	-> byte code offset #44
    //   Java source line #774	-> byte code offset #55
    //   Java source line #776	-> byte code offset #89
    //   Java source line #777	-> byte code offset #101
    //   Java source line #776	-> byte code offset #174
    //   Java source line #768	-> byte code offset #177
    //   Java source line #777	-> byte code offset #186
    //   Java source line #768	-> byte code offset #230
    //   Java source line #777	-> byte code offset #238
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	277	0	this	JDBCSessionDataStore
    //   0	277	1	id	String
    //   7	262	2	connection	Connection
    //   9	251	3	localThrowable6	Throwable
    //   23	198	4	statement	PreparedStatement
    //   230	6	4	localThrowable4	Throwable
    //   26	185	5	localThrowable7	Throwable
    //   42	48	6	rows	int
    //   177	7	6	localThrowable2	Throwable
    //   99	76	7	bool	boolean
    //   121	5	8	localThrowable	Throwable
    //   157	4	8	localThrowable1	Throwable
    //   186	42	9	localObject1	Object
    //   208	5	10	localThrowable3	Throwable
    //   238	37	11	localObject2	Object
    //   257	4	12	localThrowable5	Throwable
    // Exception table:
    //   from	to	target	type
    //   111	118	121	java/lang/Throwable
    //   148	154	157	java/lang/Throwable
    //   28	101	177	java/lang/Throwable
    //   28	101	186	finally
    //   177	188	186	finally
    //   198	205	208	java/lang/Throwable
    //   10	140	230	java/lang/Throwable
    //   177	230	230	java/lang/Throwable
    //   10	140	238	finally
    //   177	240	238	finally
    //   248	254	257	java/lang/Throwable
  }
  
  public void doStore(String id, SessionData data, long lastSaveTime)
    throws Exception
  {
    if ((data == null) || (id == null)) {
      return;
    }
    if (lastSaveTime <= 0L)
    {
      doInsert(id, data);
    }
    else
    {
      doUpdate(id, data);
    }
  }
  

  private void doInsert(String id, SessionData data)
    throws Exception
  {
    String s = _sessionTableSchema.getInsertSessionStatementAsString();
    

    Connection connection = _dbAdaptor.getConnection();Throwable localThrowable6 = null;
    try {
      connection.setAutoCommit(true);
      PreparedStatement statement = connection.prepareStatement(s);Throwable localThrowable7 = null;
      try {
        statement.setString(1, id);
        statement.setString(2, _context.getCanonicalContextPath());
        statement.setString(3, _context.getVhost());
        statement.setString(4, data.getLastNode());
        statement.setLong(5, data.getAccessed());
        statement.setLong(6, data.getLastAccessed());
        statement.setLong(7, data.getCreated());
        statement.setLong(8, data.getCookieSet());
        statement.setLong(9, data.getLastSaved());
        statement.setLong(10, data.getExpiry());
        statement.setLong(11, data.getMaxInactiveMs());
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(data.getAllAttributes());
        oos.flush();
        byte[] bytes = baos.toByteArray();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        statement.setBinaryStream(12, bais, bytes.length);
        statement.executeUpdate();
        if (LOG.isDebugEnabled()) {
          LOG.debug("Inserted session " + data, new Object[0]);
        }
      }
      catch (Throwable localThrowable1)
      {
        localThrowable7 = localThrowable1;throw localThrowable1;
      }
      finally {}
    }
    catch (Throwable localThrowable4)
    {
      localThrowable6 = localThrowable4;throw localThrowable4;













    }
    finally
    {












      if (connection != null) if (localThrowable6 != null) try { connection.close(); } catch (Throwable localThrowable5) { localThrowable6.addSuppressed(localThrowable5); } else { connection.close();
        }
    }
  }
  
  private void doUpdate(String id, SessionData data)
    throws Exception
  {
    Connection connection = _dbAdaptor.getConnection();Throwable localThrowable6 = null;
    try {
      connection.setAutoCommit(true);
      PreparedStatement statement = _sessionTableSchema.getUpdateSessionStatement(connection, _context.getCanonicalContextPath());Throwable localThrowable7 = null;
      try {
        statement.setString(1, data.getLastNode());
        statement.setLong(2, data.getAccessed());
        statement.setLong(3, data.getLastAccessed());
        statement.setLong(4, data.getLastSaved());
        statement.setLong(5, data.getExpiry());
        statement.setLong(6, data.getMaxInactiveMs());
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(data.getAllAttributes());
        oos.flush();
        byte[] bytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        statement.setBinaryStream(7, bais, bytes.length);
        
        if (((_context.getCanonicalContextPath() == null) || ("".equals(_context.getCanonicalContextPath()))) && (_dbAdaptor.isEmptyStringNull()))
        {
          statement.setString(8, id);
          statement.setString(9, _context.getVhost());
        }
        else
        {
          statement.setString(8, id);
          statement.setString(9, _context.getCanonicalContextPath());
          statement.setString(10, _context.getVhost());
        }
        
        statement.executeUpdate();
        
        if (LOG.isDebugEnabled()) {
          LOG.debug("Updated session " + data, new Object[0]);
        }
      }
      catch (Throwable localThrowable1)
      {
        localThrowable7 = localThrowable1;throw localThrowable1;
      }
      finally {}
    }
    catch (Throwable localThrowable4)
    {
      localThrowable6 = localThrowable4;throw localThrowable4;

















    }
    finally
    {
















      if (connection != null) { if (localThrowable6 != null) try { connection.close(); } catch (Throwable localThrowable5) { localThrowable6.addSuppressed(localThrowable5); } else { connection.close();
        }
      }
    }
  }
  



  public Set<String> doGetExpired(Set<String> candidates)
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Getting expired sessions " + System.currentTimeMillis(), new Object[0]);
    }
    long now = System.currentTimeMillis();
    
    expiredSessionKeys = new HashSet();
    try { Connection connection = _dbAdaptor.getConnection();Throwable localThrowable21 = null;
      try {
        connection.setAutoCommit(true);
        



        long upperBound = now;
        if (LOG.isDebugEnabled()) {
          LOG.debug("{}- Pass 1: Searching for sessions for context {} managed by me {} and expired before {}", new Object[] { _context.getCanonicalContextPath(), _context.getWorkerName(), Long.valueOf(upperBound) });
        }
        PreparedStatement statement = _sessionTableSchema.getExpiredSessionsStatement(connection, _context.getCanonicalContextPath(), _context.getVhost(), upperBound);Object localObject1 = null;
        Object localObject2;
        try { ResultSet result = statement.executeQuery();localObject2 = null;
          try {
            while (result.next())
            {
              String sessionId = result.getString(_sessionTableSchema.getIdColumn());
              long exp = result.getLong(_sessionTableSchema.getExpiryTimeColumn());
              expiredSessionKeys.add(sessionId);
              if (LOG.isDebugEnabled()) LOG.debug(_context.getCanonicalContextPath() + "- Found expired sessionId=" + sessionId, new Object[0]);
            }
          }
          catch (Throwable localThrowable1)
          {
            localObject2 = localThrowable1;throw localThrowable1;
          }
          finally {}
        }
        catch (Throwable localThrowable4)
        {
          localObject1 = localThrowable4;throw localThrowable4;




        }
        finally
        {




          if (statement != null) { if (localObject1 != null) try { statement.close(); } catch (Throwable localThrowable5) { ((Throwable)localObject1).addSuppressed(localThrowable5); } else { statement.close();
            }
          }
        }
        


        PreparedStatement selectExpiredSessions = _sessionTableSchema.getAllAncientExpiredSessionsStatement(connection);localObject1 = null;
        String vh;
        try { if (_lastExpiryCheckTime <= 0L) {
            upperBound = now - 3L * (1000L * _gracePeriodSec);
          } else {
            upperBound = _lastExpiryCheckTime - 1000L * _gracePeriodSec;
          }
          if (LOG.isDebugEnabled()) { LOG.debug("{}- Pass 2: Searching for sessions expired before {}", new Object[] { _context.getWorkerName(), Long.valueOf(upperBound) });
          }
          selectExpiredSessions.setLong(1, upperBound);
          ResultSet result = selectExpiredSessions.executeQuery();localObject2 = null;
          try {
            while (result.next())
            {
              String sessionId = result.getString(_sessionTableSchema.getIdColumn());
              String ctxtpth = result.getString(_sessionTableSchema.getContextPathColumn());
              vh = result.getString(_sessionTableSchema.getVirtualHostColumn());
              expiredSessionKeys.add(sessionId);
              if (LOG.isDebugEnabled()) LOG.debug("{}- Found expired sessionId=", new Object[] { _context.getWorkerName(), sessionId });
            }
          }
          catch (Throwable localThrowable7)
          {
            localObject2 = localThrowable7;throw localThrowable7;
          }
          finally {}
        }
        catch (Throwable localThrowable10)
        {
          localObject1 = localThrowable10;throw localThrowable10;









        }
        finally
        {








          if (selectExpiredSessions != null) if (localObject1 != null) try { selectExpiredSessions.close(); } catch (Throwable localThrowable11) { ((Throwable)localObject1).addSuppressed(localThrowable11); } else { selectExpiredSessions.close();
            }
        }
        Set<String> notExpiredInDB = new HashSet();
        for (localObject1 = candidates.iterator(); ((Iterator)localObject1).hasNext();) { k = (String)((Iterator)localObject1).next();
          



          if (!expiredSessionKeys.contains(k))
            notExpiredInDB.add(k);
        }
        String k;
        Object checkSessionExists;
        if (!notExpiredInDB.isEmpty())
        {

          checkSessionExists = _sessionTableSchema.getCheckSessionExistsStatement(connection, _context.getCanonicalContextPath());k = null;
          try {
            for (localObject2 = notExpiredInDB.iterator(); ((Iterator)localObject2).hasNext();) { String k = (String)((Iterator)localObject2).next();
              
              _sessionTableSchema.fillCheckSessionExistsStatement((PreparedStatement)checkSessionExists, k, _context);
              try { ResultSet result = ((PreparedStatement)checkSessionExists).executeQuery();vh = null;
                try {
                  if (!result.next())
                  {

                    expiredSessionKeys.add(k);
                  }
                }
                catch (Throwable localThrowable25)
                {
                  vh = localThrowable25;throw localThrowable25;


                }
                finally {}


              }
              catch (Exception e)
              {

                LOG.warn("Problem checking if potentially expired session {} exists in db", new Object[] { k, e });
              }
            }
          }
          catch (Throwable localThrowable23)
          {
            k = localThrowable23;throw localThrowable23;








          }
          finally
          {








            if (checkSessionExists != null) if (k != null) try { ((PreparedStatement)checkSessionExists).close(); } catch (Throwable localThrowable17) { k.addSuppressed(localThrowable17); } else ((PreparedStatement)checkSessionExists).close();
          }
        }
        return expiredSessionKeys;
      }
      catch (Throwable localThrowable19)
      {
        localThrowable21 = localThrowable19;throw localThrowable19;












































      }
      finally
      {












































        if (connection != null) { if (localThrowable21 != null) try { connection.close(); } catch (Throwable localThrowable20) { localThrowable21.addSuppressed(localThrowable20); } else { connection.close();
          }
        }
      }
      return expiredSessionKeys;
    }
    catch (Exception e)
    {
      LOG.warn(e);
    }
  }
  


  public void setDatabaseAdaptor(DatabaseAdaptor dbAdaptor)
  {
    checkStarted();
    updateBean(_dbAdaptor, dbAdaptor);
    _dbAdaptor = dbAdaptor;
  }
  
  public void setSessionTableSchema(SessionTableSchema schema)
  {
    checkStarted();
    updateBean(_sessionTableSchema, schema);
    _sessionTableSchema = schema;
    _schemaProvided = true;
  }
  







  public boolean isPassivating()
  {
    return true;
  }
  
  /* Error */
  public boolean exists(String id)
    throws Exception
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 32	org/seleniumhq/jetty9/server/session/JDBCSessionDataStore:_dbAdaptor	Lorg/seleniumhq/jetty9/server/session/DatabaseAdaptor;
    //   4: invokevirtual 109	org/seleniumhq/jetty9/server/session/DatabaseAdaptor:getConnection	()Ljava/sql/Connection;
    //   7: astore_2
    //   8: aconst_null
    //   9: astore_3
    //   10: aload_2
    //   11: iconst_1
    //   12: invokeinterface 119 2 0
    //   17: aload_0
    //   18: getfield 51	org/seleniumhq/jetty9/server/session/JDBCSessionDataStore:_sessionTableSchema	Lorg/seleniumhq/jetty9/server/session/JDBCSessionDataStore$SessionTableSchema;
    //   21: aload_2
    //   22: aload_0
    //   23: getfield 78	org/seleniumhq/jetty9/server/session/JDBCSessionDataStore:_context	Lorg/seleniumhq/jetty9/server/session/SessionContext;
    //   26: invokevirtual 193	org/seleniumhq/jetty9/server/session/SessionContext:getCanonicalContextPath	()Ljava/lang/String;
    //   29: invokevirtual 401	org/seleniumhq/jetty9/server/session/JDBCSessionDataStore$SessionTableSchema:getCheckSessionExistsStatement	(Ljava/sql/Connection;Ljava/lang/String;)Ljava/sql/PreparedStatement;
    //   32: astore 4
    //   34: aconst_null
    //   35: astore 5
    //   37: aload_0
    //   38: getfield 51	org/seleniumhq/jetty9/server/session/JDBCSessionDataStore:_sessionTableSchema	Lorg/seleniumhq/jetty9/server/session/JDBCSessionDataStore$SessionTableSchema;
    //   41: aload 4
    //   43: aload_1
    //   44: aload_0
    //   45: getfield 78	org/seleniumhq/jetty9/server/session/JDBCSessionDataStore:_context	Lorg/seleniumhq/jetty9/server/session/SessionContext;
    //   48: invokevirtual 405	org/seleniumhq/jetty9/server/session/JDBCSessionDataStore$SessionTableSchema:fillCheckSessionExistsStatement	(Ljava/sql/PreparedStatement;Ljava/lang/String;Lorg/seleniumhq/jetty9/server/session/SessionContext;)V
    //   51: aload 4
    //   53: invokeinterface 332 1 0
    //   58: astore 6
    //   60: aconst_null
    //   61: astore 7
    //   63: aload 6
    //   65: invokeinterface 337 1 0
    //   70: ifne +121 -> 191
    //   73: iconst_0
    //   74: istore 8
    //   76: aload 6
    //   78: ifnull +37 -> 115
    //   81: aload 7
    //   83: ifnull +25 -> 108
    //   86: aload 6
    //   88: invokeinterface 357 1 0
    //   93: goto +22 -> 115
    //   96: astore 9
    //   98: aload 7
    //   100: aload 9
    //   102: invokevirtual 158	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   105: goto +10 -> 115
    //   108: aload 6
    //   110: invokeinterface 357 1 0
    //   115: aload 4
    //   117: ifnull +37 -> 154
    //   120: aload 5
    //   122: ifnull +25 -> 147
    //   125: aload 4
    //   127: invokeinterface 154 1 0
    //   132: goto +22 -> 154
    //   135: astore 9
    //   137: aload 5
    //   139: aload 9
    //   141: invokevirtual 158	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   144: goto +10 -> 154
    //   147: aload 4
    //   149: invokeinterface 154 1 0
    //   154: aload_2
    //   155: ifnull +33 -> 188
    //   158: aload_3
    //   159: ifnull +23 -> 182
    //   162: aload_2
    //   163: invokeinterface 159 1 0
    //   168: goto +20 -> 188
    //   171: astore 9
    //   173: aload_3
    //   174: aload 9
    //   176: invokevirtual 158	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   179: goto +9 -> 188
    //   182: aload_2
    //   183: invokeinterface 159 1 0
    //   188: iload 8
    //   190: ireturn
    //   191: aload 6
    //   193: aload_0
    //   194: getfield 51	org/seleniumhq/jetty9/server/session/JDBCSessionDataStore:_sessionTableSchema	Lorg/seleniumhq/jetty9/server/session/JDBCSessionDataStore$SessionTableSchema;
    //   197: invokevirtual 347	org/seleniumhq/jetty9/server/session/JDBCSessionDataStore$SessionTableSchema:getExpiryTimeColumn	()Ljava/lang/String;
    //   200: invokeinterface 351 2 0
    //   205: lstore 10
    //   207: lload 10
    //   209: lconst_0
    //   210: lcmp
    //   211: ifgt +121 -> 332
    //   214: iconst_1
    //   215: istore 12
    //   217: aload 6
    //   219: ifnull +37 -> 256
    //   222: aload 7
    //   224: ifnull +25 -> 249
    //   227: aload 6
    //   229: invokeinterface 357 1 0
    //   234: goto +22 -> 256
    //   237: astore 13
    //   239: aload 7
    //   241: aload 13
    //   243: invokevirtual 158	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   246: goto +10 -> 256
    //   249: aload 6
    //   251: invokeinterface 357 1 0
    //   256: aload 4
    //   258: ifnull +37 -> 295
    //   261: aload 5
    //   263: ifnull +25 -> 288
    //   266: aload 4
    //   268: invokeinterface 154 1 0
    //   273: goto +22 -> 295
    //   276: astore 13
    //   278: aload 5
    //   280: aload 13
    //   282: invokevirtual 158	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   285: goto +10 -> 295
    //   288: aload 4
    //   290: invokeinterface 154 1 0
    //   295: aload_2
    //   296: ifnull +33 -> 329
    //   299: aload_3
    //   300: ifnull +23 -> 323
    //   303: aload_2
    //   304: invokeinterface 159 1 0
    //   309: goto +20 -> 329
    //   312: astore 13
    //   314: aload_3
    //   315: aload 13
    //   317: invokevirtual 158	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   320: goto +9 -> 329
    //   323: aload_2
    //   324: invokeinterface 159 1 0
    //   329: iload 12
    //   331: ireturn
    //   332: lload 10
    //   334: invokestatic 306	java/lang/System:currentTimeMillis	()J
    //   337: lcmp
    //   338: ifle +7 -> 345
    //   341: iconst_1
    //   342: goto +4 -> 346
    //   345: iconst_0
    //   346: istore 12
    //   348: aload 6
    //   350: ifnull +37 -> 387
    //   353: aload 7
    //   355: ifnull +25 -> 380
    //   358: aload 6
    //   360: invokeinterface 357 1 0
    //   365: goto +22 -> 387
    //   368: astore 13
    //   370: aload 7
    //   372: aload 13
    //   374: invokevirtual 158	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   377: goto +10 -> 387
    //   380: aload 6
    //   382: invokeinterface 357 1 0
    //   387: aload 4
    //   389: ifnull +37 -> 426
    //   392: aload 5
    //   394: ifnull +25 -> 419
    //   397: aload 4
    //   399: invokeinterface 154 1 0
    //   404: goto +22 -> 426
    //   407: astore 13
    //   409: aload 5
    //   411: aload 13
    //   413: invokevirtual 158	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   416: goto +10 -> 426
    //   419: aload 4
    //   421: invokeinterface 154 1 0
    //   426: aload_2
    //   427: ifnull +33 -> 460
    //   430: aload_3
    //   431: ifnull +23 -> 454
    //   434: aload_2
    //   435: invokeinterface 159 1 0
    //   440: goto +20 -> 460
    //   443: astore 13
    //   445: aload_3
    //   446: aload 13
    //   448: invokevirtual 158	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   451: goto +9 -> 460
    //   454: aload_2
    //   455: invokeinterface 159 1 0
    //   460: iload 12
    //   462: ireturn
    //   463: astore 8
    //   465: aload 8
    //   467: astore 7
    //   469: aload 8
    //   471: athrow
    //   472: astore 14
    //   474: aload 6
    //   476: ifnull +37 -> 513
    //   479: aload 7
    //   481: ifnull +25 -> 506
    //   484: aload 6
    //   486: invokeinterface 357 1 0
    //   491: goto +22 -> 513
    //   494: astore 15
    //   496: aload 7
    //   498: aload 15
    //   500: invokevirtual 158	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   503: goto +10 -> 513
    //   506: aload 6
    //   508: invokeinterface 357 1 0
    //   513: aload 14
    //   515: athrow
    //   516: astore 6
    //   518: aload 6
    //   520: astore 5
    //   522: aload 6
    //   524: athrow
    //   525: astore 16
    //   527: aload 4
    //   529: ifnull +37 -> 566
    //   532: aload 5
    //   534: ifnull +25 -> 559
    //   537: aload 4
    //   539: invokeinterface 154 1 0
    //   544: goto +22 -> 566
    //   547: astore 17
    //   549: aload 5
    //   551: aload 17
    //   553: invokevirtual 158	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   556: goto +10 -> 566
    //   559: aload 4
    //   561: invokeinterface 154 1 0
    //   566: aload 16
    //   568: athrow
    //   569: astore 4
    //   571: aload 4
    //   573: astore_3
    //   574: aload 4
    //   576: athrow
    //   577: astore 18
    //   579: aload_2
    //   580: ifnull +33 -> 613
    //   583: aload_3
    //   584: ifnull +23 -> 607
    //   587: aload_2
    //   588: invokeinterface 159 1 0
    //   593: goto +20 -> 613
    //   596: astore 19
    //   598: aload_3
    //   599: aload 19
    //   601: invokevirtual 158	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   604: goto +9 -> 613
    //   607: aload_2
    //   608: invokeinterface 159 1 0
    //   613: aload 18
    //   615: athrow
    // Line number table:
    //   Java source line #1038	-> byte code offset #0
    //   Java source line #1040	-> byte code offset #10
    //   Java source line #1043	-> byte code offset #17
    //   Java source line #1045	-> byte code offset #37
    //   Java source line #1046	-> byte code offset #51
    //   Java source line #1048	-> byte code offset #63
    //   Java source line #1050	-> byte code offset #73
    //   Java source line #1060	-> byte code offset #76
    //   Java source line #1061	-> byte code offset #115
    //   Java source line #1062	-> byte code offset #154
    //   Java source line #1050	-> byte code offset #188
    //   Java source line #1054	-> byte code offset #191
    //   Java source line #1055	-> byte code offset #207
    //   Java source line #1056	-> byte code offset #214
    //   Java source line #1060	-> byte code offset #217
    //   Java source line #1061	-> byte code offset #256
    //   Java source line #1062	-> byte code offset #295
    //   Java source line #1056	-> byte code offset #329
    //   Java source line #1058	-> byte code offset #332
    //   Java source line #1060	-> byte code offset #348
    //   Java source line #1061	-> byte code offset #387
    //   Java source line #1062	-> byte code offset #426
    //   Java source line #1058	-> byte code offset #460
    //   Java source line #1046	-> byte code offset #463
    //   Java source line #1060	-> byte code offset #472
    //   Java source line #1043	-> byte code offset #516
    //   Java source line #1061	-> byte code offset #525
    //   Java source line #1038	-> byte code offset #569
    //   Java source line #1062	-> byte code offset #577
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	616	0	this	JDBCSessionDataStore
    //   0	616	1	id	String
    //   7	601	2	connection	Connection
    //   9	590	3	localThrowable15	Throwable
    //   32	528	4	checkSessionExists	PreparedStatement
    //   569	6	4	localThrowable13	Throwable
    //   35	515	5	localThrowable16	Throwable
    //   58	449	6	result	ResultSet
    //   516	7	6	localThrowable11	Throwable
    //   61	436	7	localThrowable17	Throwable
    //   74	115	8	bool1	boolean
    //   463	7	8	localThrowable9	Throwable
    //   463	7	8	localThrowable18	Throwable
    //   96	5	9	localThrowable	Throwable
    //   135	5	9	localThrowable1	Throwable
    //   171	4	9	localThrowable2	Throwable
    //   205	128	10	expiry	long
    //   215	246	12	bool2	boolean
    //   237	5	13	localThrowable3	Throwable
    //   276	5	13	localThrowable4	Throwable
    //   312	4	13	localThrowable5	Throwable
    //   368	5	13	localThrowable6	Throwable
    //   407	5	13	localThrowable7	Throwable
    //   443	4	13	localThrowable8	Throwable
    //   472	42	14	localObject1	Object
    //   494	5	15	localThrowable10	Throwable
    //   525	42	16	localObject2	Object
    //   547	5	17	localThrowable12	Throwable
    //   577	37	18	localObject3	Object
    //   596	4	19	localThrowable14	Throwable
    // Exception table:
    //   from	to	target	type
    //   86	93	96	java/lang/Throwable
    //   125	132	135	java/lang/Throwable
    //   162	168	171	java/lang/Throwable
    //   227	234	237	java/lang/Throwable
    //   266	273	276	java/lang/Throwable
    //   303	309	312	java/lang/Throwable
    //   358	365	368	java/lang/Throwable
    //   397	404	407	java/lang/Throwable
    //   434	440	443	java/lang/Throwable
    //   63	76	463	java/lang/Throwable
    //   191	217	463	java/lang/Throwable
    //   332	348	463	java/lang/Throwable
    //   63	76	472	finally
    //   191	217	472	finally
    //   332	348	472	finally
    //   463	474	472	finally
    //   484	491	494	java/lang/Throwable
    //   37	115	516	java/lang/Throwable
    //   191	256	516	java/lang/Throwable
    //   332	387	516	java/lang/Throwable
    //   463	516	516	java/lang/Throwable
    //   37	115	525	finally
    //   191	256	525	finally
    //   332	387	525	finally
    //   463	527	525	finally
    //   537	544	547	java/lang/Throwable
    //   10	154	569	java/lang/Throwable
    //   191	295	569	java/lang/Throwable
    //   332	426	569	java/lang/Throwable
    //   463	569	569	java/lang/Throwable
    //   10	154	577	finally
    //   191	295	577	finally
    //   332	426	577	finally
    //   463	579	577	finally
    //   587	593	596	java/lang/Throwable
  }
}
