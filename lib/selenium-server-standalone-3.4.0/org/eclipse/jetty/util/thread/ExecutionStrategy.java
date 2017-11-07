package org.eclipse.jetty.util.thread;

import java.lang.reflect.Constructor;
import java.util.concurrent.Executor;
import org.eclipse.jetty.util.Loader;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.thread.strategy.ExecuteProduceConsume;






































































public abstract interface ExecutionStrategy
{
  public abstract void dispatch();
  
  public abstract void produce();
  
  public static abstract interface Producer
  {
    public abstract Runnable produce();
  }
  
  public static abstract interface Factory
  {
    public abstract ExecutionStrategy newExecutionStrategy(ExecutionStrategy.Producer paramProducer, Executor paramExecutor);
    
    public static Factory getDefault()
    {
      return ExecutionStrategy.DefaultExecutionStrategyFactory.INSTANCE;
    }
  }
  
  public static class DefaultExecutionStrategyFactory implements ExecutionStrategy.Factory
  {
    private static final Logger LOG = Log.getLogger(ExecutionStrategy.Factory.class);
    private static final ExecutionStrategy.Factory INSTANCE = new DefaultExecutionStrategyFactory();
    
    public DefaultExecutionStrategyFactory() {}
    
    public ExecutionStrategy newExecutionStrategy(ExecutionStrategy.Producer producer, Executor executor) {
      String strategy = System.getProperty(producer.getClass().getName() + ".ExecutionStrategy");
      if (strategy != null)
      {
        try
        {
          Class<? extends ExecutionStrategy> c = Loader.loadClass(strategy);
          Constructor<? extends ExecutionStrategy> m = c.getConstructor(new Class[] { ExecutionStrategy.Producer.class, Executor.class });
          LOG.info("Use {} for {}", new Object[] { c.getSimpleName(), producer.getClass().getName() });
          return (ExecutionStrategy)m.newInstance(new Object[] { producer, executor });
        }
        catch (Exception e)
        {
          LOG.warn(e);
        }
      }
      
      return new ExecuteProduceConsume(producer, executor);
    }
  }
}
