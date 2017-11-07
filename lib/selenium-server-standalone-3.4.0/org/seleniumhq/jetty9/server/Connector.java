package org.seleniumhq.jetty9.server;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import org.seleniumhq.jetty9.io.ByteBufferPool;
import org.seleniumhq.jetty9.io.EndPoint;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.component.Graceful;
import org.seleniumhq.jetty9.util.component.LifeCycle;
import org.seleniumhq.jetty9.util.thread.Scheduler;

@ManagedObject("Connector Interface")
public abstract interface Connector
  extends LifeCycle, Graceful
{
  public abstract Server getServer();
  
  public abstract Executor getExecutor();
  
  public abstract Scheduler getScheduler();
  
  public abstract ByteBufferPool getByteBufferPool();
  
  public abstract ConnectionFactory getConnectionFactory(String paramString);
  
  public abstract <T> T getConnectionFactory(Class<T> paramClass);
  
  public abstract ConnectionFactory getDefaultConnectionFactory();
  
  public abstract Collection<ConnectionFactory> getConnectionFactories();
  
  public abstract List<String> getProtocols();
  
  @ManagedAttribute("maximum time a connection can be idle before being closed (in ms)")
  public abstract long getIdleTimeout();
  
  public abstract Object getTransport();
  
  public abstract Collection<EndPoint> getConnectedEndPoints();
  
  public abstract String getName();
}
