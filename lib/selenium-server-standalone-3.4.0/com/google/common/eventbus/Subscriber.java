package com.google.common.eventbus;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.j2objc.annotations.Weak;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import javax.annotation.Nullable;




















class Subscriber
{
  @Weak
  private EventBus bus;
  @VisibleForTesting
  final Object target;
  private final Method method;
  private final Executor executor;
  
  static Subscriber create(EventBus bus, Object listener, Method method)
  {
    return isDeclaredThreadSafe(method) ? new Subscriber(bus, listener, method) : new SynchronizedSubscriber(bus, listener, method, null);
  }
  













  private Subscriber(EventBus bus, Object target, Method method)
  {
    this.bus = bus;
    this.target = Preconditions.checkNotNull(target);
    this.method = method;
    method.setAccessible(true);
    
    executor = bus.executor();
  }
  


  final void dispatchEvent(final Object event)
  {
    executor.execute(new Runnable()
    {
      public void run()
      {
        try {
          invokeSubscriberMethod(event);
        } catch (InvocationTargetException e) {
          bus.handleSubscriberException(e.getCause(), Subscriber.this.context(event));
        }
      }
    });
  }
  

  @VisibleForTesting
  void invokeSubscriberMethod(Object event)
    throws InvocationTargetException
  {
    try
    {
      method.invoke(target, new Object[] { Preconditions.checkNotNull(event) });
    } catch (IllegalArgumentException e) {
      throw new Error("Method rejected target/argument: " + event, e);
    } catch (IllegalAccessException e) {
      throw new Error("Method became inaccessible: " + event, e);
    } catch (InvocationTargetException e) {
      if ((e.getCause() instanceof Error)) {
        throw ((Error)e.getCause());
      }
      throw e;
    }
  }
  


  private SubscriberExceptionContext context(Object event)
  {
    return new SubscriberExceptionContext(bus, event, target, method);
  }
  
  public final int hashCode()
  {
    return (31 + method.hashCode()) * 31 + System.identityHashCode(target);
  }
  
  public final boolean equals(@Nullable Object obj)
  {
    if ((obj instanceof Subscriber)) {
      Subscriber that = (Subscriber)obj;
      


      return (target == target) && (method.equals(method));
    }
    return false;
  }
  



  private static boolean isDeclaredThreadSafe(Method method)
  {
    return method.getAnnotation(AllowConcurrentEvents.class) != null;
  }
  


  @VisibleForTesting
  static final class SynchronizedSubscriber
    extends Subscriber
  {
    private SynchronizedSubscriber(EventBus bus, Object target, Method method)
    {
      super(target, method, null);
    }
    
    void invokeSubscriberMethod(Object event) throws InvocationTargetException
    {
      synchronized (this) {
        super.invokeSubscriberMethod(event);
      }
    }
  }
}
