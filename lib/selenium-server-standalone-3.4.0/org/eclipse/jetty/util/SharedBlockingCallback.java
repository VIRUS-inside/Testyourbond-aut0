package org.eclipse.jetty.util;

import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.thread.Invocable.InvocationType;




































public class SharedBlockingCallback
{
  static final Logger LOG = Log.getLogger(SharedBlockingCallback.class);
  
  private static Throwable IDLE = new ConstantThrowable("IDLE");
  private static Throwable SUCCEEDED = new ConstantThrowable("SUCCEEDED");
  
  private static Throwable FAILED = new ConstantThrowable("FAILED");
  
  private final ReentrantLock _lock = new ReentrantLock();
  private final Condition _idle = _lock.newCondition();
  private final Condition _complete = _lock.newCondition();
  private Blocker _blocker = new Blocker();
  
  public SharedBlockingCallback() {}
  
  protected long getIdleTimeout() { return -1L; }
  
  public Blocker acquire()
    throws IOException
  {
    long idle = getIdleTimeout();
    _lock.lock();
    try
    {
      while (_blocker._state != IDLE)
      {
        if ((idle > 0L) && (idle < 4611686018427387903L))
        {

          if (!_idle.await(idle * 2L, TimeUnit.MILLISECONDS)) {
            throw new IOException(new TimeoutException());
          }
        } else
          _idle.await();
      }
      _blocker._state = null;
      return _blocker;
    }
    catch (InterruptedException x)
    {
      throw new InterruptedIOException();
    }
    finally
    {
      _lock.unlock();
    }
  }
  
  protected void notComplete(Blocker blocker)
  {
    LOG.warn("Blocker not complete {}", new Object[] { blocker });
    if (LOG.isDebugEnabled()) {
      LOG.debug(new Throwable());
    }
  }
  
  private static class BlockerTimeoutException
    extends TimeoutException
  {
    private BlockerTimeoutException() {}
  }
  
  public class Blocker implements Callback, Closeable
  {
    private Throwable _state = SharedBlockingCallback.IDLE;
    


    protected Blocker() {}
    

    public Invocable.InvocationType getInvocationType()
    {
      return Invocable.InvocationType.NON_BLOCKING;
    }
    

    public void succeeded()
    {
      _lock.lock();
      try
      {
        if (_state == null)
        {
          _state = SharedBlockingCallback.SUCCEEDED;
          _complete.signalAll();
        }
        else {
          throw new IllegalStateException(_state);
        }
        

        _lock.unlock(); } finally { _lock.unlock();
      }
    }
    

    public void failed(Throwable cause)
    {
      _lock.lock();
      try
      {
        if (_state == null)
        {
          if (cause == null) {
            _state = SharedBlockingCallback.FAILED;
          } else if ((cause instanceof SharedBlockingCallback.BlockerTimeoutException))
          {
            _state = new IOException(cause);
          } else
            _state = cause;
          _complete.signalAll();
        }
        else if (!(_state instanceof SharedBlockingCallback.BlockerTimeoutException))
        {





          throw new IllegalStateException(_state);
        }
        


        _lock.unlock(); } finally { _lock.unlock();
      }
    }
    






    public void block()
      throws IOException
    {
      long idle = getIdleTimeout();
      _lock.lock();
      try
      {
        while (_state == null)
        {
          if (idle > 0L)
          {



            long excess = Math.min(idle / 2L, 1000L);
            if (!_complete.await(idle + excess, TimeUnit.MILLISECONDS))
            {


              _state = new SharedBlockingCallback.BlockerTimeoutException(null);
            }
          }
          else
          {
            _complete.await();
          }
        }
        
        if (_state == SharedBlockingCallback.SUCCEEDED)
          return;
        if (_state == SharedBlockingCallback.IDLE)
          throw new IllegalStateException("IDLE");
        if ((_state instanceof IOException))
          throw ((IOException)_state);
        if ((_state instanceof CancellationException))
          throw ((CancellationException)_state);
        if ((_state instanceof RuntimeException))
          throw ((RuntimeException)_state);
        if ((_state instanceof Error))
          throw ((Error)_state);
        throw new IOException(_state);
      }
      catch (InterruptedException e)
      {
        throw new InterruptedIOException();
      }
      finally
      {
        _lock.unlock();
      }
    }
    
    /* Error */
    public void close()
    {
      // Byte code:
      //   0: aload_0
      //   1: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
      //   4: invokestatic 6	org/eclipse/jetty/util/SharedBlockingCallback:access$200	(Lorg/eclipse/jetty/util/SharedBlockingCallback;)Ljava/util/concurrent/locks/ReentrantLock;
      //   7: invokevirtual 7	java/util/concurrent/locks/ReentrantLock:lock	()V
      //   10: aload_0
      //   11: getfield 1	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:_state	Ljava/lang/Throwable;
      //   14: invokestatic 4	org/eclipse/jetty/util/SharedBlockingCallback:access$100	()Ljava/lang/Throwable;
      //   17: if_acmpne +13 -> 30
      //   20: new 11	java/lang/IllegalStateException
      //   23: dup
      //   24: ldc 28
      //   26: invokespecial 29	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
      //   29: athrow
      //   30: aload_0
      //   31: getfield 1	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:_state	Ljava/lang/Throwable;
      //   34: ifnonnull +11 -> 45
      //   37: aload_0
      //   38: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
      //   41: aload_0
      //   42: invokevirtual 36	org/eclipse/jetty/util/SharedBlockingCallback:notComplete	(Lorg/eclipse/jetty/util/SharedBlockingCallback$Blocker;)V
      //   45: aload_0
      //   46: getfield 1	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:_state	Ljava/lang/Throwable;
      //   49: instanceof 15
      //   52: ifeq +25 -> 77
      //   55: aload_0
      //   56: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
      //   59: new 37	org/eclipse/jetty/util/SharedBlockingCallback$Blocker
      //   62: dup
      //   63: aload_0
      //   64: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
      //   67: invokespecial 38	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:<init>	(Lorg/eclipse/jetty/util/SharedBlockingCallback;)V
      //   70: invokestatic 39	org/eclipse/jetty/util/SharedBlockingCallback:access$702	(Lorg/eclipse/jetty/util/SharedBlockingCallback;Lorg/eclipse/jetty/util/SharedBlockingCallback$Blocker;)Lorg/eclipse/jetty/util/SharedBlockingCallback$Blocker;
      //   73: pop
      //   74: goto +10 -> 84
      //   77: aload_0
      //   78: invokestatic 4	org/eclipse/jetty/util/SharedBlockingCallback:access$100	()Ljava/lang/Throwable;
      //   81: putfield 1	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:_state	Ljava/lang/Throwable;
      //   84: aload_0
      //   85: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
      //   88: invokestatic 40	org/eclipse/jetty/util/SharedBlockingCallback:access$800	(Lorg/eclipse/jetty/util/SharedBlockingCallback;)Ljava/util/concurrent/locks/Condition;
      //   91: invokeinterface 10 1 0
      //   96: aload_0
      //   97: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
      //   100: invokestatic 9	org/eclipse/jetty/util/SharedBlockingCallback:access$400	(Lorg/eclipse/jetty/util/SharedBlockingCallback;)Ljava/util/concurrent/locks/Condition;
      //   103: invokeinterface 10 1 0
      //   108: aload_0
      //   109: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
      //   112: invokestatic 6	org/eclipse/jetty/util/SharedBlockingCallback:access$200	(Lorg/eclipse/jetty/util/SharedBlockingCallback;)Ljava/util/concurrent/locks/ReentrantLock;
      //   115: invokevirtual 13	java/util/concurrent/locks/ReentrantLock:unlock	()V
      //   118: goto +16 -> 134
      //   121: astore_1
      //   122: aload_0
      //   123: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
      //   126: invokestatic 6	org/eclipse/jetty/util/SharedBlockingCallback:access$200	(Lorg/eclipse/jetty/util/SharedBlockingCallback;)Ljava/util/concurrent/locks/ReentrantLock;
      //   129: invokevirtual 13	java/util/concurrent/locks/ReentrantLock:unlock	()V
      //   132: aload_1
      //   133: athrow
      //   134: goto +95 -> 229
      //   137: astore_2
      //   138: aload_0
      //   139: getfield 1	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:_state	Ljava/lang/Throwable;
      //   142: instanceof 15
      //   145: ifeq +25 -> 170
      //   148: aload_0
      //   149: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
      //   152: new 37	org/eclipse/jetty/util/SharedBlockingCallback$Blocker
      //   155: dup
      //   156: aload_0
      //   157: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
      //   160: invokespecial 38	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:<init>	(Lorg/eclipse/jetty/util/SharedBlockingCallback;)V
      //   163: invokestatic 39	org/eclipse/jetty/util/SharedBlockingCallback:access$702	(Lorg/eclipse/jetty/util/SharedBlockingCallback;Lorg/eclipse/jetty/util/SharedBlockingCallback$Blocker;)Lorg/eclipse/jetty/util/SharedBlockingCallback$Blocker;
      //   166: pop
      //   167: goto +10 -> 177
      //   170: aload_0
      //   171: invokestatic 4	org/eclipse/jetty/util/SharedBlockingCallback:access$100	()Ljava/lang/Throwable;
      //   174: putfield 1	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:_state	Ljava/lang/Throwable;
      //   177: aload_0
      //   178: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
      //   181: invokestatic 40	org/eclipse/jetty/util/SharedBlockingCallback:access$800	(Lorg/eclipse/jetty/util/SharedBlockingCallback;)Ljava/util/concurrent/locks/Condition;
      //   184: invokeinterface 10 1 0
      //   189: aload_0
      //   190: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
      //   193: invokestatic 9	org/eclipse/jetty/util/SharedBlockingCallback:access$400	(Lorg/eclipse/jetty/util/SharedBlockingCallback;)Ljava/util/concurrent/locks/Condition;
      //   196: invokeinterface 10 1 0
      //   201: aload_0
      //   202: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
      //   205: invokestatic 6	org/eclipse/jetty/util/SharedBlockingCallback:access$200	(Lorg/eclipse/jetty/util/SharedBlockingCallback;)Ljava/util/concurrent/locks/ReentrantLock;
      //   208: invokevirtual 13	java/util/concurrent/locks/ReentrantLock:unlock	()V
      //   211: goto +16 -> 227
      //   214: astore_3
      //   215: aload_0
      //   216: getfield 2	org/eclipse/jetty/util/SharedBlockingCallback$Blocker:this$0	Lorg/eclipse/jetty/util/SharedBlockingCallback;
      //   219: invokestatic 6	org/eclipse/jetty/util/SharedBlockingCallback:access$200	(Lorg/eclipse/jetty/util/SharedBlockingCallback;)Ljava/util/concurrent/locks/ReentrantLock;
      //   222: invokevirtual 13	java/util/concurrent/locks/ReentrantLock:unlock	()V
      //   225: aload_3
      //   226: athrow
      //   227: aload_2
      //   228: athrow
      //   229: return
      // Line number table:
      //   Java source line #244	-> byte code offset #0
      //   Java source line #247	-> byte code offset #10
      //   Java source line #248	-> byte code offset #20
      //   Java source line #249	-> byte code offset #30
      //   Java source line #250	-> byte code offset #37
      //   Java source line #257	-> byte code offset #45
      //   Java source line #259	-> byte code offset #55
      //   Java source line #262	-> byte code offset #77
      //   Java source line #263	-> byte code offset #84
      //   Java source line #264	-> byte code offset #96
      //   Java source line #268	-> byte code offset #108
      //   Java source line #269	-> byte code offset #118
      //   Java source line #268	-> byte code offset #121
      //   Java source line #270	-> byte code offset #134
      //   Java source line #254	-> byte code offset #137
      //   Java source line #257	-> byte code offset #138
      //   Java source line #259	-> byte code offset #148
      //   Java source line #262	-> byte code offset #170
      //   Java source line #263	-> byte code offset #177
      //   Java source line #264	-> byte code offset #189
      //   Java source line #268	-> byte code offset #201
      //   Java source line #269	-> byte code offset #211
      //   Java source line #268	-> byte code offset #214
      //   Java source line #271	-> byte code offset #229
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	230	0	this	Blocker
      //   121	12	1	localObject1	Object
      //   137	91	2	localObject2	Object
      //   214	12	3	localObject3	Object
      // Exception table:
      //   from	to	target	type
      //   45	108	121	finally
      //   10	45	137	finally
      //   138	201	214	finally
    }
    
    public String toString()
    {
      _lock.lock();
      try
      {
        return String.format("%s@%x{%s}", new Object[] { Blocker.class.getSimpleName(), Integer.valueOf(hashCode()), _state });
      }
      finally
      {
        _lock.unlock();
      }
    }
  }
}
