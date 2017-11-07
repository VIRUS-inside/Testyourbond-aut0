package org.eclipse.jetty.io;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import org.eclipse.jetty.util.annotation.ManagedAttribute;
import org.eclipse.jetty.util.annotation.ManagedObject;
import org.eclipse.jetty.util.annotation.ManagedOperation;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.component.ContainerLifeCycle;
import org.eclipse.jetty.util.component.Dumpable;
import org.eclipse.jetty.util.statistic.CounterStatistic;
import org.eclipse.jetty.util.statistic.SampleStatistic;
























@ManagedObject("Tracks statistics on connections")
public class ConnectionStatistics
  extends AbstractLifeCycle
  implements Connection.Listener, Dumpable
{
  private final CounterStatistic _connections = new CounterStatistic();
  private final SampleStatistic _connectionsDuration = new SampleStatistic();
  private final LongAdder _rcvdBytes = new LongAdder();
  private final AtomicLong _bytesInStamp = new AtomicLong();
  private final LongAdder _sentBytes = new LongAdder();
  private final AtomicLong _bytesOutStamp = new AtomicLong();
  private final LongAdder _messagesIn = new LongAdder();
  private final AtomicLong _messagesInStamp = new AtomicLong();
  private final LongAdder _messagesOut = new LongAdder();
  private final AtomicLong _messagesOutStamp = new AtomicLong();
  
  public ConnectionStatistics() {}
  
  @ManagedOperation(value="Resets the statistics", impact="ACTION")
  public void reset() { _connections.reset();
    _connectionsDuration.reset();
    _rcvdBytes.reset();
    _bytesInStamp.set(System.nanoTime());
    _sentBytes.reset();
    _bytesOutStamp.set(System.nanoTime());
    _messagesIn.reset();
    _messagesInStamp.set(System.nanoTime());
    _messagesOut.reset();
    _messagesOutStamp.set(System.nanoTime());
  }
  
  protected void doStart()
    throws Exception
  {
    reset();
  }
  

  public void onOpened(Connection connection)
  {
    if (!isStarted()) {
      return;
    }
    _connections.increment();
  }
  

  public void onClosed(Connection connection)
  {
    if (!isStarted()) {
      return;
    }
    _connections.decrement();
    
    long elapsed = System.currentTimeMillis() - connection.getCreatedTimeStamp();
    _connectionsDuration.set(elapsed);
    
    long bytesIn = connection.getBytesIn();
    if (bytesIn > 0L)
      _rcvdBytes.add(bytesIn);
    long bytesOut = connection.getBytesOut();
    if (bytesOut > 0L) {
      _sentBytes.add(bytesOut);
    }
    long messagesIn = connection.getMessagesIn();
    if (messagesIn > 0L)
      _messagesIn.add(messagesIn);
    long messagesOut = connection.getMessagesOut();
    if (messagesOut > 0L) {
      _messagesOut.add(messagesOut);
    }
  }
  
  @ManagedAttribute("Total number of bytes received by tracked connections")
  public long getReceivedBytes() {
    return _rcvdBytes.sum();
  }
  
  @ManagedAttribute("Total number of bytes received per second since the last invocation of this method")
  public long getReceivedBytesRate()
  {
    long now = System.nanoTime();
    long then = _bytesInStamp.getAndSet(now);
    long elapsed = TimeUnit.NANOSECONDS.toMillis(now - then);
    return elapsed == 0L ? 0L : getReceivedBytes() * 1000L / elapsed;
  }
  
  @ManagedAttribute("Total number of bytes sent by tracked connections")
  public long getSentBytes()
  {
    return _sentBytes.sum();
  }
  
  @ManagedAttribute("Total number of bytes sent per second since the last invocation of this method")
  public long getSentBytesRate()
  {
    long now = System.nanoTime();
    long then = _bytesOutStamp.getAndSet(now);
    long elapsed = TimeUnit.NANOSECONDS.toMillis(now - then);
    return elapsed == 0L ? 0L : getSentBytes() * 1000L / elapsed;
  }
  
  @ManagedAttribute("The max duration of a connection in ms")
  public long getConnectionDurationMax()
  {
    return _connectionsDuration.getMax();
  }
  
  @ManagedAttribute("The mean duration of a connection in ms")
  public double getConnectionDurationMean()
  {
    return _connectionsDuration.getMean();
  }
  
  @ManagedAttribute("The standard deviation of the duration of a connection")
  public double getConnectionDurationStdDev()
  {
    return _connectionsDuration.getStdDev();
  }
  
  @ManagedAttribute("The total number of connections opened")
  public long getConnectionsTotal()
  {
    return _connections.getTotal();
  }
  
  @ManagedAttribute("The current number of open connections")
  public long getConnections()
  {
    return _connections.getCurrent();
  }
  
  @ManagedAttribute("The max number of open connections")
  public long getConnectionsMax()
  {
    return _connections.getMax();
  }
  
  @ManagedAttribute("The total number of messages received")
  public long getReceivedMessages()
  {
    return _messagesIn.sum();
  }
  
  @ManagedAttribute("Total number of messages received per second since the last invocation of this method")
  public long getReceivedMessagesRate()
  {
    long now = System.nanoTime();
    long then = _messagesInStamp.getAndSet(now);
    long elapsed = TimeUnit.NANOSECONDS.toMillis(now - then);
    return elapsed == 0L ? 0L : getReceivedMessages() * 1000L / elapsed;
  }
  
  @ManagedAttribute("The total number of messages sent")
  public long getSentMessages()
  {
    return _messagesOut.sum();
  }
  
  @ManagedAttribute("Total number of messages sent per second since the last invocation of this method")
  public long getSentMessagesRate()
  {
    long now = System.nanoTime();
    long then = _messagesOutStamp.getAndSet(now);
    long elapsed = TimeUnit.NANOSECONDS.toMillis(now - then);
    return elapsed == 0L ? 0L : getSentMessages() * 1000L / elapsed;
  }
  

  public String dump()
  {
    return ContainerLifeCycle.dump(this);
  }
  
  public void dump(Appendable out, String indent)
    throws IOException
  {
    ContainerLifeCycle.dumpObject(out, this);
    List<String> children = new ArrayList();
    children.add(String.format("connections=%s", new Object[] { _connections }));
    children.add(String.format("durations=%s", new Object[] { _connectionsDuration }));
    children.add(String.format("bytes in/out=%s/%s", new Object[] { Long.valueOf(getReceivedBytes()), Long.valueOf(getSentBytes()) }));
    children.add(String.format("messages in/out=%s/%s", new Object[] { Long.valueOf(getReceivedMessages()), Long.valueOf(getSentMessages()) }));
    ContainerLifeCycle.dump(out, indent, new Collection[] { children });
  }
  

  public String toString()
  {
    return String.format("%s@%x", new Object[] { getClass().getSimpleName(), Integer.valueOf(hashCode()) });
  }
}
