package org.seleniumhq.jetty9.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.seleniumhq.jetty9.io.ArrayByteBufferPool;
import org.seleniumhq.jetty9.io.ByteBufferPool;
import org.seleniumhq.jetty9.io.EndPoint;
import org.seleniumhq.jetty9.util.FutureCallback;
import org.seleniumhq.jetty9.util.StringUtil;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.component.ContainerLifeCycle;
import org.seleniumhq.jetty9.util.component.Dumpable;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.thread.ScheduledExecutorScheduler;
import org.seleniumhq.jetty9.util.thread.Scheduler;








































































































@ManagedObject("Abstract implementation of the Connector Interface")
public abstract class AbstractConnector
  extends ContainerLifeCycle
  implements Connector, Dumpable
{
  protected final Logger LOG = Log.getLogger(AbstractConnector.class);
  
  private final Map<String, ConnectionFactory> _factories = new LinkedHashMap();
  private final Server _server;
  private final Executor _executor;
  private final Scheduler _scheduler;
  private final ByteBufferPool _byteBufferPool;
  private final Thread[] _acceptors;
  private final Set<EndPoint> _endpoints = Collections.newSetFromMap(new ConcurrentHashMap());
  private final Set<EndPoint> _immutableEndPoints = Collections.unmodifiableSet(_endpoints);
  private volatile CountDownLatch _stopping;
  private long _idleTimeout = 30000L;
  private String _defaultProtocol;
  private ConnectionFactory _defaultConnectionFactory;
  private String _name;
  private int _acceptorPriorityDelta = -2;
  















  public AbstractConnector(Server server, Executor executor, Scheduler scheduler, ByteBufferPool pool, int acceptors, ConnectionFactory... factories)
  {
    _server = server;
    _executor = (executor != null ? executor : _server.getThreadPool());
    if (scheduler == null)
      scheduler = (Scheduler)_server.getBean(Scheduler.class);
    _scheduler = (scheduler != null ? scheduler : new ScheduledExecutorScheduler());
    if (pool == null)
      pool = (ByteBufferPool)_server.getBean(ByteBufferPool.class);
    _byteBufferPool = (pool != null ? pool : new ArrayByteBufferPool());
    
    addBean(_server, false);
    addBean(_executor);
    if (executor == null)
      unmanage(_executor);
    addBean(_scheduler);
    addBean(_byteBufferPool);
    
    for (ConnectionFactory factory : factories) {
      addConnectionFactory(factory);
    }
    int cores = Runtime.getRuntime().availableProcessors();
    if (acceptors < 0)
      acceptors = Math.max(1, Math.min(4, cores / 8));
    if (acceptors > cores)
      LOG.warn("Acceptors should be <= availableProcessors: " + this, new Object[0]);
    _acceptors = new Thread[acceptors];
  }
  


  public Server getServer()
  {
    return _server;
  }
  

  public Executor getExecutor()
  {
    return _executor;
  }
  

  public ByteBufferPool getByteBufferPool()
  {
    return _byteBufferPool;
  }
  

  @ManagedAttribute("Idle timeout")
  public long getIdleTimeout()
  {
    return _idleTimeout;
  }
  













  public void setIdleTimeout(long idleTimeout)
  {
    _idleTimeout = idleTimeout;
  }
  



  @ManagedAttribute("number of acceptor threads")
  public int getAcceptors()
  {
    return _acceptors.length;
  }
  
  protected void doStart()
    throws Exception
  {
    if (_defaultProtocol == null)
      throw new IllegalStateException("No default protocol for " + this);
    _defaultConnectionFactory = getConnectionFactory(_defaultProtocol);
    if (_defaultConnectionFactory == null)
      throw new IllegalStateException("No protocol factory for default protocol '" + _defaultProtocol + "' in " + this);
    SslConnectionFactory ssl = (SslConnectionFactory)getConnectionFactory(SslConnectionFactory.class);
    if (ssl != null)
    {
      String next = ssl.getNextProtocol();
      ConnectionFactory cf = getConnectionFactory(next);
      if (cf == null) {
        throw new IllegalStateException("No protocol factory for SSL next protocol: '" + next + "' in " + this);
      }
    }
    super.doStart();
    
    _stopping = new CountDownLatch(_acceptors.length);
    for (int i = 0; i < _acceptors.length; i++)
    {
      Acceptor a = new Acceptor(i, null);
      addBean(a);
      getExecutor().execute(a);
    }
    
    LOG.info("Started {}", new Object[] { this });
  }
  

  protected void interruptAcceptors()
  {
    synchronized (this)
    {
      for (Thread thread : _acceptors)
      {
        if (thread != null) {
          thread.interrupt();
        }
      }
    }
  }
  
  public Future<Void> shutdown()
  {
    return new FutureCallback(true);
  }
  

  protected void doStop()
    throws Exception
  {
    interruptAcceptors();
    

    long stopTimeout = getStopTimeout();
    CountDownLatch stopping = _stopping;
    if ((stopTimeout > 0L) && (stopping != null) && (getAcceptors() > 0))
      stopping.await(stopTimeout, TimeUnit.MILLISECONDS);
    _stopping = null;
    
    super.doStop();
    
    for (Acceptor a : getBeans(Acceptor.class)) {
      removeBean(a);
    }
    LOG.info("Stopped {}", new Object[] { this });
  }
  
  public void join() throws InterruptedException
  {
    join(0L);
  }
  
  public void join(long timeout) throws InterruptedException
  {
    synchronized (this)
    {
      for (Thread thread : _acceptors) {
        if (thread != null) {
          thread.join(timeout);
        }
      }
    }
  }
  

  protected abstract void accept(int paramInt)
    throws IOException, InterruptedException;
  

  protected boolean isAccepting()
  {
    return isRunning();
  }
  

  public ConnectionFactory getConnectionFactory(String protocol)
  {
    synchronized (_factories)
    {
      return (ConnectionFactory)_factories.get(StringUtil.asciiToLowerCase(protocol));
    }
  }
  

  public <T> T getConnectionFactory(Class<T> factoryType)
  {
    synchronized (_factories)
    {
      for (ConnectionFactory f : _factories.values())
        if (factoryType.isAssignableFrom(f.getClass()))
          return f;
      return null;
    }
  }
  
  public void addConnectionFactory(ConnectionFactory factory)
  {
    synchronized (_factories)
    {
      Set<ConnectionFactory> to_remove = new HashSet();
      for (String key : factory.getProtocols())
      {
        key = StringUtil.asciiToLowerCase(key);
        ConnectionFactory old = (ConnectionFactory)_factories.remove(key);
        if (old != null)
        {
          if (old.getProtocol().equals(_defaultProtocol))
            _defaultProtocol = null;
          to_remove.add(old);
        }
        _factories.put(key, factory);
      }
      

      for (ConnectionFactory f : _factories.values()) {
        to_remove.remove(f);
      }
      
      for (ConnectionFactory old : to_remove)
      {
        removeBean(old);
        if (LOG.isDebugEnabled()) {
          LOG.debug("{} removed {}", new Object[] { this, old });
        }
      }
      
      addBean(factory);
      if (_defaultProtocol == null)
        _defaultProtocol = factory.getProtocol();
      if (LOG.isDebugEnabled()) {
        LOG.debug("{} added {}", new Object[] { this, factory });
      }
    }
  }
  
  public void addFirstConnectionFactory(ConnectionFactory factory) {
    synchronized (_factories)
    {
      List<ConnectionFactory> existings = new ArrayList(_factories.values());
      _factories.clear();
      addConnectionFactory(factory);
      for (ConnectionFactory existing : existings)
        addConnectionFactory(existing);
      _defaultProtocol = factory.getProtocol();
    }
  }
  
  public void addIfAbsentConnectionFactory(ConnectionFactory factory)
  {
    synchronized (_factories)
    {
      String key = StringUtil.asciiToLowerCase(factory.getProtocol());
      if (_factories.containsKey(key))
      {
        if (LOG.isDebugEnabled()) {
          LOG.debug("{} addIfAbsent ignored {}", new Object[] { this, factory });
        }
      }
      else {
        _factories.put(key, factory);
        addBean(factory);
        if (_defaultProtocol == null)
          _defaultProtocol = factory.getProtocol();
        if (LOG.isDebugEnabled()) {
          LOG.debug("{} addIfAbsent added {}", new Object[] { this, factory });
        }
      }
    }
  }
  
  public ConnectionFactory removeConnectionFactory(String protocol) {
    synchronized (_factories)
    {
      ConnectionFactory factory = (ConnectionFactory)_factories.remove(StringUtil.asciiToLowerCase(protocol));
      removeBean(factory);
      return factory;
    }
  }
  

  public Collection<ConnectionFactory> getConnectionFactories()
  {
    synchronized (_factories)
    {
      return _factories.values();
    }
  }
  
  public void setConnectionFactories(Collection<ConnectionFactory> factories)
  {
    synchronized (_factories)
    {
      List<ConnectionFactory> existing = new ArrayList(_factories.values());
      for (ConnectionFactory factory : existing)
        removeConnectionFactory(factory.getProtocol());
      for (ConnectionFactory factory : factories) {
        if (factory != null)
          addConnectionFactory(factory);
      }
    }
  }
  
  @ManagedAttribute("The priority delta to apply to acceptor threads")
  public int getAcceptorPriorityDelta() {
    return _acceptorPriorityDelta;
  }
  








  public void setAcceptorPriorityDelta(int acceptorPriorityDelta)
  {
    int old = _acceptorPriorityDelta;
    _acceptorPriorityDelta = acceptorPriorityDelta;
    if ((old != acceptorPriorityDelta) && (isStarted()))
    {
      for (Thread thread : _acceptors) {
        thread.setPriority(Math.max(1, Math.min(10, thread.getPriority() - old + acceptorPriorityDelta)));
      }
    }
  }
  
  @ManagedAttribute("Protocols supported by this connector")
  public List<String> getProtocols()
  {
    synchronized (_factories)
    {
      return new ArrayList(_factories.keySet());
    }
  }
  
  public void clearConnectionFactories()
  {
    synchronized (_factories)
    {
      _factories.clear();
    }
  }
  
  @ManagedAttribute("This connector's default protocol")
  public String getDefaultProtocol()
  {
    return _defaultProtocol;
  }
  
  public void setDefaultProtocol(String defaultProtocol)
  {
    _defaultProtocol = StringUtil.asciiToLowerCase(defaultProtocol);
    if (isRunning()) {
      _defaultConnectionFactory = getConnectionFactory(_defaultProtocol);
    }
  }
  
  public ConnectionFactory getDefaultConnectionFactory()
  {
    if (isStarted())
      return _defaultConnectionFactory;
    return getConnectionFactory(_defaultProtocol);
  }
  
  protected boolean handleAcceptFailure(Throwable previous, Throwable current)
  {
    if (isAccepting())
    {
      if (previous == null) {
        LOG.warn(current);
      } else {
        LOG.debug(current);
      }
      

      try
      {
        Thread.sleep(1000L);
        return true;
      }
      catch (Throwable x)
      {
        return false;
      }
    }
    

    LOG.ignore(current);
    return false;
  }
  
  private class Acceptor
    implements Runnable
  {
    private final int _id;
    private String _name;
    
    private Acceptor(int id)
    {
      _id = id;
    }
    
    /* Error */
    public void run()
    {
      // Byte code:
      //   0: invokestatic 39	java/lang/Thread:currentThread	()Ljava/lang/Thread;
      //   3: astore_1
      //   4: aload_1
      //   5: invokevirtual 43	java/lang/Thread:getName	()Ljava/lang/String;
      //   8: astore_2
      //   9: aload_0
      //   10: ldc 45
      //   12: iconst_4
      //   13: anewarray 4	java/lang/Object
      //   16: dup
      //   17: iconst_0
      //   18: aload_2
      //   19: aastore
      //   20: dup
      //   21: iconst_1
      //   22: aload_0
      //   23: getfield 27	org/seleniumhq/jetty9/server/AbstractConnector$Acceptor:_id	I
      //   26: invokestatic 51	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
      //   29: aastore
      //   30: dup
      //   31: iconst_2
      //   32: aload_0
      //   33: invokevirtual 55	java/lang/Object:hashCode	()I
      //   36: invokestatic 51	java/lang/Integer:valueOf	(I)Ljava/lang/Integer;
      //   39: aastore
      //   40: dup
      //   41: iconst_3
      //   42: aload_0
      //   43: getfield 22	org/seleniumhq/jetty9/server/AbstractConnector$Acceptor:this$0	Lorg/seleniumhq/jetty9/server/AbstractConnector;
      //   46: invokevirtual 58	org/seleniumhq/jetty9/server/AbstractConnector:toString	()Ljava/lang/String;
      //   49: aastore
      //   50: invokestatic 64	java/lang/String:format	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
      //   53: putfield 66	org/seleniumhq/jetty9/server/AbstractConnector$Acceptor:_name	Ljava/lang/String;
      //   56: aload_1
      //   57: aload_0
      //   58: getfield 66	org/seleniumhq/jetty9/server/AbstractConnector$Acceptor:_name	Ljava/lang/String;
      //   61: invokevirtual 70	java/lang/Thread:setName	(Ljava/lang/String;)V
      //   64: aload_1
      //   65: invokevirtual 73	java/lang/Thread:getPriority	()I
      //   68: istore_3
      //   69: aload_0
      //   70: getfield 22	org/seleniumhq/jetty9/server/AbstractConnector$Acceptor:this$0	Lorg/seleniumhq/jetty9/server/AbstractConnector;
      //   73: invokestatic 77	org/seleniumhq/jetty9/server/AbstractConnector:access$100	(Lorg/seleniumhq/jetty9/server/AbstractConnector;)I
      //   76: ifeq +25 -> 101
      //   79: aload_1
      //   80: iconst_1
      //   81: bipush 10
      //   83: iload_3
      //   84: aload_0
      //   85: getfield 22	org/seleniumhq/jetty9/server/AbstractConnector$Acceptor:this$0	Lorg/seleniumhq/jetty9/server/AbstractConnector;
      //   88: invokestatic 77	org/seleniumhq/jetty9/server/AbstractConnector:access$100	(Lorg/seleniumhq/jetty9/server/AbstractConnector;)I
      //   91: iadd
      //   92: invokestatic 83	java/lang/Math:min	(II)I
      //   95: invokestatic 86	java/lang/Math:max	(II)I
      //   98: invokevirtual 90	java/lang/Thread:setPriority	(I)V
      //   101: aload_0
      //   102: getfield 22	org/seleniumhq/jetty9/server/AbstractConnector$Acceptor:this$0	Lorg/seleniumhq/jetty9/server/AbstractConnector;
      //   105: dup
      //   106: astore 4
      //   108: monitorenter
      //   109: aload_0
      //   110: getfield 22	org/seleniumhq/jetty9/server/AbstractConnector$Acceptor:this$0	Lorg/seleniumhq/jetty9/server/AbstractConnector;
      //   113: invokestatic 94	org/seleniumhq/jetty9/server/AbstractConnector:access$200	(Lorg/seleniumhq/jetty9/server/AbstractConnector;)[Ljava/lang/Thread;
      //   116: aload_0
      //   117: getfield 27	org/seleniumhq/jetty9/server/AbstractConnector$Acceptor:_id	I
      //   120: aload_1
      //   121: aastore
      //   122: aload 4
      //   124: monitorexit
      //   125: goto +11 -> 136
      //   128: astore 5
      //   130: aload 4
      //   132: monitorexit
      //   133: aload 5
      //   135: athrow
      //   136: aconst_null
      //   137: astore 4
      //   139: aload_0
      //   140: getfield 22	org/seleniumhq/jetty9/server/AbstractConnector$Acceptor:this$0	Lorg/seleniumhq/jetty9/server/AbstractConnector;
      //   143: invokevirtual 98	org/seleniumhq/jetty9/server/AbstractConnector:isAccepting	()Z
      //   146: ifeq +49 -> 195
      //   149: aload_0
      //   150: getfield 22	org/seleniumhq/jetty9/server/AbstractConnector$Acceptor:this$0	Lorg/seleniumhq/jetty9/server/AbstractConnector;
      //   153: aload_0
      //   154: getfield 27	org/seleniumhq/jetty9/server/AbstractConnector$Acceptor:_id	I
      //   157: invokevirtual 101	org/seleniumhq/jetty9/server/AbstractConnector:accept	(I)V
      //   160: aconst_null
      //   161: astore 4
      //   163: goto -24 -> 139
      //   166: astore 5
      //   168: aload_0
      //   169: getfield 22	org/seleniumhq/jetty9/server/AbstractConnector$Acceptor:this$0	Lorg/seleniumhq/jetty9/server/AbstractConnector;
      //   172: aload 4
      //   174: aload 5
      //   176: invokevirtual 105	org/seleniumhq/jetty9/server/AbstractConnector:handleAcceptFailure	(Ljava/lang/Throwable;Ljava/lang/Throwable;)Z
      //   179: ifeq +10 -> 189
      //   182: aload 5
      //   184: astore 4
      //   186: goto +6 -> 192
      //   189: goto +6 -> 195
      //   192: goto -53 -> 139
      //   195: aload_1
      //   196: aload_2
      //   197: invokevirtual 70	java/lang/Thread:setName	(Ljava/lang/String;)V
      //   200: aload_0
      //   201: getfield 22	org/seleniumhq/jetty9/server/AbstractConnector$Acceptor:this$0	Lorg/seleniumhq/jetty9/server/AbstractConnector;
      //   204: invokestatic 77	org/seleniumhq/jetty9/server/AbstractConnector:access$100	(Lorg/seleniumhq/jetty9/server/AbstractConnector;)I
      //   207: ifeq +8 -> 215
      //   210: aload_1
      //   211: iload_3
      //   212: invokevirtual 90	java/lang/Thread:setPriority	(I)V
      //   215: aload_0
      //   216: getfield 22	org/seleniumhq/jetty9/server/AbstractConnector$Acceptor:this$0	Lorg/seleniumhq/jetty9/server/AbstractConnector;
      //   219: dup
      //   220: astore 4
      //   222: monitorenter
      //   223: aload_0
      //   224: getfield 22	org/seleniumhq/jetty9/server/AbstractConnector$Acceptor:this$0	Lorg/seleniumhq/jetty9/server/AbstractConnector;
      //   227: invokestatic 94	org/seleniumhq/jetty9/server/AbstractConnector:access$200	(Lorg/seleniumhq/jetty9/server/AbstractConnector;)[Ljava/lang/Thread;
      //   230: aload_0
      //   231: getfield 27	org/seleniumhq/jetty9/server/AbstractConnector$Acceptor:_id	I
      //   234: aconst_null
      //   235: aastore
      //   236: aload 4
      //   238: monitorexit
      //   239: goto +11 -> 250
      //   242: astore 6
      //   244: aload 4
      //   246: monitorexit
      //   247: aload 6
      //   249: athrow
      //   250: aload_0
      //   251: getfield 22	org/seleniumhq/jetty9/server/AbstractConnector$Acceptor:this$0	Lorg/seleniumhq/jetty9/server/AbstractConnector;
      //   254: invokestatic 109	org/seleniumhq/jetty9/server/AbstractConnector:access$300	(Lorg/seleniumhq/jetty9/server/AbstractConnector;)Ljava/util/concurrent/CountDownLatch;
      //   257: astore 4
      //   259: aload 4
      //   261: ifnull +8 -> 269
      //   264: aload 4
      //   266: invokevirtual 114	java/util/concurrent/CountDownLatch:countDown	()V
      //   269: goto +82 -> 351
      //   272: astore 7
      //   274: aload_1
      //   275: aload_2
      //   276: invokevirtual 70	java/lang/Thread:setName	(Ljava/lang/String;)V
      //   279: aload_0
      //   280: getfield 22	org/seleniumhq/jetty9/server/AbstractConnector$Acceptor:this$0	Lorg/seleniumhq/jetty9/server/AbstractConnector;
      //   283: invokestatic 77	org/seleniumhq/jetty9/server/AbstractConnector:access$100	(Lorg/seleniumhq/jetty9/server/AbstractConnector;)I
      //   286: ifeq +8 -> 294
      //   289: aload_1
      //   290: iload_3
      //   291: invokevirtual 90	java/lang/Thread:setPriority	(I)V
      //   294: aload_0
      //   295: getfield 22	org/seleniumhq/jetty9/server/AbstractConnector$Acceptor:this$0	Lorg/seleniumhq/jetty9/server/AbstractConnector;
      //   298: dup
      //   299: astore 8
      //   301: monitorenter
      //   302: aload_0
      //   303: getfield 22	org/seleniumhq/jetty9/server/AbstractConnector$Acceptor:this$0	Lorg/seleniumhq/jetty9/server/AbstractConnector;
      //   306: invokestatic 94	org/seleniumhq/jetty9/server/AbstractConnector:access$200	(Lorg/seleniumhq/jetty9/server/AbstractConnector;)[Ljava/lang/Thread;
      //   309: aload_0
      //   310: getfield 27	org/seleniumhq/jetty9/server/AbstractConnector$Acceptor:_id	I
      //   313: aconst_null
      //   314: aastore
      //   315: aload 8
      //   317: monitorexit
      //   318: goto +11 -> 329
      //   321: astore 9
      //   323: aload 8
      //   325: monitorexit
      //   326: aload 9
      //   328: athrow
      //   329: aload_0
      //   330: getfield 22	org/seleniumhq/jetty9/server/AbstractConnector$Acceptor:this$0	Lorg/seleniumhq/jetty9/server/AbstractConnector;
      //   333: invokestatic 109	org/seleniumhq/jetty9/server/AbstractConnector:access$300	(Lorg/seleniumhq/jetty9/server/AbstractConnector;)Ljava/util/concurrent/CountDownLatch;
      //   336: astore 8
      //   338: aload 8
      //   340: ifnull +8 -> 348
      //   343: aload 8
      //   345: invokevirtual 114	java/util/concurrent/CountDownLatch:countDown	()V
      //   348: aload 7
      //   350: athrow
      //   351: return
      // Line number table:
      //   Java source line #582	-> byte code offset #0
      //   Java source line #583	-> byte code offset #4
      //   Java source line #584	-> byte code offset #9
      //   Java source line #585	-> byte code offset #56
      //   Java source line #587	-> byte code offset #64
      //   Java source line #588	-> byte code offset #69
      //   Java source line #589	-> byte code offset #79
      //   Java source line #591	-> byte code offset #101
      //   Java source line #593	-> byte code offset #109
      //   Java source line #594	-> byte code offset #122
      //   Java source line #598	-> byte code offset #136
      //   Java source line #599	-> byte code offset #139
      //   Java source line #603	-> byte code offset #149
      //   Java source line #604	-> byte code offset #160
      //   Java source line #612	-> byte code offset #163
      //   Java source line #606	-> byte code offset #166
      //   Java source line #608	-> byte code offset #168
      //   Java source line #609	-> byte code offset #182
      //   Java source line #611	-> byte code offset #189
      //   Java source line #612	-> byte code offset #192
      //   Java source line #617	-> byte code offset #195
      //   Java source line #618	-> byte code offset #200
      //   Java source line #619	-> byte code offset #210
      //   Java source line #621	-> byte code offset #215
      //   Java source line #623	-> byte code offset #223
      //   Java source line #624	-> byte code offset #236
      //   Java source line #625	-> byte code offset #250
      //   Java source line #626	-> byte code offset #259
      //   Java source line #627	-> byte code offset #264
      //   Java source line #628	-> byte code offset #269
      //   Java source line #617	-> byte code offset #272
      //   Java source line #618	-> byte code offset #279
      //   Java source line #619	-> byte code offset #289
      //   Java source line #621	-> byte code offset #294
      //   Java source line #623	-> byte code offset #302
      //   Java source line #624	-> byte code offset #315
      //   Java source line #625	-> byte code offset #329
      //   Java source line #626	-> byte code offset #338
      //   Java source line #627	-> byte code offset #343
      //   Java source line #628	-> byte code offset #348
      //   Java source line #629	-> byte code offset #351
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	352	0	this	Acceptor
      //   3	287	1	thread	Thread
      //   8	268	2	name	String
      //   68	223	3	priority	int
      //   106	25	4	Ljava/lang/Object;	Object
      //   137	48	4	exception	Throwable
      //   257	8	4	stopping	CountDownLatch
      //   128	6	5	localObject1	Object
      //   166	17	5	x	Throwable
      //   242	6	6	localObject2	Object
      //   272	77	7	localObject3	Object
      //   336	8	8	stopping	CountDownLatch
      //   321	6	9	localObject4	Object
      // Exception table:
      //   from	to	target	type
      //   109	125	128	finally
      //   128	133	128	finally
      //   149	163	166	java/lang/Throwable
      //   223	239	242	finally
      //   242	247	242	finally
      //   136	195	272	finally
      //   272	274	272	finally
      //   302	318	321	finally
      //   321	326	321	finally
    }
    
    public String toString()
    {
      String name = _name;
      if (name == null)
        return String.format("acceptor-%d@%x", new Object[] { Integer.valueOf(_id), Integer.valueOf(hashCode()) });
      return name;
    }
  }
  

























  public Collection<EndPoint> getConnectedEndPoints()
  {
    return _immutableEndPoints;
  }
  
  protected void onEndPointOpened(EndPoint endp)
  {
    _endpoints.add(endp);
  }
  
  protected void onEndPointClosed(EndPoint endp)
  {
    _endpoints.remove(endp);
  }
  

  public Scheduler getScheduler()
  {
    return _scheduler;
  }
  

  public String getName()
  {
    return _name;
  }
  







  public void setName(String name)
  {
    _name = name;
  }
  

  public String toString()
  {
    return String.format("%s@%x{%s,%s}", new Object[] { _name == null ? 
      getClass().getSimpleName() : _name, 
      Integer.valueOf(hashCode()), 
      getDefaultProtocol(), getProtocols() });
  }
}
