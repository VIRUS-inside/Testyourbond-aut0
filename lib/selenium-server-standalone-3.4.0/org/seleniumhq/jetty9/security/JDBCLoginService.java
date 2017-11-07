package org.seleniumhq.jetty9.security;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import org.seleniumhq.jetty9.util.Loader;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.resource.Resource;
import org.seleniumhq.jetty9.util.security.Credential;






































public class JDBCLoginService
  extends AbstractLoginService
{
  private static final Logger LOG = Log.getLogger(JDBCLoginService.class);
  
  protected String _config;
  protected String _jdbcDriver;
  protected String _url;
  protected String _userName;
  protected String _password;
  protected String _userTableKey;
  protected String _userTablePasswordField;
  protected String _roleTableRoleField;
  protected Connection _con;
  protected String _userSql;
  protected String _roleSql;
  public JDBCLoginService()
    throws IOException
  {}
  
  public class JDBCUserPrincipal
    extends AbstractLoginService.UserPrincipal
  {
    int _userKey;
    
    public JDBCUserPrincipal(String name, Credential credential, int key)
    {
      super(credential);
      _userKey = key;
    }
    

    public int getUserKey()
    {
      return _userKey;
    }
  }
  







  public JDBCLoginService(String name)
    throws IOException
  {
    setName(name);
  }
  

  public JDBCLoginService(String name, String config)
    throws IOException
  {
    setName(name);
    setConfig(config);
  }
  

  public JDBCLoginService(String name, IdentityService identityService, String config)
    throws IOException
  {
    setName(name);
    setIdentityService(identityService);
    setConfig(config);
  }
  


  protected void doStart()
    throws Exception
  {
    Properties properties = new Properties();
    Resource resource = Resource.newResource(_config);
    InputStream in = resource.getInputStream();Throwable localThrowable3 = null;
    try {
      properties.load(in);
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;
    }
    finally {
      if (in != null) if (localThrowable3 != null) try { in.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else in.close(); }
    _jdbcDriver = properties.getProperty("jdbcdriver");
    _url = properties.getProperty("url");
    _userName = properties.getProperty("username");
    _password = properties.getProperty("password");
    String _userTable = properties.getProperty("usertable");
    _userTableKey = properties.getProperty("usertablekey");
    String _userTableUserField = properties.getProperty("usertableuserfield");
    _userTablePasswordField = properties.getProperty("usertablepasswordfield");
    String _roleTable = properties.getProperty("roletable");
    String _roleTableKey = properties.getProperty("roletablekey");
    _roleTableRoleField = properties.getProperty("roletablerolefield");
    String _userRoleTable = properties.getProperty("userroletable");
    String _userRoleTableUserKey = properties.getProperty("userroletableuserkey");
    String _userRoleTableRoleKey = properties.getProperty("userroletablerolekey");
    

    if ((_jdbcDriver == null) || (_jdbcDriver.equals("")) || (_url == null) || 
    
      (_url.equals("")) || (_userName == null) || 
      
      (_userName.equals("")) || (_password == null))
    {

      LOG.warn("UserRealm " + getName() + " has not been properly configured", new Object[0]);
    }
    
    _userSql = ("select " + _userTableKey + "," + _userTablePasswordField + " from " + _userTable + " where " + _userTableUserField + " = ?");
    _roleSql = ("select r." + _roleTableRoleField + " from " + _roleTable + " r, " + _userRoleTable + " u where u." + _userRoleTableUserKey + " = ? and r." + _roleTableKey + " = u." + _userRoleTableRoleKey);
    











    Loader.loadClass(_jdbcDriver).newInstance();
    super.doStart();
  }
  


  public String getConfig()
  {
    return _config;
  }
  






  public void setConfig(String config)
  {
    if (isRunning())
      throw new IllegalStateException("Running");
    _config = config;
  }
  




  public void connectDatabase()
  {
    try
    {
      Class.forName(_jdbcDriver);
      _con = DriverManager.getConnection(_url, _userName, _password);
    }
    catch (SQLException e)
    {
      LOG.warn("UserRealm " + getName() + " could not connect to database; will try later", e);
    }
    catch (ClassNotFoundException e)
    {
      LOG.warn("UserRealm " + getName() + " could not connect to database; will try later", e);
    }
  }
  




  public AbstractLoginService.UserPrincipal loadUserInfo(String username)
  {
    try
    {
      if (null == _con) {
        connectDatabase();
      }
      if (null == _con) {
        throw new SQLException("Can't connect to database");
      }
      PreparedStatement stat1 = _con.prepareStatement(_userSql);Throwable localThrowable8 = null;
      try {
        stat1.setObject(1, username);
        ResultSet rs1 = stat1.executeQuery();Throwable localThrowable9 = null;
        try {
          if (rs1.next())
          {
            int key = rs1.getInt(_userTableKey);
            String credentials = rs1.getString(_userTablePasswordField);
            
            return new JDBCUserPrincipal(username, Credential.getCredential(credentials), key);
          }
        }
        catch (Throwable localThrowable3)
        {
          localThrowable9 = localThrowable3;throw localThrowable3;
        }
        finally {}
      }
      catch (Throwable localThrowable6)
      {
        localThrowable8 = localThrowable6;throw localThrowable6;





      }
      finally
      {




        if (stat1 != null) if (localThrowable8 != null) try { stat1.close(); } catch (Throwable localThrowable7) { localThrowable8.addSuppressed(localThrowable7); } else stat1.close();
      }
    }
    catch (SQLException e) {
      LOG.warn("UserRealm " + getName() + " could not load user information from database", e);
      closeConnection();
    }
    
    return null;
  }
  
  /* Error */
  public String[] loadRoleInfo(AbstractLoginService.UserPrincipal user)
  {
    // Byte code:
    //   0: aload_1
    //   1: checkcast 7	org/seleniumhq/jetty9/security/JDBCLoginService$JDBCUserPrincipal
    //   4: astore_2
    //   5: aconst_null
    //   6: aload_0
    //   7: getfield 244	org/seleniumhq/jetty9/security/JDBCLoginService:_con	Ljava/sql/Connection;
    //   10: if_acmpne +7 -> 17
    //   13: aload_0
    //   14: invokevirtual 256	org/seleniumhq/jetty9/security/JDBCLoginService:connectDatabase	()V
    //   17: aconst_null
    //   18: aload_0
    //   19: getfield 244	org/seleniumhq/jetty9/security/JDBCLoginService:_con	Ljava/sql/Connection;
    //   22: if_acmpne +14 -> 36
    //   25: new 231	java/sql/SQLException
    //   28: dup
    //   29: ldc_w 258
    //   32: invokespecial 259	java/sql/SQLException:<init>	(Ljava/lang/String;)V
    //   35: athrow
    //   36: new 314	java/util/ArrayList
    //   39: dup
    //   40: invokespecial 315	java/util/ArrayList:<init>	()V
    //   43: astore_3
    //   44: aload_0
    //   45: getfield 244	org/seleniumhq/jetty9/security/JDBCLoginService:_con	Ljava/sql/Connection;
    //   48: aload_0
    //   49: getfield 190	org/seleniumhq/jetty9/security/JDBCLoginService:_roleSql	Ljava/lang/String;
    //   52: invokeinterface 265 2 0
    //   57: astore 4
    //   59: aconst_null
    //   60: astore 5
    //   62: aload 4
    //   64: iconst_1
    //   65: aload_2
    //   66: invokevirtual 319	org/seleniumhq/jetty9/security/JDBCLoginService$JDBCUserPrincipal:getUserKey	()I
    //   69: invokeinterface 323 3 0
    //   74: aload 4
    //   76: invokeinterface 275 1 0
    //   81: astore 6
    //   83: aconst_null
    //   84: astore 7
    //   86: aload 6
    //   88: invokeinterface 280 1 0
    //   93: ifeq +24 -> 117
    //   96: aload_3
    //   97: aload 6
    //   99: aload_0
    //   100: getfield 124	org/seleniumhq/jetty9/security/JDBCLoginService:_roleTableRoleField	Ljava/lang/String;
    //   103: invokeinterface 287 2 0
    //   108: invokeinterface 328 2 0
    //   113: pop
    //   114: goto -28 -> 86
    //   117: aload_3
    //   118: aload_3
    //   119: invokeinterface 331 1 0
    //   124: anewarray 134	java/lang/String
    //   127: invokeinterface 335 2 0
    //   132: checkcast 337	[Ljava/lang/String;
    //   135: astore 8
    //   137: aload 6
    //   139: ifnull +37 -> 176
    //   142: aload 7
    //   144: ifnull +25 -> 169
    //   147: aload 6
    //   149: invokeinterface 297 1 0
    //   154: goto +22 -> 176
    //   157: astore 9
    //   159: aload 7
    //   161: aload 9
    //   163: invokevirtual 84	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   166: goto +10 -> 176
    //   169: aload 6
    //   171: invokeinterface 297 1 0
    //   176: aload 4
    //   178: ifnull +37 -> 215
    //   181: aload 5
    //   183: ifnull +25 -> 208
    //   186: aload 4
    //   188: invokeinterface 298 1 0
    //   193: goto +22 -> 215
    //   196: astore 9
    //   198: aload 5
    //   200: aload 9
    //   202: invokevirtual 84	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   205: goto +10 -> 215
    //   208: aload 4
    //   210: invokeinterface 298 1 0
    //   215: aload 8
    //   217: areturn
    //   218: astore 8
    //   220: aload 8
    //   222: astore 7
    //   224: aload 8
    //   226: athrow
    //   227: astore 10
    //   229: aload 6
    //   231: ifnull +37 -> 268
    //   234: aload 7
    //   236: ifnull +25 -> 261
    //   239: aload 6
    //   241: invokeinterface 297 1 0
    //   246: goto +22 -> 268
    //   249: astore 11
    //   251: aload 7
    //   253: aload 11
    //   255: invokevirtual 84	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   258: goto +10 -> 268
    //   261: aload 6
    //   263: invokeinterface 297 1 0
    //   268: aload 10
    //   270: athrow
    //   271: astore 6
    //   273: aload 6
    //   275: astore 5
    //   277: aload 6
    //   279: athrow
    //   280: astore 12
    //   282: aload 4
    //   284: ifnull +37 -> 321
    //   287: aload 5
    //   289: ifnull +25 -> 314
    //   292: aload 4
    //   294: invokeinterface 298 1 0
    //   299: goto +22 -> 321
    //   302: astore 13
    //   304: aload 5
    //   306: aload 13
    //   308: invokevirtual 84	java/lang/Throwable:addSuppressed	(Ljava/lang/Throwable;)V
    //   311: goto +10 -> 321
    //   314: aload 4
    //   316: invokeinterface 298 1 0
    //   321: aload 12
    //   323: athrow
    //   324: astore_3
    //   325: getstatic 140	org/seleniumhq/jetty9/security/JDBCLoginService:LOG	Lorg/seleniumhq/jetty9/util/log/Logger;
    //   328: new 142	java/lang/StringBuilder
    //   331: dup
    //   332: invokespecial 143	java/lang/StringBuilder:<init>	()V
    //   335: ldc -111
    //   337: invokevirtual 149	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   340: aload_0
    //   341: invokevirtual 153	org/seleniumhq/jetty9/security/JDBCLoginService:getName	()Ljava/lang/String;
    //   344: invokevirtual 149	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   347: ldc_w 300
    //   350: invokevirtual 149	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   353: invokevirtual 158	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   356: aload_3
    //   357: invokeinterface 249 3 0
    //   362: aload_0
    //   363: invokespecial 303	org/seleniumhq/jetty9/security/JDBCLoginService:closeConnection	()V
    //   366: aconst_null
    //   367: areturn
    // Line number table:
    //   Java source line #260	-> byte code offset #0
    //   Java source line #264	-> byte code offset #5
    //   Java source line #265	-> byte code offset #13
    //   Java source line #267	-> byte code offset #17
    //   Java source line #268	-> byte code offset #25
    //   Java source line #271	-> byte code offset #36
    //   Java source line #273	-> byte code offset #44
    //   Java source line #275	-> byte code offset #62
    //   Java source line #276	-> byte code offset #74
    //   Java source line #278	-> byte code offset #86
    //   Java source line #279	-> byte code offset #96
    //   Java source line #280	-> byte code offset #117
    //   Java source line #281	-> byte code offset #137
    //   Java source line #282	-> byte code offset #176
    //   Java source line #280	-> byte code offset #215
    //   Java source line #276	-> byte code offset #218
    //   Java source line #281	-> byte code offset #227
    //   Java source line #273	-> byte code offset #271
    //   Java source line #282	-> byte code offset #280
    //   Java source line #284	-> byte code offset #324
    //   Java source line #286	-> byte code offset #325
    //   Java source line #287	-> byte code offset #362
    //   Java source line #290	-> byte code offset #366
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	368	0	this	JDBCLoginService
    //   0	368	1	user	AbstractLoginService.UserPrincipal
    //   4	62	2	jdbcUser	JDBCUserPrincipal
    //   43	76	3	roles	java.util.List<String>
    //   324	33	3	e	SQLException
    //   57	258	4	stat2	PreparedStatement
    //   60	245	5	localThrowable6	Throwable
    //   81	181	6	rs2	ResultSet
    //   271	7	6	localThrowable4	Throwable
    //   84	168	7	localThrowable7	Throwable
    //   218	7	8	localThrowable2	Throwable
    //   218	7	8	localThrowable8	Throwable
    //   157	5	9	localThrowable	Throwable
    //   196	5	9	localThrowable1	Throwable
    //   227	42	10	localObject1	Object
    //   249	5	11	localThrowable3	Throwable
    //   280	42	12	localObject2	Object
    //   302	5	13	localThrowable5	Throwable
    // Exception table:
    //   from	to	target	type
    //   147	154	157	java/lang/Throwable
    //   186	193	196	java/lang/Throwable
    //   86	137	218	java/lang/Throwable
    //   86	137	227	finally
    //   218	229	227	finally
    //   239	246	249	java/lang/Throwable
    //   62	176	271	java/lang/Throwable
    //   218	271	271	java/lang/Throwable
    //   62	176	280	finally
    //   218	282	280	finally
    //   292	299	302	java/lang/Throwable
    //   5	215	324	java/sql/SQLException
    //   218	324	324	java/sql/SQLException
  }
  
  protected void doStop()
    throws Exception
  {
    closeConnection();
    super.doStop();
  }
  




  private void closeConnection()
  {
    if (_con != null)
    {
      if (LOG.isDebugEnabled()) LOG.debug("Closing db connection for JDBCUserRealm", new Object[0]);
      try { _con.close(); } catch (Exception e) { LOG.ignore(e);
      } }
    _con = null;
  }
}
