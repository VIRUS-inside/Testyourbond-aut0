package org.apache.commons.lang3.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.lang3.builder.Builder;
































































































public class BasicThreadFactory
  implements ThreadFactory
{
  private final AtomicLong threadCounter;
  private final ThreadFactory wrappedFactory;
  private final Thread.UncaughtExceptionHandler uncaughtExceptionHandler;
  private final String namingPattern;
  private final Integer priority;
  private final Boolean daemonFlag;
  
  private BasicThreadFactory(Builder builder)
  {
    if (wrappedFactory == null) {
      wrappedFactory = Executors.defaultThreadFactory();
    } else {
      wrappedFactory = wrappedFactory;
    }
    
    namingPattern = namingPattern;
    priority = priority;
    daemonFlag = daemonFlag;
    uncaughtExceptionHandler = exceptionHandler;
    
    threadCounter = new AtomicLong();
  }
  







  public final ThreadFactory getWrappedFactory()
  {
    return wrappedFactory;
  }
  





  public final String getNamingPattern()
  {
    return namingPattern;
  }
  







  public final Boolean getDaemonFlag()
  {
    return daemonFlag;
  }
  





  public final Integer getPriority()
  {
    return priority;
  }
  





  public final Thread.UncaughtExceptionHandler getUncaughtExceptionHandler()
  {
    return uncaughtExceptionHandler;
  }
  






  public long getThreadCount()
  {
    return threadCounter.get();
  }
  








  public Thread newThread(Runnable r)
  {
    Thread t = getWrappedFactory().newThread(r);
    initializeThread(t);
    
    return t;
  }
  








  private void initializeThread(Thread t)
  {
    if (getNamingPattern() != null) {
      Long count = Long.valueOf(threadCounter.incrementAndGet());
      t.setName(String.format(getNamingPattern(), new Object[] { count }));
    }
    
    if (getUncaughtExceptionHandler() != null) {
      t.setUncaughtExceptionHandler(getUncaughtExceptionHandler());
    }
    
    if (getPriority() != null) {
      t.setPriority(getPriority().intValue());
    }
    
    if (getDaemonFlag() != null) {
      t.setDaemon(getDaemonFlag().booleanValue());
    }
  }
  





  public static class Builder
    implements Builder<BasicThreadFactory>
  {
    private ThreadFactory wrappedFactory;
    




    private Thread.UncaughtExceptionHandler exceptionHandler;
    



    private String namingPattern;
    



    private Integer priority;
    



    private Boolean daemonFlag;
    




    public Builder() {}
    




    public Builder wrappedFactory(ThreadFactory factory)
    {
      if (factory == null) {
        throw new NullPointerException("Wrapped ThreadFactory must not be null!");
      }
      

      wrappedFactory = factory;
      return this;
    }
    







    public Builder namingPattern(String pattern)
    {
      if (pattern == null) {
        throw new NullPointerException("Naming pattern must not be null!");
      }
      

      namingPattern = pattern;
      return this;
    }
    







    public Builder daemon(boolean f)
    {
      daemonFlag = Boolean.valueOf(f);
      return this;
    }
    






    public Builder priority(int prio)
    {
      priority = Integer.valueOf(prio);
      return this;
    }
    









    public Builder uncaughtExceptionHandler(Thread.UncaughtExceptionHandler handler)
    {
      if (handler == null) {
        throw new NullPointerException("Uncaught exception handler must not be null!");
      }
      

      exceptionHandler = handler;
      return this;
    }
    





    public void reset()
    {
      wrappedFactory = null;
      exceptionHandler = null;
      namingPattern = null;
      priority = null;
      daemonFlag = null;
    }
    







    public BasicThreadFactory build()
    {
      BasicThreadFactory factory = new BasicThreadFactory(this, null);
      reset();
      return factory;
    }
  }
}
