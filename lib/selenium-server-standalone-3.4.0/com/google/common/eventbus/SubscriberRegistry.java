package com.google.common.eventbus;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.reflect.TypeToken;
import com.google.common.reflect.TypeToken.TypeSet;
import com.google.common.util.concurrent.UncheckedExecutionException;
import com.google.j2objc.annotations.Weak;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;
import javax.annotation.Nullable;


























final class SubscriberRegistry
{
  private final ConcurrentMap<Class<?>, CopyOnWriteArraySet<Subscriber>> subscribers = Maps.newConcurrentMap();
  
  @Weak
  private final EventBus bus;
  

  SubscriberRegistry(EventBus bus)
  {
    this.bus = ((EventBus)Preconditions.checkNotNull(bus));
  }
  


  void register(Object listener)
  {
    Multimap<Class<?>, Subscriber> listenerMethods = findAllSubscribers(listener);
    
    for (Map.Entry<Class<?>, Collection<Subscriber>> entry : listenerMethods.asMap().entrySet()) {
      Class<?> eventType = (Class)entry.getKey();
      Collection<Subscriber> eventMethodsInListener = (Collection)entry.getValue();
      
      CopyOnWriteArraySet<Subscriber> eventSubscribers = (CopyOnWriteArraySet)subscribers.get(eventType);
      
      if (eventSubscribers == null) {
        CopyOnWriteArraySet<Subscriber> newSet = new CopyOnWriteArraySet();
        
        eventSubscribers = (CopyOnWriteArraySet)MoreObjects.firstNonNull(subscribers.putIfAbsent(eventType, newSet), newSet);
      }
      
      eventSubscribers.addAll(eventMethodsInListener);
    }
  }
  


  void unregister(Object listener)
  {
    Multimap<Class<?>, Subscriber> listenerMethods = findAllSubscribers(listener);
    
    for (Map.Entry<Class<?>, Collection<Subscriber>> entry : listenerMethods.asMap().entrySet()) {
      Class<?> eventType = (Class)entry.getKey();
      Collection<Subscriber> listenerMethodsForType = (Collection)entry.getValue();
      
      CopyOnWriteArraySet<Subscriber> currentSubscribers = (CopyOnWriteArraySet)subscribers.get(eventType);
      if ((currentSubscribers == null) || (!currentSubscribers.removeAll(listenerMethodsForType)))
      {



        throw new IllegalArgumentException("missing event subscriber for an annotated method. Is " + listener + " registered?");
      }
    }
  }
  



  @VisibleForTesting
  Set<Subscriber> getSubscribersForTesting(Class<?> eventType)
  {
    return (Set)MoreObjects.firstNonNull(subscribers.get(eventType), ImmutableSet.of());
  }
  



  Iterator<Subscriber> getSubscribers(Object event)
  {
    ImmutableSet<Class<?>> eventTypes = flattenHierarchy(event.getClass());
    

    List<Iterator<Subscriber>> subscriberIterators = Lists.newArrayListWithCapacity(eventTypes.size());
    
    for (UnmodifiableIterator localUnmodifiableIterator = eventTypes.iterator(); localUnmodifiableIterator.hasNext();) { Class<?> eventType = (Class)localUnmodifiableIterator.next();
      CopyOnWriteArraySet<Subscriber> eventSubscribers = (CopyOnWriteArraySet)subscribers.get(eventType);
      if (eventSubscribers != null)
      {
        subscriberIterators.add(eventSubscribers.iterator());
      }
    }
    
    return Iterators.concat(subscriberIterators.iterator());
  }
  







  private static final LoadingCache<Class<?>, ImmutableList<Method>> subscriberMethodsCache = CacheBuilder.newBuilder()
    .weakKeys()
    .build(new CacheLoader()
  {
    public ImmutableList<Method> load(Class<?> concreteClass)
      throws Exception
    {
      return SubscriberRegistry.getAnnotatedMethodsNotCached(concreteClass);
    }
  });
  








  private Multimap<Class<?>, Subscriber> findAllSubscribers(Object listener)
  {
    Multimap<Class<?>, Subscriber> methodsInListener = HashMultimap.create();
    Class<?> clazz = listener.getClass();
    for (UnmodifiableIterator localUnmodifiableIterator = getAnnotatedMethods(clazz).iterator(); localUnmodifiableIterator.hasNext();) { Method method = (Method)localUnmodifiableIterator.next();
      Class<?>[] parameterTypes = method.getParameterTypes();
      Class<?> eventType = parameterTypes[0];
      methodsInListener.put(eventType, Subscriber.create(bus, listener, method));
    }
    return methodsInListener;
  }
  
  private static ImmutableList<Method> getAnnotatedMethods(Class<?> clazz) {
    return (ImmutableList)subscriberMethodsCache.getUnchecked(clazz);
  }
  
  private static ImmutableList<Method> getAnnotatedMethodsNotCached(Class<?> clazz) {
    Set<? extends Class<?>> supertypes = TypeToken.of(clazz).getTypes().rawTypes();
    Map<MethodIdentifier, Method> identifiers = Maps.newHashMap();
    for (Class<?> supertype : supertypes) {
      for (Method method : supertype.getDeclaredMethods()) {
        if ((method.isAnnotationPresent(Subscribe.class)) && (!method.isSynthetic()))
        {
          Class<?>[] parameterTypes = method.getParameterTypes();
          Preconditions.checkArgument(parameterTypes.length == 1, "Method %s has @Subscribe annotation but has %s parameters.Subscriber methods must have exactly 1 parameter.", method, parameterTypes.length);
          





          MethodIdentifier ident = new MethodIdentifier(method);
          if (!identifiers.containsKey(ident)) {
            identifiers.put(ident, method);
          }
        }
      }
    }
    return ImmutableList.copyOf(identifiers.values());
  }
  




  private static final LoadingCache<Class<?>, ImmutableSet<Class<?>>> flattenHierarchyCache = CacheBuilder.newBuilder()
    .weakKeys()
    .build(new CacheLoader()
  {


    public ImmutableSet<Class<?>> load(Class<?> concreteClass)
    {

      return ImmutableSet.copyOf(
        TypeToken.of(concreteClass).getTypes().rawTypes());
    }
  });
  











  @VisibleForTesting
  static ImmutableSet<Class<?>> flattenHierarchy(Class<?> concreteClass)
  {
    try
    {
      return (ImmutableSet)flattenHierarchyCache.getUnchecked(concreteClass);
    } catch (UncheckedExecutionException e) {
      throw Throwables.propagate(e.getCause());
    }
  }
  
  private static final class MethodIdentifier
  {
    private final String name;
    private final List<Class<?>> parameterTypes;
    
    MethodIdentifier(Method method) {
      name = method.getName();
      parameterTypes = Arrays.asList(method.getParameterTypes());
    }
    
    public int hashCode()
    {
      return Objects.hashCode(new Object[] { name, parameterTypes });
    }
    
    public boolean equals(@Nullable Object o)
    {
      if ((o instanceof MethodIdentifier)) {
        MethodIdentifier ident = (MethodIdentifier)o;
        return (name.equals(name)) && (parameterTypes.equals(parameterTypes));
      }
      return false;
    }
  }
}
