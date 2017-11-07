package org.eclipse.jetty.util.thread.strategy;

import java.io.Closeable;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.thread.ExecutionStrategy;
import org.eclipse.jetty.util.thread.Invocable;
import org.eclipse.jetty.util.thread.Invocable.InvocationType;
























public abstract class ExecutingExecutionStrategy
  implements ExecutionStrategy
{
  private static final Logger LOG = Log.getLogger(ExecutingExecutionStrategy.class);
  
  private final Executor _executor;
  private final Invocable.InvocationType _preferredInvocationType;
  
  protected ExecutingExecutionStrategy(Executor executor, Invocable.InvocationType preferred)
  {
    _executor = executor;
    _preferredInvocationType = preferred;
  }
  
  public Invocable.InvocationType getPreferredInvocationType()
  {
    return _preferredInvocationType;
  }
  
  public void invoke(Runnable task)
  {
    if (LOG.isDebugEnabled())
      LOG.debug("{} invoke  {}", new Object[] { this, task });
    Invocable.invokePreferred(task, _preferredInvocationType);
    if (LOG.isDebugEnabled()) {
      LOG.debug("{} invoked {}", new Object[] { this, task });
    }
  }
  
  protected boolean execute(Runnable task)
  {
    try {
      _executor.execute(Invocable.asPreferred(task, _preferredInvocationType));
      return true;

    }
    catch (RejectedExecutionException e)
    {
      LOG.debug(e);
      LOG.warn("Rejected execution of {}", new Object[] { task });
      try
      {
        if ((task instanceof Closeable)) {
          ((Closeable)task).close();
        }
      }
      catch (Exception x) {
        e.addSuppressed(x);
        LOG.warn(e);
      }
    }
    return false;
  }
}
