package com.google.common.util.concurrent;

import com.google.common.annotations.Beta;
import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicates;
import com.google.common.base.Stopwatch;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.ImmutableSetMultimap.Builder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.MultimapBuilder.MultimapBuilderWithKeys;
import com.google.common.collect.MultimapBuilder.SetMultimapBuilder;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Multiset;
import com.google.common.collect.Ordering;
import com.google.common.collect.SetMultimap;
import com.google.common.collect.UnmodifiableIterator;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.concurrent.GuardedBy;












































































@Beta
@GwtIncompatible
public final class ServiceManager
{
  private static final Logger logger = Logger.getLogger(ServiceManager.class.getName());
  private static final ListenerCallQueue.Callback<Listener> HEALTHY_CALLBACK = new ListenerCallQueue.Callback("healthy()")
  {
    void call(ServiceManager.Listener listener)
    {
      listener.healthy();
    }
  };
  private static final ListenerCallQueue.Callback<Listener> STOPPED_CALLBACK = new ListenerCallQueue.Callback("stopped()")
  {
    void call(ServiceManager.Listener listener)
    {
      listener.stopped();
    }
  };
  





  private final ServiceManagerState state;
  





  private final ImmutableList<Service> services;
  






  @Beta
  public static abstract class Listener
  {
    public Listener() {}
    





    public void healthy() {}
    





    public void stopped() {}
    





    public void failure(Service service) {}
  }
  





  public ServiceManager(Iterable<? extends Service> services)
  {
    ImmutableList<Service> copy = ImmutableList.copyOf(services);
    if (copy.isEmpty())
    {

      logger.log(Level.WARNING, "ServiceManager configured with no services.  Is your application configured properly?", new EmptyServiceManagerWarning(null));
      


      copy = ImmutableList.of(new NoOpService(null));
    }
    state = new ServiceManagerState(copy);
    this.services = copy;
    WeakReference<ServiceManagerState> stateReference = new WeakReference(state);
    
    for (UnmodifiableIterator localUnmodifiableIterator = copy.iterator(); localUnmodifiableIterator.hasNext();) { Service service = (Service)localUnmodifiableIterator.next();
      service.addListener(new ServiceListener(service, stateReference), MoreExecutors.directExecutor());
      

      Preconditions.checkArgument(service.state() == Service.State.NEW, "Can only manage NEW services, %s", service);
    }
    

    state.markReady();
  }
  






















  public void addListener(Listener listener, Executor executor)
  {
    state.addListener(listener, executor);
  }
  















  public void addListener(Listener listener)
  {
    state.addListener(listener, MoreExecutors.directExecutor());
  }
  







  @CanIgnoreReturnValue
  public ServiceManager startAsync()
  {
    for (UnmodifiableIterator localUnmodifiableIterator = services.iterator(); localUnmodifiableIterator.hasNext();) { Service service = (Service)localUnmodifiableIterator.next();
      Service.State state = service.state();
      Preconditions.checkState(state == Service.State.NEW, "Service %s is %s, cannot start it.", service, state);
    }
    for (localUnmodifiableIterator = services.iterator(); localUnmodifiableIterator.hasNext();) { Service service = (Service)localUnmodifiableIterator.next();
      try {
        this.state.tryStartTiming(service);
        service.startAsync();

      }
      catch (IllegalStateException e)
      {

        logger.log(Level.WARNING, "Unable to start Service " + service, e);
      }
    }
    return this;
  }
  







  public void awaitHealthy()
  {
    state.awaitHealthy();
  }
  









  public void awaitHealthy(long timeout, TimeUnit unit)
    throws TimeoutException
  {
    state.awaitHealthy(timeout, unit);
  }
  





  @CanIgnoreReturnValue
  public ServiceManager stopAsync()
  {
    for (UnmodifiableIterator localUnmodifiableIterator = services.iterator(); localUnmodifiableIterator.hasNext();) { Service service = (Service)localUnmodifiableIterator.next();
      service.stopAsync();
    }
    return this;
  }
  




  public void awaitStopped()
  {
    state.awaitStopped();
  }
  







  public void awaitStopped(long timeout, TimeUnit unit)
    throws TimeoutException
  {
    state.awaitStopped(timeout, unit);
  }
  





  public boolean isHealthy()
  {
    for (UnmodifiableIterator localUnmodifiableIterator = services.iterator(); localUnmodifiableIterator.hasNext();) { Service service = (Service)localUnmodifiableIterator.next();
      if (!service.isRunning()) {
        return false;
      }
    }
    return true;
  }
  





  public ImmutableMultimap<Service.State, Service> servicesByState()
  {
    return state.servicesByState();
  }
  






  public ImmutableMap<Service, Long> startupTimes()
  {
    return state.startupTimes();
  }
  
  public String toString()
  {
    return 
    
      MoreObjects.toStringHelper(ServiceManager.class).add("services", Collections2.filter(services, Predicates.not(Predicates.instanceOf(NoOpService.class)))).toString();
  }
  



  private static final class ServiceManagerState
  {
    final Monitor monitor = new Monitor();
    

    @GuardedBy("monitor")
    final SetMultimap<Service.State, Service> servicesByState = MultimapBuilder.enumKeys(Service.State.class).linkedHashSetValues().build();
    @GuardedBy("monitor")
    final Multiset<Service.State> states = servicesByState
      .keys();
    
    @GuardedBy("monitor")
    final Map<Service, Stopwatch> startupTimers = Maps.newIdentityHashMap();
    





    @GuardedBy("monitor")
    boolean ready;
    




    @GuardedBy("monitor")
    boolean transitioned;
    




    final int numberOfServices;
    




    final Monitor.Guard awaitHealthGuard = new AwaitHealthGuard();
    
    final class AwaitHealthGuard extends Monitor.Guard
    {
      AwaitHealthGuard() {
        super();
      }
      

      public boolean isSatisfied()
      {
        return (states.count(Service.State.RUNNING) == numberOfServices) || 
          (states.contains(Service.State.STOPPING)) || 
          (states.contains(Service.State.TERMINATED)) || 
          (states.contains(Service.State.FAILED));
      }
    }
    



    final Monitor.Guard stoppedGuard = new StoppedGuard();
    
    final class StoppedGuard extends Monitor.Guard
    {
      StoppedGuard() {
        super();
      }
      
      public boolean isSatisfied()
      {
        return states.count(Service.State.TERMINATED) + states.count(Service.State.FAILED) == numberOfServices;
      }
    }
    


    @GuardedBy("monitor")
    final List<ListenerCallQueue<ServiceManager.Listener>> listeners = Collections.synchronizedList(new ArrayList());
    





    ServiceManagerState(ImmutableCollection<Service> services)
    {
      numberOfServices = services.size();
      servicesByState.putAll(Service.State.NEW, services);
    }
    



    void tryStartTiming(Service service)
    {
      monitor.enter();
      try {
        Stopwatch stopwatch = (Stopwatch)startupTimers.get(service);
        if (stopwatch == null) {
          startupTimers.put(service, Stopwatch.createStarted());
        }
        
        monitor.leave(); } finally { monitor.leave();
      }
    }
    



    void markReady()
    {
      monitor.enter();
      try {
        if (!transitioned)
        {
          ready = true;
        }
        else {
          List<Service> servicesInBadStates = Lists.newArrayList();
          for (UnmodifiableIterator localUnmodifiableIterator = servicesByState().values().iterator(); localUnmodifiableIterator.hasNext();) { Service service = (Service)localUnmodifiableIterator.next();
            if (service.state() != Service.State.NEW) {
              servicesInBadStates.add(service);
            }
          }
          throw new IllegalArgumentException("Services started transitioning asynchronously before the ServiceManager was constructed: " + servicesInBadStates);
        }
        
      }
      finally
      {
        monitor.leave();
      }
    }
    
    void addListener(ServiceManager.Listener listener, Executor executor) {
      Preconditions.checkNotNull(listener, "listener");
      Preconditions.checkNotNull(executor, "executor");
      monitor.enter();
      try
      {
        if (!stoppedGuard.isSatisfied()) {
          listeners.add(new ListenerCallQueue(listener, executor));
        }
        
        monitor.leave(); } finally { monitor.leave();
      }
    }
    
    void awaitHealthy() {
      monitor.enterWhenUninterruptibly(awaitHealthGuard);
      try {
        checkHealthy();
        
        monitor.leave(); } finally { monitor.leave();
      }
    }
    
    void awaitHealthy(long timeout, TimeUnit unit) throws TimeoutException {
      monitor.enter();
      try {
        if (!monitor.waitForUninterruptibly(awaitHealthGuard, timeout, unit))
        {


          throw new TimeoutException("Timeout waiting for the services to become healthy. The following services have not started: " + Multimaps.filterKeys(servicesByState, Predicates.in(ImmutableSet.of(Service.State.NEW, Service.State.STARTING))));
        }
        checkHealthy();
      } finally {
        monitor.leave();
      }
    }
    
    void awaitStopped() {
      monitor.enterWhenUninterruptibly(stoppedGuard);
      monitor.leave();
    }
    
    void awaitStopped(long timeout, TimeUnit unit) throws TimeoutException {
      monitor.enter();
      try {
        if (!monitor.waitForUninterruptibly(stoppedGuard, timeout, unit))
        {


          throw new TimeoutException("Timeout waiting for the services to stop. The following services have not stopped: " + Multimaps.filterKeys(servicesByState, Predicates.not(Predicates.in(EnumSet.of(Service.State.TERMINATED, Service.State.FAILED)))));
        }
      } finally {
        monitor.leave();
      }
    }
    
    ImmutableMultimap<Service.State, Service> servicesByState() {
      ImmutableSetMultimap.Builder<Service.State, Service> builder = ImmutableSetMultimap.builder();
      monitor.enter();
      try {
        for (Map.Entry<Service.State, Service> entry : servicesByState.entries()) {
          if (!(entry.getValue() instanceof ServiceManager.NoOpService)) {
            builder.put(entry);
          }
        }
      } finally {
        monitor.leave();
      }
      return builder.build();
    }
    
    ImmutableMap<Service, Long> startupTimes()
    {
      monitor.enter();
      try {
        loadTimes = Lists.newArrayListWithCapacity(startupTimers.size());
        
        for (Map.Entry<Service, Stopwatch> entry : startupTimers.entrySet()) {
          Service service = (Service)entry.getKey();
          Stopwatch stopWatch = (Stopwatch)entry.getValue();
          if ((!stopWatch.isRunning()) && (!(service instanceof ServiceManager.NoOpService)))
            loadTimes.add(Maps.immutableEntry(service, Long.valueOf(stopWatch.elapsed(TimeUnit.MILLISECONDS))));
        }
      } finally {
        List<Map.Entry<Service, Long>> loadTimes;
        monitor.leave(); }
      List<Map.Entry<Service, Long>> loadTimes;
      Collections.sort(loadTimes, 
      
        Ordering.natural()
        .onResultOf(new Function()
        {

          public Long apply(Map.Entry<Service, Long> input)
          {
            return (Long)input.getValue();
          }
        }));
      return ImmutableMap.copyOf(loadTimes);
    }
    










    void transitionService(Service service, Service.State from, Service.State to)
    {
      Preconditions.checkNotNull(service);
      Preconditions.checkArgument(from != to);
      monitor.enter();
      try {
        transitioned = true;
        if (!ready) {
          return;
        }
        
        Preconditions.checkState(
          servicesByState.remove(from, service), "Service %s not at the expected location in the state map %s", service, from);
        


        Preconditions.checkState(servicesByState
          .put(to, service), "Service %s in the state map unexpectedly at %s", service, to);
        



        Stopwatch stopwatch = (Stopwatch)startupTimers.get(service);
        if (stopwatch == null)
        {
          stopwatch = Stopwatch.createStarted();
          startupTimers.put(service, stopwatch);
        }
        if ((to.compareTo(Service.State.RUNNING) >= 0) && (stopwatch.isRunning()))
        {
          stopwatch.stop();
          if (!(service instanceof ServiceManager.NoOpService)) {
            ServiceManager.logger.log(Level.FINE, "Started {0} in {1}.", new Object[] { service, stopwatch });
          }
        }
        


        if (to == Service.State.FAILED) {
          fireFailedListeners(service);
        }
        
        if (states.count(Service.State.RUNNING) == numberOfServices)
        {

          fireHealthyListeners();
        } else if (states.count(Service.State.TERMINATED) + states.count(Service.State.FAILED) == numberOfServices) {
          fireStoppedListeners();
        }
      } finally {
        monitor.leave();
        
        executeListeners();
      }
    }
    
    @GuardedBy("monitor")
    void fireStoppedListeners() {
      ServiceManager.STOPPED_CALLBACK.enqueueOn(listeners);
    }
    
    @GuardedBy("monitor")
    void fireHealthyListeners() {
      ServiceManager.HEALTHY_CALLBACK.enqueueOn(listeners);
    }
    




    @GuardedBy("monitor")
    void fireFailedListeners(final Service service)
    {
      new ListenerCallQueue.Callback("failed({service=" + service + "})")
      {
        void call(ServiceManager.Listener listener)
        {
          listener.failure(service); } }
      
        .enqueueOn(listeners);
    }
    
    void executeListeners()
    {
      Preconditions.checkState(
        !monitor.isOccupiedByCurrentThread(), "It is incorrect to execute listeners with the monitor held.");
      

      for (int i = 0; i < listeners.size(); i++) {
        ((ListenerCallQueue)listeners.get(i)).execute();
      }
    }
    
    @GuardedBy("monitor")
    void checkHealthy() {
      if (states.count(Service.State.RUNNING) != numberOfServices)
      {


        IllegalStateException exception = new IllegalStateException("Expected to be healthy after starting. The following services are not running: " + Multimaps.filterKeys(servicesByState, Predicates.not(Predicates.equalTo(Service.State.RUNNING))));
        throw exception;
      }
    }
  }
  


  private static final class ServiceListener
    extends Service.Listener
  {
    final Service service;
    
    final WeakReference<ServiceManager.ServiceManagerState> state;
    

    ServiceListener(Service service, WeakReference<ServiceManager.ServiceManagerState> state)
    {
      this.service = service;
      this.state = state;
    }
    
    public void starting()
    {
      ServiceManager.ServiceManagerState state = (ServiceManager.ServiceManagerState)this.state.get();
      if (state != null) {
        state.transitionService(service, Service.State.NEW, Service.State.STARTING);
        if (!(service instanceof ServiceManager.NoOpService)) {
          ServiceManager.logger.log(Level.FINE, "Starting {0}.", service);
        }
      }
    }
    
    public void running()
    {
      ServiceManager.ServiceManagerState state = (ServiceManager.ServiceManagerState)this.state.get();
      if (state != null) {
        state.transitionService(service, Service.State.STARTING, Service.State.RUNNING);
      }
    }
    
    public void stopping(Service.State from)
    {
      ServiceManager.ServiceManagerState state = (ServiceManager.ServiceManagerState)this.state.get();
      if (state != null) {
        state.transitionService(service, from, Service.State.STOPPING);
      }
    }
    
    public void terminated(Service.State from)
    {
      ServiceManager.ServiceManagerState state = (ServiceManager.ServiceManagerState)this.state.get();
      if (state != null) {
        if (!(service instanceof ServiceManager.NoOpService)) {
          ServiceManager.logger.log(Level.FINE, "Service {0} has terminated. Previous state was: {1}", new Object[] { service, from });
        }
        


        state.transitionService(service, from, Service.State.TERMINATED);
      }
    }
    
    public void failed(Service.State from, Throwable failure)
    {
      ServiceManager.ServiceManagerState state = (ServiceManager.ServiceManagerState)this.state.get();
      if (state != null)
      {

        boolean log = !(service instanceof ServiceManager.NoOpService);
        if (log) {
          ServiceManager.logger.log(Level.SEVERE, "Service " + service + " has failed in the " + from + " state.", failure);
        }
        


        state.transitionService(service, from, Service.State.FAILED);
      }
    }
  }
  


  private static final class NoOpService
    extends AbstractService
  {
    private NoOpService() {}
    


    protected void doStart()
    {
      notifyStarted();
    }
    
    protected void doStop()
    {
      notifyStopped();
    }
  }
  
  private static final class EmptyServiceManagerWarning
    extends Throwable
  {
    private EmptyServiceManagerWarning() {}
  }
}
