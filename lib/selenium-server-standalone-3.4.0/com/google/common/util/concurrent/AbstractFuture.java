package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtCompatible;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.locks.LockSupport;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import sun.misc.Unsafe;







































@GwtCompatible(emulated=true)
public abstract class AbstractFuture<V>
  implements ListenableFuture<V>
{
  private static final boolean GENERATE_CANCELLATION_CAUSES = Boolean.parseBoolean(
    System.getProperty("guava.concurrent.generate_cancellation_cause", "false"));
  
  static abstract class TrustedFuture<V>
    extends AbstractFuture<V>
  {
    TrustedFuture() {}
    
    @CanIgnoreReturnValue
    public final V get() throws InterruptedException, ExecutionException
    {
      return super.get();
    }
    
    @CanIgnoreReturnValue
    public final V get(long timeout, TimeUnit unit)
      throws InterruptedException, ExecutionException, TimeoutException
    {
      return super.get(timeout, unit);
    }
    
    public final boolean isDone()
    {
      return super.isDone();
    }
    
    public final boolean isCancelled()
    {
      return super.isCancelled();
    }
    
    public final void addListener(Runnable listener, Executor executor)
    {
      super.addListener(listener, executor);
    }
    
    @CanIgnoreReturnValue
    public final boolean cancel(boolean mayInterruptIfRunning)
    {
      return super.cancel(mayInterruptIfRunning);
    }
  }
  

  private static final Logger log = Logger.getLogger(AbstractFuture.class.getName());
  
  private static final long SPIN_THRESHOLD_NANOS = 1000L;
  
  private static final AtomicHelper ATOMIC_HELPER;
  

  static
  {
    AtomicHelper helper;
    try
    {
      helper = new UnsafeAtomicHelper(null);
    }
    catch (Throwable unsafeFailure)
    {
      try
      {
        AtomicHelper helper;
        




        helper = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Thread.class, "thread"), AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Waiter.class, "next"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Waiter.class, "waiters"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Listener.class, "listeners"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Object.class, "value"));
      }
      catch (Throwable atomicReferenceFieldUpdaterFailure)
      {
        AtomicHelper helper;
        
        log.log(Level.SEVERE, "UnsafeAtomicHelper is broken!", unsafeFailure);
        log.log(Level.SEVERE, "SafeAtomicHelper is broken!", atomicReferenceFieldUpdaterFailure);
        helper = new SynchronizedHelper(null);
      }
    }
    ATOMIC_HELPER = helper;
    



    unsafeFailure = LockSupport.class;
  }
  


  private static final class Waiter
  {
    static final Waiter TOMBSTONE = new Waiter(false);
    
    @Nullable
    volatile Thread thread;
    
    @Nullable
    volatile Waiter next;
    

    Waiter(boolean unused) {}
    
    Waiter()
    {
      AbstractFuture.ATOMIC_HELPER.putThread(this, Thread.currentThread());
    }
    

    void setNext(Waiter next)
    {
      AbstractFuture.ATOMIC_HELPER.putNext(this, next);
    }
    


    void unpark()
    {
      Thread w = thread;
      if (w != null) {
        thread = null;
        LockSupport.unpark(w);
      }
    }
  }
  









  private void removeWaiter(Waiter node)
  {
    thread = null;
    

    Waiter pred = null;
    Waiter curr = waiters;
    if (curr == Waiter.TOMBSTONE) {
      return;
    }
    for (;;) {
      if (curr == null) return;
      Waiter succ = next;
      if (thread != null) {
        pred = curr;
      } else { if (pred != null) {
          next = succ;
          if (thread != null) break label78;
          break;
        }
        if (!ATOMIC_HELPER.casWaiters(this, curr, succ)) break;
      }
      label78:
      curr = succ;
    }
  }
  


  private static final class Listener
  {
    static final Listener TOMBSTONE = new Listener(null, null);
    final Runnable task;
    final Executor executor;
    @Nullable
    Listener next;
    
    Listener(Runnable task, Executor executor)
    {
      this.task = task;
      this.executor = executor;
    }
  }
  

  private static final Object NULL = new Object();
  private volatile Object value;
  
  private static final class Failure {
    static final Failure FALLBACK_INSTANCE = new Failure(new Throwable("Failure occurred while trying to finish a future.")
    {

      public synchronized Throwable fillInStackTrace()
      {
        return this;
      }
    });
    


    final Throwable exception;
    



    Failure(Throwable exception)
    {
      this.exception = ((Throwable)Preconditions.checkNotNull(exception));
    }
  }
  
  private static final class Cancellation {
    final boolean wasInterrupted;
    @Nullable
    final Throwable cause;
    
    Cancellation(boolean wasInterrupted, @Nullable Throwable cause) {
      this.wasInterrupted = wasInterrupted;
      this.cause = cause;
    }
  }
  
  private static final class SetFuture<V> implements Runnable
  {
    final AbstractFuture<V> owner;
    final ListenableFuture<? extends V> future;
    
    SetFuture(AbstractFuture<V> owner, ListenableFuture<? extends V> future) {
      this.owner = owner;
      this.future = future;
    }
    
    public void run()
    {
      if (owner.value != this)
      {
        return;
      }
      Object valueToSet = AbstractFuture.getFutureValue(future);
      if (AbstractFuture.ATOMIC_HELPER.casValue(owner, this, valueToSet)) {
        AbstractFuture.complete(owner);
      }
    }
  }
  























  private volatile Listener listeners;
  






















  private volatile Waiter waiters;
  





















  @CanIgnoreReturnValue
  public V get(long timeout, TimeUnit unit)
    throws InterruptedException, TimeoutException, ExecutionException
  {
    long remainingNanos = unit.toNanos(timeout);
    if (Thread.interrupted()) {
      throw new InterruptedException();
    }
    Object localValue = value;
    if (((localValue != null ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) != 0) {
      return getDoneValue(localValue);
    }
    
    long endNanos = remainingNanos > 0L ? System.nanoTime() + remainingNanos : 0L;
    
    if (remainingNanos >= 1000L) {
      Waiter oldHead = waiters;
      if (oldHead != Waiter.TOMBSTONE) {
        Waiter node = new Waiter();
        do {
          node.setNext(oldHead);
          if (ATOMIC_HELPER.casWaiters(this, oldHead, node)) {
            do {
              LockSupport.parkNanos(this, remainingNanos);
              
              if (Thread.interrupted()) {
                removeWaiter(node);
                throw new InterruptedException();
              }
              


              localValue = value;
              if (((localValue != null ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) != 0) {
                return getDoneValue(localValue);
              }
              

              remainingNanos = endNanos - System.nanoTime();
            } while (remainingNanos >= 1000L);
            
            removeWaiter(node);
            break;
          }
          

          oldHead = waiters;
        } while (oldHead != Waiter.TOMBSTONE);
      }
      

      return getDoneValue(value);
    }
    

    while (remainingNanos > 0L) {
      localValue = value;
      if (((localValue != null ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) != 0) {
        return getDoneValue(localValue);
      }
      if (Thread.interrupted()) {
        throw new InterruptedException();
      }
      remainingNanos = endNanos - System.nanoTime();
    }
    throw new TimeoutException();
  }
  















  @CanIgnoreReturnValue
  public V get()
    throws InterruptedException, ExecutionException
  {
    if (Thread.interrupted()) {
      throw new InterruptedException();
    }
    Object localValue = value;
    if (((localValue != null ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) != 0) {
      return getDoneValue(localValue);
    }
    Waiter oldHead = waiters;
    if (oldHead != Waiter.TOMBSTONE) {
      Waiter node = new Waiter();
      do {
        node.setNext(oldHead);
        if (ATOMIC_HELPER.casWaiters(this, oldHead, node))
        {
          do {
            LockSupport.park(this);
            
            if (Thread.interrupted()) {
              removeWaiter(node);
              throw new InterruptedException();
            }
            

            localValue = value;
          } while (((localValue != null ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0)) == 0);
          return getDoneValue(localValue);
        }
        

        oldHead = waiters;
      } while (oldHead != Waiter.TOMBSTONE);
    }
    

    return getDoneValue(value);
  }
  



  private V getDoneValue(Object obj)
    throws ExecutionException
  {
    if ((obj instanceof Cancellation))
      throw cancellationExceptionWithCause("Task was cancelled.", cause);
    if ((obj instanceof Failure))
      throw new ExecutionException(exception);
    if (obj == NULL) {
      return null;
    }
    
    V asV = obj;
    return asV;
  }
  

  public boolean isDone()
  {
    Object localValue = value;
    return (localValue != null ? 1 : 0) & (!(localValue instanceof SetFuture) ? 1 : 0);
  }
  
  public boolean isCancelled()
  {
    Object localValue = value;
    return localValue instanceof Cancellation;
  }
  







  @CanIgnoreReturnValue
  public boolean cancel(boolean mayInterruptIfRunning)
  {
    Object localValue = value;
    boolean rValue = false;
    if ((localValue == null | localValue instanceof SetFuture))
    {

      Throwable cause = GENERATE_CANCELLATION_CAUSES ? new CancellationException("Future.cancel() was called.") : null;
      


      Object valueToSet = new Cancellation(mayInterruptIfRunning, cause);
      AbstractFuture<?> abstractFuture = this;
      for (;;) {
        if (ATOMIC_HELPER.casValue(abstractFuture, localValue, valueToSet)) {
          rValue = true;
          

          if (mayInterruptIfRunning) {
            abstractFuture.interruptTask();
          }
          complete(abstractFuture);
          if ((localValue instanceof SetFuture))
          {

            ListenableFuture<?> futureToPropagateTo = future;
            
            if ((futureToPropagateTo instanceof TrustedFuture))
            {






              AbstractFuture<?> trusted = (AbstractFuture)futureToPropagateTo;
              localValue = value;
              if ((localValue == null | localValue instanceof SetFuture)) {
                abstractFuture = trusted;
                continue;
              }
            }
            else {
              futureToPropagateTo.cancel(mayInterruptIfRunning);
            }
          }
        }
        else
        {
          localValue = value;
          if (!(localValue instanceof SetFuture)) {
            break;
          }
        }
      }
    }
    

    return rValue;
  }
  















  protected final boolean wasInterrupted()
  {
    Object localValue = value;
    return ((localValue instanceof Cancellation)) && (wasInterrupted);
  }
  





  public void addListener(Runnable listener, Executor executor)
  {
    Preconditions.checkNotNull(listener, "Runnable was null.");
    Preconditions.checkNotNull(executor, "Executor was null.");
    Listener oldHead = listeners;
    if (oldHead != Listener.TOMBSTONE) {
      Listener newNode = new Listener(listener, executor);
      do {
        next = oldHead;
        if (ATOMIC_HELPER.casListeners(this, oldHead, newNode)) {
          return;
        }
        oldHead = listeners;
      } while (oldHead != Listener.TOMBSTONE);
    }
    

    executeListener(listener, executor);
  }
  











  @CanIgnoreReturnValue
  protected boolean set(@Nullable V value)
  {
    Object valueToSet = value == null ? NULL : value;
    if (ATOMIC_HELPER.casValue(this, null, valueToSet)) {
      complete(this);
      return true;
    }
    return false;
  }
  











  @CanIgnoreReturnValue
  protected boolean setException(Throwable throwable)
  {
    Object valueToSet = new Failure((Throwable)Preconditions.checkNotNull(throwable));
    if (ATOMIC_HELPER.casValue(this, null, valueToSet)) {
      complete(this);
      return true;
    }
    return false;
  }
  




















  @Beta
  @CanIgnoreReturnValue
  protected boolean setFuture(ListenableFuture<? extends V> future)
  {
    Preconditions.checkNotNull(future);
    Object localValue = this.value;
    if (localValue == null) {
      if (future.isDone()) {
        Object value = getFutureValue(future);
        if (ATOMIC_HELPER.casValue(this, null, value)) {
          complete(this);
          return true;
        }
        return false;
      }
      SetFuture valueToSet = new SetFuture(this, future);
      if (ATOMIC_HELPER.casValue(this, null, valueToSet))
      {
        try
        {
          future.addListener(valueToSet, MoreExecutors.directExecutor());
        }
        catch (Throwable t)
        {
          Failure failure;
          try
          {
            failure = new Failure(t);
          } catch (Throwable oomMostLikely) { Failure failure;
            failure = Failure.FALLBACK_INSTANCE;
          }
          
          oomMostLikely = ATOMIC_HELPER.casValue(this, valueToSet, failure);
        }
        return true;
      }
      localValue = this.value;
    }
    

    if ((localValue instanceof Cancellation))
    {
      future.cancel(wasInterrupted);
    }
    return false;
  }
  






  private static Object getFutureValue(ListenableFuture<?> future)
  {
    if ((future instanceof TrustedFuture))
    {



      return value;
    }
    Object valueToSet;
    try {
      Object v = Futures.getDone(future);
      valueToSet = v == null ? NULL : v;
    } catch (ExecutionException exception) { Object valueToSet;
      valueToSet = new Failure(exception.getCause());
    } catch (CancellationException cancellation) { Object valueToSet;
      valueToSet = new Cancellation(false, cancellation);
    } catch (Throwable t) { Object valueToSet;
      valueToSet = new Failure(t);
    }
    
    return valueToSet;
  }
  
  private static void complete(AbstractFuture<?> future)
  {
    Listener next = null;
    
    future.releaseWaiters();
    



    future.afterDone();
    
    next = future.clearListeners(next);
    future = null;
    label100: for (;;) { if (next == null) return;
      Listener curr = next;
      next = next;
      Runnable task = task;
      if ((task instanceof SetFuture)) {
        SetFuture<?> setFuture = (SetFuture)task;
        




        future = owner;
        if (value == setFuture) {
          Object valueToSet = getFutureValue(future);
          if (ATOMIC_HELPER.casValue(future, setFuture, valueToSet)) {
            break;
          }
        }
        break label100;
      }
      executeListener(task, executor);
    }
  }
  























  final Throwable trustedGetException()
  {
    return value).exception;
  }
  






  final void maybePropagateCancellation(@Nullable Future<?> related)
  {
    if ((related != null & isCancelled())) {
      related.cancel(wasInterrupted());
    }
  }
  
  private void releaseWaiters()
  {
    Waiter head;
    do {
      head = waiters;
    } while (!ATOMIC_HELPER.casWaiters(this, head, Waiter.TOMBSTONE));
    for (Waiter currentWaiter = head; 
        currentWaiter != null; 
        currentWaiter = next) {
      currentWaiter.unpark();
    }
  }
  




  private Listener clearListeners(Listener onto)
  {
    Listener head;
    



    do
    {
      head = listeners;
    } while (!ATOMIC_HELPER.casListeners(this, head, Listener.TOMBSTONE));
    Listener reversedList = onto;
    while (head != null) {
      Listener tmp = head;
      head = next;
      next = reversedList;
      reversedList = tmp;
    }
    return reversedList;
  }
  


  private static void executeListener(Runnable runnable, Executor executor)
  {
    try
    {
      executor.execute(runnable);

    }
    catch (RuntimeException e)
    {
      log.log(Level.SEVERE, "RuntimeException while executing runnable " + runnable + " with executor " + executor, e);
    }
  }
  
  private static abstract class AtomicHelper
  {
    private AtomicHelper() {}
    
    abstract void putThread(AbstractFuture.Waiter paramWaiter, Thread paramThread);
    
    abstract void putNext(AbstractFuture.Waiter paramWaiter1, AbstractFuture.Waiter paramWaiter2);
    
    abstract boolean casWaiters(AbstractFuture<?> paramAbstractFuture, AbstractFuture.Waiter paramWaiter1, AbstractFuture.Waiter paramWaiter2);
    
    abstract boolean casListeners(AbstractFuture<?> paramAbstractFuture, AbstractFuture.Listener paramListener1, AbstractFuture.Listener paramListener2);
    
    abstract boolean casValue(AbstractFuture<?> paramAbstractFuture, Object paramObject1, Object paramObject2);
  }
  
  private static final class UnsafeAtomicHelper extends AbstractFuture.AtomicHelper
  {
    static final Unsafe UNSAFE;
    static final long LISTENERS_OFFSET;
    static final long WAITERS_OFFSET;
    static final long VALUE_OFFSET;
    static final long WAITER_THREAD_OFFSET;
    static final long WAITER_NEXT_OFFSET;
    
    private UnsafeAtomicHelper()
    {
      super();
    }
    




    static
    {
      Unsafe unsafe = null;
      try {
        unsafe = Unsafe.getUnsafe();
      }
      catch (SecurityException tryReflectionInstead) {
        try {
          unsafe = (Unsafe)AccessController.doPrivileged(new PrivilegedExceptionAction()
          {
            public Unsafe run() throws Exception
            {
              Class<Unsafe> k = Unsafe.class;
              for (Field f : k.getDeclaredFields()) {
                f.setAccessible(true);
                Object x = f.get(null);
                if (k.isInstance(x)) {
                  return (Unsafe)k.cast(x);
                }
              }
              throw new NoSuchFieldError("the Unsafe");
            }
          });
        } catch (PrivilegedActionException e) {
          throw new RuntimeException("Could not initialize intrinsics", e.getCause());
        }
      }
      try {
        Class<?> abstractFuture = AbstractFuture.class;
        WAITERS_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("waiters"));
        LISTENERS_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("listeners"));
        VALUE_OFFSET = unsafe.objectFieldOffset(abstractFuture.getDeclaredField("value"));
        WAITER_THREAD_OFFSET = unsafe.objectFieldOffset(AbstractFuture.Waiter.class.getDeclaredField("thread"));
        WAITER_NEXT_OFFSET = unsafe.objectFieldOffset(AbstractFuture.Waiter.class.getDeclaredField("next"));
        UNSAFE = unsafe;
      } catch (Exception e) {
        Throwables.throwIfUnchecked(e);
        throw new RuntimeException(e);
      }
    }
    
    void putThread(AbstractFuture.Waiter waiter, Thread newValue)
    {
      UNSAFE.putObject(waiter, WAITER_THREAD_OFFSET, newValue);
    }
    
    void putNext(AbstractFuture.Waiter waiter, AbstractFuture.Waiter newValue)
    {
      UNSAFE.putObject(waiter, WAITER_NEXT_OFFSET, newValue);
    }
    

    boolean casWaiters(AbstractFuture<?> future, AbstractFuture.Waiter expect, AbstractFuture.Waiter update)
    {
      return UNSAFE.compareAndSwapObject(future, WAITERS_OFFSET, expect, update);
    }
    

    boolean casListeners(AbstractFuture<?> future, AbstractFuture.Listener expect, AbstractFuture.Listener update)
    {
      return UNSAFE.compareAndSwapObject(future, LISTENERS_OFFSET, expect, update);
    }
    

    boolean casValue(AbstractFuture<?> future, Object expect, Object update)
    {
      return UNSAFE.compareAndSwapObject(future, VALUE_OFFSET, expect, update);
    }
  }
  

  private static final class SafeAtomicHelper
    extends AbstractFuture.AtomicHelper
  {
    final AtomicReferenceFieldUpdater<AbstractFuture.Waiter, Thread> waiterThreadUpdater;
    
    final AtomicReferenceFieldUpdater<AbstractFuture.Waiter, AbstractFuture.Waiter> waiterNextUpdater;
    final AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture.Waiter> waitersUpdater;
    final AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture.Listener> listenersUpdater;
    final AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater;
    
    SafeAtomicHelper(AtomicReferenceFieldUpdater<AbstractFuture.Waiter, Thread> waiterThreadUpdater, AtomicReferenceFieldUpdater<AbstractFuture.Waiter, AbstractFuture.Waiter> waiterNextUpdater, AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture.Waiter> waitersUpdater, AtomicReferenceFieldUpdater<AbstractFuture, AbstractFuture.Listener> listenersUpdater, AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater)
    {
      super();
      this.waiterThreadUpdater = waiterThreadUpdater;
      this.waiterNextUpdater = waiterNextUpdater;
      this.waitersUpdater = waitersUpdater;
      this.listenersUpdater = listenersUpdater;
      this.valueUpdater = valueUpdater;
    }
    
    void putThread(AbstractFuture.Waiter waiter, Thread newValue)
    {
      waiterThreadUpdater.lazySet(waiter, newValue);
    }
    
    void putNext(AbstractFuture.Waiter waiter, AbstractFuture.Waiter newValue)
    {
      waiterNextUpdater.lazySet(waiter, newValue);
    }
    
    boolean casWaiters(AbstractFuture<?> future, AbstractFuture.Waiter expect, AbstractFuture.Waiter update)
    {
      return waitersUpdater.compareAndSet(future, expect, update);
    }
    
    boolean casListeners(AbstractFuture<?> future, AbstractFuture.Listener expect, AbstractFuture.Listener update)
    {
      return listenersUpdater.compareAndSet(future, expect, update);
    }
    
    boolean casValue(AbstractFuture<?> future, Object expect, Object update)
    {
      return valueUpdater.compareAndSet(future, expect, update);
    }
  }
  

  private static final class SynchronizedHelper
    extends AbstractFuture.AtomicHelper
  {
    private SynchronizedHelper()
    {
      super();
    }
    
    void putThread(AbstractFuture.Waiter waiter, Thread newValue) { thread = newValue; }
    

    void putNext(AbstractFuture.Waiter waiter, AbstractFuture.Waiter newValue)
    {
      next = newValue;
    }
    
    boolean casWaiters(AbstractFuture<?> future, AbstractFuture.Waiter expect, AbstractFuture.Waiter update)
    {
      synchronized (future) {
        if (waiters == expect) {
          waiters = update;
          return true;
        }
        return false;
      }
    }
    
    boolean casListeners(AbstractFuture<?> future, AbstractFuture.Listener expect, AbstractFuture.Listener update)
    {
      synchronized (future) {
        if (listeners == expect) {
          listeners = update;
          return true;
        }
        return false;
      }
    }
    
    boolean casValue(AbstractFuture<?> future, Object expect, Object update)
    {
      synchronized (future) {
        if (value == expect) {
          value = update;
          return true;
        }
        return false;
      }
    }
  }
  
  private static CancellationException cancellationExceptionWithCause(@Nullable String message, @Nullable Throwable cause)
  {
    CancellationException exception = new CancellationException(message);
    exception.initCause(cause);
    return exception;
  }
  
  protected AbstractFuture() {}
  
  protected void interruptTask() {}
  
  @Beta
  protected void afterDone() {}
}
