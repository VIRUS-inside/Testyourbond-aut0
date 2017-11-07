package org.seleniumhq.jetty9.server;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.seleniumhq.jetty9.io.EndPoint;
import org.seleniumhq.jetty9.util.annotation.ManagedAttribute;
import org.seleniumhq.jetty9.util.annotation.ManagedObject;
import org.seleniumhq.jetty9.util.annotation.Name;
import org.seleniumhq.jetty9.util.component.AbstractLifeCycle;
import org.seleniumhq.jetty9.util.log.Log;
import org.seleniumhq.jetty9.util.log.Logger;
import org.seleniumhq.jetty9.util.thread.ScheduledExecutorScheduler;
import org.seleniumhq.jetty9.util.thread.Scheduler;
import org.seleniumhq.jetty9.util.thread.ThreadPool;












































@ManagedObject("Monitor for low resource conditions and activate a low resource mode if detected")
public class LowResourceMonitor
  extends AbstractLifeCycle
{
  private static final Logger LOG = Log.getLogger(LowResourceMonitor.class);
  private final Server _server;
  private Scheduler _scheduler;
  private Connector[] _monitoredConnectors;
  private int _period = 1000;
  private int _maxConnections;
  private long _maxMemory;
  private int _lowResourcesIdleTimeout = 1000;
  private int _maxLowResourcesTime = 0;
  private boolean _monitorThreads = true;
  private final AtomicBoolean _low = new AtomicBoolean();
  
  private String _cause;
  private String _reasons;
  private long _lowStarted;
  private final Runnable _monitor = new Runnable()
  {

    public void run()
    {
      if (isRunning())
      {
        monitor();
        _scheduler.schedule(_monitor, _period, TimeUnit.MILLISECONDS);
      }
    }
  };
  
  public LowResourceMonitor(@Name("server") Server server)
  {
    _server = server;
  }
  
  @ManagedAttribute("Are the monitored connectors low on resources?")
  public boolean isLowOnResources()
  {
    return _low.get();
  }
  
  @ManagedAttribute("The reason(s) the monitored connectors are low on resources")
  public String getLowResourcesReasons()
  {
    return _reasons;
  }
  
  @ManagedAttribute("Get the timestamp in ms since epoch that low resources state started")
  public long getLowResourcesStarted()
  {
    return _lowStarted;
  }
  
  @ManagedAttribute("The monitored connectors. If null then all server connectors are monitored")
  public Collection<Connector> getMonitoredConnectors()
  {
    if (_monitoredConnectors == null)
      return Collections.emptyList();
    return Arrays.asList(_monitoredConnectors);
  }
  



  public void setMonitoredConnectors(Collection<Connector> monitoredConnectors)
  {
    if ((monitoredConnectors == null) || (monitoredConnectors.size() == 0)) {
      _monitoredConnectors = null;
    } else {
      _monitoredConnectors = ((Connector[])monitoredConnectors.toArray(new Connector[monitoredConnectors.size()]));
    }
  }
  
  @ManagedAttribute("The monitor period in ms")
  public int getPeriod() {
    return _period;
  }
  



  public void setPeriod(int periodMS)
  {
    _period = periodMS;
  }
  
  @ManagedAttribute("True if low available threads status is monitored")
  public boolean getMonitorThreads()
  {
    return _monitorThreads;
  }
  




  public void setMonitorThreads(boolean monitorThreads)
  {
    _monitorThreads = monitorThreads;
  }
  
  @ManagedAttribute("The maximum connections allowed for the monitored connectors before low resource handling is activated")
  public int getMaxConnections()
  {
    return _maxConnections;
  }
  



  public void setMaxConnections(int maxConnections)
  {
    _maxConnections = maxConnections;
  }
  
  @ManagedAttribute("The maximum memory (in bytes) that can be used before low resources is triggered.  Memory used is calculated as (totalMemory-freeMemory).")
  public long getMaxMemory()
  {
    return _maxMemory;
  }
  



  public void setMaxMemory(long maxMemoryBytes)
  {
    _maxMemory = maxMemoryBytes;
  }
  
  @ManagedAttribute("The idletimeout in ms to apply to all existing connections when low resources is detected")
  public int getLowResourcesIdleTimeout()
  {
    return _lowResourcesIdleTimeout;
  }
  



  public void setLowResourcesIdleTimeout(int lowResourcesIdleTimeoutMS)
  {
    _lowResourcesIdleTimeout = lowResourcesIdleTimeoutMS;
  }
  
  @ManagedAttribute("The maximum time in ms that low resources condition can persist before lowResourcesIdleTimeout is applied to new connections as well as existing connections")
  public int getMaxLowResourcesTime()
  {
    return _maxLowResourcesTime;
  }
  



  public void setMaxLowResourcesTime(int maxLowResourcesTimeMS)
  {
    _maxLowResourcesTime = maxLowResourcesTimeMS;
  }
  
  protected void doStart()
    throws Exception
  {
    _scheduler = ((Scheduler)_server.getBean(Scheduler.class));
    
    if (_scheduler == null)
    {
      _scheduler = new LRMScheduler(null);
      _scheduler.start();
    }
    super.doStart();
    
    _scheduler.schedule(_monitor, _period, TimeUnit.MILLISECONDS);
  }
  
  protected void doStop()
    throws Exception
  {
    if ((_scheduler instanceof LRMScheduler))
      _scheduler.stop();
    super.doStop();
  }
  
  protected Connector[] getMonitoredOrServerConnectors()
  {
    if ((_monitoredConnectors != null) && (_monitoredConnectors.length > 0))
      return _monitoredConnectors;
    return _server.getConnectors();
  }
  
  protected void monitor()
  {
    String reasons = null;
    String cause = "";
    int connections = 0;
    
    ThreadPool serverThreads = _server.getThreadPool();
    if ((_monitorThreads) && (serverThreads.isLowOnThreads()))
    {
      reasons = low(reasons, "Server low on threads: " + serverThreads);
      cause = cause + "S";
    }
    
    for (Connector connector : getMonitoredOrServerConnectors())
    {
      connections += connector.getConnectedEndPoints().size();
      
      Executor executor = connector.getExecutor();
      if (((executor instanceof ThreadPool)) && (executor != serverThreads))
      {
        ThreadPool connectorThreads = (ThreadPool)executor;
        if ((_monitorThreads) && (connectorThreads.isLowOnThreads()))
        {
          reasons = low(reasons, "Connector low on threads: " + connectorThreads);
          cause = cause + "T";
        }
      }
    }
    
    if ((_maxConnections > 0) && (connections > _maxConnections))
    {
      reasons = low(reasons, "Max Connections exceeded: " + connections + ">" + _maxConnections);
      cause = cause + "C";
    }
    
    long memory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    if ((_maxMemory > 0L) && (memory > _maxMemory))
    {
      reasons = low(reasons, "Max memory exceeded: " + memory + ">" + _maxMemory);
      cause = cause + "M";
    }
    
    if (reasons != null)
    {

      if (!cause.equals(_cause))
      {
        LOG.warn("Low Resources: {}", new Object[] { reasons });
        _cause = cause;
      }
      

      if (_low.compareAndSet(false, true))
      {
        _reasons = reasons;
        _lowStarted = System.currentTimeMillis();
        setLowResources();
      }
      

      if ((_maxLowResourcesTime > 0) && (System.currentTimeMillis() - _lowStarted > _maxLowResourcesTime)) {
        setLowResources();
      }
      
    }
    else if (_low.compareAndSet(true, false))
    {
      LOG.info("Low Resources cleared", new Object[0]);
      _reasons = null;
      _lowStarted = 0L;
      _cause = null;
      clearLowResources();
    }
  }
  

  protected void setLowResources()
  {
    for (Connector connector : getMonitoredOrServerConnectors())
    {
      for (EndPoint endPoint : connector.getConnectedEndPoints())
        endPoint.setIdleTimeout(_lowResourcesIdleTimeout);
    }
  }
  
  protected void clearLowResources() {
    Connector connector;
    for (connector : getMonitoredOrServerConnectors())
    {
      for (EndPoint endPoint : connector.getConnectedEndPoints()) {
        endPoint.setIdleTimeout(connector.getIdleTimeout());
      }
    }
  }
  
  private String low(String reasons, String newReason) {
    if (reasons == null)
      return newReason;
    return reasons + ", " + newReason;
  }
  
  private static class LRMScheduler
    extends ScheduledExecutorScheduler
  {
    private LRMScheduler() {}
  }
}
