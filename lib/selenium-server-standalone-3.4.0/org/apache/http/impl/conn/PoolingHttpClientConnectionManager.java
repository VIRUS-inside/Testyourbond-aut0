package org.apache.http.impl.conn;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Lookup;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.ConnectionRequest;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.HttpClientConnectionOperator;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.pool.ConnFactory;
import org.apache.http.pool.ConnPoolControl;
import org.apache.http.pool.PoolEntryCallback;
import org.apache.http.pool.PoolStats;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;



























































@Contract(threading=ThreadingBehavior.SAFE_CONDITIONAL)
public class PoolingHttpClientConnectionManager
  implements HttpClientConnectionManager, ConnPoolControl<HttpRoute>, Closeable
{
  private final Log log = LogFactory.getLog(getClass());
  private final ConfigData configData;
  private final CPool pool;
  private final HttpClientConnectionOperator connectionOperator;
  private final AtomicBoolean isShutDown;
  
  private static Registry<ConnectionSocketFactory> getDefaultRegistry()
  {
    return RegistryBuilder.create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
  }
  


  public PoolingHttpClientConnectionManager()
  {
    this(getDefaultRegistry());
  }
  
  public PoolingHttpClientConnectionManager(long timeToLive, TimeUnit tunit) {
    this(getDefaultRegistry(), null, null, null, timeToLive, tunit);
  }
  
  public PoolingHttpClientConnectionManager(Registry<ConnectionSocketFactory> socketFactoryRegistry)
  {
    this(socketFactoryRegistry, null, null);
  }
  

  public PoolingHttpClientConnectionManager(Registry<ConnectionSocketFactory> socketFactoryRegistry, DnsResolver dnsResolver)
  {
    this(socketFactoryRegistry, null, dnsResolver);
  }
  

  public PoolingHttpClientConnectionManager(Registry<ConnectionSocketFactory> socketFactoryRegistry, HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory)
  {
    this(socketFactoryRegistry, connFactory, null);
  }
  
  public PoolingHttpClientConnectionManager(HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory)
  {
    this(getDefaultRegistry(), connFactory, null);
  }
  


  public PoolingHttpClientConnectionManager(Registry<ConnectionSocketFactory> socketFactoryRegistry, HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory, DnsResolver dnsResolver)
  {
    this(socketFactoryRegistry, connFactory, null, dnsResolver, -1L, TimeUnit.MILLISECONDS);
  }
  




  public PoolingHttpClientConnectionManager(Registry<ConnectionSocketFactory> socketFactoryRegistry, HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory, SchemePortResolver schemePortResolver, DnsResolver dnsResolver, long timeToLive, TimeUnit tunit)
  {
    this(new DefaultHttpClientConnectionOperator(socketFactoryRegistry, schemePortResolver, dnsResolver), connFactory, timeToLive, tunit);
  }
  










  public PoolingHttpClientConnectionManager(HttpClientConnectionOperator httpClientConnectionOperator, HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory, long timeToLive, TimeUnit tunit)
  {
    configData = new ConfigData();
    pool = new CPool(new InternalConnectionFactory(configData, connFactory), 2, 20, timeToLive, tunit);
    
    pool.setValidateAfterInactivity(2000);
    connectionOperator = ((HttpClientConnectionOperator)Args.notNull(httpClientConnectionOperator, "HttpClientConnectionOperator"));
    isShutDown = new AtomicBoolean(false);
  }
  







  PoolingHttpClientConnectionManager(CPool pool, Lookup<ConnectionSocketFactory> socketFactoryRegistry, SchemePortResolver schemePortResolver, DnsResolver dnsResolver)
  {
    configData = new ConfigData();
    this.pool = pool;
    connectionOperator = new DefaultHttpClientConnectionOperator(socketFactoryRegistry, schemePortResolver, dnsResolver);
    
    isShutDown = new AtomicBoolean(false);
  }
  
  protected void finalize() throws Throwable
  {
    try {
      shutdown();
    } finally {
      super.finalize();
    }
  }
  
  public void close()
  {
    shutdown();
  }
  
  private String format(HttpRoute route, Object state) {
    StringBuilder buf = new StringBuilder();
    buf.append("[route: ").append(route).append("]");
    if (state != null) {
      buf.append("[state: ").append(state).append("]");
    }
    return buf.toString();
  }
  
  private String formatStats(HttpRoute route) {
    StringBuilder buf = new StringBuilder();
    PoolStats totals = pool.getTotalStats();
    PoolStats stats = pool.getStats(route);
    buf.append("[total kept alive: ").append(totals.getAvailable()).append("; ");
    buf.append("route allocated: ").append(stats.getLeased() + stats.getAvailable());
    buf.append(" of ").append(stats.getMax()).append("; ");
    buf.append("total allocated: ").append(totals.getLeased() + totals.getAvailable());
    buf.append(" of ").append(totals.getMax()).append("]");
    return buf.toString();
  }
  
  private String format(CPoolEntry entry) {
    StringBuilder buf = new StringBuilder();
    buf.append("[id: ").append(entry.getId()).append("]");
    buf.append("[route: ").append(entry.getRoute()).append("]");
    Object state = entry.getState();
    if (state != null) {
      buf.append("[state: ").append(state).append("]");
    }
    return buf.toString();
  }
  


  public ConnectionRequest requestConnection(HttpRoute route, Object state)
  {
    Args.notNull(route, "HTTP route");
    if (log.isDebugEnabled()) {
      log.debug("Connection request: " + format(route, state) + formatStats(route));
    }
    final Future<CPoolEntry> future = pool.lease(route, state, null);
    new ConnectionRequest()
    {
      public boolean cancel()
      {
        return future.cancel(true);
      }
      

      public HttpClientConnection get(long timeout, TimeUnit tunit)
        throws InterruptedException, ExecutionException, ConnectionPoolTimeoutException
      {
        return leaseConnection(future, timeout, tunit);
      }
    };
  }
  



  protected HttpClientConnection leaseConnection(Future<CPoolEntry> future, long timeout, TimeUnit tunit)
    throws InterruptedException, ExecutionException, ConnectionPoolTimeoutException
  {
    try
    {
      CPoolEntry entry = (CPoolEntry)future.get(timeout, tunit);
      if ((entry == null) || (future.isCancelled())) {
        throw new InterruptedException();
      }
      Asserts.check(entry.getConnection() != null, "Pool entry with no connection");
      if (log.isDebugEnabled()) {
        log.debug("Connection leased: " + format(entry) + formatStats((HttpRoute)entry.getRoute()));
      }
      return CPoolProxy.newProxy(entry);
    } catch (TimeoutException ex) {
      throw new ConnectionPoolTimeoutException("Timeout waiting for connection from pool");
    }
  }
  



  public void releaseConnection(HttpClientConnection managedConn, Object state, long keepalive, TimeUnit tunit)
  {
    Args.notNull(managedConn, "Managed connection");
    synchronized (managedConn) {
      CPoolEntry entry = CPoolProxy.detach(managedConn);
      if (entry == null) {
        return;
      }
      ManagedHttpClientConnection conn = (ManagedHttpClientConnection)entry.getConnection();
      try {
        if (conn.isOpen()) {
          TimeUnit effectiveUnit = tunit != null ? tunit : TimeUnit.MILLISECONDS;
          entry.setState(state);
          entry.updateExpiry(keepalive, effectiveUnit);
          if (log.isDebugEnabled()) { String s;
            String s;
            if (keepalive > 0L) {
              s = "for " + effectiveUnit.toMillis(keepalive) / 1000.0D + " seconds";
            } else {
              s = "indefinitely";
            }
            log.debug("Connection " + format(entry) + " can be kept alive " + s);
          }
        }
      } finally {
        pool.release(entry, (conn.isOpen()) && (entry.isRouteComplete()));
        if (log.isDebugEnabled()) {
          log.debug("Connection released: " + format(entry) + formatStats((HttpRoute)entry.getRoute()));
        }
      }
    }
  }
  



  public void connect(HttpClientConnection managedConn, HttpRoute route, int connectTimeout, HttpContext context)
    throws IOException
  {
    Args.notNull(managedConn, "Managed Connection");
    Args.notNull(route, "HTTP route");
    ManagedHttpClientConnection conn;
    synchronized (managedConn) {
      CPoolEntry entry = CPoolProxy.getPoolEntry(managedConn);
      conn = (ManagedHttpClientConnection)entry.getConnection(); }
    HttpHost host;
    HttpHost host;
    if (route.getProxyHost() != null) {
      host = route.getProxyHost();
    } else {
      host = route.getTargetHost();
    }
    InetSocketAddress localAddress = route.getLocalSocketAddress();
    SocketConfig socketConfig = configData.getSocketConfig(host);
    if (socketConfig == null) {
      socketConfig = configData.getDefaultSocketConfig();
    }
    if (socketConfig == null) {
      socketConfig = SocketConfig.DEFAULT;
    }
    connectionOperator.connect(conn, host, localAddress, connectTimeout, socketConfig, context);
  }
  



  public void upgrade(HttpClientConnection managedConn, HttpRoute route, HttpContext context)
    throws IOException
  {
    Args.notNull(managedConn, "Managed Connection");
    Args.notNull(route, "HTTP route");
    ManagedHttpClientConnection conn;
    synchronized (managedConn) {
      CPoolEntry entry = CPoolProxy.getPoolEntry(managedConn);
      conn = (ManagedHttpClientConnection)entry.getConnection();
    }
    connectionOperator.upgrade(conn, route.getTargetHost(), context);
  }
  


  public void routeComplete(HttpClientConnection managedConn, HttpRoute route, HttpContext context)
    throws IOException
  {
    Args.notNull(managedConn, "Managed Connection");
    Args.notNull(route, "HTTP route");
    synchronized (managedConn) {
      CPoolEntry entry = CPoolProxy.getPoolEntry(managedConn);
      entry.markRouteComplete();
    }
  }
  
  public void shutdown()
  {
    if (isShutDown.compareAndSet(false, true)) {
      log.debug("Connection manager is shutting down");
      try {
        pool.shutdown();
      } catch (IOException ex) {
        log.debug("I/O exception shutting down connection manager", ex);
      }
      log.debug("Connection manager shut down");
    }
  }
  
  public void closeIdleConnections(long idleTimeout, TimeUnit tunit)
  {
    if (log.isDebugEnabled()) {
      log.debug("Closing connections idle longer than " + idleTimeout + " " + tunit);
    }
    pool.closeIdle(idleTimeout, tunit);
  }
  
  public void closeExpiredConnections()
  {
    log.debug("Closing expired connections");
    pool.closeExpired();
  }
  
  protected void enumAvailable(PoolEntryCallback<HttpRoute, ManagedHttpClientConnection> callback) {
    pool.enumAvailable(callback);
  }
  
  protected void enumLeased(PoolEntryCallback<HttpRoute, ManagedHttpClientConnection> callback) {
    pool.enumLeased(callback);
  }
  
  public int getMaxTotal()
  {
    return pool.getMaxTotal();
  }
  
  public void setMaxTotal(int max)
  {
    pool.setMaxTotal(max);
  }
  
  public int getDefaultMaxPerRoute()
  {
    return pool.getDefaultMaxPerRoute();
  }
  
  public void setDefaultMaxPerRoute(int max)
  {
    pool.setDefaultMaxPerRoute(max);
  }
  
  public int getMaxPerRoute(HttpRoute route)
  {
    return pool.getMaxPerRoute(route);
  }
  
  public void setMaxPerRoute(HttpRoute route, int max)
  {
    pool.setMaxPerRoute(route, max);
  }
  
  public PoolStats getTotalStats()
  {
    return pool.getTotalStats();
  }
  
  public PoolStats getStats(HttpRoute route)
  {
    return pool.getStats(route);
  }
  


  public Set<HttpRoute> getRoutes()
  {
    return pool.getRoutes();
  }
  
  public SocketConfig getDefaultSocketConfig() {
    return configData.getDefaultSocketConfig();
  }
  
  public void setDefaultSocketConfig(SocketConfig defaultSocketConfig) {
    configData.setDefaultSocketConfig(defaultSocketConfig);
  }
  
  public ConnectionConfig getDefaultConnectionConfig() {
    return configData.getDefaultConnectionConfig();
  }
  
  public void setDefaultConnectionConfig(ConnectionConfig defaultConnectionConfig) {
    configData.setDefaultConnectionConfig(defaultConnectionConfig);
  }
  
  public SocketConfig getSocketConfig(HttpHost host) {
    return configData.getSocketConfig(host);
  }
  
  public void setSocketConfig(HttpHost host, SocketConfig socketConfig) {
    configData.setSocketConfig(host, socketConfig);
  }
  
  public ConnectionConfig getConnectionConfig(HttpHost host) {
    return configData.getConnectionConfig(host);
  }
  
  public void setConnectionConfig(HttpHost host, ConnectionConfig connectionConfig) {
    configData.setConnectionConfig(host, connectionConfig);
  }
  




  public int getValidateAfterInactivity()
  {
    return pool.getValidateAfterInactivity();
  }
  










  public void setValidateAfterInactivity(int ms)
  {
    pool.setValidateAfterInactivity(ms);
  }
  
  static class ConfigData
  {
    private final Map<HttpHost, SocketConfig> socketConfigMap;
    private final Map<HttpHost, ConnectionConfig> connectionConfigMap;
    private volatile SocketConfig defaultSocketConfig;
    private volatile ConnectionConfig defaultConnectionConfig;
    
    ConfigData()
    {
      socketConfigMap = new ConcurrentHashMap();
      connectionConfigMap = new ConcurrentHashMap();
    }
    
    public SocketConfig getDefaultSocketConfig() {
      return defaultSocketConfig;
    }
    
    public void setDefaultSocketConfig(SocketConfig defaultSocketConfig) {
      this.defaultSocketConfig = defaultSocketConfig;
    }
    
    public ConnectionConfig getDefaultConnectionConfig() {
      return defaultConnectionConfig;
    }
    
    public void setDefaultConnectionConfig(ConnectionConfig defaultConnectionConfig) {
      this.defaultConnectionConfig = defaultConnectionConfig;
    }
    
    public SocketConfig getSocketConfig(HttpHost host) {
      return (SocketConfig)socketConfigMap.get(host);
    }
    
    public void setSocketConfig(HttpHost host, SocketConfig socketConfig) {
      socketConfigMap.put(host, socketConfig);
    }
    
    public ConnectionConfig getConnectionConfig(HttpHost host) {
      return (ConnectionConfig)connectionConfigMap.get(host);
    }
    
    public void setConnectionConfig(HttpHost host, ConnectionConfig connectionConfig) {
      connectionConfigMap.put(host, connectionConfig);
    }
  }
  

  static class InternalConnectionFactory
    implements ConnFactory<HttpRoute, ManagedHttpClientConnection>
  {
    private final PoolingHttpClientConnectionManager.ConfigData configData;
    
    private final HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory;
    
    InternalConnectionFactory(PoolingHttpClientConnectionManager.ConfigData configData, HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory)
    {
      this.configData = (configData != null ? configData : new PoolingHttpClientConnectionManager.ConfigData());
      this.connFactory = (connFactory != null ? connFactory : ManagedHttpClientConnectionFactory.INSTANCE);
    }
    
    public ManagedHttpClientConnection create(HttpRoute route)
      throws IOException
    {
      ConnectionConfig config = null;
      if (route.getProxyHost() != null) {
        config = configData.getConnectionConfig(route.getProxyHost());
      }
      if (config == null) {
        config = configData.getConnectionConfig(route.getTargetHost());
      }
      if (config == null) {
        config = configData.getDefaultConnectionConfig();
      }
      if (config == null) {
        config = ConnectionConfig.DEFAULT;
      }
      return (ManagedHttpClientConnection)connFactory.create(route, config);
    }
  }
}
