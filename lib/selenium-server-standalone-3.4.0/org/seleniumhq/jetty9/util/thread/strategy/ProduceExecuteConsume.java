package org.seleniumhq.jetty9.util.thread.strategy;

import java.util.concurrent.Executor;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.thread.ExecutionStrategy;
import org.seleniumhq.jetty9.util.thread.ExecutionStrategy.Factory;
import org.seleniumhq.jetty9.util.thread.ExecutionStrategy.Producer;
import org.seleniumhq.jetty9.util.thread.Invocable.InvocationType;
import org.seleniumhq.jetty9.util.thread.Locker;
import org.seleniumhq.jetty9.util.thread.Locker.Lock;




















public class ProduceExecuteConsume
  extends ExecutingExecutionStrategy
  implements ExecutionStrategy
{
  private static final Logger LOG = Log.getLogger(ProduceExecuteConsume.class);
  
  private final Locker _locker = new Locker();
  private final ExecutionStrategy.Producer _producer;
  private State _state = State.IDLE;
  
  public ProduceExecuteConsume(ExecutionStrategy.Producer producer, Executor executor)
  {
    this(producer, executor, Invocable.InvocationType.NON_BLOCKING);
  }
  
  public ProduceExecuteConsume(ExecutionStrategy.Producer producer, Executor executor, Invocable.InvocationType preferred)
  {
    super(executor, preferred);
    _producer = producer;
  }
  

  public void produce()
  {
    Locker.Lock locked = _locker.lock();Throwable localThrowable9 = null;
    try {
      switch (1.$SwitchMap$org$eclipse$jetty$util$thread$strategy$ProduceExecuteConsume$State[_state.ordinal()])
      {
      case 1: 
        _state = State.PRODUCE;
        break;
      
      case 2: 
      case 3: 
        _state = State.EXECUTE;
        return;
      }
    }
    catch (Throwable localThrowable2)
    {
      localThrowable9 = localThrowable2;throw localThrowable2;





    }
    finally
    {




      if (locked != null) { if (localThrowable9 != null) try { locked.close(); } catch (Throwable localThrowable3) { localThrowable9.addSuppressed(localThrowable3); } else { locked.close();
        }
      }
    }
    for (;;)
    {
      Runnable task = _producer.produce();
      if (LOG.isDebugEnabled()) {
        LOG.debug("{} produced {}", new Object[] { _producer, task });
      }
      if (task == null)
      {
        Locker.Lock locked = _locker.lock();localThrowable2 = null;
        try {
          switch (1.$SwitchMap$org$eclipse$jetty$util$thread$strategy$ProduceExecuteConsume$State[_state.ordinal()])
          {
          case 1: 
            throw new IllegalStateException();
          case 2: 
            _state = State.IDLE;
            return;
          case 3: 
            _state = State.PRODUCE;
            

            if (locked == null) continue; if (localThrowable2 != null) { try { locked.close(); } catch (Throwable localThrowable11) {} localThrowable2.addSuppressed(localThrowable11); continue; } locked.close(); continue;
          }
        }
        catch (Throwable localThrowable13)
        {
          localThrowable2 = localThrowable13;throw localThrowable13;





        }
        finally
        {




          if (locked != null) if (localThrowable2 != null) try { locked.close(); } catch (Throwable localThrowable8) { localThrowable2.addSuppressed(localThrowable8); } else locked.close();
        }
      }
      else {
        execute(task);
      }
    }
  }
  
  public void dispatch()
  {
    produce();
  }
  
  public static class Factory implements ExecutionStrategy.Factory
  {
    public Factory() {}
    
    public ExecutionStrategy newExecutionStrategy(ExecutionStrategy.Producer producer, Executor executor) {
      return new ProduceExecuteConsume(producer, executor);
    }
  }
  
  private static enum State
  {
    IDLE,  PRODUCE,  EXECUTE;
    
    private State() {}
  }
}
