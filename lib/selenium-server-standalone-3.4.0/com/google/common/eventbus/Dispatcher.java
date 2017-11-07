package com.google.common.eventbus;

import com.google.common.base.Preconditions;
import com.google.common.collect.Queues;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

































abstract class Dispatcher
{
  Dispatcher() {}
  
  static Dispatcher perThreadDispatchQueue()
  {
    return new PerThreadQueuedDispatcher(null);
  }
  





  static Dispatcher legacyAsync()
  {
    return new LegacyAsyncDispatcher(null);
  }
  




  static Dispatcher immediate()
  {
    return ImmediateDispatcher.INSTANCE;
  }
  





  abstract void dispatch(Object paramObject, Iterator<Subscriber> paramIterator);
  





  private static final class PerThreadQueuedDispatcher
    extends Dispatcher
  {
    private final ThreadLocal<Queue<Event>> queue = new ThreadLocal()
    {
      protected Queue<Dispatcher.PerThreadQueuedDispatcher.Event> initialValue()
      {
        return Queues.newArrayDeque();
      }
    };
    



    private final ThreadLocal<Boolean> dispatching = new ThreadLocal()
    {

      protected Boolean initialValue() {
        return Boolean.valueOf(false); }
    };
    
    private PerThreadQueuedDispatcher() {}
    
    void dispatch(Object event, Iterator<Subscriber> subscribers) {
      Preconditions.checkNotNull(event);
      Preconditions.checkNotNull(subscribers);
      Queue<Event> queueForThread = (Queue)queue.get();
      queueForThread.offer(new Event(event, subscribers, null));
      
      if (!((Boolean)dispatching.get()).booleanValue()) {
        dispatching.set(Boolean.valueOf(true));
        try {
          Event nextEvent;
          if ((nextEvent = (Event)queueForThread.poll()) != null) {
            while (subscribers.hasNext()) {
              ((Subscriber)subscribers.next()).dispatchEvent(event);
            }
          }
        } finally {
          dispatching.remove();
          queue.remove();
        }
      }
    }
    
    private static final class Event {
      private final Object event;
      private final Iterator<Subscriber> subscribers;
      
      private Event(Object event, Iterator<Subscriber> subscribers) {
        this.event = event;
        this.subscribers = subscribers;
      }
    }
  }
  
























  private static final class LegacyAsyncDispatcher
    extends Dispatcher
  {
    private final ConcurrentLinkedQueue<EventWithSubscriber> queue = Queues.newConcurrentLinkedQueue();
    
    private LegacyAsyncDispatcher() {}
    
    void dispatch(Object event, Iterator<Subscriber> subscribers) { Preconditions.checkNotNull(event);
      while (subscribers.hasNext()) {
        queue.add(new EventWithSubscriber(event, (Subscriber)subscribers.next(), null));
      }
      
      EventWithSubscriber e;
      while ((e = (EventWithSubscriber)queue.poll()) != null) {
        subscriber.dispatchEvent(event);
      }
    }
    
    private static final class EventWithSubscriber {
      private final Object event;
      private final Subscriber subscriber;
      
      private EventWithSubscriber(Object event, Subscriber subscriber) {
        this.event = event;
        this.subscriber = subscriber;
      }
    }
  }
  

  private static final class ImmediateDispatcher
    extends Dispatcher
  {
    private static final ImmediateDispatcher INSTANCE = new ImmediateDispatcher();
    
    private ImmediateDispatcher() {}
    
    void dispatch(Object event, Iterator<Subscriber> subscribers) { Preconditions.checkNotNull(event);
      while (subscribers.hasNext()) {
        ((Subscriber)subscribers.next()).dispatchEvent(event);
      }
    }
  }
}
