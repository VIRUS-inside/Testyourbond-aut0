package org.eclipse.jetty.websocket.common;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.eclipse.jetty.io.ByteBufferPool;
import org.eclipse.jetty.io.Connection;
import org.eclipse.jetty.io.Connection.Listener;
import org.eclipse.jetty.util.annotation.ManagedAttribute;
import org.eclipse.jetty.util.annotation.ManagedObject;
import org.eclipse.jetty.util.component.ContainerLifeCycle;
import org.eclipse.jetty.util.component.Dumpable;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.thread.ThreadClassLoaderScope;
import org.eclipse.jetty.websocket.api.BatchMode;
import org.eclipse.jetty.websocket.api.CloseException;
import org.eclipse.jetty.websocket.api.CloseStatus;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.SuspendToken;
import org.eclipse.jetty.websocket.api.UpgradeRequest;
import org.eclipse.jetty.websocket.api.UpgradeResponse;
import org.eclipse.jetty.websocket.api.WebSocketBehavior;
import org.eclipse.jetty.websocket.api.WebSocketException;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.api.extensions.ExtensionFactory;
import org.eclipse.jetty.websocket.api.extensions.Frame;
import org.eclipse.jetty.websocket.api.extensions.IncomingFrames;
import org.eclipse.jetty.websocket.api.extensions.OutgoingFrames;
import org.eclipse.jetty.websocket.common.events.EventDriver;
import org.eclipse.jetty.websocket.common.io.IOState;
import org.eclipse.jetty.websocket.common.io.IOState.ConnectionStateListener;
import org.eclipse.jetty.websocket.common.scopes.WebSocketContainerScope;
import org.eclipse.jetty.websocket.common.scopes.WebSocketSessionScope;

















@ManagedObject("A Jetty WebSocket Session")
public class WebSocketSession
  extends ContainerLifeCycle
  implements Session, RemoteEndpointFactory, WebSocketSessionScope, IncomingFrames, Connection.Listener, IOState.ConnectionStateListener
{
  private static final Logger LOG = Log.getLogger(WebSocketSession.class);
  private static final Logger LOG_OPEN = Log.getLogger(WebSocketSession.class.getName() + "_OPEN");
  private final WebSocketContainerScope containerScope;
  private final URI requestURI;
  private final LogicalConnection connection;
  private final EventDriver websocket;
  private final Executor executor;
  private final WebSocketPolicy policy;
  private ClassLoader classLoader;
  private ExtensionFactory extensionFactory;
  private RemoteEndpointFactory remoteEndpointFactory;
  private String protocolVersion;
  private Map<String, String[]> parameterMap = new HashMap();
  private RemoteEndpoint remote;
  private IncomingFrames incomingHandler;
  private OutgoingFrames outgoingHandler;
  private UpgradeRequest upgradeRequest;
  private UpgradeResponse upgradeResponse;
  private CompletableFuture<Session> openFuture;
  
  public WebSocketSession(WebSocketContainerScope containerScope, URI requestURI, EventDriver websocket, LogicalConnection connection)
  {
    Objects.requireNonNull(containerScope, "Container Scope cannot be null");
    Objects.requireNonNull(requestURI, "Request URI cannot be null");
    
    classLoader = Thread.currentThread().getContextClassLoader();
    this.containerScope = containerScope;
    this.requestURI = requestURI;
    this.websocket = websocket;
    this.connection = connection;
    executor = connection.getExecutor();
    outgoingHandler = connection;
    incomingHandler = websocket;
    this.connection.getIOState().addListener(this);
    policy = websocket.getPolicy();
    
    addBean(this.connection);
    addBean(this.websocket);
  }
  


  public void close()
  {
    close(1000, null);
  }
  

  public void close(CloseStatus closeStatus)
  {
    close(closeStatus.getCode(), closeStatus.getPhrase());
  }
  

  public void close(int statusCode, String reason)
  {
    connection.close(statusCode, reason);
  }
  




  public void disconnect()
  {
    connection.disconnect();
    

    notifyClose(1006, "Harsh disconnect");
  }
  
  public void dispatch(Runnable runnable)
  {
    executor.execute(runnable);
  }
  
  protected void doStart()
    throws Exception
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("starting - {}", new Object[] { this });
    }
    Iterator<RemoteEndpointFactory> iter = ServiceLoader.load(RemoteEndpointFactory.class).iterator();
    if (iter.hasNext()) {
      remoteEndpointFactory = ((RemoteEndpointFactory)iter.next());
    }
    if (remoteEndpointFactory == null) {
      remoteEndpointFactory = this;
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("Using RemoteEndpointFactory: {}", new Object[] { remoteEndpointFactory });
    }
    super.doStart();
  }
  
  protected void doStop()
    throws Exception
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("stopping - {}", new Object[] { this });
    }
    try {
      close(1001, "Shutdown");
    }
    catch (Throwable t)
    {
      LOG.debug("During Connection Shutdown", t);
    }
    super.doStop();
  }
  
  public void dump(Appendable out, String indent)
    throws IOException
  {
    dumpThis(out);
    out.append(indent).append(" +- incomingHandler : ");
    if ((incomingHandler instanceof Dumpable))
    {
      ((Dumpable)incomingHandler).dump(out, indent + "    ");
    }
    else
    {
      out.append(incomingHandler.toString()).append(System.lineSeparator());
    }
    
    out.append(indent).append(" +- outgoingHandler : ");
    if ((outgoingHandler instanceof Dumpable))
    {
      ((Dumpable)outgoingHandler).dump(out, indent + "    ");
    }
    else
    {
      out.append(outgoingHandler.toString()).append(System.lineSeparator());
    }
  }
  

  public boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    if (obj == null)
    {
      return false;
    }
    if (getClass() != obj.getClass())
    {
      return false;
    }
    WebSocketSession other = (WebSocketSession)obj;
    if (connection == null)
    {
      if (connection != null)
      {
        return false;
      }
    }
    else if (!connection.equals(connection))
    {
      return false;
    }
    return true;
  }
  
  public ByteBufferPool getBufferPool()
  {
    return connection.getBufferPool();
  }
  
  public ClassLoader getClassLoader()
  {
    return getClass().getClassLoader();
  }
  
  public LogicalConnection getConnection()
  {
    return connection;
  }
  

  public WebSocketContainerScope getContainerScope()
  {
    return containerScope;
  }
  
  public ExtensionFactory getExtensionFactory()
  {
    return extensionFactory;
  }
  




  public long getIdleTimeout()
  {
    return connection.getMaxIdleTimeout();
  }
  
  @ManagedAttribute(readonly=true)
  public IncomingFrames getIncomingHandler()
  {
    return incomingHandler;
  }
  

  public InetSocketAddress getLocalAddress()
  {
    return connection.getLocalAddress();
  }
  
  @ManagedAttribute(readonly=true)
  public OutgoingFrames getOutgoingHandler()
  {
    return outgoingHandler;
  }
  

  public WebSocketPolicy getPolicy()
  {
    return policy;
  }
  

  public String getProtocolVersion()
  {
    return protocolVersion;
  }
  

  public RemoteEndpoint getRemote()
  {
    if (LOG_OPEN.isDebugEnabled())
      LOG_OPEN.debug("[{}] {}.getRemote()", new Object[] { policy.getBehavior(), getClass().getSimpleName() });
    ConnectionState state = connection.getIOState().getConnectionState();
    
    if ((state == ConnectionState.OPEN) || (state == ConnectionState.CONNECTED))
    {
      return remote;
    }
    
    throw new WebSocketException("RemoteEndpoint unavailable, current state [" + state + "], expecting [OPEN or CONNECTED]");
  }
  

  public InetSocketAddress getRemoteAddress()
  {
    return remote.getInetSocketAddress();
  }
  
  public URI getRequestURI()
  {
    return requestURI;
  }
  

  public UpgradeRequest getUpgradeRequest()
  {
    return upgradeRequest;
  }
  

  public UpgradeResponse getUpgradeResponse()
  {
    return upgradeResponse;
  }
  


  public WebSocketSession getWebSocketSession()
  {
    return this;
  }
  

  public int hashCode()
  {
    int prime = 31;
    int result = 1;
    result = 31 * result + (connection == null ? 0 : connection.hashCode());
    return result;
  }
  





  public void incomingError(Throwable t)
  {
    websocket.incomingError(t);
  }
  




  public void incomingFrame(Frame frame)
  {
    ClassLoader old = Thread.currentThread().getContextClassLoader();
    try
    {
      Thread.currentThread().setContextClassLoader(classLoader);
      if (connection.getIOState().isInputAvailable())
      {

        incomingHandler.incomingFrame(frame);
      }
      


      Thread.currentThread().setContextClassLoader(old); } finally { Thread.currentThread().setContextClassLoader(old);
    }
  }
  

  public boolean isOpen()
  {
    if (connection == null)
    {
      return false;
    }
    return connection.isOpen();
  }
  

  public boolean isSecure()
  {
    if (upgradeRequest == null)
    {
      throw new IllegalStateException("No valid UpgradeRequest yet");
    }
    
    URI requestURI = upgradeRequest.getRequestURI();
    
    return "wss".equalsIgnoreCase(requestURI.getScheme());
  }
  
  public void notifyClose(int statusCode, String reason)
  {
    if (LOG.isDebugEnabled())
    {
      LOG.debug("notifyClose({},{})", new Object[] { Integer.valueOf(statusCode), reason });
    }
    websocket.onClose(new CloseInfo(statusCode, reason));
  }
  
  public void notifyError(Throwable cause)
  {
    if ((openFuture != null) && (!openFuture.isDone()))
      openFuture.completeExceptionally(cause);
    incomingError(cause);
  }
  


  public void onClosed(Connection connection) {}
  


  public void onOpened(Connection connection)
  {
    if (LOG_OPEN.isDebugEnabled())
      LOG_OPEN.debug("[{}] {}.onOpened()", new Object[] { policy.getBehavior(), getClass().getSimpleName() });
    open();
  }
  


  public void onConnectionStateChange(ConnectionState state)
  {
    switch (1.$SwitchMap$org$eclipse$jetty$websocket$common$ConnectionState[state.ordinal()])
    {
    case 1: 
      IOState ioState = connection.getIOState();
      CloseInfo close = ioState.getCloseInfo();
      
      notifyClose(close.getStatusCode(), close.getReason());
      try
      {
        if (LOG.isDebugEnabled())
          LOG.debug("{}.onSessionClosed()", new Object[] { containerScope.getClass().getSimpleName() });
        containerScope.onSessionClosed(this);
      }
      catch (Throwable t)
      {
        LOG.ignore(t);
      }
    

    case 2: 
      try
      {
        if (LOG.isDebugEnabled())
          LOG.debug("{}.onSessionOpened()", new Object[] { containerScope.getClass().getSimpleName() });
        containerScope.onSessionOpened(this);
      }
      catch (Throwable t)
      {
        LOG.ignore(t);
      }
    }
    
  }
  
  public WebSocketRemoteEndpoint newRemoteEndpoint(LogicalConnection connection, OutgoingFrames outgoingFrames, BatchMode batchMode)
  {
    return new WebSocketRemoteEndpoint(connection, outgoingHandler, getBatchMode());
  }
  



  public void open()
  {
    if (LOG_OPEN.isDebugEnabled()) {
      LOG_OPEN.debug("[{}] {}.open()", new Object[] { policy.getBehavior(), getClass().getSimpleName() });
    }
    if (remote != null)
    {

      return;
    }
    try {
      ThreadClassLoaderScope scope = new ThreadClassLoaderScope(classLoader);Throwable localThrowable4 = null;
      try
      {
        connection.getIOState().onConnected();
        

        remote = remoteEndpointFactory.newRemoteEndpoint(connection, outgoingHandler, getBatchMode());
        if (LOG_OPEN.isDebugEnabled()) {
          LOG_OPEN.debug("[{}] {}.open() remote={}", new Object[] { policy.getBehavior(), getClass().getSimpleName(), remote });
        }
        
        websocket.openSession(this);
        

        connection.getIOState().onOpened();
        
        if (LOG.isDebugEnabled())
        {
          LOG.debug("open -> {}", new Object[] { dump() });
        }
        
        if (openFuture != null)
        {
          openFuture.complete(this);
        }
      }
      catch (Throwable localThrowable2)
      {
        localThrowable4 = localThrowable2;throw localThrowable2;











      }
      finally
      {










        if (scope != null) if (localThrowable4 != null) try { scope.close(); } catch (Throwable localThrowable3) { localThrowable4.addSuppressed(localThrowable3); } else scope.close();
      }
    } catch (CloseException ce) {
      LOG.warn(ce);
      close(ce.getStatusCode(), ce.getMessage());
    }
    catch (Throwable t)
    {
      LOG.warn(t);
      

      int statusCode = 1011;
      if (policy.getBehavior() == WebSocketBehavior.CLIENT)
      {
        statusCode = 1008;
      }
      close(statusCode, t.getMessage());
    }
  }
  
  public void setExtensionFactory(ExtensionFactory extensionFactory)
  {
    this.extensionFactory = extensionFactory;
  }
  
  public void setFuture(CompletableFuture<Session> fut)
  {
    openFuture = fut;
  }
  




  public void setIdleTimeout(long ms)
  {
    connection.setMaxIdleTimeout(ms);
  }
  
  public void setOutgoingHandler(OutgoingFrames outgoing)
  {
    outgoingHandler = outgoing;
  }
  


  @Deprecated
  public void setPolicy(WebSocketPolicy policy) {}
  

  public void setUpgradeRequest(UpgradeRequest request)
  {
    upgradeRequest = request;
    protocolVersion = request.getProtocolVersion();
    parameterMap.clear();
    if (request.getParameterMap() != null)
    {
      for (Map.Entry<String, List<String>> entry : request.getParameterMap().entrySet())
      {
        List<String> values = (List)entry.getValue();
        if (values != null)
        {
          parameterMap.put(entry.getKey(), values.toArray(new String[values.size()]));
        }
        else
        {
          parameterMap.put(entry.getKey(), new String[0]);
        }
      }
    }
  }
  
  public void setUpgradeResponse(UpgradeResponse response)
  {
    upgradeResponse = response;
  }
  

  public SuspendToken suspend()
  {
    return connection.suspend();
  }
  



  public BatchMode getBatchMode()
  {
    return BatchMode.AUTO;
  }
  

  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append("WebSocketSession[");
    builder.append("websocket=").append(websocket);
    builder.append(",behavior=").append(policy.getBehavior());
    builder.append(",connection=").append(connection);
    builder.append(",remote=").append(remote);
    builder.append(",incoming=").append(incomingHandler);
    builder.append(",outgoing=").append(outgoingHandler);
    builder.append("]");
    return builder.toString();
  }
  
  public static abstract interface Listener
  {
    public abstract void onOpened(WebSocketSession paramWebSocketSession);
    
    public abstract void onClosed(WebSocketSession paramWebSocketSession);
  }
}
