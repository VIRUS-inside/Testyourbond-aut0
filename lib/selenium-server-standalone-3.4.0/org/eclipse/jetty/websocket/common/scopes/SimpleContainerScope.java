package org.eclipse.jetty.websocket.common.scopes;

import java.util.concurrent.Executor;
import org.eclipse.jetty.io.ByteBufferPool;
import org.eclipse.jetty.io.MappedByteBufferPool;
import org.eclipse.jetty.util.DecoratedObjectFactory;
import org.eclipse.jetty.util.component.ContainerLifeCycle;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.websocket.api.WebSocketPolicy;
import org.eclipse.jetty.websocket.common.WebSocketSession;


















public class SimpleContainerScope
  extends ContainerLifeCycle
  implements WebSocketContainerScope
{
  private final ByteBufferPool bufferPool;
  private final DecoratedObjectFactory objectFactory;
  private final WebSocketPolicy policy;
  private Executor executor;
  private SslContextFactory sslContextFactory;
  
  public SimpleContainerScope(WebSocketPolicy policy)
  {
    this(policy, new MappedByteBufferPool(), new DecoratedObjectFactory());
    sslContextFactory = new SslContextFactory();
  }
  
  public SimpleContainerScope(WebSocketPolicy policy, ByteBufferPool bufferPool)
  {
    this(policy, bufferPool, new DecoratedObjectFactory());
  }
  
  public SimpleContainerScope(WebSocketPolicy policy, ByteBufferPool bufferPool, DecoratedObjectFactory objectFactory)
  {
    this.policy = policy;
    this.bufferPool = bufferPool;
    this.objectFactory = objectFactory;
    
    QueuedThreadPool threadPool = new QueuedThreadPool();
    String name = SimpleContainerScope.class.getSimpleName() + ".Executor@" + hashCode();
    threadPool.setName(name);
    threadPool.setDaemon(true);
    executor = threadPool;
  }
  
  protected void doStart()
    throws Exception
  {
    super.doStart();
  }
  
  protected void doStop()
    throws Exception
  {
    super.doStop();
  }
  

  public ByteBufferPool getBufferPool()
  {
    return bufferPool;
  }
  

  public Executor getExecutor()
  {
    return executor;
  }
  

  public DecoratedObjectFactory getObjectFactory()
  {
    return objectFactory;
  }
  

  public WebSocketPolicy getPolicy()
  {
    return policy;
  }
  

  public SslContextFactory getSslContextFactory()
  {
    return sslContextFactory;
  }
  
  public void setSslContextFactory(SslContextFactory sslContextFactory)
  {
    this.sslContextFactory = sslContextFactory;
  }
  
  public void onSessionOpened(WebSocketSession session) {}
  
  public void onSessionClosed(WebSocketSession session) {}
}
