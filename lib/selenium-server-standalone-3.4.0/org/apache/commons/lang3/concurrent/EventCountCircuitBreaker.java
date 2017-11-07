package org.apache.commons.lang3.concurrent;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;



































































































































public class EventCountCircuitBreaker
  extends AbstractCircuitBreaker<Integer>
{
  private static final Map<AbstractCircuitBreaker.State, StateStrategy> STRATEGY_MAP = ;
  




  private final AtomicReference<CheckIntervalData> checkIntervalData;
  




  private final int openingThreshold;
  




  private final long openingInterval;
  




  private final int closingThreshold;
  



  private final long closingInterval;
  




  public EventCountCircuitBreaker(int openingThreshold, long openingInterval, TimeUnit openingUnit, int closingThreshold, long closingInterval, TimeUnit closingUnit)
  {
    checkIntervalData = new AtomicReference(new CheckIntervalData(0, 0L));
    this.openingThreshold = openingThreshold;
    this.openingInterval = openingUnit.toNanos(openingInterval);
    this.closingThreshold = closingThreshold;
    this.closingInterval = closingUnit.toNanos(closingInterval);
  }
  













  public EventCountCircuitBreaker(int openingThreshold, long checkInterval, TimeUnit checkUnit, int closingThreshold)
  {
    this(openingThreshold, checkInterval, checkUnit, closingThreshold, checkInterval, checkUnit);
  }
  










  public EventCountCircuitBreaker(int threshold, long checkInterval, TimeUnit checkUnit)
  {
    this(threshold, checkInterval, checkUnit, threshold);
  }
  






  public int getOpeningThreshold()
  {
    return openingThreshold;
  }
  




  public long getOpeningInterval()
  {
    return openingInterval;
  }
  






  public int getClosingThreshold()
  {
    return closingThreshold;
  }
  




  public long getClosingInterval()
  {
    return closingInterval;
  }
  





  public boolean checkState()
  {
    return performStateCheck(0);
  }
  



  public boolean incrementAndCheckState(Integer increment)
    throws CircuitBreakingException
  {
    return performStateCheck(1);
  }
  







  public boolean incrementAndCheckState()
  {
    return incrementAndCheckState(Integer.valueOf(1));
  }
  






  public void open()
  {
    super.open();
    checkIntervalData.set(new CheckIntervalData(0, now()));
  }
  






  public void close()
  {
    super.close();
    checkIntervalData.set(new CheckIntervalData(0, now()));
  }
  


  private boolean performStateCheck(int increment)
  {
    AbstractCircuitBreaker.State currentState;
    

    CheckIntervalData currentData;
    
    CheckIntervalData nextData;
    
    do
    {
      long time = now();
      currentState = (AbstractCircuitBreaker.State)state.get();
      currentData = (CheckIntervalData)checkIntervalData.get();
      nextData = nextCheckIntervalData(increment, currentData, currentState, time);
    } while (!updateCheckIntervalData(currentData, nextData));
    


    if (stateStrategy(currentState).isStateTransition(this, currentData, nextData)) {
      currentState = currentState.oppositeState();
      changeStateAndStartNewCheckInterval(currentState);
    }
    return !isOpen(currentState);
  }
  










  private boolean updateCheckIntervalData(CheckIntervalData currentData, CheckIntervalData nextData)
  {
    return (currentData == nextData) || 
      (checkIntervalData.compareAndSet(currentData, nextData));
  }
  





  private void changeStateAndStartNewCheckInterval(AbstractCircuitBreaker.State newState)
  {
    changeState(newState);
    checkIntervalData.set(new CheckIntervalData(0, now()));
  }
  




  private CheckIntervalData nextCheckIntervalData(int increment, CheckIntervalData currentData, AbstractCircuitBreaker.State currentState, long time)
  {
    CheckIntervalData nextData;
    


    CheckIntervalData nextData;
    


    if (stateStrategy(currentState).isCheckIntervalFinished(this, currentData, time)) {
      nextData = new CheckIntervalData(increment, time);
    } else {
      nextData = currentData.increment(increment);
    }
    return nextData;
  }
  





  long now()
  {
    return System.nanoTime();
  }
  






  private static StateStrategy stateStrategy(AbstractCircuitBreaker.State state)
  {
    StateStrategy strategy = (StateStrategy)STRATEGY_MAP.get(state);
    return strategy;
  }
  





  private static Map<AbstractCircuitBreaker.State, StateStrategy> createStrategyMap()
  {
    Map<AbstractCircuitBreaker.State, StateStrategy> map = new EnumMap(AbstractCircuitBreaker.State.class);
    map.put(AbstractCircuitBreaker.State.CLOSED, new StateStrategyClosed(null));
    map.put(AbstractCircuitBreaker.State.OPEN, new StateStrategyOpen(null));
    return map;
  }
  




  private static class CheckIntervalData
  {
    private final int eventCount;
    



    private final long checkIntervalStart;
    




    public CheckIntervalData(int count, long intervalStart)
    {
      eventCount = count;
      checkIntervalStart = intervalStart;
    }
    




    public int getEventCount()
    {
      return eventCount;
    }
    




    public long getCheckIntervalStart()
    {
      return checkIntervalStart;
    }
    






    public CheckIntervalData increment(int delta)
    {
      return delta != 0 ? new CheckIntervalData(getEventCount() + delta, 
        getCheckIntervalStart()) : this;
    }
  }
  





  private static abstract class StateStrategy
  {
    private StateStrategy() {}
    





    public boolean isCheckIntervalFinished(EventCountCircuitBreaker breaker, EventCountCircuitBreaker.CheckIntervalData currentData, long now)
    {
      return now - currentData.getCheckIntervalStart() > fetchCheckInterval(breaker);
    }
    






    public abstract boolean isStateTransition(EventCountCircuitBreaker paramEventCountCircuitBreaker, EventCountCircuitBreaker.CheckIntervalData paramCheckIntervalData1, EventCountCircuitBreaker.CheckIntervalData paramCheckIntervalData2);
    





    protected abstract long fetchCheckInterval(EventCountCircuitBreaker paramEventCountCircuitBreaker);
  }
  





  private static class StateStrategyClosed
    extends EventCountCircuitBreaker.StateStrategy
  {
    private StateStrategyClosed()
    {
      super();
    }
    



    public boolean isStateTransition(EventCountCircuitBreaker breaker, EventCountCircuitBreaker.CheckIntervalData currentData, EventCountCircuitBreaker.CheckIntervalData nextData)
    {
      return nextData.getEventCount() > breaker.getOpeningThreshold();
    }
    



    protected long fetchCheckInterval(EventCountCircuitBreaker breaker)
    {
      return breaker.getOpeningInterval();
    }
  }
  
  private static class StateStrategyOpen extends EventCountCircuitBreaker.StateStrategy
  {
    private StateStrategyOpen() {
      super();
    }
    


    public boolean isStateTransition(EventCountCircuitBreaker breaker, EventCountCircuitBreaker.CheckIntervalData currentData, EventCountCircuitBreaker.CheckIntervalData nextData)
    {
      return 
        (nextData.getCheckIntervalStart() != currentData.getCheckIntervalStart()) && 
        (currentData.getEventCount() < breaker.getClosingThreshold());
    }
    



    protected long fetchCheckInterval(EventCountCircuitBreaker breaker)
    {
      return breaker.getClosingInterval();
    }
  }
}
