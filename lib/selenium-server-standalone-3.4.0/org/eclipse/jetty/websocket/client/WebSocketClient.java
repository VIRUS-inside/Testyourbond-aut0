package org.eclipse.jetty.websocket.client;

import java.io.IOException;
import java.net.CookieStore;
import java.net.SocketAddress;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.io.ByteBufferPool;
import org.eclipse.jetty.io.MappedByteBufferPool;
import org.eclipse.jetty.util.DecoratedObjectFactory;
import org.eclipse.jetty.util.StringUtil;
import org.eclipse.jetty.util.component.ContainerLifeCycle;
import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.Scheduler;
import org.eclipse.jetty.util.thread.ShutdownThread;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.api.extensions.ExtensionConfig;
import org.eclipse.jetty.websocket.api.extensions.ExtensionFactory;
import org.eclipse.jetty.websocket.client.io.ConnectionManager;
import org.eclipse.jetty.websocket.client.io.UpgradeListener;
import org.eclipse.jetty.websocket.client.masks.Masker;
import org.eclipse.jetty.websocket.client.masks.RandomMasker;
import org.eclipse.jetty.websocket.common.SessionFactory;
import org.eclipse.jetty.websocket.common.WebSocketSession;
import org.eclipse.jetty.websocket.common.WebSocketSessionFactory;
import org.eclipse.jetty.websocket.common.events.EventDriverFactory;
import org.eclipse.jetty.websocket.common.extensions.WebSocketExtensionFactory;
import org.eclipse.jetty.websocket.common.scopes.SimpleContainerScope;
import org.eclipse.jetty.websocket.common.scopes.WebSocketContainerScope;



















public class WebSocketClient
  extends ContainerLifeCycle
  implements WebSocketContainerScope
{
  private static final Logger LOG = Log.getLogger(WebSocketClient.class);
  
  private final HttpClient httpClient;
  
  private final WebSocketContainerScope containerScope;
  
  private final WebSocketExtensionFactory extensionRegistry;
  
  private final EventDriverFactory eventDriverFactory;
  
  private final SessionFactory sessionFactory;
  private Masker masker;
  private final int id = ThreadLocalRandom.current().nextInt();
  




  public WebSocketClient()
  {
    this(new HttpClient());
    addBean(httpClient);
  }
  






  public WebSocketClient(HttpClient httpClient)
  {
    this(httpClient, new DecoratedObjectFactory());
  }
  








  public WebSocketClient(HttpClient httpClient, DecoratedObjectFactory objectFactory)
  {
    containerScope = new SimpleContainerScope(WebSocketPolicy.newClientPolicy(), new MappedByteBufferPool(), objectFactory);
    this.httpClient = httpClient;
    extensionRegistry = new WebSocketExtensionFactory(containerScope);
    masker = new RandomMasker();
    eventDriverFactory = new EventDriverFactory(containerScope);
    sessionFactory = new WebSocketSessionFactory(containerScope);
  }
  







  @Deprecated
  public WebSocketClient(Executor executor)
  {
    this(null, executor);
  }
  







  @Deprecated
  public WebSocketClient(ByteBufferPool bufferPool)
  {
    this(null, null, bufferPool);
  }
  






  public WebSocketClient(SslContextFactory sslContextFactory)
  {
    this(sslContextFactory, null);
  }
  









  @Deprecated
  public WebSocketClient(SslContextFactory sslContextFactory, Executor executor)
  {
    this(sslContextFactory, executor, new MappedByteBufferPool());
  }
  







  public WebSocketClient(WebSocketContainerScope scope)
  {
    this(scope.getSslContextFactory(), scope.getExecutor(), scope.getBufferPool(), scope.getObjectFactory());
  }
  










  public WebSocketClient(WebSocketContainerScope scope, SslContextFactory sslContextFactory)
  {
    this(sslContextFactory, scope.getExecutor(), scope.getBufferPool(), scope.getObjectFactory());
  }
  











  public WebSocketClient(SslContextFactory sslContextFactory, Executor executor, ByteBufferPool bufferPool)
  {
    this(sslContextFactory, executor, bufferPool, new DecoratedObjectFactory());
  }
  













  private WebSocketClient(SslContextFactory sslContextFactory, Executor executor, ByteBufferPool bufferPool, DecoratedObjectFactory objectFactory)
  {
    httpClient = new HttpClient(sslContextFactory);
    httpClient.setExecutor(executor);
    httpClient.setByteBufferPool(bufferPool);
    addBean(httpClient);
    
    containerScope = new SimpleContainerScope(WebSocketPolicy.newClientPolicy(), bufferPool, objectFactory);
    
    extensionRegistry = new WebSocketExtensionFactory(containerScope);
    
    masker = new RandomMasker();
    eventDriverFactory = new EventDriverFactory(containerScope);
    sessionFactory = new WebSocketSessionFactory(containerScope);
  }
  











  public WebSocketClient(WebSocketContainerScope scope, EventDriverFactory eventDriverFactory, SessionFactory sessionFactory)
  {
    containerScope = scope;
    SslContextFactory sslContextFactory = scope.getSslContextFactory();
    if (sslContextFactory == null)
    {
      sslContextFactory = new SslContextFactory();
    }
    httpClient = new HttpClient(sslContextFactory);
    httpClient.setExecutor(scope.getExecutor());
    addBean(httpClient);
    
    extensionRegistry = new WebSocketExtensionFactory(containerScope);
    
    masker = new RandomMasker();
    this.eventDriverFactory = eventDriverFactory;
    this.sessionFactory = sessionFactory;
  }
  
  public Future<Session> connect(Object websocket, URI toUri) throws IOException
  {
    ClientUpgradeRequest request = new ClientUpgradeRequest(toUri);
    request.setRequestURI(toUri);
    request.setLocalEndpoint(websocket);
    
    return connect(websocket, toUri, request);
  }
  












  public Future<Session> connect(Object websocket, URI toUri, ClientUpgradeRequest request)
    throws IOException
  {
    return connect(websocket, toUri, request, (UpgradeListener)null);
  }
  

















  public Future<Session> connect(Object websocket, URI toUri, ClientUpgradeRequest request, UpgradeListener upgradeListener)
    throws IOException
  {
    if (!isStarted())
    {
      throw new IllegalStateException(WebSocketClient.class.getSimpleName() + "@" + hashCode() + " is not started");
    }
    

    if (!toUri.isAbsolute())
    {
      throw new IllegalArgumentException("WebSocket URI must be absolute");
    }
    
    if (StringUtil.isBlank(toUri.getScheme()))
    {
      throw new IllegalArgumentException("WebSocket URI must include a scheme");
    }
    
    String scheme = toUri.getScheme().toLowerCase(Locale.ENGLISH);
    if ((!"ws".equals(scheme)) && (!"wss".equals(scheme)))
    {
      throw new IllegalArgumentException("WebSocket URI scheme only supports [ws] and [wss], not [" + scheme + "]");
    }
    
    request.setRequestURI(toUri);
    request.setLocalEndpoint(websocket);
    

    for (ExtensionConfig reqExt : request.getExtensions())
    {
      if (!extensionRegistry.isAvailable(reqExt.getName()))
      {
        throw new IllegalArgumentException("Requested extension [" + reqExt.getName() + "] is not installed");
      }
    }
    
    if (LOG.isDebugEnabled()) {
      LOG.debug("connect websocket {} to {}", new Object[] { websocket, toUri });
    }
    init();
    
    WebSocketUpgradeRequest wsReq = new WebSocketUpgradeRequest(this, httpClient, request);
    
    wsReq.setUpgradeListener(upgradeListener);
    return wsReq.sendAsync();
  }
  
  protected void doStop()
    throws Exception
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("Stopping {}", new Object[] { this });
    }
    
    if (ShutdownThread.isRegistered(this))
    {
      ShutdownThread.deregister(this);
    }
    
    super.doStop();
    
    if (LOG.isDebugEnabled()) {
      LOG.debug("Stopped {}", new Object[] { this });
    }
  }
  
  @Deprecated
  public boolean isDispatchIO() {
    return httpClient.isDispatchIO();
  }
  





  public long getAsyncWriteTimeout()
  {
    return containerScope.getPolicy().getAsyncWriteTimeout();
  }
  
  public SocketAddress getBindAddress()
  {
    return httpClient.getBindAddress();
  }
  
  public ByteBufferPool getBufferPool()
  {
    return httpClient.getByteBufferPool();
  }
  
  @Deprecated
  public ConnectionManager getConnectionManager()
  {
    throw new UnsupportedOperationException("ConnectionManager is no longer supported");
  }
  
  public long getConnectTimeout()
  {
    return httpClient.getConnectTimeout();
  }
  
  public CookieStore getCookieStore()
  {
    return httpClient.getCookieStore();
  }
  
  public EventDriverFactory getEventDriverFactory()
  {
    return eventDriverFactory;
  }
  
  public Executor getExecutor()
  {
    return httpClient.getExecutor();
  }
  
  public ExtensionFactory getExtensionFactory()
  {
    return extensionRegistry;
  }
  
  public Masker getMasker()
  {
    return masker;
  }
  





  public int getMaxBinaryMessageBufferSize()
  {
    return getPolicy().getMaxBinaryMessageBufferSize();
  }
  





  public long getMaxBinaryMessageSize()
  {
    return getPolicy().getMaxBinaryMessageSize();
  }
  





  public long getMaxIdleTimeout()
  {
    return getPolicy().getIdleTimeout();
  }
  





  public int getMaxTextMessageBufferSize()
  {
    return getPolicy().getMaxTextMessageBufferSize();
  }
  





  public long getMaxTextMessageSize()
  {
    return getPolicy().getMaxTextMessageSize();
  }
  
  public DecoratedObjectFactory getObjectFactory()
  {
    return containerScope.getObjectFactory();
  }
  
  public Set<WebSocketSession> getOpenSessions()
  {
    return Collections.unmodifiableSet(new HashSet(getBeans(WebSocketSession.class)));
  }
  
  public WebSocketPolicy getPolicy()
  {
    return containerScope.getPolicy();
  }
  
  public Scheduler getScheduler()
  {
    return httpClient.getScheduler();
  }
  
  public SessionFactory getSessionFactory()
  {
    return sessionFactory;
  }
  




  public SslContextFactory getSslContextFactory()
  {
    return httpClient.getSslContextFactory();
  }
  
  private synchronized void init() throws IOException
  {
    if (!ShutdownThread.isRegistered(this))
    {
      ShutdownThread.register(new LifeCycle[] { this });
    }
  }
  






  @Deprecated
  protected ConnectionManager newConnectionManager()
  {
    throw new UnsupportedOperationException("ConnectionManager is no longer supported");
  }
  

  public void onSessionClosed(WebSocketSession session)
  {
    if (LOG.isDebugEnabled())
      LOG.debug("Session Closed: {}", new Object[] { session });
    removeBean(session);
  }
  

  public void onSessionOpened(WebSocketSession session)
  {
    if (LOG.isDebugEnabled())
      LOG.debug("Session Opened: {}", new Object[] { session });
    addManaged(session);
    LOG.debug("post-onSessionOpened() - {}", new Object[] { this });
  }
  
  public void setAsyncWriteTimeout(long ms)
  {
    getPolicy().setAsyncWriteTimeout(ms);
  }
  





  @Deprecated
  public void setBindAdddress(SocketAddress bindAddress)
  {
    setBindAddress(bindAddress);
  }
  
  public void setBindAddress(SocketAddress bindAddress)
  {
    httpClient.setBindAddress(bindAddress);
  }
  
  public void setBufferPool(ByteBufferPool bufferPool)
  {
    httpClient.setByteBufferPool(bufferPool);
  }
  






  public void setConnectTimeout(long ms)
  {
    httpClient.setConnectTimeout(ms);
  }
  
  public void setCookieStore(CookieStore cookieStore)
  {
    httpClient.setCookieStore(cookieStore);
  }
  


  public void setDaemon(boolean daemon) {}
  

  @Deprecated
  public void setDispatchIO(boolean dispatchIO)
  {
    httpClient.setDispatchIO(dispatchIO);
  }
  
  public void setExecutor(Executor executor)
  {
    httpClient.setExecutor(executor);
  }
  
  public void setMasker(Masker masker)
  {
    this.masker = masker;
  }
  
  public void setMaxBinaryMessageBufferSize(int max)
  {
    getPolicy().setMaxBinaryMessageBufferSize(max);
  }
  








  public void setMaxIdleTimeout(long ms)
  {
    getPolicy().setIdleTimeout(ms);
    httpClient.setIdleTimeout(ms);
  }
  
  public void setMaxTextMessageBufferSize(int max)
  {
    getPolicy().setMaxTextMessageBufferSize(max);
  }
  
  public void dump(Appendable out, String indent)
    throws IOException
  {
    dumpThis(out);
    dump(out, indent, new Collection[] { getOpenSessions() });
  }
  
  public HttpClient getHttpClient()
  {
    return httpClient;
  }
  

  public String toString()
  {
    StringBuilder sb = new StringBuilder("WebSocketClient@");
    sb.append(Integer.toHexString(id));
    sb.append("[httpClient=").append(httpClient);
    sb.append(",openSessions.size=");
    sb.append(getOpenSessions().size());
    sb.append(']');
    return sb.toString();
  }
}
