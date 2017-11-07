package org.apache.http.impl.conn;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.TimeUnit;
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
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;
import org.apache.http.util.LangUtils;














































@Contract(threading=ThreadingBehavior.SAFE_CONDITIONAL)
public class BasicHttpClientConnectionManager
  implements HttpClientConnectionManager, Closeable
{
  private final Log log = LogFactory.getLog(getClass());
  
  private final HttpClientConnectionOperator connectionOperator;
  
  private final HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory;
  private ManagedHttpClientConnection conn;
  private HttpRoute route;
  private Object state;
  private long updated;
  private long expiry;
  private boolean leased;
  private SocketConfig socketConfig;
  private ConnectionConfig connConfig;
  private final AtomicBoolean isShutdown;
  
  private static Registry<ConnectionSocketFactory> getDefaultRegistry()
  {
    return RegistryBuilder.create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
  }
  






  public BasicHttpClientConnectionManager(Lookup<ConnectionSocketFactory> socketFactoryRegistry, HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory, SchemePortResolver schemePortResolver, DnsResolver dnsResolver)
  {
    this(new DefaultHttpClientConnectionOperator(socketFactoryRegistry, schemePortResolver, dnsResolver), connFactory);
  }
  








  public BasicHttpClientConnectionManager(HttpClientConnectionOperator httpClientConnectionOperator, HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory)
  {
    connectionOperator = ((HttpClientConnectionOperator)Args.notNull(httpClientConnectionOperator, "Connection operator"));
    this.connFactory = (connFactory != null ? connFactory : ManagedHttpClientConnectionFactory.INSTANCE);
    expiry = Long.MAX_VALUE;
    socketConfig = SocketConfig.DEFAULT;
    connConfig = ConnectionConfig.DEFAULT;
    isShutdown = new AtomicBoolean(false);
  }
  

  public BasicHttpClientConnectionManager(Lookup<ConnectionSocketFactory> socketFactoryRegistry, HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory)
  {
    this(socketFactoryRegistry, connFactory, null, null);
  }
  
  public BasicHttpClientConnectionManager(Lookup<ConnectionSocketFactory> socketFactoryRegistry)
  {
    this(socketFactoryRegistry, null, null, null);
  }
  
  public BasicHttpClientConnectionManager() {
    this(getDefaultRegistry(), null, null, null);
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
  
  HttpRoute getRoute() {
    return route;
  }
  
  Object getState() {
    return state;
  }
  
  public synchronized SocketConfig getSocketConfig() {
    return socketConfig;
  }
  
  public synchronized void setSocketConfig(SocketConfig socketConfig) {
    this.socketConfig = (socketConfig != null ? socketConfig : SocketConfig.DEFAULT);
  }
  
  public synchronized ConnectionConfig getConnectionConfig() {
    return connConfig;
  }
  
  public synchronized void setConnectionConfig(ConnectionConfig connConfig) {
    this.connConfig = (connConfig != null ? connConfig : ConnectionConfig.DEFAULT);
  }
  


  public final ConnectionRequest requestConnection(final HttpRoute route, final Object state)
  {
    Args.notNull(route, "Route");
    new ConnectionRequest()
    {

      public boolean cancel()
      {
        return false;
      }
      
      public HttpClientConnection get(long timeout, TimeUnit tunit)
      {
        return getConnection(route, state);
      }
    };
  }
  

  private void closeConnection()
  {
    if (conn != null) {
      log.debug("Closing connection");
      try {
        conn.close();
      } catch (IOException iox) {
        if (log.isDebugEnabled()) {
          log.debug("I/O exception closing connection", iox);
        }
      }
      conn = null;
    }
  }
  
  private void shutdownConnection() {
    if (conn != null) {
      log.debug("Shutting down connection");
      try {
        conn.shutdown();
      } catch (IOException iox) {
        if (log.isDebugEnabled()) {
          log.debug("I/O exception shutting down connection", iox);
        }
      }
      conn = null;
    }
  }
  
  private void checkExpiry() {
    if ((conn != null) && (System.currentTimeMillis() >= expiry)) {
      if (log.isDebugEnabled()) {
        log.debug("Connection expired @ " + new Date(expiry));
      }
      closeConnection();
    }
  }
  
  synchronized HttpClientConnection getConnection(HttpRoute route, Object state) {
    Asserts.check(!isShutdown.get(), "Connection manager has been shut down");
    if (log.isDebugEnabled()) {
      log.debug("Get connection for route " + route);
    }
    Asserts.check(!leased, "Connection is still allocated");
    if ((!LangUtils.equals(this.route, route)) || (!LangUtils.equals(this.state, state))) {
      closeConnection();
    }
    this.route = route;
    this.state = state;
    checkExpiry();
    if (conn == null) {
      conn = ((ManagedHttpClientConnection)connFactory.create(route, connConfig));
    }
    leased = true;
    return conn;
  }
  



  public synchronized void releaseConnection(HttpClientConnection conn, Object state, long keepalive, TimeUnit tunit)
  {
    Args.notNull(conn, "Connection");
    Asserts.check(conn == this.conn, "Connection not obtained from this manager");
    if (log.isDebugEnabled()) {
      log.debug("Releasing connection " + conn);
    }
    if (isShutdown.get()) {
      return;
    }
    try {
      updated = System.currentTimeMillis();
      if (!this.conn.isOpen()) {
        this.conn = null;
        route = null;
        this.conn = null;
        expiry = Long.MAX_VALUE;
      } else {
        this.state = state;
        if (log.isDebugEnabled()) { String s;
          String s;
          if (keepalive > 0L) {
            s = "for " + keepalive + " " + tunit;
          } else {
            s = "indefinitely";
          }
          log.debug("Connection can be kept alive " + s);
        }
        if (keepalive > 0L) {
          expiry = (updated + tunit.toMillis(keepalive));
        } else {
          expiry = Long.MAX_VALUE;
        }
      }
    } finally {
      leased = false;
    }
  }
  



  public void connect(HttpClientConnection conn, HttpRoute route, int connectTimeout, HttpContext context)
    throws IOException
  {
    Args.notNull(conn, "Connection");
    Args.notNull(route, "HTTP route");
    Asserts.check(conn == this.conn, "Connection not obtained from this manager");
    HttpHost host;
    HttpHost host; if (route.getProxyHost() != null) {
      host = route.getProxyHost();
    } else {
      host = route.getTargetHost();
    }
    InetSocketAddress localAddress = route.getLocalSocketAddress();
    connectionOperator.connect(this.conn, host, localAddress, connectTimeout, socketConfig, context);
  }
  



  public void upgrade(HttpClientConnection conn, HttpRoute route, HttpContext context)
    throws IOException
  {
    Args.notNull(conn, "Connection");
    Args.notNull(route, "HTTP route");
    Asserts.check(conn == this.conn, "Connection not obtained from this manager");
    connectionOperator.upgrade(this.conn, route.getTargetHost(), context);
  }
  


  public void routeComplete(HttpClientConnection conn, HttpRoute route, HttpContext context)
    throws IOException
  {}
  

  public synchronized void closeExpiredConnections()
  {
    if (isShutdown.get()) {
      return;
    }
    if (!leased) {
      checkExpiry();
    }
  }
  
  public synchronized void closeIdleConnections(long idletime, TimeUnit tunit)
  {
    Args.notNull(tunit, "Time unit");
    if (isShutdown.get()) {
      return;
    }
    if (!leased) {
      long time = tunit.toMillis(idletime);
      if (time < 0L) {
        time = 0L;
      }
      long deadline = System.currentTimeMillis() - time;
      if (updated <= deadline) {
        closeConnection();
      }
    }
  }
  
  public synchronized void shutdown()
  {
    if (isShutdown.compareAndSet(false, true)) {
      shutdownConnection();
    }
  }
}
