package org.apache.http.impl.client;

import java.util.concurrent.atomic.AtomicLong;






























public final class FutureRequestExecutionMetrics
{
  private final AtomicLong activeConnections = new AtomicLong();
  private final AtomicLong scheduledConnections = new AtomicLong();
  private final DurationCounter successfulConnections = new DurationCounter();
  private final DurationCounter failedConnections = new DurationCounter();
  private final DurationCounter requests = new DurationCounter();
  private final DurationCounter tasks = new DurationCounter();
  
  FutureRequestExecutionMetrics() {}
  
  AtomicLong getActiveConnections()
  {
    return activeConnections;
  }
  
  AtomicLong getScheduledConnections() {
    return scheduledConnections;
  }
  
  DurationCounter getSuccessfulConnections() {
    return successfulConnections;
  }
  
  DurationCounter getFailedConnections() {
    return failedConnections;
  }
  
  DurationCounter getRequests() {
    return requests;
  }
  
  DurationCounter getTasks() {
    return tasks;
  }
  
  public long getActiveConnectionCount() {
    return activeConnections.get();
  }
  
  public long getScheduledConnectionCount() {
    return scheduledConnections.get();
  }
  
  public long getSuccessfulConnectionCount() {
    return successfulConnections.count();
  }
  
  public long getSuccessfulConnectionAverageDuration() {
    return successfulConnections.averageDuration();
  }
  
  public long getFailedConnectionCount() {
    return failedConnections.count();
  }
  
  public long getFailedConnectionAverageDuration() {
    return failedConnections.averageDuration();
  }
  
  public long getRequestCount() {
    return requests.count();
  }
  
  public long getRequestAverageDuration() {
    return requests.averageDuration();
  }
  
  public long getTaskCount() {
    return tasks.count();
  }
  
  public long getTaskAverageDuration() {
    return tasks.averageDuration();
  }
  
  public String toString()
  {
    StringBuilder builder = new StringBuilder();
    builder.append("[activeConnections=").append(activeConnections).append(", scheduledConnections=").append(scheduledConnections).append(", successfulConnections=").append(successfulConnections).append(", failedConnections=").append(failedConnections).append(", requests=").append(requests).append(", tasks=").append(tasks).append("]");
    





    return builder.toString();
  }
  

  static class DurationCounter
  {
    DurationCounter() {}
    
    private final AtomicLong count = new AtomicLong(0L);
    private final AtomicLong cumulativeDuration = new AtomicLong(0L);
    
    public void increment(long startTime) {
      count.incrementAndGet();
      cumulativeDuration.addAndGet(System.currentTimeMillis() - startTime);
    }
    
    public long count() {
      return count.get();
    }
    
    public long averageDuration() {
      long counter = count.get();
      return counter > 0L ? cumulativeDuration.get() / counter : 0L;
    }
    
    public String toString()
    {
      StringBuilder builder = new StringBuilder();
      builder.append("[count=").append(count()).append(", averageDuration=").append(averageDuration()).append("]");
      

      return builder.toString();
    }
  }
}
