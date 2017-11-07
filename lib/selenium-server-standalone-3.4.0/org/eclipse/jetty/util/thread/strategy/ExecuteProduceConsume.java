package org.eclipse.jetty.util.thread.strategy;

import java.util.concurrent.Executor;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.thread.ExecutionStrategy;
import org.eclipse.jetty.util.thread.ExecutionStrategy.Factory;
import org.eclipse.jetty.util.thread.ExecutionStrategy.Producer;
import org.eclipse.jetty.util.thread.Invocable;
import org.eclipse.jetty.util.thread.Invocable.InvocationType;
import org.eclipse.jetty.util.thread.Locker;
import org.eclipse.jetty.util.thread.Locker.Lock;





























public class ExecuteProduceConsume
  extends ExecutingExecutionStrategy
  implements ExecutionStrategy, Runnable
{
  private static final Logger LOG = Log.getLogger(ExecuteProduceConsume.class);
  
  private final Locker _locker = new Locker();
  private final Runnable _runProduce = new RunProduce(null);
  private final ExecutionStrategy.Producer _producer;
  private boolean _idle = true;
  
  private boolean _execute;
  private boolean _producing;
  private boolean _pending;
  
  public ExecuteProduceConsume(ExecutionStrategy.Producer producer, Executor executor)
  {
    this(producer, executor, Invocable.InvocationType.BLOCKING);
  }
  
  public ExecuteProduceConsume(ExecutionStrategy.Producer producer, Executor executor, Invocable.InvocationType preferred)
  {
    super(executor, preferred);
    _producer = producer;
  }
  

  public void produce()
  {
    if (LOG.isDebugEnabled()) {
      LOG.debug("{} execute", new Object[] { this });
    }
    boolean produce = false;
    Locker.Lock locked = _locker.lock();Throwable localThrowable3 = null;
    try
    {
      if (_idle)
      {
        if (_producing) {
          throw new IllegalStateException();
        }
        
        produce = this._producing = 1;
        
        _idle = false;

      }
      else
      {

        _execute = true;
      }
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;








    }
    finally
    {







      if (locked != null) if (localThrowable3 != null) try { locked.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else locked.close();
    }
    if (produce) {
      produceConsume();
    }
  }
  
  public void dispatch()
  {
    if (LOG.isDebugEnabled())
      LOG.debug("{} spawning", new Object[] { this });
    boolean dispatch = false;
    Locker.Lock locked = _locker.lock();Throwable localThrowable3 = null;
    try {
      if (_idle) {
        dispatch = true;
      } else {
        _execute = true;
      }
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;

    }
    finally
    {

      if (locked != null) if (localThrowable3 != null) try { locked.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else locked.close(); }
    if (dispatch) {
      execute(_runProduce);
    }
  }
  
  public void run()
  {
    if (LOG.isDebugEnabled())
      LOG.debug("{} run", new Object[] { this });
    boolean produce = false;
    Locker.Lock locked = _locker.lock();Throwable localThrowable3 = null;
    try {
      _pending = false;
      if ((!_idle) && (!_producing))
      {
        produce = this._producing = 1;
      }
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;


    }
    finally
    {

      if (locked != null) if (localThrowable3 != null) try { locked.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else locked.close();
    }
    if (produce) {
      produceConsume();
    }
  }
  
  private void produceConsume() {
    if (LOG.isDebugEnabled()) {
      LOG.debug("{} produce enter", new Object[] { this });
    }
    
    for (;;)
    {
      if (LOG.isDebugEnabled()) {
        LOG.debug("{} producing", new Object[] { this });
      }
      Runnable task = _producer.produce();
      
      if (LOG.isDebugEnabled()) {
        LOG.debug("{} produced {}", new Object[] { this, task });
      }
      boolean dispatch = false;
      Locker.Lock locked = _locker.lock();Throwable localThrowable9 = null;
      try
      {
        _producing = false;
        

        if (task == null)
        {


          if (_execute)
          {
            _idle = false;
            _producing = true;
            _execute = false;
            
















            if (locked == null) continue; if (localThrowable9 != null) { try { locked.close(); } catch (Throwable localThrowable) {} localThrowable9.addSuppressed(localThrowable); continue; } locked.close(); continue;
          }
          _idle = true;
          











          if (locked == null) break; if (localThrowable9 != null) try { locked.close(); } catch (Throwable localThrowable1) { localThrowable9.addSuppressed(localThrowable1); } locked.close(); break;
        }
        if (!_pending)
        {

          dispatch = this._pending = Invocable.getInvocationType(task) != Invocable.InvocationType.NON_BLOCKING ? 1 : 0;
        }
        
        _execute = false;
      }
      catch (Throwable localThrowable3)
      {
        localThrowable9 = localThrowable3;throw localThrowable3;














      }
      finally
      {














        if (locked != null) if (localThrowable9 != null) try { locked.close(); } catch (Throwable localThrowable4) { localThrowable9.addSuppressed(localThrowable4); } else { locked.close();
          }
      }
      if (dispatch)
      {

        if (LOG.isDebugEnabled())
          LOG.debug("{} dispatch", new Object[] { this });
        if (!execute(this)) {
          task = null;
        }
      }
      
      if (LOG.isDebugEnabled())
        LOG.debug("{} run {}", new Object[] { this, task });
      if (task != null)
        invoke(task);
      if (LOG.isDebugEnabled()) {
        LOG.debug("{} ran {}", new Object[] { this, task });
      }
      
      Locker.Lock locked = _locker.lock();localThrowable9 = null;
      try
      {
        if ((_producing) || (_idle))
        {

          if (locked == null) break; if (localThrowable9 != null) try { locked.close(); } catch (Throwable localThrowable5) { localThrowable9.addSuppressed(localThrowable5); } locked.close(); break;
        }
        _producing = true;
      }
      catch (Throwable localThrowable7)
      {
        localThrowable9 = localThrowable7;throw localThrowable7;

      }
      finally
      {

        if (locked != null) if (localThrowable9 != null) try { locked.close(); } catch (Throwable localThrowable8) { localThrowable9.addSuppressed(localThrowable8); } else locked.close();
      }
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("{} produce exit", new Object[] { this });
    }
  }
  
  public Boolean isIdle() {
    Locker.Lock locked = _locker.lock();Throwable localThrowable3 = null;
    try {
      return Boolean.valueOf(_idle);
    }
    catch (Throwable localThrowable4)
    {
      localThrowable3 = localThrowable4;throw localThrowable4;
    }
    finally {
      if (locked != null) if (localThrowable3 != null) try { locked.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else locked.close();
    }
  }
  
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("EPC ");
    Locker.Lock locked = _locker.lock();Throwable localThrowable3 = null;
    try {
      builder.append(_idle ? "Idle/" : "");
      builder.append(_producing ? "Prod/" : "");
      builder.append(_pending ? "Pend/" : "");
      builder.append(_execute ? "Exec/" : "");
    }
    catch (Throwable localThrowable1)
    {
      localThrowable3 = localThrowable1;throw localThrowable1;

    }
    finally
    {

      if (locked != null) if (localThrowable3 != null) try { locked.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else locked.close(); }
    builder.append(_producer);
    return builder.toString();
  }
  
  private class RunProduce implements Runnable
  {
    private RunProduce() {}
    
    public void run() {
      produce();
    }
  }
  
  public static class Factory implements ExecutionStrategy.Factory
  {
    public Factory() {}
    
    public ExecutionStrategy newExecutionStrategy(ExecutionStrategy.Producer producer, Executor executor) {
      return new ExecuteProduceConsume(producer, executor);
    }
  }
}
