package org.seleniumhq.jetty9.server;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import org.seleniumhq.jetty9.io.Connection;
import org.seleniumhq.jetty9.io.Connection.Listener;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.annotation.ManagedOperation;
import org.seleniumhq.jetty9.util.component.AbstractLifeCycle;
import org.seleniumhq.jetty9.util.component.Container;
import org.seleniumhq.jetty9.util.component.ContainerLifeCycle;
import org.seleniumhq.jetty9.util.component.Dumpable;
import org.seleniumhq.jetty9.util.statistic.CounterStatistic;
import org.seleniumhq.jetty9.util.statistic.SampleStatistic;
























@Deprecated
@ManagedObject("Connector Statistics")
public class ConnectorStatistics
  extends AbstractLifeCycle
  implements Dumpable, Connection.Listener
{
  private static final Sample ZERO = new Sample();
  private final AtomicLong _startMillis = new AtomicLong(-1L);
  private final CounterStatistic _connectionStats = new CounterStatistic();
  private final SampleStatistic _messagesIn = new SampleStatistic();
  private final SampleStatistic _messagesOut = new SampleStatistic();
  private final SampleStatistic _connectionDurationStats = new SampleStatistic();
  private final ConcurrentMap<Connection, Sample> _samples = new ConcurrentHashMap();
  private final LongAdder _closedIn = new LongAdder();
  private final LongAdder _closedOut = new LongAdder();
  private AtomicLong _nanoStamp = new AtomicLong();
  private volatile int _messagesInPerSecond;
  private volatile int _messagesOutPerSecond;
  
  public ConnectorStatistics() {}
  
  public void onOpened(Connection connection) {
    if (isStarted())
    {
      _connectionStats.increment();
      _samples.put(connection, ZERO);
    }
  }
  

  public void onClosed(Connection connection)
  {
    if (isStarted())
    {
      long msgsIn = connection.getMessagesIn();
      long msgsOut = connection.getMessagesOut();
      _messagesIn.set(msgsIn);
      _messagesOut.set(msgsOut);
      _connectionStats.decrement();
      _connectionDurationStats.set(System.currentTimeMillis() - connection.getCreatedTimeStamp());
      
      Sample sample = (Sample)_samples.remove(connection);
      if (sample != null)
      {
        _closedIn.add(msgsIn - _messagesIn);
        _closedOut.add(msgsOut - _messagesOut);
      }
    }
  }
  

  @ManagedAttribute("Total number of bytes received by this connector")
  public int getBytesIn()
  {
    return -1;
  }
  

  @ManagedAttribute("Total number of bytes sent by this connector")
  public int getBytesOut()
  {
    return -1;
  }
  
  @ManagedAttribute("Total number of connections seen by this connector")
  public int getConnections()
  {
    return (int)_connectionStats.getTotal();
  }
  
  @ManagedAttribute("Connection duration maximum in ms")
  public long getConnectionDurationMax()
  {
    return _connectionDurationStats.getMax();
  }
  
  @ManagedAttribute("Connection duration mean in ms")
  public double getConnectionDurationMean()
  {
    return _connectionDurationStats.getMean();
  }
  
  @ManagedAttribute("Connection duration standard deviation")
  public double getConnectionDurationStdDev()
  {
    return _connectionDurationStats.getStdDev();
  }
  
  @ManagedAttribute("Messages In for all connections")
  public int getMessagesIn()
  {
    return (int)_messagesIn.getTotal();
  }
  
  @ManagedAttribute("Messages In per connection maximum")
  public int getMessagesInPerConnectionMax()
  {
    return (int)_messagesIn.getMax();
  }
  
  @ManagedAttribute("Messages In per connection mean")
  public double getMessagesInPerConnectionMean()
  {
    return _messagesIn.getMean();
  }
  
  @ManagedAttribute("Messages In per connection standard deviation")
  public double getMessagesInPerConnectionStdDev()
  {
    return _messagesIn.getStdDev();
  }
  
  @ManagedAttribute("Connections open")
  public int getConnectionsOpen()
  {
    return (int)_connectionStats.getCurrent();
  }
  
  @ManagedAttribute("Connections open maximum")
  public int getConnectionsOpenMax()
  {
    return (int)_connectionStats.getMax();
  }
  
  @ManagedAttribute("Messages Out for all connections")
  public int getMessagesOut()
  {
    return (int)_messagesIn.getTotal();
  }
  
  @ManagedAttribute("Messages In per connection maximum")
  public int getMessagesOutPerConnectionMax()
  {
    return (int)_messagesIn.getMax();
  }
  
  @ManagedAttribute("Messages In per connection mean")
  public double getMessagesOutPerConnectionMean()
  {
    return _messagesIn.getMean();
  }
  
  @ManagedAttribute("Messages In per connection standard deviation")
  public double getMessagesOutPerConnectionStdDev()
  {
    return _messagesIn.getStdDev();
  }
  
  @ManagedAttribute("Connection statistics started ms since epoch")
  public long getStartedMillis()
  {
    long start = _startMillis.get();
    return start < 0L ? 0L : System.currentTimeMillis() - start;
  }
  
  @ManagedAttribute("Messages in per second calculated over period since last called")
  public int getMessagesInPerSecond()
  {
    update();
    return _messagesInPerSecond;
  }
  
  @ManagedAttribute("Messages out per second calculated over period since last called")
  public int getMessagesOutPerSecond()
  {
    update();
    return _messagesOutPerSecond;
  }
  

  public void doStart()
  {
    reset();
  }
  

  public void doStop()
  {
    _samples.clear();
  }
  
  @ManagedOperation("Reset the statistics")
  public void reset()
  {
    _startMillis.set(System.currentTimeMillis());
    _messagesIn.reset();
    _messagesOut.reset();
    _connectionStats.reset();
    _connectionDurationStats.reset();
    _samples.clear();
  }
  

  @ManagedOperation("dump thread state")
  public String dump()
  {
    return ContainerLifeCycle.dump(this);
  }
  
  public void dump(Appendable out, String indent)
    throws IOException
  {
    ContainerLifeCycle.dumpObject(out, this);
    ContainerLifeCycle.dump(out, indent, new Collection[] { Arrays.asList(new String[] { "connections=" + _connectionStats, "duration=" + _connectionDurationStats, "in=" + _messagesIn, "out=" + _messagesOut }) });
  }
  
  public static void addToAllConnectors(Server server)
  {
    for (Connector connector : server.getConnectors())
    {
      if ((connector instanceof Container))
        ((Container)connector).addBean(new ConnectorStatistics());
    }
  }
  
  private static final long SECOND_NANOS = TimeUnit.SECONDS.toNanos(1L);
  
  private synchronized void update() {
    long now = System.nanoTime();
    long then = _nanoStamp.get();
    long duration = now - then;
    
    if (duration > SECOND_NANOS / 2L)
    {
      if (_nanoStamp.compareAndSet(then, now))
      {
        long msgsIn = _closedIn.sumThenReset();
        long msgsOut = _closedOut.sumThenReset();
        
        for (Map.Entry<Connection, Sample> entry : _samples.entrySet())
        {
          Connection connection = (Connection)entry.getKey();
          Sample sample = (Sample)entry.getValue();
          Sample next = new Sample(connection);
          if (_samples.replace(connection, sample, next))
          {
            msgsIn += _messagesIn - _messagesIn;
            msgsOut += _messagesOut - _messagesOut;
          }
        }
        
        _messagesInPerSecond = ((int)(msgsIn * SECOND_NANOS / duration));
        _messagesOutPerSecond = ((int)(msgsOut * SECOND_NANOS / duration));
      } }
  }
  
  private static class Sample {
    final long _messagesIn;
    final long _messagesOut;
    
    Sample() {
      _messagesIn = 0L;
      _messagesOut = 0L;
    }
    
    Sample(Connection connection)
    {
      _messagesIn = connection.getMessagesIn();
      _messagesOut = connection.getMessagesOut();
    }
  }
}
