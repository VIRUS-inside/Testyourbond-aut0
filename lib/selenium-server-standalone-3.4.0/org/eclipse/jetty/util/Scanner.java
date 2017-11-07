package org.eclipse.jetty.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;




























public class Scanner
  extends AbstractLifeCycle
{
  private static final Logger LOG = Log.getLogger(Scanner.class);
  private static int __scannerId = 0;
  private int _scanInterval;
  private int _scanCount = 0;
  private final List<Listener> _listeners = new ArrayList();
  private final Map<String, TimeNSize> _prevScan = new HashMap();
  private final Map<String, TimeNSize> _currentScan = new HashMap();
  private FilenameFilter _filter;
  private final List<File> _scanDirs = new ArrayList();
  private volatile boolean _running = false;
  private boolean _reportExisting = true;
  private boolean _reportDirs = true;
  private Timer _timer;
  private TimerTask _task;
  private int _scanDepth = 0;
  
  public static enum Notification { ADDED,  CHANGED,  REMOVED;
    private Notification() {} } private final Map<String, Notification> _notifications = new HashMap();
  
  static class TimeNSize
  {
    final long _lastModified;
    final long _size;
    
    public TimeNSize(long lastModified, long size)
    {
      _lastModified = lastModified;
      _size = size;
    }
    

    public int hashCode()
    {
      return (int)_lastModified ^ (int)_size;
    }
    

    public boolean equals(Object o)
    {
      if ((o instanceof TimeNSize))
      {
        TimeNSize tns = (TimeNSize)o;
        return (_lastModified == _lastModified) && (_size == _size);
      }
      return false;
    }
    

    public String toString()
    {
      return "[lm=" + _lastModified + ",s=" + _size + "]";
    }
  }
  























  public Scanner() {}
  






















  public synchronized int getScanInterval()
  {
    return _scanInterval;
  }
  




  public synchronized void setScanInterval(int scanInterval)
  {
    _scanInterval = scanInterval;
    schedule();
  }
  
  public void setScanDirs(List<File> dirs)
  {
    _scanDirs.clear();
    _scanDirs.addAll(dirs);
  }
  
  public synchronized void addScanDir(File dir)
  {
    _scanDirs.add(dir);
  }
  
  public List<File> getScanDirs()
  {
    return Collections.unmodifiableList(_scanDirs);
  }
  





  public void setRecursive(boolean recursive)
  {
    _scanDepth = (recursive ? -1 : 0);
  }
  





  public boolean getRecursive()
  {
    return _scanDepth == -1;
  }
  




  public int getScanDepth()
  {
    return _scanDepth;
  }
  




  public void setScanDepth(int scanDepth)
  {
    _scanDepth = scanDepth;
  }
  





  public void setFilenameFilter(FilenameFilter filter)
  {
    _filter = filter;
  }
  




  public FilenameFilter getFilenameFilter()
  {
    return _filter;
  }
  







  public void setReportExistingFilesOnStartup(boolean reportExisting)
  {
    _reportExisting = reportExisting;
  }
  

  public boolean getReportExistingFilesOnStartup()
  {
    return _reportExisting;
  }
  




  public void setReportDirs(boolean dirs)
  {
    _reportDirs = dirs;
  }
  

  public boolean getReportDirs()
  {
    return _reportDirs;
  }
  





  public synchronized void addListener(Listener listener)
  {
    if (listener == null)
      return;
    _listeners.add(listener);
  }
  





  public synchronized void removeListener(Listener listener)
  {
    if (listener == null)
      return;
    _listeners.remove(listener);
  }
  





  public synchronized void doStart()
  {
    if (_running) {
      return;
    }
    _running = true;
    
    if (_reportExisting)
    {

      scan();
      scan();

    }
    else
    {
      scanFiles();
      _prevScan.putAll(_currentScan);
    }
    schedule();
  }
  
  public TimerTask newTimerTask()
  {
    new TimerTask()
    {
      public void run() {
        scan();
      }
    };
  }
  
  public Timer newTimer() {
    return new Timer("Scanner-" + __scannerId++, true);
  }
  
  public void schedule()
  {
    if (_running)
    {
      if (_timer != null)
        _timer.cancel();
      if (_task != null)
        _task.cancel();
      if (getScanInterval() > 0)
      {
        _timer = newTimer();
        _task = newTimerTask();
        _timer.schedule(_task, 1010L * getScanInterval(), 1010L * getScanInterval());
      }
    }
  }
  



  public synchronized void doStop()
  {
    if (_running)
    {
      _running = false;
      if (_timer != null)
        _timer.cancel();
      if (_task != null)
        _task.cancel();
      _task = null;
      _timer = null;
    }
  }
  




  public boolean exists(String path)
  {
    for (File dir : _scanDirs)
      if (new File(dir, path).exists())
        return true;
    return false;
  }
  




  public synchronized void scan()
  {
    reportScanStart(++_scanCount);
    scanFiles();
    reportDifferences(_currentScan, _prevScan);
    _prevScan.clear();
    _prevScan.putAll(_currentScan);
    reportScanEnd(_scanCount);
    
    for (Listener l : _listeners)
    {
      try
      {
        if ((l instanceof ScanListener)) {
          ((ScanListener)l).scan();
        }
      }
      catch (Exception e) {
        LOG.warn(e);
      }
      catch (Error e)
      {
        LOG.warn(e);
      }
    }
  }
  



  public synchronized void scanFiles()
  {
    if (_scanDirs == null) {
      return;
    }
    _currentScan.clear();
    Iterator<File> itor = _scanDirs.iterator();
    while (itor.hasNext())
    {
      File dir = (File)itor.next();
      
      if ((dir != null) && (dir.exists())) {
        try
        {
          scanFile(dir.getCanonicalFile(), _currentScan, 0);
        }
        catch (IOException e)
        {
          LOG.warn("Error scanning files.", e);
        }
      }
    }
  }
  








  public synchronized void reportDifferences(Map<String, TimeNSize> currentScan, Map<String, TimeNSize> oldScan)
  {
    Set<String> oldScanKeys = new HashSet(oldScan.keySet());
    

    for (Map.Entry<String, TimeNSize> entry : currentScan.entrySet())
    {
      String file = (String)entry.getKey();
      if (!oldScanKeys.contains(file))
      {
        Notification old = (Notification)_notifications.put(file, Notification.ADDED);
        if (old != null)
        {
          switch (2.$SwitchMap$org$eclipse$jetty$util$Scanner$Notification[old.ordinal()])
          {
          case 1: 
          case 2: 
            _notifications.put(file, Notification.CHANGED);
          }
        }
      }
      else if (!((TimeNSize)oldScan.get(file)).equals(currentScan.get(file)))
      {
        Notification old = (Notification)_notifications.put(file, Notification.CHANGED);
        if (old != null)
        {
          switch (2.$SwitchMap$org$eclipse$jetty$util$Scanner$Notification[old.ordinal()])
          {
          case 3: 
            _notifications.put(file, Notification.ADDED);
          }
          
        }
      }
    }
    
    for (String file : oldScan.keySet())
    {
      if (!currentScan.containsKey(file))
      {
        Notification old = (Notification)_notifications.put(file, Notification.REMOVED);
        if (old != null)
        {
          switch (2.$SwitchMap$org$eclipse$jetty$util$Scanner$Notification[old.ordinal()])
          {
          case 3: 
            _notifications.remove(file);
          }
          
        }
      }
    }
    if (LOG.isDebugEnabled()) {
      LOG.debug("scanned " + _scanDirs + ": " + _notifications, new Object[0]);
    }
    

    Object bulkChanges = new ArrayList();
    for (Iterator<Map.Entry<String, Notification>> iter = _notifications.entrySet().iterator(); iter.hasNext();)
    {
      Map.Entry<String, Notification> entry = (Map.Entry)iter.next();
      String file = (String)entry.getKey();
      

      if (oldScan.containsKey(file) ? 
      
        ((TimeNSize)oldScan.get(file)).equals(currentScan.get(file)) : 
        

        !currentScan.containsKey(file))
      {


        Notification notification = (Notification)entry.getValue();
        iter.remove();
        ((List)bulkChanges).add(file);
        switch (2.$SwitchMap$org$eclipse$jetty$util$Scanner$Notification[notification.ordinal()])
        {
        case 3: 
          reportAddition(file);
          break;
        case 2: 
          reportChange(file);
          break;
        case 1: 
          reportRemoval(file);
        }
      }
    }
    if (!((List)bulkChanges).isEmpty()) {
      reportBulkChanges((List)bulkChanges);
    }
  }
  






  private void scanFile(File f, Map<String, TimeNSize> scanInfoMap, int depth)
  {
    try
    {
      if (!f.exists()) {
        return;
      }
      if ((f.isFile()) || ((depth > 0) && (_reportDirs) && (f.isDirectory())))
      {
        if ((_filter == null) || ((_filter != null) && (_filter.accept(f.getParentFile(), f.getName()))))
        {
          if (LOG.isDebugEnabled())
            LOG.debug("scan accepted {}", new Object[] { f });
          String name = f.getCanonicalPath();
          scanInfoMap.put(name, new TimeNSize(f.lastModified(), f.isDirectory() ? 0L : f.length()));


        }
        else if (LOG.isDebugEnabled()) {
          LOG.debug("scan rejected {}", new Object[] { f });
        }
      }
      

      if ((f.isDirectory()) && ((depth < _scanDepth) || (_scanDepth == -1) || (_scanDirs.contains(f))))
      {
        File[] files = f.listFiles();
        if (files != null)
        {
          for (int i = 0; i < files.length; i++) {
            scanFile(files[i], scanInfoMap, depth + 1);
          }
        } else {
          LOG.warn("Error listing files in directory {}", new Object[] { f });
        }
      }
    }
    catch (IOException e) {
      LOG.warn("Error scanning watched files", e);
    }
  }
  
  private void warn(Object listener, String filename, Throwable th)
  {
    LOG.warn(listener + " failed on '" + filename, th);
  }
  




  private void reportAddition(String filename)
  {
    Iterator<Listener> itor = _listeners.iterator();
    while (itor.hasNext())
    {
      Listener l = (Listener)itor.next();
      try
      {
        if ((l instanceof DiscreteListener)) {
          ((DiscreteListener)l).fileAdded(filename);
        }
      }
      catch (Exception e) {
        warn(l, filename, e);
      }
      catch (Error e)
      {
        warn(l, filename, e);
      }
    }
  }
  





  private void reportRemoval(String filename)
  {
    Iterator<Listener> itor = _listeners.iterator();
    while (itor.hasNext())
    {
      Object l = itor.next();
      try
      {
        if ((l instanceof DiscreteListener)) {
          ((DiscreteListener)l).fileRemoved(filename);
        }
      }
      catch (Exception e) {
        warn(l, filename, e);
      }
      catch (Error e)
      {
        warn(l, filename, e);
      }
    }
  }
  





  private void reportChange(String filename)
  {
    Iterator<Listener> itor = _listeners.iterator();
    while (itor.hasNext())
    {
      Listener l = (Listener)itor.next();
      try
      {
        if ((l instanceof DiscreteListener)) {
          ((DiscreteListener)l).fileChanged(filename);
        }
      }
      catch (Exception e) {
        warn(l, filename, e);
      }
      catch (Error e)
      {
        warn(l, filename, e);
      }
    }
  }
  
  private void reportBulkChanges(List<String> filenames)
  {
    Iterator<Listener> itor = _listeners.iterator();
    while (itor.hasNext())
    {
      Listener l = (Listener)itor.next();
      try
      {
        if ((l instanceof BulkListener)) {
          ((BulkListener)l).filesChanged(filenames);
        }
      }
      catch (Exception e) {
        warn(l, filenames.toString(), e);
      }
      catch (Error e)
      {
        warn(l, filenames.toString(), e);
      }
    }
  }
  



  private void reportScanStart(int cycle)
  {
    for (Listener listener : _listeners)
    {
      try
      {
        if ((listener instanceof ScanCycleListener))
        {
          ((ScanCycleListener)listener).scanStarted(cycle);
        }
      }
      catch (Exception e)
      {
        LOG.warn(listener + " failed on scan start for cycle " + cycle, e);
      }
    }
  }
  



  private void reportScanEnd(int cycle)
  {
    for (Listener listener : _listeners)
    {
      try
      {
        if ((listener instanceof ScanCycleListener))
        {
          ((ScanCycleListener)listener).scanEnded(cycle);
        }
      }
      catch (Exception e)
      {
        LOG.warn(listener + " failed on scan end for cycle " + cycle, e);
      }
    }
  }
  
  public static abstract interface Listener {}
  
  public static abstract interface ScanListener
    extends Scanner.Listener
  {
    public abstract void scan();
  }
  
  public static abstract interface DiscreteListener
    extends Scanner.Listener
  {
    public abstract void fileChanged(String paramString)
      throws Exception;
    
    public abstract void fileAdded(String paramString)
      throws Exception;
    
    public abstract void fileRemoved(String paramString)
      throws Exception;
  }
  
  public static abstract interface BulkListener
    extends Scanner.Listener
  {
    public abstract void filesChanged(List<String> paramList)
      throws Exception;
  }
  
  public static abstract interface ScanCycleListener
    extends Scanner.Listener
  {
    public abstract void scanStarted(int paramInt)
      throws Exception;
    
    public abstract void scanEnded(int paramInt)
      throws Exception;
  }
}
